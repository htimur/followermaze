package de.khamrakulov.followermaze.nio.handler

import java.nio.channels.AsynchronousSocketChannel
import java.util.concurrent.BlockingQueue

import de.khamrakulov.followermaze.UntypedEvent
import de.khamrakulov.followermaze.controller.ClientController

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
trait ChannelHandler {
  def handler(channel: AsynchronousSocketChannel): Unit
}

object ChannelHandler {
  def client(clientController: ClientController) = new ClientHandler(clientController)
  def eventSource(eventSourceQueue: BlockingQueue[UntypedEvent]) = new EventSourceHandler(eventSourceQueue)
}