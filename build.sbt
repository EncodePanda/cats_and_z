name := "cats_and_z"

scalaVersion := "2.11.8"

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.8.0")

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.0",
  "org.scalaz" %% "scalaz-concurrent" % "7.2.0",
  "org.typelevel" %% "cats" % "0.5.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

