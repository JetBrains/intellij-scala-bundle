package org.intellij.scala

import java.io._
import java.net.URL
import java.util.Properties

import org.apache.commons.compress.utils.IOUtils

/**
  * @author Pavel Fatin
  */
package object bundle {
  def info(message: String): Unit = println(message)

  def error(message: String): Unit = System.err.println(message)

  def file(path: String): File = new File(path)

  def octalToDecimal(i: Int): Int = Integer.parseInt(i.toString, 8)

  def using[A <: Closeable, B](a: A)(block: A => B): B = try {
    block(a)
  } finally {
    a.close()
  }

  def download(source: URL, destination: File): Unit = {
    try {
      val input = new BufferedInputStream(source.openConnection().getInputStream)
      val output = new BufferedOutputStream(new FileOutputStream(destination))
      IOUtils.copy(input, output)
      output.close()
      input.close()
    } catch {
      case _: Throwable =>
        if (!destination.delete()) {
          destination.deleteOnExit()
        }
    }
  }

  def copy(from: File, to: File): Unit = {
    try {
      val input = new BufferedInputStream(new FileInputStream(from))
      val output = new BufferedOutputStream(new FileOutputStream(to))
      IOUtils.copy(input, output)
      output.close()
      input.close()
    } catch {
      case _: Throwable =>
        if (!to.delete()) {
          to.deleteOnExit()
        }
    }
  }

  def propertiesIn(file: File): Properties = {
    val properties = new Properties()
    val input = new BufferedInputStream(new FileInputStream(file))
    try {
      properties.load(input)
      properties
    } finally {
      input.close()
    }
  }

  implicit class FileExt(val file: File) extends AnyVal {
    def /(path: String): File = new File(file, path)
  }
}
