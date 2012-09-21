package controllers

import play.api._
import libs.concurrent.Promise
import libs.iteratee.{PushEnumerator, Enumerator, Iteratee}
import play.api.mvc._
/**
 *
 * User: takeshita
 * DateTime: 12/09/22 0:48
 */
object Chat extends Controller {

  def index = Action{
    Ok(views.html.chat.index("aaa"))
  }

  var roomConnections : List[PushEnumerator[String]] = Nil

  def chat =  WebSocket.async[String] {
    request => {
      val out = Enumerator.imperative[String]()
      // register
      roomConnections ::= out

      val in = Iteratee.foreach[String](s => {
        println(s)
        // push message to each connections
        roomConnections.foreach(_.push(s))
      }).mapDone(_ => {
        // unregister
        roomConnections = roomConnections.filterNot( _ == out)
        println("Disconnected")
      })

      Promise.pure((in,out))

    }
  }

}
