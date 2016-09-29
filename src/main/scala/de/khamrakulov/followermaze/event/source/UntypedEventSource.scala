package de.khamrakulov.followermaze.event.source

import java.io.{BufferedReader, InputStreamReader}
import java.nio.channels.{AsynchronousSocketChannel, Channels}
import java.util.concurrent.BlockingQueue

import de.khamrakulov.followermaze.UntypedEvent

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class UntypedEventSource(val socket: AsynchronousSocketChannel, val out: BlockingQueue[UntypedEvent]) extends EventSource[UntypedEvent] with Runnable {

  override def run(): Unit = {
    while (socket.isOpen) {
      val inputStream = Channels.newInputStream(socket)
      val reader = new BufferedReader(new InputStreamReader(inputStream))
      out.put(UntypedEvent(reader.readLine()))
    }
  }
}
