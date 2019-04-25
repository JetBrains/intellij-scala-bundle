package org.intellij.scala.bundle

import org.intellij.scala.bundle.Component._

/**
  * @author Pavel Fatin
  */
class Component private (val location: String, name: Option[String]) {
  def downloadable: Boolean = location.startsWith("http")

  def path: String = name.getOrElse(if (downloadable) nameIn(location) else location)
}

object Component {
  def apply(location: String) = new Component(location, None)

  def apply(location: String, name: String) = new Component(location, Some(name))

  private def nameIn(location: String): String = location.substring(location.lastIndexOf('/').max(location.lastIndexOf('=')) + 1)
}