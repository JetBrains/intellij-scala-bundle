package org.intellij.scala.bundle

import java.io.File
import java.net.URL

import org.intellij.scala.bundle.Descriptor._
import org.intellij.scala.bundle.Mapper.{matches, _}

import scala.Function._
import scala.util.matching.Regex

/**
  * @author Pavel Fatin
  */
object Main {
  private val Application = s"intellij-scala-bundle-${Versions.Bundle}"
  private val MacHostProperties = new File("mac-host.properties")

  def main(args: Array[String]): Unit = {
    val target = file("./target")

    val repository = target / "repository"

    repository.mkdir()

    Components.All.filter(_.downloadable).par.foreach { component =>
      downloadComponent(repository, component)
    }

    val commands = Seq(
      () => build(repository, Components.All, Descriptors.Windows)(target / s"$Application-windows.zip"),
      () => build(repository, Components.All, Descriptors.Linux)(target / s"$Application-linux.tar.gz"),
      () => build(repository, Components.All, Descriptors.Mac)(target / s"$Application-osx.tar.gz"),
    )

    commands.par.foreach(_.apply())

    if (MacHostProperties.exists) {
      println("Creating a signed disk image for OSX...")
      MacHost.createSignedDiskImage(Application, Versions.Idea, MacHostProperties)
      (target / s"$Application-osx.tar.gz").delete()
    } else {
      System.err.println(s"Warning: $MacHostProperties is not present, won't create a signed disk image for OSX.")
      System.err.println("See mac-host.properties.example")
    }

    info(s"Done.")
  }

  private object Versions {
    private val properties = propertiesIn(file("version.properties"))
    import properties.{getProperty => valueOf}

    val Bundle = valueOf("Bundle")

    val Idea = valueOf("Idea")
    val IdeaWindows = valueOf("IdeaWindows") // for idea.exe only
    val ScalaPlugin = valueOf("ScalaPlugin")
    val Sdk = valueOf("Sdk")
    val Scala = valueOf("Scala")
  }

  private object Components {
    object Idea {
      val Bundle = Component(s"https://www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/idea/ideaIC/${Versions.Idea}/ideaIC-${Versions.Idea}.zip")
      val Windows = Component(s"https://download.jetbrains.com/idea/ideaIC-${Versions.IdeaWindows}.win.zip")
      val ScalaPlugin = Component(s"https://plugins.jetbrains.com/plugin/download?pluginId=org.intellij.scala&version=${Versions.ScalaPlugin}", s"scala-intellij-bin-${Versions.ScalaPlugin}.zip")
      val Resources = Component("../../src/main/resources")
    }

    object Sdk {
      private val Pattern = new Regex("""(\d+)\.(\d+)\.(\d+)\+\d+-b(\d+)\.(\d+)""")

      val Windows = Component(s"https://bintray.com/jetbrains/intellij-jbr/download_file?file_path=jbrsdk-${format(Versions.Sdk, "windows")}.tar.gz")
      val Linux = Component(s"https://bintray.com/jetbrains/intellij-jbr/download_file?file_path=jbrsdk-${format(Versions.Sdk, "linux")}.tar.gz")
      val Mac = Component(s"https://bintray.com/jetbrains/intellij-jbr/download_file?file_path=jbrsdk-${format(Versions.Sdk, "osx")}.tar.gz")

      private def format(version: String, os: String) = version match {
        case Pattern(n1, n2, n3, n4, n5) => s"${n1}_${n2}_$n3-$os-x64-b$n4.$n5"
        case v => throw new IllegalArgumentException("Version " + v + "doesn't match " + Pattern.pattern.pattern())
      }
    }

    object Scala {
      val Windows = Component(s"https://downloads.lightbend.com/scala/${Versions.Scala}/scala-${Versions.Scala}.zip")
      val Unix = Component(s"https://downloads.lightbend.com/scala/${Versions.Scala}/scala-${Versions.Scala}.tgz")
      val Sources = Component(s"https://github.com/scala/scala/archive/v${Versions.Scala}.tar.gz")
    }

    val Repository = Component("./")

    val All = Seq(
      Idea.Bundle, Idea.Windows, Idea.ScalaPlugin, Idea.Resources,
      Sdk.Windows, Sdk.Linux, Sdk.Mac,
      Scala.Windows, Scala.Unix, Scala.Sources,
      Repository
    )
  }

  private object Descriptors {
    import Components._

    private val Common: Descriptor = {
      case Idea.Bundle =>
        matches("bin/appletviewer\\.policy") |
          matches("bin/log\\.xml") |
          matches("lib/platform-impl\\.jar") & repack("lib/platform-impl.jar", 0) { (source, destination) =>
            source.collect(-(matches("com/intellij/ui/AppUIUtil.class") | matches("com/intellij/idea/StartupUtil.class"))).foreach(destination(_))
            using(Source(file("./src/main/resources/patch")))(_.collect(
              matches("AppUIUtil.class") & to("com/intellij/ui/") |
              matches("BundleStartupListener.*\\.class") & to("com/intellij/idea/") |
              matches("StartupListener.class") & to("com/intellij/idea/") |
              matches("StartupPhase.class") & to("com/intellij/idea/") |
              matches("StartupUtil.class") & to("com/intellij/idea/") |
              matches("BundleAgreement.html") & to("com/intellij/idea/")).foreach(destination(_)))
          } |
          matches("lib/.*") - matches("lib/libpty.*") - matches("lib/platform-impl.jar") |
          matches("license/.*") |
          matches("plugins/(git4idea|github|java|java-ide-customization|junit|IntelliLang|maven|properties|terminal)/.*") |
          matches("build.txt") |
          matches("product-info.json") |
          matches("LICENSE.txt") |
          matches("NOTICE.txt")
      case Idea.ScalaPlugin =>
        to("data/plugins/")
      case Repository =>
        matches(Scala.Sources.path) & repack("scala-library.zip", 9, from(s"scala-${Versions.Scala}/src/library/")) & to("scala/src/")
      case Idea.Resources =>
        matches("data/.*") |
          from("patch/BundleAgreement.html") & to("README.html") |
          matches("bundle.txt")
    }

    private val WindowsSpecific: Descriptor = {
      case Idea.Bundle =>
        from("bin/win/") & to("bin/") |
          matches("bin/.*\\.(bat|dll|exe|ico)") |
          matches("lib/libpty/win/.*")
      case Idea.Windows =>
        matches("bin/idea64.exe")
      case Sdk.Windows =>
        from("jbrsdk/") & to("jbr/")
      case Scala.Windows =>
        from(s"scala-${Versions.Scala}/") & to("scala/")
      case Idea.Resources =>
        matches("IDEA.lnk")
    }

    private val LinuxSpecific: Descriptor = {
      case Idea.Bundle =>
        from("bin/linux/") & to("bin/") |
          matches("bin/.*\\.(py|sh|png)") | matches("bin/fsnotifier") |
          matches("lib/libpty/linux/.*")
      case Sdk.Linux =>
        from("jbrsdk/") & to("jbr/")
      case Scala.Unix =>
        from(s"scala-${Versions.Scala}/") & to("scala/")
      case Idea.Resources =>
        matches("idea.sh") & edit(_.replaceAll("\r", ""))
    }

    private val MacSpecific: Descriptor = {
      case Idea.Bundle =>
        from("bin/mac/") & to("bin/") |
          matches("bin/.*\\.(py|sh|dylib)") - matches("bin/idea\\.sh") - matches("bin/restart\\.py") |
          matches("bin/fsnotifier") |
          matches("bin/restarter") |
          matches("MacOS/.*") |
          matches("Resources/.*") |
          matches("Info\\.plist") |
          matches("lib/libpty/macosx/.*")
      case Sdk.Mac =>
        from("jbrsdk/") & to("jbr/")
      case Scala.Unix =>
        from(s"scala-${Versions.Scala}/") & to("scala/")
    }

    private def Patches(separator: String): Descriptor = {
      case Idea.Bundle =>
        matches("bin/idea\\.properties") & edit(_ + IdeaPropertiesPatch.replaceAll("\n", separator)) |
          any
      case Idea.Resources =>
        matches("bundle.txt") & edit(const(BundleTxt.replaceAll("\n", separator))) |
          any
      case _ => any
    }

    private def Repack: Descriptor = {
//      case _ =>
//        matches(".*\\.(jar|zip)") & repack(any) |
//          any
      case _ => any
    }

    private val IdeaPropertiesPatch: String = "\n" +
      "idea.startup.listener=com.intellij.idea.BundleStartupListener\n\n" +
      "idea.config.path=${idea.home.path}/data/config\n\n" +
      "idea.system.path=${idea.home.path}/data/system\n\n" +
      "idea.plugins.path=${idea.home.path}/data/plugins\n      "

    private def BundleTxt =
      s"IntelliJ Scala Bundle ${Versions.Bundle}:\n" +
        s"* IntelliJ IDEA ${Versions.Idea}\n" +
        s"* Scala Plugin ${Versions.ScalaPlugin}\n" +
        s"* JetBrains SDK ${Versions.Sdk}\n" +
        s"* Scala ${Versions.Scala}\n\n" +
        s"See https://github.com/JetBrains/intellij-scala-bundle for more info."

    private val MacPatches: Descriptor = {
      val toResources = matches("[^/]+\\.(txt|json|html)") & to("Resources/")

      {
        case Idea.Bundle =>
          matches("Info\\.plist") & edit(_.replaceFirst("NoJavaDistribution", "jdk-bundled")) |
            toResources |
            any
        case Idea.Resources =>
          matches("data/config/options/jdk\\.table\\.xml") &
            edit(_.replaceAll("\\$APPLICATION_HOME_DIR\\$/jbr", "\\$APPLICATION_HOME_DIR\\$/jdk/Contents/Home")) |
            toResources |
            any
        case _ => any
      }
    }

    private val Permissions: Descriptor = {
      case _ =>
        (matches("bin/.*\\.(sh|py)") |
          matches("bin/fsnotifier(|-arm|64)") |
          matches("bin/restarter") |
          matches("MacOS/idea") |
          matches("idea.sh")) & setMode(100755) | any
    }

    val Windows: Descriptor = ((Common | WindowsSpecific) & Repack & Patches("\r\n")).andThen(_ & to(s"$Application/"))

    val Linux: Descriptor  = ((Common | LinuxSpecific) & Repack & Patches("\n") & Permissions).andThen(_ & to(s"$Application/"))

    val Mac: Descriptor = ((Common | MacSpecific) & Repack & Patches("\n") & MacPatches & Permissions).andThen(_ & to(s"$Application.app/Contents/"))
  }

  private def build(base: File, components: Seq[Component], descriptor: Descriptor)(output: File) {
    info(s"Building ${output.getName}...")

    using(Destination(output)) { destination =>
      components.foreach { component =>
        descriptor.lift(component).foreach { mapper =>
          using(Source(base / component.path)) { source =>
            source.collect(mapper).foreach(destination(_))
          }
        }
      }
    }
  }

  private def downloadComponent(base: File, component: Component): Unit = {
    val destination = base / component.path

    if (!destination.exists) {
      info(s"Downloading ${component.path}...")
      download(new URL(component.location), destination)
      if (!destination.exists) {
        error(s"Error downloading ${component.location}")
        sys.exit(-1)
      }
    }
  }
}
