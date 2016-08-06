import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import io.StdIn

object WebServer {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("web-server-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route =
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/xml(UTF-8)`, "<h1>Say hello</h1>"))
        }
      }

    val port = 8091
    val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", port)
    println(s"Server online at http://localhost:$port. Press Return to stop...")
//    StdIn.readLine()
//    bindingFuture
//      .flatMap(_.unbind())
//      .onComplete(_ => system.terminate())
  }
}
