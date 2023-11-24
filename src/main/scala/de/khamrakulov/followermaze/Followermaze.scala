package de.khamrakulov.followermaze

import java.net.InetAddress
import java.util.concurrent.LinkedBlockingQueue

import de.khamrakulov.followermaze.controller.{ClientController, FollowController}
import de.khamrakulov.followermaze.event.Router
import de.khamrakulov.followermaze.event.transformer.EventTransformer
import de.khamrakulov.followermaze.nio.NioListener
import de.khamrakulov.followermaze.nio.handler.ChannelHandler

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
object Followermaze extends App {

  val clientController = ClientController()
  val followerController = FollowController()

  val eventSourceQueue = new LinkedBlockingQueue[UntypedEvent]()
  val transformedQueue = new LinkedBlockingQueue[TypedEvent]()

  val eventSourceListener = NioListener("EventSource", InetAddress.getLoopbackAddress, 9090, ChannelHandler.eventSource(eventSourceQueue).handler)
  val clientListener = NioListener("ClientSource", InetAddress.getLoopbackAddress, 9099, ChannelHandler.client(clientController).handler)

  eventSourceListener.start()
  clientListener.start()

  new Thread(EventTransformer.toTypedEvent(eventSourceQueue, transformedQueue), "Transformer thread").start()
  new Thread(Router(transformedQueue, clientController, followerController), "Router thread").start()
}
