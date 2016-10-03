package de.khamrakulov.followermaze.event

import java.util.concurrent.BlockingQueue

import com.typesafe.scalalogging.Logger
import de.khamrakulov.followermaze._
import de.khamrakulov.followermaze.controller.{ClientController, EventSeqController, FollowController}

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
object Router {
  def apply(in: BlockingQueue[TypedEvent], clients: ClientController, followers: FollowController, running: LazyBool = LazyTrue) = new Router(in, clients, followers, running)
}

class Router(in: BlockingQueue[TypedEvent], clients: ClientController, followers: FollowController, running: LazyBool) extends Runnable {
  private val logger = Logger[Router]
  private val seqController = EventSeqController.typed()

  override def run(): Unit = while (running()) {
    val take = in.take()
    seqController.next(take) {
      case event@Follow(seq, from, to) =>
        followers.add(from, to)
        clients.get(to) match {
          case Some(client) =>
            client.consume(event)
          case _ =>
        }
      case event@Unfollow(seq, from, to) =>
        followers.remove(from, to)
      case event: Broadcast =>
        clients.all.foreach { case (id, client) =>
          client.consume(event)
        }
      case event@PrivateMsg(seq, from, to) =>
        clients.get(to) match {
          case Some(client) => client.consume(event)
          case _ =>
        }
      case event@StatusUpdate(seq, from) =>
        followers.get(from) match {
          case Some(list) => for (id <- list) {
            clients.get(id) match {
              case Some(client) => client.consume(event)
              case None =>
            }
          }
          case None =>
        }
      case unsupportedEvent => logger.info(s"Unsupported event $unsupportedEvent")
    }
  }
}
