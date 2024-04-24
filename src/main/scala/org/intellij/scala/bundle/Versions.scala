package org.intellij.scala.bundle

private object Versions {
  private val properties = propertiesIn(file("version.properties"))
  import properties.{getProperty => valueOf}

  lazy val Bundle = valueOf("Bundle")

  lazy val Idea = valueOf("Idea")
  lazy val IdeaWindows = valueOf("IdeaWindows") // for idea.exe only
  lazy val ScalaPlugin = valueOf("ScalaPlugin")
  lazy val Sdk = valueOf("Sdk")
  lazy val Scala = valueOf("Scala")
}