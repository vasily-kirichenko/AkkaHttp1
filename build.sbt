name := "AkkaHttp1"
version := "1.0"
scalaVersion := "2.11.8"
enablePlugins(DockerPlugin)

lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging)

libraryDependencies += "io.spray" %%  "spray-json" % "1.3.2"

val akkaV       = "2.4.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaV
)

mainClass in Compile := Some("WebServer")