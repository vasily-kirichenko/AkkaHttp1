import akka.http.scaladsl.model._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import kamon.Kamon

import scala.util.Random
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import akka.http.scaladsl.server.Directives._
import contracts.FormatType
import io.circe.generic.auto._
import kamon.metric.instrument.UnitOfMeasurement
import kamon.trace.{TraceContext, Tracer}
import kamon.util.executors.ExecutorServiceMetrics
import sun.misc.BASE64Decoder

object WebServer {
  def main(args: Array[String]): Unit = {
    Kamon.start()
    implicit val system = ActorSystem("web-server-system")
    implicit val materializer = ActorMaterializer()
    //implicit val executionContext = system.dispatcher

    val requestCounter = Kamon.metrics.counter("request")

    val route =
      get {
        extractCredentials { creds =>
          val user = creds map { creds => Utils.extractUserName(creds.params("")) }

          path("formatType" / IntNumber) { id: Int =>
            authorize(_ => true) {
              requestCounter.increment()
              rejectEmptyResponse {
                Tracer.setCurrentContext(Kamon.tracer.newContext("foo"))
                onSuccess(WlDb.getFormatById(id.toShort)) {
                  case Some(x) => complete(contracts.Envelope[FormatType](creds, user, x))
                  case None => complete(StatusCodes.NotFound)
                }
              }
            }
          }
        }
      }

    val port = 29001
    Http().bindAndHandle(route, "0.0.0.0", port)
    println(s"Server is online at http://localhost:$port")
  }
}
