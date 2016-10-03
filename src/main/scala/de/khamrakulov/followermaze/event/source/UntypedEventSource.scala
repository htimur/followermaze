package de.khamrakulov.followermaze.event.source

import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.{BlockingQueue, TimeUnit}

import com.typesafe.scalalogging.Logger
import de.khamrakulov.followermaze.UntypedEvent

import scala.util.Try

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class UntypedEventSource(val socket: AsynchronousSocketChannel, val out: BlockingQueue[UntypedEvent]) extends EventSource[UntypedEvent] with Runnable {
  private val logger = Logger[UntypedEventSource]
  private val stringBuilder = new StringBuilder()
  private val counter = new AtomicLong()

  override def run(): Unit = {
    val byteBuffer = ByteBuffer.allocate(4096)
    while (socket.isOpen) {
      Try {
        val bytesRead = socket.read(byteBuffer).get(1, TimeUnit.SECONDS)
        if (bytesRead != -1) {
          logger.debug(s"bytes read: $bytesRead")

          // Make sure that we have data to read
          if (byteBuffer.position() > 1) {
            // Make the buffer ready to read
            byteBuffer.flip()

            // Convert the buffer into a string
            val lineBytes = new Array[Byte](bytesRead)

            byteBuffer.get(lineBytes, 0, bytesRead)
            val chars = new String(lineBytes, "utf-8").toCharArray
            for (char <- chars) char match {
              case '\n' =>
                out.add(UntypedEvent(stringBuilder.toString()))
                stringBuilder.clear()
                counter.incrementAndGet()
              case '\r' => // do nothing
              case element =>
                stringBuilder.append(element)
            }
            // Make the buffer ready to write
            byteBuffer.clear()
          }
        } else {
          socket.close()
        }
      } recover {
        case error =>
          logger.error("EventSource read error", error)
          socket.close()
      }
    }
    logger.info(s"Received ${counter.get()} events")
  }
}