package de.khamrakulov.followermaze.event.transformer

import java.util.concurrent.BlockingQueue

import de.khamrakulov.followermaze.{Parser, TypedEvent, UntypedEvent}

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class ToTypedEventTransformer(val in: BlockingQueue[UntypedEvent], val out: BlockingQueue[TypedEvent]) extends EventTransformer[UntypedEvent, TypedEvent] with Runnable {
  override def run(): Unit = while (true) {
    in.take().value match {
      case Parser.FollowEvent(event) => out.put(event)
      case Parser.UnfollowEvent(event) => out.put(event)
      case Parser.BroadcastEvent(event) => out.put(event)
      case Parser.PrivateMsgEvent(event) => out.put(event)
      case Parser.StatusUpdatedEvent(event) => out.put(event)
    }
  }
}
