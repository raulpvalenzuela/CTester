# Developing a test

## Index

- [Requirements](#Requirements)
- [Creating a new project](#Creating&#32;a&#32;new&#32;project)
- [Developing the test](#Developing&#32;the&#32;test)

## Requirements

- [JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [NetBeans 8.2](https://netbeans.org/downloads/8.2/) (Recommended).

## Creating a new project

The template provides a NetBeans project already configured in which you can create new tests based on the one existing (`Test.java`). If you wish to create a different project, follow these steps:

1. Create a new `Java Application` project.
    - Choose a project name and the location.
2. Once created, right-click in the project -> properties -> Libraries -> Compile.
    - Click on `Add JAR/Folder` and select `CTesterAPI.jar` and `CTesterLib.jar` located in `CTester/CTesterFX/libs`.
    - Optionally, you can add the javadocs for both. Just click on the jar file and then `Edit`, in the dialog select the corresponding javadoc located in the same folder.

## Developing the test

To implement a new tests, these steps have to be followed:

1. Right-click in the package -> New Java file.
2. This test has to extend the class `com.lsc.ctesterapi.AbstractTest`.
3. Implement the logic inside the `setUp`, `run` and `tearDown` methods and return `true` or `false` whether the execution is succesful or not.

For reference, refer to the `Test.java` file in the template project.

### Stopping a test

Tests are run in the background through [Tasks](https://docs.oracle.com/javafx/2/api/javafx/concurrent/Task.html). In Java, threads are not killed, the stopping of a thread is done in a __cooperative way__. The thread is asked to terminate and the thread can then shutdown gracefully.

Thus, in order to be able to stop the test quickly, `Thread.currentThread().isInterrupted()` has to be used periodically. If this returns true, the test should terminate.

---

Note: The tests can be located anywhere and there are no restrictions about package naming either. However, do notice that, in a java file, the package name specifies the physical folder structure, and thus, the java file have to be inside this structure in order to be compiled. Here are some examples:

```#comment
package testing;

.
├─testing
└───test.java
```

```#comment
package testing.performance;

.
├─testing
├───performance
└─────test.java
```

So, if you want to change the test location, you will have to copy those folders as well.
