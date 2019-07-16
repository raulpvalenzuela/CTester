# How to use CTester

## Index

- [Requirements](#Requirements)
- [Folder structure](#Folder&#32;Structure)
- [How to run tests](#How&#32;to&#32;run&#32;tests)
  - [GUI](#GUI)
  - [Command line](#Command&#32;line)

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

Once there are some tests implemented, you can run them using the GUI or through the command line.

### GUI

1. In the left sidebar, click on __Add new tests__ and select the tests.
2. Use the checkboxes to select the tests to be run.
3. In the left sidebar, click on __Run selected tests__.

A log file will be created for every test in the same path as the test.

### Command line

1. Create an `.lst` file with the relative paths to the tests. (Refer to this [section](#LST&#32;file) for more details about .lst files)
2. cd to the Executable folder.
3. Run the following command:

    ```shell
    java -jar CTester.jar -l <path_to_lst>
    ```

    Note: refer to the command help for more details about the usage.

    ```shell
    java -jar CTester.jar --help
    ```

    or

    ```shell
    java -jar CTester.jar -h
    ```

#### LST file

An .lst file is simply a text file containing the list of relative paths to test files:

```text
; This is a comment
ReleaseTesting/TestingA/Test1.java
ReleaseTesting/TestingA/Test2.java
ReleaseTesting/TestingB/Test1.java
```
