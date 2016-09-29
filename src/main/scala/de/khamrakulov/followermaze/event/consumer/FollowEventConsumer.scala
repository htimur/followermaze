package de.khamrakulov.followermaze.event.consumer

import de.khamrakulov.followermaze.Follow

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class FollowEventConsumer extends EventConsumer[Follow] {
  override def consume(event: Follow): Unit = ???
}
