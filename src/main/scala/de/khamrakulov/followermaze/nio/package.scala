package de.khamrakulov.followermaze

import java.nio.channels.AsynchronousSocketChannel

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
package object nio {
  type ChannelHandler = AsynchronousSocketChannel => Unit
}
