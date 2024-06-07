import java.nio.file.{Files, Path, Paths}
import sys.process.*
import WhereAmI.error
import org.apache.commons.io.FileUtils
import org.apache.commons.compress.utils.IOUtils
import org.apache.commons.io.filefilter.TrueFileFilter

import java.io.{BufferedInputStream, BufferedOutputStream, File, FileOutputStream}
import java.net.URL
import scala.jdk.FunctionConverters.*
import scala.jdk.CollectionConverters.*

object FileOperations:
  def delete(path: Path): Unit =
    if path.toString == "." || path.toString == ".." then
      error("Don't try to delete the current directory")
    else if Files.exists(path) then
      println(s"deleting $path")
      if Files.isDirectory(path) then FileUtils.deleteDirectory(path.toFile)
      else FileUtils.delete(path.toFile)
    else
      error(s"File doesn't exist: $path")

  def makeExecutable(path: Path): Unit =
    def setExec(f: File): Unit = {
      println(s"setting ${f.toPath} executable")
      f.setExecutable(true)
    }
    
    if Files.isDirectory(path) then
      FileUtils
        .iterateFiles(path.toFile, TrueFileFilter.TRUE, TrueFileFilter.TRUE)
        .asScala
        .foreach(setExec)
    else if Files.exists(path) && !Files.isExecutable(path) then
      setExec(path.toFile)

  def download(downloadUrl: URL, destination: Path): Option[Path] =
    println(s"Downloading $downloadUrl to $destination")
    try {
      val input = new BufferedInputStream(downloadUrl.openConnection().getInputStream)
      val output = new BufferedOutputStream(new FileOutputStream(destination.toFile))
      IOUtils.copy(input, output)
      output.close()
      input.close()
      Some(destination)
    } catch {
      case t: Throwable =>
        if (!destination.toFile.delete()) destination.toFile.deleteOnExit()
        WhereAmI.error(t)
        None
    }
