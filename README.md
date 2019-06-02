# CAB302-Assignment2

[![Build Status](https://travis-ci.com/BradF-99/CAB302-Assignment2.svg?token=RmWc9zzyFjnuzmgBsmgB&branch=master)](https://travis-ci.com/BradF-99/CAB302-Assignment2)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e2cb98918a2f44cc9795e38a9c0f7bfb)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=BradF-99/CAB302-Assignment2&amp;utm_campaign=Badge_Grade)
[![Gitter](https://badges.gitter.im/CAB302-Group369/community.svg)](https://gitter.im/CAB302-Group369/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

Assignment 2 (Vector Design Tool) for CAB302, Semester 1, 2019.   
Built to Assignment Specification v1.0, current as of 2019-06-02.

## Authors

* Alex Butler (Assasindie)
* Brad Fuller (BradF-99)  
* Joshua O'Riordan (o-jc)

## Dependencies and Testing

* Apache Ant 1.10.6 is used by TravisCI for testing and deploying.
    * TravisCI will automatically attempt to build and test every commit, excluding tagged releases with a build number.
    * TravisCI runs Ant commands in this order (these can be seen in the build.xml file):
        1. `init` - initialises folders for build
        2. `compile` - compiles all Java in `src/` folder
        3. `test` - runs all tests in the `src/tests` folder that end with `Tests.class`. **If any tests fail TravisCI will stop immediately.**
        4. `dist` - packages the compiled Java in to a JAR file
        5. `clean` - deletes any build artifacts (mainly the `out/` folder).
* JUnit Jupiter 5.5.0 and JUnit Jupiter 1.5.0 are used for unit testing.
    * For development purposes, these are located in the *lib* folder.
    * For TravisCI, all dependencies are downloaded during TravisCI's install phase and do not need to be included in the repository.
* IntelliJ 2019.1.3 is used for development. 
* pdfTeX 3.14159265-2.6-1.40.19 (TeX Live 2018) is used for compiling supporting documentation.

## Notes

* When making changes, please create a new branch and then make a pull request when complete.
    * TravisCI will automatically complete unit tests after PR creation.
        * It will also complete continuous integration testing (testing your changes against master branch).
    * Codacy will only check Brad's commits due to limitations of the platform. Therefore, Codacy is not required for a pull request to be merged.
* Assuming all testing passes, any changes to master will result in TravisCI automatically deploying the build to [GitHub Releases](https://github.com/BradF-99/CAB302-Assignment2/releases) and tagging the commit.
    * Releases before assignment due date will be denoted as 0.(TravisCI Build Number) - for example, 0.119.
    * Ideally, the final version before submitting the assignment will be version 1.0 - this will be manually set before submission.
* Unfortunately, due to limitations with Apache Ant 1.10.6 and JUnit Jupiter 5, we are unable to automatically calculate code coverage during CI/CD. 
    * However, you can still do this yourself by running your tests using IntelliJ's coverage engine.