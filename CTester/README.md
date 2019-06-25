# CTester design

## Index

- [Introduction](#Introduction)

## Introduction

CTester is organized in three different projects:

- `CTesterFX`: the core of the application is located here.
- `CTesterAPI`: the tests __must not__ have direct access to the application, instead it shall make use of an __API__ that will act as a bridge. This API will be located here.
- `CTesterLib`: compilation of self-contained modules to be used by the application and the tests.
