## AntiAfk
An application that simulates selected key presses.

## Releases
Currently none since it's in alpha stage, build it yourself :) see below.

## Requirements
Java 17, can be downloaded [here](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html),
or alternatively through your package manager.


## Building
This projects uses gradle so building an executable jar can be done by running

`./gradlew bin` on Linux, or similarly `./gradlew.bat bin` on Windows. This will generate a `bin` directory 
containing an executable jar file `AntiAfk.jar` that can be run by `java -jar AntiAfk.jar` 
(or by creating a shell/bat file).

### Dependencies
All are handled through gradle:
* Jetbrains compose
* kotlinx serialization, datetime and coroutines
* ShadowJar
