package de.khamrakulov.followermaze.controller

import de.khamrakulov.followermaze.TypedEvent

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class TypedEventSeqController(private var nextSeq: Int) extends EventSeqController[TypedEvent] {
  private val buffer = new scala.collection.mutable.HashMap[Int, TypedEvent]()

  def next(event: TypedEvent)(handler: TypedEvent => Unit) = {
    buffer(event.sequence) = event
    while (buffer.contains(nextSeq)) {
      handler(buffer.remove(nextSeq).get)
      nextSeq += 1
    }
  }
}
