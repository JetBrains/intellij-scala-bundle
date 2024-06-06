//> using scala 3.3.3
//> using dep org.apache.commons:commons-compress:1.26.2
//> using dep commons-io:commons-io:2.16.1

// requires tar and 7z for packing/unpacking, and create-dmg for creating the Mac install package

import WhereAmI.error

import java.nio.file.Path
import scala.util.chaining.*

@main
def main(): Unit = {
  prepareLinux()
  prepareWindows()
  prepareMac()
}

def prepareLinux(): Unit =
  val linux = Component.IdeaLinux
  val linuxPath = Component.get(linux)
  linuxPath.foreach { p =>
    FileOperations.delete(linux.tempDir)
    val isUnpacked = ZipOperations.unpack(p, linux.tempDir)
    if isUnpacked then
      linux.toDelete.foreach(str => FileOperations.delete(linux.contentsPath.resolve(str)))
      linux.toExec.foreach(str => FileOperations.makeExecutable(linux.contentsPath.resolve(str)))
      addPlugin(linux.contentsPath)
      addScala(linux.contentsPath)
      ZipOperations.packLinux(linux)
    else
      error(s"Unpacking failed: $p")
  }

def prepareWindows(): Unit =
  val windows = Component.IdeaWindows
  val windowsPath = Component.get(windows)
  windowsPath.foreach { p =>
    FileOperations.delete(windows.tempDir)
    val isUnpacked = ZipOperations.unpack(p, windows.tempDir)
    if isUnpacked then
      windows.toDelete.foreach(str => FileOperations.delete(windows.contentsPath.resolve(str)))
      windows.toExec.foreach(str => FileOperations.makeExecutable(windows.contentsPath.resolve(str)))
      addPlugin(windows.contentsPath)
      addScala(windows.contentsPath)
      ZipOperations.packWindows(windows)
    else
      error(s"Unpacking failed: $p")
  }

def prepareMac(): Unit =
  val mac = Component.IdeaMac
  val macPath = Component.get(mac)
  macPath.foreach { p =>
    FileOperations.delete(mac.tempDir)
    val isUnpacked = ZipOperations.unpack(p, mac.tempDir)
    if isUnpacked then
      mac.toDelete.foreach(str => FileOperations.delete(mac.contentsPath.resolve(str)))
      mac.toExec.foreach(str => FileOperations.makeExecutable(mac.contentsPath.resolve(str)))
      addPlugin(mac.contentsPath.resolve("Contents"))
      addScala(mac.contentsPath.resolve("Contents"))
      ZipOperations.packMac(mac)
    else
      error(s"Unpacking failed: $p")
  }

def addPlugin(contentsPath: Path): Unit = {
  val plugin = Plugin.ScalaPlugin
  val pluginPath = Plugin.get(plugin)
  val res = pluginPath.fold(false) { path =>
    ZipOperations.unpack(path, contentsPath.resolve("plugins"))
  }
  println(s"Adding the plugin ${plugin.source} ${if res then "successful" else "failed"}")
}

def addScala(contentsPath: Path): Unit = {
  val plugin = Plugin.Scala
  val pluginPath = Plugin.get(plugin)
  val res = pluginPath.fold(false) { path =>
    ZipOperations.unpack(path, contentsPath.resolve("scala"))
  }
  println(s"Adding Scala ${plugin.source} ${if res then "successful" else "failed"}")
}