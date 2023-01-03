name := "intellij-scala-bundle"

organization := "org.jetbrains"

description := "IntelliJ Scala Bundle"

version := "1.0"

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

scalaVersion := "2.13.10"

libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"
libraryDependencies += "org.apache.commons" % "commons-compress" % "1.22"
libraryDependencies += "com.hierynomus" % "sshj" % "0.27.0"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.8.0-beta4"

Compile/mainClass := Some("org.intellij.scala.bundle.Main")
