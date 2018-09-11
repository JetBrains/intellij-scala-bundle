#!/bin/sh
wget https://raw.githubusercontent.com/JetBrains/intellij-community/idea/182.4323.46/platform/platform-impl/src/com/intellij/ui/AppUIUtil.java
wget https://raw.githubusercontent.com/JetBrains/intellij-community/idea/182.4323.46/platform/platform-impl/src/com/intellij/idea/StartupUtil.java
patch AppUIUtil.java AppUIUtil.java.patch 
patch StartupUtil.java StartupUtil.java.patch
javac -g:vars -cp lib/* AppUIUtil.java
javac -g:vars -cp lib/* StartupUtil.java
rm AppUIUtil$*.class
rm StartupUtil*.class
rm AppUIUtil.java
rm StartupUtil.java
