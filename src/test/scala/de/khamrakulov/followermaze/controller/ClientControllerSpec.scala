package de.khamrakulov.followermaze.controller

import de.khamrakulov.followermaze.nio.NioClientImpl
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class ClientControllerSpec extends AnyFlatSpec with Matchers with MockitoSugar {
  "ClientController" should "add and get clients" in {
    val controller = ClientController()
    val client = mock[NioClientImpl]

    controller.add(1, client)

    controller.get(1) shouldBe Some(client)
    controller.get(2) shouldBe None
  }

  it should "list all clients" in {
    val controller = ClientController()
    val client1 = mock[NioClientImpl]
    val client2 = mock[NioClientImpl]
    val client3 = mock[NioClientImpl]

    controller.add(1, client1)
    controller.add(2, client2)
    controller.add(3, client3)

    controller.all.toMap shouldBe Map(1 -> client1, 2 -> client2, 3 -> client3)
  }
}
