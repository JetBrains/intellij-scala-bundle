import java.net.URL
import java.nio.file.{Files, Path, Paths}
import Properties.*

final case class Plugin(source:      String,
                        tmpDirStr:   String,
                        downloadStr: String):
  lazy val sourcePath:  Path = Paths.get(source)
  lazy val downloadUrl: URL  = new URL(downloadStr)
  lazy val tempDir:     Path = Paths.get(tmpDirStr)

object Plugin:
  def get(plugin: Plugin): Option[Path] =
    val p = CacheDir.resolve(plugin.sourcePath)
    if Files.exists(p) then 
      Some(p)
    else
      FileOperations.download(plugin.downloadUrl, p)

  val ScalaPlugin: Plugin = Plugin(
    s"scala-intellij-bin-$PluginVersion.zip",
    "ScalaPlugin",
    "https://plugins.jetbrains.com/plugin/download?rel=true&updateId=530536" // TODO
  )

  val Scala: Plugin = Plugin(
    s"scala3-$ScalaVersion.tar.gz",
    "Scala",
    s"https://github.com/scala/scala3/releases/download/3.3.3/scala3-$ScalaVersion.tar.gz"
  )
