package controllers

import play.api._
import libs.concurrent.Promise
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.iteratee.{Concurrent, Enumerator, Iteratee}
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
/**
 *
 * User: takeshita
 * DateTime: 12/09/22 0:48
 */
object Chat extends Controller {

  def index = Action{
    Ok(views.html.chat.index("aaa"))
  }

  var roomConnections : List[Channel[String]] = Nil

  def chat =  WebSocket.using[String] {
    request => {

      val (out,channel) = Concurrent.broadcast[String]
      // register
      roomConnections ::= channel

      val in = Iteratee.foreach[String](s => {
        println(s)
        // push message to each connections
        roomConnections.foreach(_.push(s))
      }).map(_ => {
        // unregister
        roomConnections = roomConnections.filterNot( _ == channel)
        println("Disconnected")
      })

      (in,out)

    }
  }

}
