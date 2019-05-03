#!/bin/sh

set -e

#diff -u AppUIUtil.java.original AppUIUtil.java > AppUIUtil.java.patch
#diff -u StartupUtil.java.original StartupUtil.java > StartupUtil.java.patch

curl -O https://raw.githubusercontent.com/JetBrains/intellij-community/idea/191.6707.61/platform/platform-impl/src/com/intellij/ui/AppUIUtil.java
curl -O https://raw.githubusercontent.com/JetBrains/intellij-community/idea/191.6707.61/platform/platform-impl/src/com/intellij/idea/StartupUtil.java

patch AppUIUtil.java AppUIUtil.java.patch
patch StartupUtil.java StartupUtil.java.patch

javac -g:vars -cp "lib/*" -target 1.8 *.java

rm AppUIUtil\$*.class
rm StartupUtil\$*.class

rm AppUIUtil.java
rm StartupUtil.java
