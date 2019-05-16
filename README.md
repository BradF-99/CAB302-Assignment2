# CAB302-Assignment2

[![Build Status](https://travis-ci.com/BradF-99/CAB302-Assignment2.svg?token=RmWc9zzyFjnuzmgBsmgB&branch=master)](https://travis-ci.com/BradF-99/CAB302-Assignment2)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e2cb98918a2f44cc9795e38a9c0f7bfb)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=BradF-99/CAB302-Assignment2&amp;utm_campaign=Badge_Grade)

Assignment 2 (Vector Design Tool) for CAB302, Semester 1, 2019.   
Built to Assignment Specification v1.0, current as of 2019-05-03.

## Authors

* Alex Butler (Assasindie)
* Brad Fuller (BradF-99)  
* Joshua O'Riordan (o-jc)

## Dependencies and Testing

* Apache Ant 1.10.6 is used for TravisCI only in this project.
* JUnit Jupiter 5.5.0 and JUnit Jupiter 1.5.0 are used for unit testing. (These are downloaded during )
* IntelliJ 2019.1.2 is used for development. 
* pdfTeX 3.14159265-2.6-1.40.19 (TeX Live 2018) is used for compiling supporting documentation.

## Notes

* When making changes, please create a new branch and then make a pull request when complete.
    * TravisCI will automatically complete unit tests after PR creation.
        * It will also complete continuous integration testing (testing your changes against master branch).
    * Codacy will only check Brad's commits due to limitations of the platform. This shouldn't matter as it should check master regardless of who merged the changes. 
    * All tests must pass before changes can be merged to master.
* All changes to master will result in TravisCI automatically deploying the build to [GitHub Releases](https://github.com/BradF-99/CAB302-Assignment2/releases).
    * Releases before assignment due date will be denoted as 0.(TravisCI Build Number) - for example, 0.119.
    * Ideally, the final version before submitting the assignment will be version 1.0 - this will be manually set.