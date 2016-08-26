package contracts

import akka.http.scaladsl.model.headers.HttpCredentials

case class Envelope[A](creds: Option[HttpCredentials], user: Option[String], payload: A)
