package de.khamrakulov.followermaze.event

import de.khamrakulov.followermaze._

import scala.util.matching.Regex

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
object Parser {

  trait EventParser[T <: TypedEvent] {
    val pattern: Regex

    def unapply(arg: UntypedEvent): Option[T] = unapply(arg.value)

    def unapply(arg: String): Option[T]
  }

  object FollowEvent extends EventParser[Follow] {
    override val pattern = "^([0-9]+)\\|F\\|([0-9]+)\\|([0-9]+)$".r

    override def unapply(arg: String): Option[Follow] = pattern.unapplySeq(arg) match {
      case Some(sec :: from :: to :: Nil) =>
        Some(Follow(sec.toInt, from.toInt, to.toInt))
      case _ => None
    }
  }

  object UnfollowEvent extends EventParser[Unfollow] {
    override val pattern = "^([0-9]+)\\|U\\|([0-9]+)\\|([0-9]+)$".r

    override def unapply(arg: String): Option[Unfollow] = pattern.unapplySeq(arg) match {
      case Some(sec :: from :: to :: Nil) =>
        Some(Unfollow(sec.toInt, from.toInt, to.toInt))
      case _ => None
    }
  }

  object BroadcastEvent extends EventParser[Broadcast] {
    override val pattern = "^([0-9]+)\\|B$".r

    override def unapply(arg: String): Option[Broadcast] = pattern.unapplySeq(arg) match {
      case Some(sec :: Nil) =>
        Some(Broadcast(sec.toInt))
      case _ => None
    }
  }

  object PrivateMsgEvent extends EventParser[PrivateMsg] {
    override val pattern = "^([0-9]+)\\|P\\|([0-9]+)\\|([0-9]+)$".r

    override def unapply(arg: String): Option[PrivateMsg] =pattern.unapplySeq(arg) match {
      case Some(sec :: from :: to :: Nil) =>
        Some(PrivateMsg(sec.toInt, from.toInt, to.toInt))
      case _ => None
    }
  }

  object StatusUpdatedEvent extends EventParser[StatusUpdate] {
    override val pattern = "^([0-9]+)\\|S\\|([0-9]+)$".r

    override def unapply(arg: String): Option[StatusUpdate] = pattern.unapplySeq(arg) match {
      case Some(sec :: from :: Nil) =>
        Some(StatusUpdate(sec.toInt, from.toInt))
      case _ => None
    }
  }

}
