package de.khamrakulov.followermaze.controller

import de.khamrakulov.followermaze.{Broadcast, TypedEvent}
import org.mockito.Mockito
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}


/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class TypedEventSeqControllerSpec extends FlatSpec with Matchers with MockitoSugar {
  "TypedEventSeqController" should "provide events in correct order" in {
    type Handler = TypedEvent => Unit
    val handler = mock[Handler]
    val controller = EventSeqController.typed()

    val event1 = Broadcast(1)
    val event2 = Broadcast(2)
    val event3 = Broadcast(3)

    controller.next(event3)(handler)

    Mockito.verify(handler, Mockito.never).apply(event3)

    controller.next(event1)(handler)

    Mockito.verify(handler, Mockito.times(1)).apply(event1)

    val ordered = Mockito.inOrder(handler)
    controller.next(event2)(handler)
    ordered.verify(handler).apply(event2)
    ordered.verify(handler).apply(event3)
  }
}
