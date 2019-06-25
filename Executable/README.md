# How to use CTester

## Index

- [Requirements](#Requirements)
- [Folder structure](#Folder&#32;Structure)
- [How to run tests](#How&#32;to&#32;run&#32;tests)

## Requirements

- [JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

## Folder structure

```#comment
    |Executable
    |___Config
    |       config.xml
    |___Logs
    |       ctester.log
    |___Docs
    |_______Javadoc
    |   CTester.jar
```

### Config folder

It contains the `config.xml` file.

### Logs

It contains the internal log of the application in case there's any bug or exception.

### Docs

It contains the javadocs of the `API` and the `Library` that you can use from the test.

## How to run tests

First, you will need a test implemented, refer to this [README](../Tests&#32;Template/README.md) to know how to implement one.
