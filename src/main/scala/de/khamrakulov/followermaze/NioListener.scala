//package de.khamrakulov.followermaze
//
//import java.net.{InetAddress, InetSocketAddress, StandardSocketOptions}
//import java.nio.channels.{AsynchronousServerSocketChannel, AsynchronousSocketChannel}
//
//import scala.concurrent.{Future, Promise}
//import scala.util.{Success, Try}
//
///**
//  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
//  */
//class NioListener(hostAddress: InetAddress, port: Int) {
//  val listener =
//    AsynchronousServerSocketChannel
//      .open()
//      .setOption[java.lang.Boolean](StandardSocketOptions.SO_REUSEADDR, true)
//      .bind(new InetSocketAddress(hostAddress, port))
//
//  def start(continueListening: => Boolean): Unit = {
//    while (continueListening) {
//      accept().onComplete {
//        case Success(socket) => NioConnection.newConnection(socket)
//        case _ =>
//      }
//    }
//  }
//
//  private def accept(): Future[AsynchronousSocketChannel] = {
//    val p = Promise[AsynchronousSocketChannel]
//    p.complete(Try {
//      listener.accept.get
//    })
//
//    p.future
//  }
//}
