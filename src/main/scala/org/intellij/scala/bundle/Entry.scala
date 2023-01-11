package org.intellij.scala.bundle

import java.io.InputStream

/**
  * @author Pavel Fatin
  */
final case class Entry(name: String, size: Long, lastModified: Long, mode: Option[Int], link: Option[String], input: Option[InputStream]) {
  def isFile: Boolean = input.isDefined

  def isDirectory: Boolean = !isFile
}
