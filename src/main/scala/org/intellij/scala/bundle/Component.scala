package org.intellij.scala.bundle

/**
  * @author Pavel Fatin
  */
case class Component(location: String) {
  def downloadable: Boolean = location.startsWith("http")

  def path: String = if (downloadable) location.substring(location.lastIndexOf('/').max(location.lastIndexOf('=')) + 1) else location
}
