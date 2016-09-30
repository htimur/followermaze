package de.khamrakulov.followermaze.event.consumer

import de.khamrakulov.followermaze.Event

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
trait EventConsumer[T <: Event] {
  val consumerId: Int

  def consume(event: T): Boolean
}
