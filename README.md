[![official JetBrains project](http://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/JetBrains/intellij-scala)

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
|2020-03-17|[Windows](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2020-03-17/intellij-scala-bundle-2020-03-17-windows.zip), [Mac](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2020-03-17/intellij-scala-bundle-2020-03-17-osx.dmg), [Linux](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2020-03-17/intellij-scala-bundle-2020-03-17-linux.tar.gz)|193.6911.18|2019.3.27|11.0.6+8-b520.43|2.13.1|
|2019-09-17|[Windows](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2019-09-17/intellij-scala-bundle-2019-09-17-windows.zip), [Mac](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2019-09-17/intellij-scala-bundle-2019-09-17-osx.dmg), [Linux](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2019-09-17/intellij-scala-bundle-2019-09-17-linux.tar.gz)|192.6603.28|2019.2.28|11.0.3+12-b304.56|2.13.0|
|2019-05-02|[Windows](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2019-05-02/intellij-scala-bundle-2019-05-02-windows.zip), [Mac](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2019-05-02/intellij-scala-bundle-2019-05-02-osx.dmg), [Linux](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2019-05-02/intellij-scala-bundle-2019-05-02-linux.tar.gz)|191.6707.61|2019.1.7|1.8.0_202-release-1483-b37|2.12.8|
|2018-11-30|[Windows](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2018-11-30/intellij-scala-bundle-2018-11-30-windows.zip), [Mac](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2018-11-30/intellij-scala-bundle-2018-11-30-osx.dmg), [Linux](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2018-11-30/intellij-scala-bundle-2018-11-30-linux.tar.gz)|183.4284.148|2018.3.4|8u152b1343.15|2.12.7|
|2018-09-14|[Windows](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2018-09-14/intellij-scala-bundle-2018-09-14-windows.zip), [Mac](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2018-09-14/intellij-scala-bundle-2018-09-14-osx.dmg), [Linux](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2018-09-14/intellij-scala-bundle-2018-09-14-linux.tar.gz)|182.4323.46|2018.2.11|8u152b1248.8|2.12.6|
|2018-05-04|[Windows](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2018-05-04/intellij-scala-bundle-2018-05-04-windows.zip), [Mac](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2018-05-04/intellij-scala-bundle-2018-05-04-osx.dmg), [Linux](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2018-05-04/intellij-scala-bundle-2018-05-04-linux.tar.gz)|181.4668.68|2018.1.9|8u152b1136.29|2.12.6|

By proceeding with use of the bundle, you understand that the parts of the bundle are governed by their separate license agreements, and do not form a single product ([more info](src/main/resources/patch/BundleAgreement.html)).

## Usage

Download, extract and run the application.

The bundle stores all its settings and caches inside the `data` subdirectory (except for Ivy and Maven repositories, which are system-wide).

You can use *File | Import Settings* and *File | Export Settings* to transfer the IDEA settings.

You can update or install IDEA plugins via *Settings | Plugins*.

The bundle doesn't include Git binaries (though it includes Git and GitHub integration). You may [install Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) separately, if needed.

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

Distributors of the bundle must display the text of [bundle agreement](src/main/resources/patch/BundleAgreement.html) (or link to that text) on download page.

Please note that this bundle is not supposed to replace the usual way people download, install, configure and update each of those components. The main goal of the bundle is to provide a bootstrap distribution for educational purposes ([more on intended use cases](https://youtrack.jetbrains.com/issue/SCL-11406)).