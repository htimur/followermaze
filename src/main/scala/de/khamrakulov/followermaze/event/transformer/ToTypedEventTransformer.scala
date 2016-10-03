package de.khamrakulov.followermaze.event.transformer

import java.util.concurrent.BlockingQueue

import com.typesafe.scalalogging.Logger
import de.khamrakulov.followermaze.event.{LazyBool, Parser}
import de.khamrakulov.followermaze.{TypedEvent, UntypedEvent}

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class ToTypedEventTransformer(val in: BlockingQueue[UntypedEvent], val out: BlockingQueue[TypedEvent])(running: LazyBool) extends EventTransformer[UntypedEvent, TypedEvent] with Runnable {
  private val logger = Logger[ToTypedEventTransformer]

  override def run(): Unit = while (running()) {
    val take = in.take()
    take match {
      case Parser.FollowEvent(event) => out.put(event)
      case Parser.UnfollowEvent(event) => out.put(event)
      case Parser.BroadcastEvent(event) => out.put(event)
      case Parser.PrivateMsgEvent(event) => out.put(event)
      case Parser.StatusUpdatedEvent(event) => out.put(event)
      case value => logger.info(s"Unsupported value $value")
    }
  }
}
