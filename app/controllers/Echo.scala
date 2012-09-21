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
object Echo extends Controller {

  def index = Action {
    Ok(views.html.echo.index("Echo"))
  }

  def echo = WebSocket.async[String] {
    request => {
      // create PushEnumerator
      val out = Enumerator.imperative[String]()
      val in = Iteratee.foreach[String](text => {
        println(text)
        // push
        out.push(text)
      }).mapDone(_ => {
        // on connection disconnected
        println("Disconnected")
      })
      Promise.pure(in -> out)
    }
  }

}
