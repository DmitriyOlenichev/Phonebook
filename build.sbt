name := "PhoneBook"

version := "1.0"

lazy val `phonebook` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  jdbc ,
  evolutions,
  cache ,
  ws   ,
  "com.typesafe.play" %% "anorm" % "2.5.3",
  "com.adrianhurt" %% "play-bootstrap" % "1.1-P25-B3",
  specs2 % Test
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"