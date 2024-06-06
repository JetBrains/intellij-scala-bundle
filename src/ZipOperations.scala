import java.nio.file.{Files, Path, Paths}
import sys.process.*
import WhereAmI.error

object ZipOperations {
  def unpack(source: Path, destination: Path): Boolean = {
    println(s"unpacking $source to $destination")
    if !Files.exists(destination) then
      println(s"creating directory $destination")
      Files.createDirectory(destination)
      destination.toFile.setReadable(true)
      destination.toFile.setWritable(true)
    val cmd = {
      val name = source.toString
      if name.endsWith(".zip") || name.endsWith(".dmg") then
        Some(s"7z x '$source' -o'$destination'/ ")
      else if name.endsWith("tar.gz") then
        Some(s"tar xvf '${source.toAbsolutePath}' -C '${destination.toAbsolutePath}' ")
      else
        error(s"Unrecognized suffix: $source")
        None
    }
    cmd.foreach { c =>
      println(s"cmd $c")
      val res = c.!!
      println(res)
    }
    cmd.isDefined // TODO: check if the command was executed with success
  }

  def packLinux(component: Component): Boolean = {
    val source = component.tempDir
    val destination = component.bundlePath
    println(s"packing $source to $destination")
    if !Files.exists(source) then
      error(s"Source doesn't exist: $source")
      false
    else {
      if Files.exists(destination) then
        FileOperations.delete(destination)
      val cmd = s"tar -C '$source' -czvf '$destination' '${component.mainSubDir}' "
      println(s"cmd: $cmd")
      val res = cmd.!!
      println(res)
      true // TODO: check if the command was executed with success
    }
  }

  def packWindows(component: Component): Boolean = {
    val source = component.tempDir
    val destination = component.bundlePath
    println(s"packing $source to $destination")
    if !Files.exists(source) then
      error(s"Source doesn't exist: $source")
      false
    else {
      if Files.exists(destination) then
        FileOperations.delete(destination)
      val cmd = s"7z a './$destination' './$source/*'"
      println(s"cmd: $cmd")
      val res = cmd.!!
      println(res)
      true // TODO: check if the command was executed with success
    }
  }

  def packMac(component: Component): Boolean = {
    val source = component.contentsPath
    val destination = component.bundlePath
    println(s"packing $source to $destination")
    if !Files.exists(source) then
      error(s"Source doesn't exist: $source")
      false
    else {
      if Files.exists(destination) then
        FileOperations.delete(destination)
      val cmd =
        s"""
           |create-dmg
           | --no-internet-enable
           | --background "resources/dmg_background.png"
           | --window-size 900 500
           | --app-drop-link 670 310
           | --icon "IntelliJ IDEA CE.app" 250 310
           | --hide-extension "IntelliJ IDEA CE.app"
           | --volname "IntelliJ Scala Bundle"
           | --volicon "resources/icon.icns"
           | '$destination'
           | '$source'
           |""".stripMargin.trim
      println(s"cmd: $cmd")
      val res = cmd.!!
      println(res)
      true // TODO: check if the command was executed with success
    }
  }
}
