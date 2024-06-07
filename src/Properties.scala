import java.nio.file.{Path, Paths}

object Properties:
  val BundleName:    String = "IntelliJ Scala Bundle"
  val PluginVersion: String = "2024.1.2"
  val ScalaVersion:  String = "3.3.3"
  val CacheDir:      Path   = Paths.get("cache")
