package de.khamrakulov.followermaze.controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class FollowControllerSpec extends AnyFlatSpec with Matchers {
  "FollowController" should "add, get and remove follow relations" in {
    val controller = FollowController()
    controller.add(1, 2)
    controller.add(1, 3)
    controller.add(3, 2)

    controller.get(1) shouldBe None
    controller.get(2) shouldBe Some(Set(1, 3))
    controller.get(3) shouldBe Some(Set(1))
    controller.get(4) shouldBe None

    controller.remove(3, 2)
    controller.remove(3, 1)

    controller.get(1) shouldBe None
    controller.get(2) shouldBe Some(Set(1))
    controller.get(3) shouldBe Some(Set(1))
    controller.get(4) shouldBe None
  }
}
