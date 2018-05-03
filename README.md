[![official JetBrains project](http://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/JetBrains/intellij-scala)

# IntelliJ Scala Bundle

A proof of the concept script for creating a portable bundle with:

1. [IntelliJ IDEA Community](https://www.jetbrains.com/idea/), with selected plugins ([downloads](https://www.jetbrains.com/idea/download/)).
2. [Scala plugin](https://confluence.jetbrains.com/display/SCA/Scala+Plugin+for+IntelliJ+IDEA) for IntelliJ IDEA ([downloads](https://plugins.jetbrains.com/plugin/1347-scala)).
3. [SBT launcher](https://github.com/sbt/launcher), as a part of the Scala plugin ([downloads](https://dl.bintray.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/)).
4. [JetBrains SDK](https://github.com/JetBrains/jdk8u), ([downloads](https://bintray.com/jetbrains/intellij-jdk/)).
5. JetBrains SDK sources.
6. [Scala](https://www.scala-lang.org/) binaries ([downloads](https://www.scala-lang.org/download/)).
7. [Scala Standard Library](https://www.scala-lang.org/api/current/index.html) sources ([downloads](https://www.scala-lang.org/download/)).
8. Example Scala project.
9. IntelliJ IDEA settings for all the above.

To create the bundle invoke:

    $ sbt run

The created bundle is:

* *One-click.* It doesn't require installation, let alone administrative privileges. You can simply extract the archive and run the application.
* *Pre-configured.* It doesn't require initial configuration. The default settings already include all the bundled components.
* *Self-sufficient.* It doesn't need anything besides the OS, yet you may use external dependencies, if you want to.
* *Self-contained.* It affects neither OS-level nor user-level settings. For example, you may have the exact version of IDEA in the system and the settings won't collide.
* *Location-independent.* The directory can be moved to other location.
* *Offline.* It's functional without the Internet. You can create, update, compile and run IDEA-based projects, browse Scala- and Java library sources – all without an Internet connection (but you need the Internet for SBT-based projects).

In comparison (macOS):

|Measure |Installer| Bundle  |Diff.|
|:-------|:-------:|:-------:|:---:|
|Internet|869,2 MB | 343,9 MB|1/2  |
|Disk    |1621,5 MB| 640,8 MB|1/2  |
|Click   |~100     | ~1      |1/100|
|Time    |>15 min. | <1 min. |1/50 |

The bundle stores all its settings and caches inside the `data` subdirectory (except for Ivy and Maven repositories, which are system-wide).

You can freely update / install IDEA plugins.

It should be possible to include more example projects in the bundle.

Every distributor of the bundle must display text of the [bundle agreement](src/main/resources/BundleAgreement.html) (or link to that text) on the download page.

Please note that this bundle is not supposed to replace the usual way people download, install, configure and update each of those components. The main goal of the bundle is to provide a bootstrap distribution for educational purposes ([more on intended use cases](https://youtrack.jetbrains.com/issue/SCL-11406)).
