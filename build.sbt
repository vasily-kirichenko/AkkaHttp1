name := "AkkaHttp1"
version := "1.0"
scalaVersion := "2.11.8"
enablePlugins(DockerPlugin)

lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging)

libraryDependencies += "com.typesafe.akka" %% "akka-http-experimental" % "2.4.9-RC1"
mainClass in Compile := Some("WebServer")