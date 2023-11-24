package de.khamrakulov.followermaze.event

import de.khamrakulov.followermaze._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class ParserSpec extends AnyFlatSpec with Matchers {
  val followString = "666|F|60|50"
  val unfollowString = "666|U|60|50"
  val broadcastString = "666|B"
  val privateMsgString = "666|P|66|55"
  val statusUpdateString = "666|S|66"

  "FollowEvent parser" should "properly extract Follow event from UntypedEvent and string" in {

    UntypedEvent(followString) match {
      case Parser.FollowEvent(event) =>
        event shouldBe Follow(666, 60, 50)
      case _ => fail()
    }

    followString match {
      case Parser.FollowEvent(event) =>
        event shouldBe Follow(666, 60, 50)
      case _ => fail()
    }
  }

  it should "not parse anything else" in {
    unfollowString match {
      case Parser.FollowEvent(event) =>
        fail()
      case _ =>
    }
    broadcastString match {
      case Parser.FollowEvent(event) =>
        fail()
      case _ =>
    }
    privateMsgString match {
      case Parser.FollowEvent(event) =>
        fail()
      case _ =>
    }
    statusUpdateString match {
      case Parser.FollowEvent(event) =>
        fail()
      case _ =>
    }
  }

  "UnfollowEvent parser" should "properly extract Follow event from UntypedEvent and string" in {
    UntypedEvent(unfollowString) match {
      case Parser.UnfollowEvent(event) =>
        event shouldBe Unfollow(666, 60, 50)
      case _ => fail()
    }

    unfollowString match {
      case Parser.UnfollowEvent(event) =>
        event shouldBe Unfollow(666, 60, 50)
      case _ => fail()
    }
  }

  it should "not parse anything else" in {
    followString match {
      case Parser.UnfollowEvent(event) =>
        fail()
      case _ =>
    }
    broadcastString match {
      case Parser.UnfollowEvent(event) =>
        fail()
      case _ =>
    }
    privateMsgString match {
      case Parser.UnfollowEvent(event) =>
        fail()
      case _ =>
    }
    statusUpdateString match {
      case Parser.UnfollowEvent(event) =>
        fail()
      case _ =>
    }
  }

  "BroadcastEvent parser" should "properly extract Follow event from UntypedEvent and string" in {
    UntypedEvent(broadcastString) match {
      case Parser.BroadcastEvent(event) =>
        event shouldBe Broadcast(666)
      case _ => fail()
    }

    broadcastString match {
      case Parser.BroadcastEvent(event) =>
        event shouldBe Broadcast(666)
      case _ => fail()
    }
  }

  it should "not parse anything else" in {
    unfollowString match {
      case Parser.BroadcastEvent(event) =>
        fail()
      case _ =>
    }
    followString match {
      case Parser.BroadcastEvent(event) =>
        fail()
      case _ =>
    }
    privateMsgString match {
      case Parser.BroadcastEvent(event) =>
        fail()
      case _ =>
    }
    statusUpdateString match {
      case Parser.BroadcastEvent(event) =>
        fail()
      case _ =>
    }
  }

  "PrivateMsgEvent parser" should "properly extract Follow event from UntypedEvent and string" in {
    UntypedEvent(privateMsgString) match {
      case Parser.PrivateMsgEvent(event) =>
        event shouldBe PrivateMsg(666, 66, 55)
      case _ => fail()
    }

    privateMsgString match {
      case Parser.PrivateMsgEvent(event) =>
        event shouldBe PrivateMsg(666, 66, 55)
      case _ => fail()
    }
  }

  it should "not parse anything else" in {
    unfollowString match {
      case Parser.PrivateMsgEvent(event) =>
        fail()
      case _ =>
    }
    broadcastString match {
      case Parser.PrivateMsgEvent(event) =>
        fail()
      case _ =>
    }
    followString match {
      case Parser.PrivateMsgEvent(event) =>
        fail()
      case _ =>
    }
    statusUpdateString match {
      case Parser.PrivateMsgEvent(event) =>
        fail()
      case _ =>
    }
  }

  "StatusUpdatedEvent parser" should "properly extract Follow event from UntypedEvent and string" in {
    UntypedEvent(statusUpdateString) match {
      case Parser.StatusUpdatedEvent(event) =>
        event shouldBe StatusUpdate(666, 66)
      case _ => fail()
    }

    statusUpdateString match {
      case Parser.StatusUpdatedEvent(event) =>
        event shouldBe StatusUpdate(666, 66)
      case _ => fail()
    }
  }
  it should "not parse anything else" in {
    unfollowString match {
      case Parser.StatusUpdatedEvent(event) =>
        fail()
      case _ =>
    }
    broadcastString match {
      case Parser.StatusUpdatedEvent(event) =>
        fail()
      case _ =>
    }
    privateMsgString match {
      case Parser.StatusUpdatedEvent(event) =>
        fail()
      case _ =>
    }
    followString match {
      case Parser.StatusUpdatedEvent(event) =>
        fail()
      case _ =>
    }
  }
}
