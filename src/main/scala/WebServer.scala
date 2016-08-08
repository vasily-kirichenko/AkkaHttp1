import akka.http.scaladsl.model._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import scala.concurrent.Future

trait Message
final case class Person(name: String, age: Int) extends Message
final case class Envelope[A <: Message](payload: A, server: String)

object Envelope {
  def apply[A <: Message](payload: A): Envelope[A] = Envelope(payload, java.net.InetAddress.getLocalHost.toString)
}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit def envelopeFormat[A <: Message : JsonFormat] = jsonFormat2(Envelope.apply[A])
  implicit val personFormat = jsonFormat2(Person)
}

object Db {
  def getPerson(name: String): Future[Option[Person]] = {
    name match {
      case "kot" => Future.successful(Some(Person(name, 41)))
      case _ => Future.failed(new Exception(s"Person with name '$name' was not found."))
    }
  }
}
object WebServer extends JsonSupport {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("web-server-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route =
      get {
        path("hello") {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello</h1>"))
        } ~
        pathPrefix("person" / ".*".r) { name: String =>
          rejectEmptyResponse {
            complete {
              Db.getPerson(name).map(x => x.map(x => Envelope(x)))
            }
          }
        }
      }

    val port = 8091
    val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", port)
    println(s"Server online at http://localhost:$port. Press Return to stop...")
  }
}
