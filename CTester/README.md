# CTester design

## Index

- [Introduction](#Introduction)
- [Requirements](#Requirements)
- [Building](#Building)
- [Modules](#Modules)
  - [CTesterFX](#CTesterFX)
  - [CTesterAPI](#CTesterAPI)
  - [CTesterLib](#CTesterLib)
- [Code style](#Code&#32;style)

## Introduction

CTester is organized in three different projects:

- `CTesterFX`: the core of the application is located here.
- `CTesterAPI`: the tests __must not__ have direct access to the application, instead it shall make use of an __API__ that will act as a bridge. This API will be located here.
- `CTesterLib`: compilation of self-contained modules to be used by the application and the tests.

## Requirements

- [JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [NetBeans 8.2](https://netbeans.org/downloads/8.2/)
- [Scene Builder](https://gluonhq.com/products/scene-builder/) (optional).

## Building

1. Compile `CTesterLib`.
    - The output `jar` will be placed in `CTesterFX/libs`.
    - The `javadoc` will be placed in `Executable/Docs/Javadoc/Lib`.

2. Compile `CTesterAPI`.
    - The output `jar` will be placed in `CTesterFX/libs`.
    - The `javadoc` will be placed in `Executable/Docs/Javadoc/API`.

3. Compile `CTesterFX`.
    - The output `jar` will be placed in `Executable/`.
    - The `javadoc` will be placed in `/Docs/Javadoc`.

## Modules

### CTesterFX

This module contains the core of the application which must not be visible to the tests.

### CTesterAPI

This module contains all the public functionality that has to be visible to the tests.

The tests will make use of this API for __logging__, executing __scripts__, accessing the different __readers__, etc.

### CTesterLib

This module will contained self-contained funtionalities that both the application and the tests can make use of. For instance:

- Utils class.
- GP commands.
- Constants.
- APDU generator
- etc.

## Coding rules

Please, have in mind these notes when modifying the source code so that it remains readable and consistent:

- [Java code conventions](https://www.oracle.com/technetwork/java/codeconventions-150003.pdf) is followed with one exception:
  - Braces are in the next line.

- When creating a new functionality, place it in the correspondent module:
  - `CTesterFX`: if it is internal and should not be visible to the test.
  - `CTesterLib`: if it is a self-contained functionality that both the tool and the test can make use of (eg. utils class).
  - `CTesterAPI`: if it's public to be used by the test.

- Every function should contain its correspondent __Javadoc__ header with its description and the parameters and return tags if applicable.

- Consider making the new funcionality configurable through the __config.xml__ file to avoid hardcoding frequently changing values.

- Create Unit Tests if possible. Maven will automatically run them as part of the build process.
