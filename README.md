![sofia_logo.svg](app/src/main/resources/img/sofia_logo.svg "Sofia logo")
# Sofia

Sofia ~~is~~ _will be_ a cross-platform GUI application for working with the data of 1985's most popular platform game. Written in [Kotlin](https://kotlinlang.org/), It uses [Isobel](https://github.com/jimbovm/isobel) as a backend, and [JavaFX](https://openjfx.io/) for its GUI functionality. When complete, Sofia will allow users to create "mods" in the form of original levels that can be loaded by the original game, including on real hardware. It will also have comprehensive internationalization support to enable the interface to be translated into any natural language. 

Like its backend, Sofia is open source, licensed under a public domain-equivalent MIT-0 licence (see [LICENCE.md]() for terms and [NOTICE.md]() for bundled components and dependencies with different licences) and targets the Java Virtual Machine, so it will run anywhere.

**⚠ Warning:** Sofia is incomplete to the point of being not much more than a glorified mockup at present, and not even close to being ready for general use. Work is proceeding apace but as I'm effectively learning Kotlin, JavaFX and FXML as I develop around other commitments, there's a long way to go to reach the MVP stage. Fortunately, because Isobel does most of the work and is largely complete and well tested, wiring everything together when the time comes should be a reasonably straightforward process.

## Build instructions

To build Sofia, you first need to build and install [Isobel](https://github.com/jimbovm/isobel) to your local Maven repository in the usual way.

Once you've done that, execute in the root of the cloned Sofia repository:

```
./gradlew shadowJar
```
on Mac or free Unix, or

```
./gradle.bat shadowJar
```

on Windows.

This will build a runnable "fat jar" with all dependencies included.

In the future, building for distribution will be done the right way using Java modularisation, but unfortunately Kotlin and Gradle aren't quite where they need to be for this to work properly.