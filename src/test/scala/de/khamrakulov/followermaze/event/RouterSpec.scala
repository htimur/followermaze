package de.khamrakulov.followermaze.event

import java.util.concurrent.{Executors, LinkedBlockingQueue}

import de.khamrakulov.followermaze._
import de.khamrakulov.followermaze.controller.{ClientController, FollowController}
import de.khamrakulov.followermaze.nio.NioClientImpl
import org.mockito.Mockito
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class RouterSpec extends AnyFlatSpec with Matchers with MockitoSugar {
  "Router" should "correctly route events" in {
    val service = Executors.newSingleThreadExecutor()
    val in = new LinkedBlockingQueue[TypedEvent]()
    val clientController = ClientController()
    val followController = FollowController()
    val client1 = mock[NioClientImpl]
    val client2 = mock[NioClientImpl]
    val client3 = mock[NioClientImpl]
    val order1 = Mockito.inOrder(client1)
    val order2 = Mockito.inOrder(client2)
    val order3 = Mockito.inOrder(client3)

    clientController.add(1, client1)
    clientController.add(2, client2)
    clientController.add(3, client3)

    in.add(Follow(1, 2, 3))
    in.add(StatusUpdate(2, 3))
    in.add(Unfollow(3, 2, 3))
    in.add(PrivateMsg(4, 2, 3))
    in.add(Broadcast(5))

    service.execute(Router(in, clientController, followController))

    Thread.sleep(1000)
    order1.verify(client1, Mockito.times(1)).consume(Broadcast(5))

    order2.verify(client2, Mockito.times(1)).consume(StatusUpdate(2, 3))
    order2.verify(client2, Mockito.times(1)).consume(Broadcast(5))

    order3.verify(client3, Mockito.times(1)).consume(PrivateMsg(4, 2, 3))
    order3.verify(client3, Mockito.times(1)).consume(Broadcast(5))

    service.shutdown()
  }
}
