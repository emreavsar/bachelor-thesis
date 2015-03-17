name := "quali-t"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"


libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa.exclude("org.hibernate.javax.persistence", "hibernate-jpa-2.0-api"),
  javaWs,
  "org.hibernate"             %  "hibernate-entitymanager" % "4.3.6.Final",
  "javax.validation" %    "validation-api"          % "1.1.0.Final"
)



//"postgresql"     %   "postgresql"              % "9.1-901.jdbc4"


herokuJdkVersion in Compile := "1.8"

herokuAppName in Compile := "quali-t"