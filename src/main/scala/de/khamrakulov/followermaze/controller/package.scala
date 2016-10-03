package de.khamrakulov.followermaze

import de.khamrakulov.followermaze.event.consumer.EventConsumer
import de.khamrakulov.followermaze.nio.NioClient

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
package object controller {
  type Client = NioClient with EventConsumer[TypedEvent]
}
