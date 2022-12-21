package org.intellij.scala.bundle

import org.intellij.scala.bundle.Component._

/**
  * @author Pavel Fatin
  */
final case class Component(location: String, name: Option[String]) {
  def downloadable: Boolean = location.startsWith("http")
  def path: String = name.getOrElse(if (downloadable) nameIn(location) else location)
}

object Component {
  def apply(location: String): Component = Component(location, None)

  def apply(location: String, name: String): Component = Component(location, Some(name))

  private def nameIn(location: String): String = location.substring(location.lastIndexOf('/').max(location.lastIndexOf('=')) + 1)
}