package de.khamrakulov.followermaze.event.source

import java.nio.channels.AsynchronousSocketChannel
import java.util.concurrent.BlockingQueue

import de.khamrakulov.followermaze.{Event, UntypedEvent}

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
trait EventSource[OUT <: Event] {
  val out: BlockingQueue[OUT]
}

object EventSource {
  def untyped(queue: BlockingQueue[UntypedEvent], socket: AsynchronousSocketChannel) = new UntypedEventSource(socket, queue)
}