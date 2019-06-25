# CTester design

## Index

- [Introduction](#Introduction)
- [Requirements](#Requirements)
- [Building](#Building)

## Introduction

CTester is organized in three different projects:

- `CTesterFX`: the core of the application is located here.
- `CTesterAPI`: the tests __must not__ have direct access to the application, instead it shall make use of an __API__ that will act as a bridge. This API will be located here.
- `CTesterLib`: compilation of self-contained modules to be used by the application and the tests.

## Requirements

- [JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [NetBeans 8.2](https://netbeans.org/downloads/8.2/)
- Scene Builder (optional).

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
