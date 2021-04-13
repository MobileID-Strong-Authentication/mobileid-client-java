# Build, download or link to the Mobile ID client

To use the Mobile ID client, you can build it, download it or link to it.

## Build it

The current repository holds the source code for the client. After you clone the desired branch (start with _main_ as it is the 
reference branch) you need Java 8+ and Maven 3.6+ to build it. After the cloning of the repository, run this command:

```shell
mvn install
```

Maven will build the final packages and install them in your local repository. You can also find them in the _target_ directory of 
each module.

## Download it

You can download the Mobile ID client to either using via its CLI interface (i.e. as a tool in your terminal) or download just the 
library binary to add it to your project:

- the full Mobile ID package is available in the _Releases_ section of this repository
- the Mobile ID library binaries available in the [Maven Central Repository](https://search.maven.org/search?q=ch.mobileid)

## Link it

The third option for getting the Mobile ID is to add the client as a dependency in your Maven or Gradle project. 
During your project's build phase, the Mobile ID client binary is downloaded and used as a dependency of your project. 

For Maven projects, add the following in your _POM_ file:
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <!-- ... -->
    <dependencies>
        <dependency>
            <groupId>ch.mobileid.mid-java-client</groupId>
            <artifactId>mid-java-client-rest</artifactId>
            <version>1.2.0</version> <!-- or any later version - see README.md in the repository's root -->
        </dependency>
    </dependencies>
    <!-- Alternatively, you can also reference the SOAP implementation of the client; adding both of them does not make much sense -->
    <dependencies>
        <dependency>
            <groupId>ch.mobileid.mid-java-client</groupId>
            <artifactId>mid-java-client-soap</artifactId>
            <version>1.2.0</version> <!-- or any later version - see README.md in the repository's root -->
        </dependency>
    </dependencies>
</project>
```

For Gradle projects, add the following in your _build.gradle_ file:
```groovy
plugins {
    id 'java'
}

// ...

dependencies {
    compile 'ch.mobileid.mid-java-client:mid-java-client-rest:1.2.0' // or any later version - see README.md in the repository's root
    // Alternatively, you can also reference the SOAP implementation of the client; adding both of them does not make much sense
    compile 'ch.mobileid.mid-java-client:mid-java-client-soap:1.2.0' // or any later version - see README.md in the repository's root
    // ...
}
```
