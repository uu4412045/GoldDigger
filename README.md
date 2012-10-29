Build Status:[![Build Status](https://secure.travis-ci.org/u4412045/GoldDigger.png?branch=master)](http://travis-ci.org/u4412045/GoldDigger)

Building the server can be done almost identically in Windows and Linux. Once you have installed the required software, to build the server:

- Using command prompt in Windows or Shell/Terminal in Linux go to the directory where the source code resides
- You should see a file called pom.xml in this director
- Run the following command to build the server:

mvn clean install

This should take a few minutes and then it should say "Build Successful"

If you receive a "Build Failure" message when building the server it is either because you have not terminated the server properly. In that case please refer to the terminate server page. Otherwise, it is because one or two tests which are time-sensitive have failed. In that case you may choose to disable those tests. Please follow these steps to disable them:

- Find MovementCostTest.java and MultiplayerMockitoTest located in \src\test\java\com\golddigger\services\ of the source code
- Remove these two files and build again
