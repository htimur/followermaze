package de.khamrakulov.followermaze.nio

import java.net.{InetAddress, InetSocketAddress, StandardSocketOptions}
import java.nio.channels.{AsynchronousServerSocketChannel, AsynchronousSocketChannel}

import com.typesafe.scalalogging.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Success, Try}

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
object NioListener {
  def apply(name: String, hostAddress: InetAddress, port: Int, handler: ChannelHandler) = new NioListener(name, hostAddress, port, handler)
}

class NioListener(name: String, hostAddress: InetAddress, port: Int, handler: ChannelHandler) {
  private val logger = Logger[NioListener]

  private val listener =
    AsynchronousServerSocketChannel
      .open()
      .setOption[java.lang.Boolean](StandardSocketOptions.SO_REUSEADDR, true)
      .bind(new InetSocketAddress(hostAddress, port))

  logger.info(s"Bound to $port on $hostAddress")

  private val listenerThread = new Thread(new Runnable {
    override def run(): Unit = while (listener.isOpen) {
      accept().onComplete {
        case Success(socket) => handler(socket)
        case _ =>
      }
    }
  }, s"NioListener thread $name")
  listenerThread.setDaemon(true)

  def start(): Boolean =
    if (listener.isOpen) {
      listenerThread.start()
      true
    } else {
      false
    }

  def stop(): Try[Boolean] = Try {
    if (listener.isOpen) listener.close()
    if (listenerThread.isAlive && !listenerThread.isInterrupted) listenerThread.interrupt()
    true
  }

  private def accept(): Future[AsynchronousSocketChannel] = {
    val p = Promise[AsynchronousSocketChannel]
    p.complete(Try {
      listener.accept.get
    })

    p.future
  }
}
