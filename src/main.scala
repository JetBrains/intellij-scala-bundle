//> using scala 3.3.3
//> using dep org.apache.commons:commons-compress:1.26.2
//> using dep commons-io:commons-io:2.16.1

// requires scala-cli to run, tar and 7z to pack/unpack, and create-dmg to create the Mac install package

import WhereAmI.error

import java.nio.file.Path
import scala.util.chaining.*

@main
def main(): Unit =
  prepare(Component.IdeaLinux)
  prepare(Component.IdeaWindows)
  prepare(Component.IdeaMac)

def prepare(component: Component): Unit =
  Component.get(component).foreach { p =>
    FileOperations.delete(component.tempDir)
    val isUnpacked = ZipOperations.unpack(p, component.tempDir)
    if isUnpacked then
      component.toDelete.foreach(str => FileOperations.delete(component.contentsPath.resolve(str)))
      component.toExec.foreach(str => FileOperations.makeExecutable(component.contentsPath.resolve(str)))
      val contentsPath = component.typ match
        case ComponentType.Mac => component.contentsPath.resolve("Contents") // TODO: refactor this
        case _                 => component.contentsPath
      addPlugin(Plugin.ScalaPlugin, contentsPath.resolve("plugins"))
      addPlugin(Plugin.Scala, contentsPath.resolve("scala"))
      ZipOperations.pack(component)
    else
      error(s"Unpacking failed: $p")
  }

def addPlugin(plugin: Plugin, subDirPath: Path): Unit =
  val pluginPath = Plugin.get(plugin)
  val res = pluginPath.fold(false) { ZipOperations.unpack(_, subDirPath) }
  println(s"Adding $pluginPath to $subDirPath ${if res then "successful" else "failed"}")
