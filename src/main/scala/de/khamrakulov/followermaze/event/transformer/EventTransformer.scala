package de.khamrakulov.followermaze.event.transformer

import java.util.concurrent.BlockingQueue

import de.khamrakulov.followermaze.event._
import de.khamrakulov.followermaze.{Event, TypedEvent, UntypedEvent}

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
trait EventTransformer[IN <: Event, OUT <: Event] {
  val in: BlockingQueue[IN]
  val out: BlockingQueue[OUT]
}

object EventTransformer {
  def toTypedEvent(in: BlockingQueue[UntypedEvent], out: BlockingQueue[TypedEvent], keepRunning: LazyBool = LazyTrue) = new ToTypedEventTransformer(in, out)(keepRunning)
}