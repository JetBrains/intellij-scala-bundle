package org.intellij.scala.bundle

import java.io.{BufferedOutputStream, Closeable, File, FileOutputStream}
import java.nio.file.attribute.FileTime

import org.apache.commons.compress.archivers.tar.{TarArchiveEntry, TarArchiveOutputStream, TarConstants}
import org.apache.commons.compress.archivers.zip.{ZipArchiveEntry, ZipArchiveOutputStream}
import org.apache.commons.compress.compressors.gzip.{GzipCompressorOutputStream, GzipParameters}
import org.apache.commons.compress.utils.IOUtils

/**
  * @author Pavel Fatin
  */
trait Destination extends Closeable {
  def apply(entry: Entry): Unit
}

object Destination {
  private val CompressionLevel = 9

  def apply(file: File): Destination = file.getName match {
    case name if name.endsWith(".zip") => new ZipDestination(file)
    case name if name.endsWith(".tar") | name.endsWith(".tar.gz") | name.endsWith(".tgz") => new TarDestination(file, compress = !name.endsWith(".tar"))
  }

  private class DirectoryDestination(directory: File) extends Destination {
    def apply(entry: Entry): Unit = {
      val file = directory / entry.name

      entry.input match {
        case Some(input) =>
          file.getParentFile.mkdirs()
          val output = new BufferedOutputStream(new FileOutputStream(file))
          IOUtils.copy(input, output)
          output.close()
        case None =>
          file.mkdirs()
      }

      file.setLastModified(entry.lastModified)
    }

    override def close(): Unit = {}
  }

  private class ZipDestination(file: File) extends Destination {
    private val output = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(file)))
    output.setLevel(CompressionLevel)

    def apply(entry: Entry): Unit = {
      if (entry.isDirectory) return

      val zipEntry = new ZipArchiveEntry(entry.name)
      zipEntry.setSize(entry.size)
      zipEntry.setLastModifiedTime(FileTime.fromMillis(entry.lastModified))

      output.putArchiveEntry(zipEntry)
      entry.input.foreach(IOUtils.copy(_, output))
      output.closeArchiveEntry()
    }

    override def close(): Unit = {
      output.close()
    }
  }

  private class TarDestination(file: File, compress: Boolean) extends Destination {
    private val output = {
      val parameters = new GzipParameters()
      parameters.setCompressionLevel(CompressionLevel)

      val sink = new BufferedOutputStream(new FileOutputStream(file))
      val stream = new TarArchiveOutputStream(if (compress) new GzipCompressorOutputStream(sink) else sink)
      stream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU)
      stream
    }

    def apply(entry: Entry): Unit = {
      if (entry.isDirectory) return

      val tarEntry = entry.link.map { link =>
        val result = new TarArchiveEntry(entry.name, TarConstants.LF_SYMLINK)
        result.setLinkName(link)
        result
      } getOrElse {
        new TarArchiveEntry(entry.name)
      }
      tarEntry.setSize(entry.size)
      tarEntry.setModTime(entry.lastModified)
      entry.mode.foreach(tarEntry.setMode)

      output.putArchiveEntry(tarEntry)
      entry.input.foreach(IOUtils.copy(_, output))
      output.closeArchiveEntry()
    }

    override def close(): Unit = {
      output.close()
    }
  }
}
