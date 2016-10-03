package de.khamrakulov.followermaze.nio

import java.io.{BufferedWriter, OutputStreamWriter}
import java.nio.channels.{AsynchronousSocketChannel, Channels}
import java.util.concurrent.LinkedBlockingDeque

import com.typesafe.scalalogging.Logger
import de.khamrakulov.followermaze.TypedEvent
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
  val CRLF = "\n"
  private val logger = Logger[NioClientImpl]
  private val queue = new LinkedBlockingDeque[TypedEvent]()
  private val out = new BufferedWriter(new OutputStreamWriter(Channels.newOutputStream(socket), "utf-8"))

  @volatile private var running = true

  private val consumerThread = new Thread(new Runnable {
    override def run(): Unit = while (running && socket.isOpen) Try {
      queue.take() match {
        case event: TypedEvent =>
          out.write(event.stringify)
          out.write(CRLF)
          out.flush()
        case _ =>
      }
    } recover {
      case error =>
        logger.error(s"Connection is unhealthy", error)
        stop()
    }
  }, s"NioClient consumer $consumerId")

  consumerThread.setDaemon(true)

  override def consume(event: TypedEvent) = if (isRunning) queue.add(event) else false

  override def isRunning: Boolean = socket.isOpen && consumerThread.isAlive

  override def start(): Try[Boolean] = Try {
    if (socket.isOpen) {
      consumerThread.start()
    }
    true
  } recover {
    case error =>
      logger.error("Error starting consumer thread", error)
      false
  }

  override def stop(): Try[Boolean] = Try {
    running = false
    if (socket.isOpen) socket.close()
    if (consumerThread.isAlive && !consumerThread.isInterrupted) consumerThread.interrupt()
    true
  }
}
