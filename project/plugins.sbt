logLevel := Level.Warn
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/maven-releases/"
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0-M4")
addSbtPlugin("io.spray" % "sbt-revolver" % "0.8.0")