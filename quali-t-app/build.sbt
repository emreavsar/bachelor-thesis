name := "quali-t"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"


libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa.exclude("org.hibernate.javax.persistence", "hibernate-jpa-2.0-api"),
  javaWs,
  "org.hibernate"             %   "hibernate-entitymanager" % "4.3.6.Final",
  "javax.validation"          %   "validation-api"          % "1.1.0.Final",
  "org.postgresql"            %   "postgresql"              % "9.4-1201-jdbc41",
  "joda-time"                 %   "joda-time"               % "2.7",
  "org.jadira.usertype"       %   "usertype.core"           % "3.2.0.GA",
  "commons-lang"              %   "commons-lang"            % "2.6"
)

// HEROKU sbt plugin
herokuJdkVersion in Compile := "1.8"

herokuAppName in Compile := "quali-t"

herokuProcessTypes in Compile := Map(
  "web" -> "target/universal/stage/bin/quali-t -Dhttp.port=${PORT} -DapplyEvolutions.default=true -DapplyDownEvolutions.default=true -Ddb.default.driver=org.postgresql.Driver -Ddb.default.url=${DATABASE_URL}"
)

herokuIncludePaths in Compile := Seq(
  "app", "conf", "public"
)