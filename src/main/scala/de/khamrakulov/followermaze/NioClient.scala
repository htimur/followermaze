package de.khamrakulov.followermaze

import java.io.{BufferedWriter, OutputStreamWriter}
import java.nio.channels.{AsynchronousSocketChannel, Channels}
import java.util.concurrent.LinkedBlockingDeque

import de.khamrakulov.followermaze.event.consumer.EventConsumer

import scala.util.Try

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
trait NioClient {
  def start(): Try[Boolean]

  def stop(): Try[Boolean]

  def isRunning: Boolean

}

object NioClient {
  def apply(consumerId: Int, socket: AsynchronousSocketChannel): NioClientImpl = new NioClientImpl(consumerId, socket)
}

class NioClientImpl(val consumerId: Int, private val socket: AsynchronousSocketChannel) extends NioClient with EventConsumer[TypedEvent] {
  private val queue = new LinkedBlockingDeque[TypedEvent]()
  private val out = new BufferedWriter(new OutputStreamWriter(Channels.newOutputStream(socket), "utf-8"))

  private val consumerThread = new Thread(new Runnable {
    override def run(): Unit = while (socket.isOpen) Try {
      queue.take() match {
        case event: TypedEvent =>
          out.write(event.stringify)
          out.flush()
        case _ =>
      }
    }
  }, s"NioClient consumer $consumerId")

  consumerThread.setDaemon(true)

  override def consume(event: TypedEvent) = queue.add(event)

  override def start(): Try[Boolean] = Try {
    if (socket.isOpen) {
      consumerThread.start()
    }

    true
  }

  override def stop(): Try[Boolean] = Try {
    if (socket.isOpen) socket.close()
    if (consumerThread.isAlive) consumerThread.interrupt()
    true
  }

  override def isRunning: Boolean = socket.isOpen && consumerThread.isAlive
}
