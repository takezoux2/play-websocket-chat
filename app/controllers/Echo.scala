package controllers

import play.api._
import libs.concurrent.Promise
import play.api.libs.iteratee.{Concurrent, Enumerator, Iteratee}
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 *
 * User: takeshita
 * DateTime: 12/09/22 0:48
 */
object Echo extends Controller {

  def index = Action {
    Ok(views.html.echo.index("Echo"))
  }

  def echo = WebSocket.using[String] {
    request => {
      // create PushEnumerator
      val (out,channel) = Concurrent.broadcast[String]
      val in = Iteratee.foreach[String](text => {
        println(text)
        // push
        channel.push(text)
      }).map(_ => {
        // on connection disconnected
        println("Disconnected")
      })
      in -> out
    }
  }

}
