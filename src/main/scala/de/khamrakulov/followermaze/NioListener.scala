package de.khamrakulov.followermaze

import java.net.{InetAddress, InetSocketAddress, StandardSocketOptions}
import java.nio.channels.{AsynchronousServerSocketChannel, AsynchronousSocketChannel}

import scala.concurrent.{Future, Promise}
import scala.util.{Success, Try}

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class NioListener(name: String, hostAddress: InetAddress, port: Int, connectionHandler: AsynchronousSocketChannel => Unit) {
  private val listener =
    AsynchronousServerSocketChannel
      .open()
      .setOption[java.lang.Boolean](StandardSocketOptions.SO_REUSEADDR, true)
      .bind(new InetSocketAddress(hostAddress, port))

  private val listenerThread = new Thread(new Runnable {
    override def run(): Unit = while (listener.isOpen) {
      accept().onComplete {
        case Success(socket) => connectionHandler(socket)
        case _ =>
      }
    }
  }, s"NioListener thread $name")

  def start(): Try[Boolean] = Try {
    listenerThread.start()
    true
  }

  def stop(): Try[Boolean] = Try {
    if (listener.isOpen) listener.close()
    if (listenerThread.isAlive) listenerThread.interrupt()
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
