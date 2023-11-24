package de.khamrakulov.followermaze.event.source

import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.util.concurrent.{LinkedBlockingQueue, TimeUnit, Future => JFuture}

import de.khamrakulov.followermaze.UntypedEvent
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.mockito.{Mockito, ArgumentMatchers => M}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class UntypedEventSourceSpec extends AnyFlatSpec with Matchers with MockitoSugar {
  def getSocket(output1: String, output2: String) = {
    val stringBytes1 = output1.getBytes("utf-8")
    val stringBytes2 = output2.getBytes("utf-8")

    val future = mock[JFuture[Integer]]

    Mockito.when(future.get(M.anyInt(), M.any(classOf[TimeUnit])))
      .thenReturn(stringBytes1.length, stringBytes2.length)
    Mockito.when(future.get())
      .thenReturn(stringBytes1.length, stringBytes2.length)

    val socket = mock[AsynchronousSocketChannel]

    Mockito.when(socket.isOpen).thenReturn(true, true, false)
    Mockito.when(socket.read(M.any(classOf[ByteBuffer])))
      .thenAnswer(new Answer[JFuture[Integer]] {
        override def answer(invocation: InvocationOnMock): JFuture[Integer] = {
          val buf = invocation.getArguments()(0).asInstanceOf[ByteBuffer]
          buf.clear()
          buf.put(stringBytes1)
          future
        }
      })
      .thenAnswer(new Answer[JFuture[Integer]] {
        override def answer(invocation: InvocationOnMock): JFuture[Integer] = {
          val buf = invocation.getArguments()(0).asInstanceOf[ByteBuffer]
          buf.clear()
          buf.put(stringBytes2)
          future
        }
      })

    socket
  }

  "UntypedEventSource" should "correctly handle client input" in {
    val queue = new LinkedBlockingQueue[UntypedEvent]()
    val socket = getSocket("32|F|44|", "43\n333|B\n")
    val source = EventSource.untyped(queue, socket)
    source.run()

    queue.take() shouldBe UntypedEvent("32|F|44|43")
    queue.take() shouldBe UntypedEvent("333|B")
  }
}
