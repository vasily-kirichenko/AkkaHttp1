import com.typesafe.sbt.SbtAspectj._

name := "AkkaHttp1"
version := "1.29"
scalaVersion := "2.11.8"
enablePlugins(DockerPlugin)
enablePlugins(AspectJWeaver)
enablePlugins(AspectjRunner)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  // "-Ywarn-dead-code", // N.B. doesn't work well with the ??? hole
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Xfatal-warnings")

lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging)

dockerRepository := Some("10.70.16.194:5000")
dockerBaseImage := "anapsix/alpine-java"

resolvers += "Kamon repo" at "http://repo.kamon.io"

libraryDependencies ++= Seq(
  "kamon-core",
  "kamon-akka",
  "kamon-scala",
  "kamon-jdbc",
  "kamon-influxdb",
  "kamon-system-metrics",
  "kamon-akka-http-experimental"
  //"kamon-log-reporter",
  //"kamon-statsd"
).map("io.kamon" %% _ % "0.6.2")

val akkaV = "2.4.8"

libraryDependencies ++= Seq(
  "akka-actor",
  "akka-stream",
  "akka-http-experimental",
  "akka-http-testkit")
  .map("com.typesafe.akka" %% _ % "2.4.8")

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"

libraryDependencies ++= Seq(
  "slick",
  "slick-extensions",
  "slick-hikaricp"
).map("com.typesafe.slick" %% _ % "3.1.0")

libraryDependencies += "com.zaxxer" % "HikariCP" % "2.4.7"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.21"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % "0.4.1")

resolvers += Resolver.bintrayRepo("hseeberger", "maven")
libraryDependencies += "de.heikoseeberger" %% "akka-http-circe" % "1.9.0"

mainClass in Compile := Some("WebServer")
aspectjSettings
javaOptions <++= AspectjKeys.weaverOptions in Aspectj
fork in Docker := false
fork in run := false