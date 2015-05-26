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
  "commons-lang"              %   "commons-lang"            % "2.6",
  "be.objectify"              %   "deadbolt-java_2.11"      % "2.3.3",
  "com.google.inject"         %   "guice"                   % "4.0-beta",
  "org.jsoup" % "jsoup" % "1.8.2",
  "org.apache.avalon.framework" % "avalon-framework-api" % "4.2.0" from "http://repo1.maven.org/maven2/avalon-framework/avalon-framework-api/4.2.0/avalon-framework-api-4.2.0.jar",
  "org.apache.avalon.framework" % "avalon-framework-impl" % "4.2.0" from "http://repo1.maven.org/maven2/avalon-framework/avalon-framework-impl/4.2.0/avalon-framework-impl-4.2.0.jar",
  "org.apache.xmlgraphics" % "fop" % "1.1"
)

// exclude unneeded files from build
excludeFilter in Assets := "node_modules"

// HEROKU sbt plugin
herokuJdkVersion in Compile := "1.8"

herokuAppName in Compile := "quali-t"

herokuProcessTypes in Compile := Map(
  "web" -> "target/universal/stage/bin/quali-t -Dconfig.file=./conf/heroku.conf -Dhttp.port=${PORT} -Ddb.default.url=${DATABASE_URL}"
)

herokuIncludePaths in Compile := Seq(
  "app", "conf", "public"
)

