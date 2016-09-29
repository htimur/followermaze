package de.khamrakulov.followermaze

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
trait Event

trait TypedEvent extends Event {
  val sequence: Int
  val key: String
  val stringify: String

  override def toString: String = stringify
}

case class UntypedEvent(value: String) extends Event

case class Follow(sequence: Int, from: Int, to: Int) extends TypedEvent {
  override val key = "F"

  override val stringify = s"$sequence|$key|$from|$to"

}

case class Unfollow(sequence: Int, from: Int, to: Int) extends TypedEvent {
  override val key = "U"

  override val stringify = s"$sequence|$key|$from|$to"
}

case class Broadcast(sequence: Int) extends TypedEvent {
  override val key = "B"

  override val stringify = s"$sequence|$key"
}

case class PrivateMsg(sequence: Int, from: Int, to: Int) extends TypedEvent {
  override val key = "P"

  override val stringify = s"$sequence|$key|$from|$to"
}

case class StatusUpdated(sequence: Int, from: Int) extends TypedEvent {
  override val key = "S"

  override val stringify = s"$sequence|$key|$from"
}
