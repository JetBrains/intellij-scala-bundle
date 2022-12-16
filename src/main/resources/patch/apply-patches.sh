#!/bin/sh

set -e

# VERSION=$(grep -Po '(?<=Idea=).*' ../../../../version.properties)
TARGET=../../../../target

#curl https://raw.githubusercontent.com/JetBrains/intellij-community/idea/$VERSION/platform/platform-impl/src/com/intellij/ui/AppUIUtil.java > AppUIUtil.java.original
#curl https://raw.githubusercontent.com/JetBrains/intellij-community/idea/$VERSION/platform/platform-impl/src/com/intellij/idea/StartupUtil.java > StartupUtil.java.original
#diff -u AppUIUtil.java.original AppUIUtil.java > AppUIUtil.java.patch
#diff -u StartupUtil.java.original StartupUtil.java > StartupUtil.java.patch

rm -rf $TARGET/lib
unzip $TARGET/repository/ideaIC-222.4459.24.zip lib/\* -d $TARGET

curl -O https://raw.githubusercontent.com/JetBrains/intellij-community/idea/$VERSION/platform/platform-impl/src/com/intellij/ui/AppUIUtil.java
curl -O https://raw.githubusercontent.com/JetBrains/intellij-community/idea/$VERSION/platform/platform-impl/src/com/intellij/idea/StartupUtil.java

patch AppUIUtil.java AppUIUtil.java.patch
patch StartupUtil.java StartupUtil.java.patch

javac -g:vars -cp $TARGET/lib/\* -target 17 *.java

rm StartupUtil\$*.class

#rm AppUIUtil.java
#rm StartupUtil.java

rm -rf $TARGET/lib

echo Done.