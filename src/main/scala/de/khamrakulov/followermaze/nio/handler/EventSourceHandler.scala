package de.khamrakulov.followermaze.nio.handler

import java.nio.channels.AsynchronousSocketChannel
import java.util.concurrent.BlockingQueue

import de.khamrakulov.followermaze.UntypedEvent
import de.khamrakulov.followermaze.event.source.EventSource
import de.khamrakulov.followermaze.nio.hook.CloseHook

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class EventSourceHandler(eventSourceQueue: BlockingQueue[UntypedEvent]) extends ChannelHandler {
  override def handler(channel: AsynchronousSocketChannel): Unit = {
    CloseHook.onExit(channel)

    val eventSourceThread = new Thread(EventSource.untyped(eventSourceQueue, channel), "EventSource")
    eventSourceThread.setDaemon(true)
    eventSourceThread.start()
  }
}
