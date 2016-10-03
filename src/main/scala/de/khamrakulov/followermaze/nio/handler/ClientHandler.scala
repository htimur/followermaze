package de.khamrakulov.followermaze.nio.handler

import java.io.{BufferedReader, InputStreamReader}
import java.nio.channels.{AsynchronousSocketChannel, Channels}

import de.khamrakulov.followermaze.controller.ClientController
import de.khamrakulov.followermaze.nio.NioClient
import de.khamrakulov.followermaze.nio.hook.CloseHook

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class ClientHandler(clientController: ClientController) extends ChannelHandler {
  override def handler(channel: AsynchronousSocketChannel): Unit = {
    val userId = if (channel.isOpen) {
      val inputStream = Channels.newInputStream(channel)
      val reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))
      reader.readLine() match {
        case line: String =>
          CloseHook.onExit(channel)
          Some(line.toInt)
        case _ =>
          channel.close()
          None
      }
    } else {
      channel.close()
      None
    }

    userId.map { id =>
      val client = NioClient(id, channel)
      client.start()

      clientController.get(id).map(_.stop())
      clientController.add(id, client)
    }
  }
}
