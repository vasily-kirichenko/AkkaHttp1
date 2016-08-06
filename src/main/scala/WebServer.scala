import akka.http.scaladsl.model._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future
import scala.util.{Failure, Success}

case class Person(name: String, age: Int)

object Db {
  def getPerson(name: String): Future[Option[Person]] = {
    name match {
      case "kot" => Future.successful(Some(Person(name, 41)))
      case _ => Future.failed(new Exception(s"Person with name '$name' was not found."))
    }
  }
}

object WebServer {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("web-server-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    implicit val personFormat = jsonFormat2(Person)

    val route =
      get {
        path("hello") {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello</h1>"))
        } ~
        pathPrefix("person" / ".*".r) { name: String =>
          val maybePerson = Db.getPerson(name)

          onSuccess(maybePerson) {
            case Some(person) => complete(person)
            case None => complete(StatusCodes.NotFound)
          }
          //complete(Person("kot", 41))
        }
      }

    val port = 8091
    val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", port)
    println(s"Server online at http://localhost:$port. Press Return to stop...")
  }
}
