#!/bin/sh

./gradlew build
cp -f build/libs/modid-1.0.jar /home/scott/.minecraft/mods/modid-1.0.jar
scp build/libs/modid-1.0.jar root@TeamBadass:/var/www/html/files/modid-1.0.jar
scp build/libs/modid-1.0.jar root@TeamBadass:/home/minecraft/testServer/mods/modid-1.0.jar
