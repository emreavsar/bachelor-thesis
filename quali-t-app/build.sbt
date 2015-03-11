name := """quali-t"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)

herokuAppName in Compile := "quali-t"


resolvers += "rubygems-release" at "http://rubygems-proxy.torquebox.org/releases"

libraryDependencies ++= Seq(
  "rubygems" % "travis" % "1.7.1" excludeAll(ExclusionRule("rubygems", "pry", "*"), ExclusionRule("rubygems", "ffi", "*")),
  "rubygems" % "pry" % "0.9.12.6",
  "rubygems" % "ffi" % "1.9.3"
)