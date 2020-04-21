package org.intellij.scala.bundle

import java.io.{BufferedInputStream, Closeable, File, FileInputStream}
import java.nio.file.Files

import org.apache.commons.compress.archivers.tar.{TarArchiveEntry, TarArchiveInputStream}
import org.apache.commons.compress.archivers.zip.ZipFile
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream

import scala.jdk.CollectionConverters._

/**
  * @author Pavel Fatin
  */
trait Source extends Iterator[Entry] with Closeable

object Source {
  def apply(file: File): Source = file.getName match {
    case name if name.endsWith(".zip") | name.endsWith(".jar") => new ZipSource(file)
    case name if name.endsWith(".tar.gz") | name.endsWith(".tgz") => new TarGzSource(file)
    case _ => new DirectorySource(file)
  }

  private class DirectorySource(root: File) extends Source {
    private val files = Files.walk(root.toPath).iterator().asScala.filter(_.toFile.isFile)

    override def hasNext: Boolean = files.hasNext

    override def next(): Entry = {
      val file = files.next().toFile

      def format(path: String) = path.replace('\\', '/')

      Entry(format(file.getPath).stripPrefix(format(root.getPath) + "/"), file.length(), file.lastModified(), None, None, if (file.isDirectory) None else Some(new FileInputStream(file)))
    }

    def close(): Unit = {}
  }

  private class ZipSource(file: File) extends Source {
    private val zip = new ZipFile(file)
    private val entries = zip.getEntries

    override def hasNext: Boolean = entries.hasMoreElements

    override def next(): Entry = {
      val zipEntry = entries.nextElement()
      Entry(zipEntry.getName, zipEntry.getSize, zipEntry.getLastModifiedDate.getTime, None, None, if (zipEntry.isDirectory) None else Some(zip.getInputStream(zipEntry)))
    }

    def close(): Unit = {
      zip.close()
    }
  }

  private class TarGzSource(file: File) extends Source {
    private val input = new TarArchiveInputStream(new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(file))))

    private var nextEntry: Option[TarArchiveEntry] = None

    override def hasNext: Boolean = {
      nextEntry match {
        case Some(_) => true
        case None =>
          nextEntry = Option(input.getNextTarEntry)
          nextEntry.isDefined
      }
    }

    override def next(): Entry = {
      val tarEntry = nextEntry.get
      nextEntry = None
      val link = if (tarEntry.isSymbolicLink) Some(tarEntry.getLinkName) else None
      Entry(tarEntry.getName, tarEntry.getSize, tarEntry.getLastModifiedDate.getTime, Some(tarEntry.getMode), link, if (tarEntry.isDirectory) None else Some(input))
    }

    def close(): Unit = {
      input.close()
    }
  }
}
