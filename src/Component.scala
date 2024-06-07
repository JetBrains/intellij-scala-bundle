import java.net.URL
import java.nio.file.{Files, Path, Paths}
import Properties.*
import ComponentType.*

enum ComponentType:
  case Linux, Windows, Mac
  
final case class Component(typ:          ComponentType,
                           source:       String,
                           bundleSuffix: String,
                           tmpDirStr:    String,
                           downloadStr:  String,
                           mainSubDir:   String = ".",
                           toDelete:     Seq[String] = Seq.empty,
                           toExec:       Seq[String] = Seq.empty):
  lazy val sourcePath:   Path = Paths.get(source)
  lazy val bundlePath:   Path = Paths.get(s"$BundleName.$bundleSuffix")
  lazy val contentsPath: Path = tempDir.resolve(mainSubDir)
  lazy val downloadUrl:  URL  = new URL(downloadStr)
  lazy val tempDir:      Path = Paths.get(tmpDirStr)

object Component:
  def get(component: Component): Option[Path] =
    val p = CacheDir.resolve(component.sourcePath)
    if Files.exists(p) then 
      Some(p)
    else
      FileOperations.download(component.downloadUrl, p)

  val IdeaLinux: Component = Component(
    Linux,
    s"ideaIC-$IdeaVersion.tar.gz",
    "tar.gz",
    "Linux",
    downloadStr = s"https://download-cdn.jetbrains.com/idea/ideaIC-$IdeaVersion.tar.gz",
    mainSubDir = "idea-IC-241.17011.79",
    toDelete = Seq(
      "plugins/plugin-classpath.txt",
      "plugins/Kotlin"
    ),
    toExec = Seq.empty
  )

  val IdeaWindows: Component = Component(
    Windows,
    s"ideaIC-$IdeaVersion.win.zip",
    "win.zip",
    "Windows",
    downloadStr = s"https://download-cdn.jetbrains.com/idea/ideaIC-$IdeaVersion.win.zip",
    toDelete = Seq(
      "plugins/plugin-classpath.txt",
      "plugins/Kotlin"
    ),
    toExec = Seq.empty
  )

  val IdeaMac: Component = Component(
    Mac,
    s"ideaIC-$IdeaVersion.dmg",
    "dmg",
    "Mac",
    downloadStr = s"https://download-cdn.jetbrains.com/idea/ideaIC-$IdeaVersion.dmg",
    mainSubDir = "IntelliJ IDEA CE/IntelliJ IDEA CE.app",
    toDelete = Seq(
      "Contents/plugins/plugin-classpath.txt",
      "Contents/plugins/Kotlin",
      "../.background",
      "../[HFS+ Private Data]",
      "../.DS_Store",
      "../Applications",
      "Contents/_CodeSignature"
    ),
    toExec = Seq(
      "Contents/MacOS/idea",
      "Contents/bin/format.sh",
      "Contents/bin/fsnotifier",
      "Contents/bin/inspect.sh",
      "Contents/bin/ltedit.sh",
      "Contents/bin/printenv",
      "Contents/bin/remote-dev-server",
      "Contents/bin/remote-dev-server.sh",
      "Contents/bin/repair",
      "Contents/bin/restarter",
      "Contents/jbr/Contents/Home/bin"
    )
  )