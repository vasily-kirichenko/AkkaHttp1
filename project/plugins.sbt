logLevel := Level.Warn

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/maven-releases/"
resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
resolvers += "Kamon Releases" at "http://repo.kamon.io"

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0-M4")
addSbtPlugin("io.spray" % "sbt-revolver" % "0.8.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-aspectj" %  "0.10.6")
addSbtPlugin("io.kamon" % "aspectj-runner" % "0.1.3")
//addSbtPlugin("com.gilt.sbt" % "sbt-aspectjweaver" % "0.0.2")