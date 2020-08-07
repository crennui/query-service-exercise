name := "query-service-exercise"

version := "0.1"

scalaVersion := "2.13.3"

mainClass in (Compile, run) := Some("service.Main")

val akkaHttpVersion = "10.1.12"
lazy val akkaVersion    = "2.5.26"

Compile / resourceDirectory := baseDirectory.value / "resources"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
  "ch.qos.logback"    % "logback-classic"           % "1.2.3",
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc4",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-codegen" % "3.3.2",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "com.h2database" % "h2" % "1.3.166",
  "org.postgresql" % "postgresql" % "42.2.14",
  "au.com.bytecode" % "opencsv" % "2.4",

  "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
  "org.scalatest"     %% "scalatest"                % "3.0.8"         % Test
)