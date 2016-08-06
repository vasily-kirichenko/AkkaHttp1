import akka.http.scaladsl.model._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

case class Person(name: String, age: Int)

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
        path("person") {
          complete(Person("kot", 41))
        }
      }

    val port = 8091
    val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", port)
    println(s"Server online at http://localhost:$port. Press Return to stop...")
  }
}
