package de.khamrakulov.followermaze.nio.hook

import java.nio.channels.AsynchronousSocketChannel

import com.typesafe.scalalogging.Logger

import scala.util.Try

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
object CloseHook {

  private val logger = Logger("CloseHook")

  def onExit(socket: AsynchronousSocketChannel): Unit = Runtime.getRuntime.addShutdownHook(new Thread() {
    override def run() = Try {
      socket.close()
    } recover {
      case error => logger.error("error on close socket", error)
    }
  })
}
