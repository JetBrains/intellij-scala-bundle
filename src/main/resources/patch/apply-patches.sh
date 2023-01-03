#!/bin/sh

set -e

VERSION=223.7571.182
TARGET=../../../../target

rm -rf $TARGET/lib
unzip $TARGET/repository/ideaIC-$VERSION.zip lib/\* -d $TARGET

javac -g:vars -cp $TARGET/lib/\* --add-exports=java.desktop/sun.awt=ALL-UNNAMED -target 17 *.java

rm -rf $TARGET/lib

echo Done.