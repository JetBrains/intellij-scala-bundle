name := "intellij-scala-bundle"

organization := "org.jetbrains"

description := "IntelliJ Scala Bundle"

version := "1.0"

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

scalaVersion := "2.13.1"

libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0"
libraryDependencies += "org.apache.commons" % "commons-compress" % "1.20"
libraryDependencies += "com.hierynomus" % "sshj" % "0.27.0"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.8.0-beta4"

mainClass in Compile := Some("org.intellij.scala.bundle.Main")
