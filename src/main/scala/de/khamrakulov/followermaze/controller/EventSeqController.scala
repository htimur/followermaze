package de.khamrakulov.followermaze.controller

import de.khamrakulov.followermaze.Event

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
trait EventSeqController[T <: Event] {
  def next(event: T)(nextHandler: T => Unit)
}

object EventSeqController {
  def typed(startSeq: Int = 1): TypedEventSeqController = new TypedEventSeqController(startSeq)
}



