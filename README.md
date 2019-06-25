# CTester

## Index

- [Introduction](#Introduction)
- [Characteristics](#Characteristics)
- [Libraries](#Libraries)
- [Requirements](#Requirements)
- [How to use](#How&#32;to&#32;use)
- [Developing a test](#Developing&#32;a&#32;test)

## Introduction

__CTester__ is Tool created for the __EMV__ group for testing the product in a clean and simpler way. The tool has been developed in __JavaFX__ to be cross-platform and can also be used through the command line so that the testing can be automated.

## Characteristics

- __Cross-platform__.
- __Self-contained__: the tool won't have to be recompiled if a test changes.
- __Configurable__: things than are likely to change are configured through an `.xml` file.
- Run through __GUI__ and __command line__.

## Libraries

The following libraries have been used for this project:

- [JFoenix](https://github.com/jfoenixadmin/JFoenix): material design components for JavaFX.
- [RichTextFX](https://github.com/FXMisc/RichTextFX): memory-efficient text area for JavaFX.
- [Smart Card I/O API](https://docs.oracle.com/javase/7/docs/jre/api/security/smartcardio/spec/): Java API for communication with Smart Cards using ISO/IEC 7816-4 APDUs.
- [dom4j](https://dom4j.github.io/): Flexible XML framework for Java.
- [log4j](https://logging.apache.org/log4j/1.2/): logging library for Java.

## Requirements

### For just running tests

- JDK 8.

### For test developing

- JDK 8.
- NetBeans 8.2. (optional)

### For tool developing

- JDK 8.
- NetBeans 8.2.
- Scene Builder (optional)

## How to use

### GUI

1. Open the application.

2. Add some tests.

3. Run'em!

### Command line

## Developing a test

1. Clone the following template located in: `git:XXX`

2. Checkout `develop` branch.

3. Open the project with NetBeans.

4. Create a test inside that extends the class `AbstractTest`, located in the package `com.lsc.ctesterapi`.

5. Implement the logic inside the `setUp`, `run` and `tearDown` methods and return `true` or `false` if the execution is succesful or not.

Refer to this [README](/Tests&#32;Template/README.md) for more details about the `API` and the different `Libraries` that can be used from the test.

---

Note: The tests can be located anywhere, there are no restrictions about package naming either. However, do notice that a java file, in order to be compiled, has to be inside the folder structure and has to match the package name. Here's some examples:

```#comment
package testing;

Folder structure:
    testing
    |   test.java
```

```#comment
package testing.performance;

Folder structure:

    |testing
    |___performance
    |      test.java
```
