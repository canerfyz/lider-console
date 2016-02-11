# lider-console

## Overview

lider-console contains core funtionalities (such as REST client, LDAP client), base RCP application and an API for other plug-ins.

## Prerequisites

### JDK7

- Download and install [JDK7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)

### Git

- Documentation about installing and configuring git can be found [here](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) and [here](https://git-scm.com/book/en/v2/Getting-Started-First-Time-Git-Setup)
- Git [home](http://git-scm.com/) (download, docs)

### Maven 3

- Get [Maven 3](http://maven.apache.org/install.html) (Specifically, at least **version 3.1.1** is needed to use static code analyzers and maven-tycho plugin!).
- Maven [home](https://maven.apache.org/) (download, docs)

## Project Directory Layout

    lider-console/
      lider-console-core/           --> core functionalities and API for other plug-ins
      lider-console-dependencies/   --> (non-OSGI) third party dependencies
      lider-console-products/       --> product file which is used for automated export
      lider-console-rcp/            --> base RCP application
      lider-console-core-tests/     --> test cases for core plugin
      lider-console-task/           --> task plugin which is responsible for sending tasks and displaying results
      scripts/                      --> helper scripts (such as automated product export)
      findbugs/                     --> configuration files for findbugs plugin
      checkstyle/                   --> configuration files for checkstyle plugin

## Exporting Lider Console Product

lider-console depends on [Tycho](https://eclipse.org/tycho/) to export its products for multiple operating systems. However in OSGI environment (and naturally for Tycho) in order to use a dependency, the dependency must be an OSGI bundle and it must reside in a P2 update site.

While, fortunately, many dependencies can be found as OSGI bundles in [Orbit](http://www.eclipse.org/orbit/) repositories, some of them has to be converted to OSGI bundles. To overcome this problem, we use [p2-maven-plugin](https://github.com/reficio/p2-maven-plugin/) to convert third party (non-OSGI) jar files to OSGI bundles and create a P2 update site.

#### Manual Export

1. Change directory to *lider-console-dependencies/* and run `mvn clean p2:site` to (re)generate third party dependencies. This step will convert **non-OSGI** third party jar files to OSGI bundles.
2. After dependency generation, run `mvn jetty:run` to start jetty server. This will allow Tycho to consume dependencies from the server (localhost) since Tycho can't use local repositories yet.
3. Head back to project directory and run `mvn clean verify` to export Lider Console product.

> **Tip:** It is not recommended to use the `mvn clean install` command, instead you should use `mvn clean verify`. If you would use the install option, the build result would be installed in your local Maven repository and this can lead to build problems.

#### Easy Export

1. Instead of manually exporting, just run `export-lider-console.sh` under the *scripts/* directory.

## How to Setup Development Environment

1. Install [Eclipse](https://eclipse.org/downloads/) version >=4.4 (Luna or Mars).
2. [Orbit](http://www.eclipse.org/orbit/) repository is needed to handle OSGI dependencies.
	2.1. First, we need to find the correct Orbit repository belonging to installed Eclipse version from [Orbit downloads site](http://download.eclipse.org/tools/orbit/downloads/).
	2.2. Then we need to add this URL to Eclipse via 'Help --> Install New Software --> Add'.
	2.3. Finally, required bundles can be installed. (At the moment, only *gson* and *apache httpcomponents* are required but you can always install all bundles just in case.)
2.4. Restart Eclipse after successfull installation.
3. http://directory.apache.org/studio/update add this URL to Eclipse via 'Help --> Install New Software --> Add'.
4. Clone lider-console project by running `git clone git@git.agem.com.tr:lider-ahenk/lider-console.git`.
5. Change directory to *lider-console-dependencies/* and run `mvn clean p2:site` to generate third party dependencies. This command will generate OSGI bundles under *lider-console-dependencies/target/repository/* directory.
6. Import the project into Eclipse as 'Existing Maven Projects'.
7. As the final step, go to 'Help --> Install New Software --> Add' and add generated third party dependencies (Step 4) by adding *lider-console-dependencies/target/repository/* directory as local repository.
8. Restart Eclipse and run the application as Eclipse RCP Application.

> **Warning:** When the project is run for the first time, Eclipse might throw an exception. If this error occurs, go to 'Run --> Debug Configurations...' and on the 'Plugins' tab click 'Add Required Libraries' to allow Eclipse to use all required bundles.

## Static Analyzers

We also use checkstyle and findbugs plugins to do static analyzing on the changes. Run the following commands to analyze your code to check if it is compatible.

`mvn clean compile -P findbugs`

`mvn clean validate -P checkstyle`
