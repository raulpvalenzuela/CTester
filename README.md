# CTester

- [Introduction](#Introduction)
- [Characteristics](#Characteristics)
- [Libraries](#Libraries)
- [Requirements](#Requirements)
- [How to use](#How&#32;to&#32;use)
- [Developing a test](#Developing&#32;a&#32;test)

---

## Introduction

__CTester__ is Tool created for the __EMV__ group for testing the product in a clean and simpler way. The tool has been developed in __JavaFX__ to be cross-platform and can also be used through the command line so that the testing can be automated.

---

## Characteristics

- __Cross-platform__.
- __Self-contained__: the tool won't have to be recompiled if a test changes.
- __Configurable__: things than are likely to change are configured through a `.ini` file.
- __GUI__ and __command line__.

---

## Libraries

The following libraries have been used for this project:

- GUI
  - [JFoenix](https://github.com/jfoenixadmin/JFoenix): material design components for JavaFX.
  - [RichTextFX](https://github.com/FXMisc/RichTextFX): memory-efficient text area for JavaFX.

---

## Requirements

- JDK 8 or above.
- JRE 8 or above.
- NetBeans 8.2 or Eclipse (NetBeans recommended).

---

## How to use

1. Open the application.

2. TODO

---

## Developing a test

1. Clone the following template located in: `git:XXX`

2. Checkout `develop` branch.

3. Open the project with NetBeans or Eclipse. 

4. Create a test inside `runnables` package that extends `com.lsc.ctesterfx.tests.TestRunnable`.

5. Implement the logic inside the `run` method and return `true` or `false` if the execution is succesful or not.
