package de.khamrakulov.followermaze.controller

import java.util.concurrent.{ConcurrentHashMap, ConcurrentSkipListSet}

import scala.jdk.CollectionConverters._

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
object FollowController {
  def apply(): FollowController = new FollowController()
}

class FollowController {
  val followers = new ConcurrentHashMap[Int, ConcurrentSkipListSet[Int]]().asScala

  def add(from: Int, to: Int): Unit = {
    followers.get(to) match {
      case Some(set) => set.add(from)
      case None =>
        val set = new ConcurrentSkipListSet[Int]()
        set.add(from)
        followers += (to -> set)
    }
  }

  def get(id: Int) = followers.get(id) match {
    case Some(list) => Some(list.asScala)
    case _ => None
  }

  def remove(from: Int, to: Int): Unit = {
    followers.get(to) match {
      case Some(set) => set.remove(from)
      case None =>
    }
  }
}
