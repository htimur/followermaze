package de.khamrakulov.followermaze.controller

import java.util.concurrent.ConcurrentHashMap

import scala.jdk.CollectionConverters._

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
object ClientController {
  def apply(): ClientController = new ClientController()
}

class ClientController {
  val clients =  new ConcurrentHashMap[Int, Client]().asScala

  def get(id: Int) = clients.get(id)

  def all = clients.view

  def add(id: Int, client: Client) = clients += (id -> client)
}
