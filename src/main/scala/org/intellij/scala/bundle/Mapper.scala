package org.intellij.scala.bundle

import java.io._

import org.apache.commons.compress.utils.IOUtils

import scala.util.matching.Regex

/**
  * @author Pavel Fatin
  */
object Mapper {
  type Mapper = PartialFunction[Entry, Entry]

  implicit class MapperExt(val mapper: Mapper) extends AnyVal {
    def unary_-(): Mapper = {
      case entry if !mapper.isDefinedAt(entry) => entry
    }

    def -(other: Mapper): Mapper = {
      case entry if mapper.isDefinedAt(entry) && !other.isDefinedAt(mapper(entry)) => mapper(entry)
    }

    def |(other: Mapper): Mapper = {
      case entry if mapper.isDefinedAt(entry) => mapper(entry)
      case entry if other.isDefinedAt(entry) => other(entry)
    }

    def &(other: Mapper): Mapper = {
      case entry if mapper.isDefinedAt(entry) && other.isDefinedAt(entry) => other(mapper(entry))
    }
  }

  def any: Mapper = {
    case entity => entity
  }

  def matches(pattern: String): Mapper = {
    val regex = new Regex(pattern)

    {
      case entry if regex.pattern.matcher(entry.name).matches => entry
    }
  }

  def from(path: String): Mapper = {
    case entry if entry.name.startsWith(path) && (entry.isFile || entry.name != path) =>
      info(s"from ${entry.name} to ${entry.name.stripPrefix(path)} (path: $path)")
      entry.copy(name = entry.name.stripPrefix(path))
  }

  def to(path: String): Mapper = {
    case entry =>
      entry.copy(name = path + entry.name)
  }

  def edit(editor: String => String): Mapper = {
    case entry =>
      val output = new ByteArrayOutputStream()
      IOUtils.copy(entry.input.get, output)
      val s = editor(new String(output.toByteArray))
      entry.copy(size = s.length, input = Some(new ByteArrayInputStream(s.getBytes)))
  }

  def repack(name: String, compressionLevel: Int, mapper: Mapper): Mapper = {
    repack(name, compressionLevel)((source, destination) => source.collect(mapper).foreach(destination(_)))
  }

  def repack(name: String, compressionLevel: Int)(transfer: (Source, Destination) => Unit): Mapper = {
    case entry =>

      val sourceFile = File.createTempFile("repack", entry.name.replaceAll("\\\\|/", "-"))
      using(new BufferedOutputStream(new FileOutputStream(sourceFile)))(IOUtils.copy(entry.input.get, _))

      val destinationFile = File.createTempFile("repack", name.replaceAll("\\\\|/", "-"))
      using(Destination(destinationFile, compressionLevel))(destination => using(Source(sourceFile))(transfer(_, destination)))

      sourceFile.delete()
      destinationFile.deleteOnExit()

      entry.copy(
        name = name,
        size = destinationFile.length(),
        input = Some(new BufferedInputStream(new FileInputStream(destinationFile))))
  }

  def setMode(mode: Int): Mapper = {
    case entry => entry.copy(mode = Some(octalToDecimal(mode)))
  }
}
