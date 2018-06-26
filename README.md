[![official JetBrains project](http://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/JetBrains/intellij-scala)

# IntelliJ Scala Bundle

A proof of the concept bundle that includes:

1. [IntelliJ IDEA Community](https://www.jetbrains.com/idea/), with selected plugins ([downloads](https://www.jetbrains.com/idea/download/)).
2. [Scala plugin](https://confluence.jetbrains.com/display/SCA/Scala+Plugin+for+IntelliJ+IDEA) for IntelliJ IDEA ([downloads](https://plugins.jetbrains.com/plugin/1347-scala)).
3. [SBT launcher](https://github.com/sbt/launcher), as a part of the Scala plugin ([downloads](https://dl.bintray.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/)).
4. [JetBrains SDK](https://github.com/JetBrains/jdk8u), ([downloads](https://bintray.com/jetbrains/intellij-jdk/)).
5. JetBrains SDK sources.
6. [Scala](https://www.scala-lang.org/) binaries ([downloads](https://www.scala-lang.org/download/)).
7. [Scala Standard Library](https://www.scala-lang.org/api/current/index.html) sources ([downloads](https://www.scala-lang.org/download/)).
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

## Download

* [intellij-scala-bundle-2018-05-04-windows.zip](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2018-05-04/intellij-scala-bundle-2018-05-04-windows.zip)
* [intellij-scala-bundle-2018-05-04-linux.tar.gz](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2018-05-04/intellij-scala-bundle-2018-05-04-linux.tar.gz)
* [intellij-scala-bundle-2018-05-04-osx.tar.gz](https://github.com/JetBrains/intellij-scala-bundle/releases/download/v2018-05-04/intellij-scala-bundle-2018-05-04-osx.tar.gz)

By proceeding with use of the bundle, you understand that the parts of the bundle are governed by their separate license agreements, and do not form a single product ([more info](src/main/resources/BundleAgreement.html)).


## Install the Bundle

1. After downloaded the bundle, extract the archive.
2. Move the extracted directory to a location where you keep your projects (e.g., Documents/Projects)

> For Mac Users:
 This bundle is currently not registered as an identified developer with the Mac Store.
  To use the bundle, you must override the **Security & Privacy** settings. 
 
 #### If you receive this message:
 
 > " ... can't be opened because it is from an unidentified developer" 
 
- Go to **Security & Privacy** settings on your computer under **System Preferences**
- Choose to **Open Anyways** to allow it to run **From unidentified developer**
 
 
**Alternatively**, to override your security settings and open the app anyway:
 
- In the Finder, locate the app you want to open. 
    (Don’t use Launchpad to do this. Launchpad doesn’t allow you to access the shortcut menu.)
- Press the **Control** key, then click the app icon.
- Choose **Open** from the shortcut menu.
- Click **Open**.
 
 The app is saved as an exception to your security settings, and you will be able to open it in the future by double-clicking it, just like any registered app.
 
 #### If you receive this message:
  
   >" The application is running in App Translocation, a macOS security mechanism for apps that are not properly installed. You cannot create permanent rules until you move the application to the Applications folder and launch it from there."
  
- Simply move the bundle to a different directory.
- To read more about **App Translocation**, please visit [here](https://developer.apple.com/library/content/technotes/tn2206/_index.html#//apple_ref/doc/uid/DTS40007919-CH1-TNTAG17).
  
  

## Build

To create the bundle, invoke:

    $ sbt run

The bundle stores all its settings and caches inside the `data` subdirectory (except for Ivy and Maven repositories, which are system-wide).

You can update / install IDEA plugins via IDEA / Settings / Plugins.

It should be possible to include more example projects in the bundle.

Distributors of the bundle must display the text of [bundle agreement](src/main/resources/BundleAgreement.html) (or link to that text) on download page.

Because macOS signs application as a whole (not just executable files, as in Windows), it's recommended to distribute the content of `...-osx.tar.gz` file in a [signed DMG image](https://developer.apple.com/library/content/technotes/tn2206/), to avoid the ["unidentified developer"](https://support.apple.com/kb/ph25088) message (besides, this helps to avoid ["App Translocation"](https://developer.apple.com/library/content/technotes/tn2206/_index.html#//apple_ref/doc/uid/DTS40007919-CH1-TNTAG17)).

Please note that this bundle is not supposed to replace the usual way people download, install, configure and update each of those components. The main goal of the bundle is to provide a bootstrap distribution for educational purposes ([more on intended use cases](https://youtrack.jetbrains.com/issue/SCL-11406)).
