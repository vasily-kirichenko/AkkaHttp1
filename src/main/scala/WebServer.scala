import java.util.UUID

import akka.http.scaladsl.model._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import kamon.Kamon
import spray.json.{DefaultJsonProtocol, JsonFormat}
import kamon.influxdb._
import scala.concurrent.Future
import scala.util.Random

trait Message
final case class Person(name: String, age: Int) extends Message
final case class Envelope[A <: Message](payload: A, server: String, appId: String)

object Envelope {
  private val id = UUID.randomUUID()
  def apply[A <: Message](payload: A): Envelope[A] =
    Envelope(payload, java.net.InetAddress.getLocalHost.toString, id.toString)
}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit def envelopeFormat[A <: Message : JsonFormat] = jsonFormat3(Envelope.apply[A])
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
    Kamon.start()

    implicit val system = ActorSystem("web-server-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val someHistogram = Kamon.metrics.histogram("some-histogram")
    someHistogram.record(Random.nextInt.abs.toLong)

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

    val port = 29001
    Http().bindAndHandle(route, "0.0.0.0", port).onComplete(_ => Kamon.shutdown())
    println(s"Server is online at http://localhost:$port")
  }
}
