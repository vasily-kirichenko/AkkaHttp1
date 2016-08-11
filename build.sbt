import com.typesafe.sbt.SbtAspectj._

aspectjSettings

name := "AkkaHttp1"
version := "1.3"
scalaVersion := "2.11.8"
enablePlugins(DockerPlugin)

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

resolvers += "Kamon repo" at "http://repo.kamon.io"

libraryDependencies += "io.kamon" %%  "kamon-core" % "0.6.2"
libraryDependencies += "io.kamon" %%  "kamon-akka" % "0.6.2"

libraryDependencies += "io.spray" %%  "spray-json" % "1.3.2"

val akkaV = "2.4.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaV
)

mainClass in Compile := Some("WebServer")
javaOptions <++= AspectjKeys.weaverOptions in Aspectj