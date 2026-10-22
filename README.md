[![obsolete JetBrains project](https://jb.gg/badges/obsolete.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/JetBrains/intellij-scala)

> **Project retired.**
>
> IntelliJ Scala Bundle is a discontinued experimental distribution. This repository is archived and retained for historical reference. No further development or maintenance of the standalone bundle is planned.
>
> The installers below are historical, unsupported downloads.
>
> **Scala support in IntelliJ IDEA continues to be actively developed.**
>
> For current use, install IntelliJ IDEA with the Scala plugin.
>
> Here are the current project resources:
>
> - **Getting started:** [Installation instructions](https://www.jetbrains.com/help/idea/get-started-with-scala.html), [IntelliJ IDEA downloads](https://www.jetbrains.com/idea/download/), and [Scala plugin on JetBrains Marketplace](https://plugins.jetbrains.com/plugin/1347-scala).
> - **Documentation:** [Scala support in IntelliJ IDEA](https://www.jetbrains.com/idea/help/scala.html) and the [Scala plugin overview](https://www.jetbrains.com/scala/).
> - **Bug reports and feature requests:** Please use the [Scala plugin’s YouTrack project](https://youtrack.jetbrains.com/issues/SCL#newissue) for issues affecting current versions of the plugin.
> - **Questions and discussions:** You’re welcome to join us on [Discord](https://discord.gg/aUKpZzeHCK).
> - **Source code and contributions:** [Scala plugin repository](https://github.com/JetBrains/intellij-scala) and [contribution guide](https://github.com/JetBrains/intellij-scala/blob/idea263.x/CONTRIBUTING.md).
> - **News, release announcements, and articles:** [IntelliJ Scala blog](https://blog.jetbrains.com/scala/).
> - **Social updates:** [Bluesky](https://bsky.app/profile/scala.intellijidea.com) and [X/Twitter](https://x.com/IntelliJScala).

# IntelliJ Scala Bundle

A bundle that includes:

1. [IntelliJ IDEA Community](https://github.com/JetBrains/intellij-community) with selected plugins ([downloads](https://www.jetbrains.com/idea/download/)).
2. [Scala plugin](https://github.com/JetBrains/intellij-scala) for IntelliJ IDEA ([downloads](https://plugins.jetbrains.com/plugin/1347-scala)).
3. [SBT launcher](https://github.com/sbt/launcher), as a part of the Scala plugin ([downloads](https://dl.bintray.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/)).
4. [JetBrains SDK](https://confluence.jetbrains.com/display/JBR/JetBrains+Runtime) ([downloads](https://bintray.com/jetbrains/intellij-jbr/)).
5. JetBrains SDK sources.
6. [Scala](https://github.com/scala/scala) binaries ([downloads](https://www.scala-lang.org/download/)).
7. [Scala Standard Library](https://github.com/scala/scala/tree/2.13.x/src/library) sources ([downloads](https://www.scala-lang.org/download/)).
8. Example Scala project.
9. IntelliJ IDEA settings for all the above.

The bundle is:

* *One-click.* It doesn't require installation, let alone administrative privileges. You can simply extract the archive and run the application.
* *Pre-configured.* It doesn't require initial configuration. The default settings already include all the bundled components.
* *Self-sufficient.* It doesn't need anything besides the OS, yet you may use external dependencies, if you want to.
* *Self-contained.* It affects neither OS-level nor user-level settings. For example, you may have the exact version of IDEA in the system and the settings won't collide.
* *Location-independent.* The directory can be moved to other location.
* *Offline.* It's functional without the Internet. You can create, update, compile and run IDEA-based projects, browse Scala- and Java library sources – all without an Internet connection (but you need the Internet for SBT-based projects).

In comparison (macOS):

|            |Installer| Bundle  | VS  |
|:-----------|:-------:|:-------:|:---:|
|Internet use|869,2 MB | 343,9 MB|1/2  |
|Disk use    |1621,5 MB| 640,8 MB|1/3  |
|Time        |>15 min. | <1 min. |1/50 |
|Clicks      |~100     | ~1      |1/100|

Presentation of the IntelliJ Scala Bundle: [Enabling the first step (ScalaSphere)](https://www.youtube.com/watch?v=YDKrwYgQsB8).

## Download

|Version|Download|IntelliJ IDEA|Scala Plugin|SDK|Scala|
|:-----:|:------:|:------------|:-----------|:--|:----|
|2020-04-23|[Windows](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2020-04-23/intellij-scala-bundle-2020-04-23-windows.zip), [Mac](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2020-04-23/intellij-scala-bundle-2020-04-23-osx.dmg), [Linux](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2020-04-23/intellij-scala-bundle-2020-04-23-linux.tar.gz)|201.6668.121|2020.1.27|11.0.6+8-b765.25|2.13.1|

By proceeding with use of the bundle, you understand that the parts of the bundle are governed by their separate license agreements, and do not form a single product ([more info](src/main/resources/data/config/options/BundleAgreement.html)).

## Usage

Download, extract and run the application.

The bundle stores all its settings and caches inside the `data` subdirectory (except for Ivy and Maven repositories, which are system-wide).

You can use *File | Import Settings* and *File | Export Settings* to transfer the IDEA settings.

You can update or install IDEA plugins via *Settings | Plugins*.

Although the bundle doesn't include Git binaries, it includes Git and GitHub integrations, so you can [install Git from the IDE](https://blog.jetbrains.com/idea/2020/03/intellij-idea-2020-1-eap7/#install_git_from_the_ide), if needed.

## Custom Builds

In addition to using the above downloads, you can create your own version of the bundle from scratch:

1. Clone the project:

```
  $ git clone https://github.com/JetBrains/intellij-scala-bundle
```

2. Start the assembly:

```
  $ sbt run
```

It's possible to include more example projects in the bundle.

Distributors of the bundle must display the text of [bundle agreement](src/main/resources/data/config/options/BundleAgreement.html) (or link to that text) on download page.

Please note that this bundle is not supposed to replace the usual way people download, install, configure and update each of those components. The main goal of the bundle is to provide a bootstrap distribution for educational purposes ([more on intended use cases](https://youtrack.jetbrains.com/issue/SCL-11406)).
