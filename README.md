# AgentScript (ASC2)
## What is ASC2?
ASC2 is a Multi-Agent System framework mainly for the agents created with [AgentScript agent programming language](https://github.com/mostafamohajeri/scriptcc-translator). The language of ASC2 is based on [AgentSpeak(L)/Jason](https://github.com/jason-lang/jason).

## How does it work?
ASC2 works as a cross-compiler that translates the high level language of AgentScript into lower level executable languages and in the current version the lower level language is Scala. ASC2 heavily utilizes Akka actor framework, meaning at run-time, each ASC2 agent becomes an actor-based micro-system. For more information please refer to this [paper](https://dl.acm.org/doi/abs/10.1145/3427760.3428339)

As ASC2 agents are cross-compiled into Scala, all the available mainstream development and deployment tools for JVM including online CI/CD tools are usable with ASC2 out-of-the-box. For more information about this please refer to this [paper](https://dl.acm.org/doi/abs/10.1007/978-3-030-97457-2_15).


## How to use?
There are tow main approaches to using ASC2
* (1) as a compiler plugin with maven or sbt (gradle coming soon) that builds 
  * this is the preferred approach for most project with much more robust debugging tools, in essence your agent programs become a normal part of (java or scala) project source, which among many things have access to compile time checks. This also means you can add ASC2 to an already existing non-agent-based projects.
* (2) as a library to compile asc2 code from text files at runtime.
  * this approach is more like traditional agent programming languages and may be useful as the source code of the agents does not need to be available at compile-time. However, it means that many advantages of ASC2 over other agent programming languages are also not available.

Requirements:
* Java 11+
* sbt or maven

Start by creating a Java or Scala project. 

### Maven 
The source code to this tutorial is located at [https://github.com/mostafamohajeri/asc2-java-examples](https://github.com/mostafamohajeri/asc2-java-examples)

#### With maven compiler plugin
Create a maven project, here we call it `asc-test-java`.

Firstly add the maven build plugin for asc2 to the pom file.

```xml
<build>
    <plugins>
        <plugin>
          <groupId>io.github.mostafamohajeri</groupId>
          <artifactId>scriptcc-maven-plugin</artifactId>
          <version>1.4.4</version>
          <configuration>
            <sourceDirectory>${asc2.source-directory}</sourceDirectory>
          </configuration>
          <executions>
            <execution>
              <phase>generate-sources</phase>
              <goals>
                <goal>scriptcc</goal>
              </goals>
            </execution>
          </executions>
        </plugin>
    </plugins>
</build>
```
Note the `${asc2.source-directory}` points to the directory containing the source code of the agents. This can be replaced by a string or better a property in the pom file:
```xml
<properties>
    ...
    <asc2.source-directory>src/main/asl</asc2.source-directory>
</properties>
```

The `scala-maven-plugin` is also needed as the generated sources by ASC2 are in Scala

```xml
<plugin>
    <groupId>net.alchim31.maven</groupId>
    <artifactId>scala-maven-plugin</artifactId>
    <version>4.8.0</version>
</plugin>
```

Adding these two plugins is basically enough for running a project, but, for more control over the agents we will add two more dependencies:

```xml
<dependencies>
    <dependency>
        <groupId>io.github.mostafamohajeri</groupId>
        <artifactId>agentscript-grounds_2.13</artifactId>
        <version>0.47</version>
    </dependency>
    <dependency>
        <groupId>io.github.mostafamohajeri</groupId>
        <artifactId>agentscript-commons_2.13</artifactId>
        <version>0.47</version>
    </dependency>
</dependencies>
```

Now we are ready to write our agent programs, which should be located in the same directory as the one passed to the compiler plugin. We define two agents `pinger.asl` and `ponger.asl`.

`${asc2.source-directory}/pinger.asl`: 
```java
!init_pinging("ponger").

+!init(PongerAgentName) =>
    #println(Self + ": pinging " + PongerAgentName);
    #coms.inform(PongerAgentName,ping).

+pong => #println(Self + ": ponged by " + Source).
```

`${asc2.source-directory}/ponger.asl`:
```java
+ping =>
    #println(Self + ": pinged by " + Source);
    #println(Self + ": ponging " + Source);
    #coms.inform(Source , pong).
```

Now we can simply compile the project with `$ mvn clean generate-sources` which should result in creation of a package with two scala files in `target/generated-sources/` directory.

To compile the agents we can use 

```$ mvn clean generate-sources scala:compile```

(Note that IDE's like IntellijIdea take care of this process automatically, also it is not needed to clean, re-generate and re-compile everything all the time, this is just for learning purposes here)

##### Utilizing the agents
To run these agents we need to create a system (MAS). The following is an example of how this can be done:
```java
import infrastructure.AgentRequest;
import infrastructure.AgentRequestMessage;
import infrastructure.MAS;
import akka.actor.typed.ActorSystem;
import scala.collection.immutable.Seq;
import java.util.List;

public class MainMaven {
  public static void main(String[] args) {

    // Create the system
    MAS mas = MAS.build();
    var system = ActorSystem.create(mas.applyDefaults(), "MAS");

    // Tell the system how many of which agent to add
    // Starts as soon as all have been initialized
    system.tell(
            AgentRequestMessage.apply(
                    toSeq(List.of(
                                    new AgentRequest(asl.pinger_companion.create(), "pinger", 1),
                                    new AgentRequest(asl.ponger_companion.create(), "ponger", 1)
                            )
                    ),
                    system
            )
    );

  }

  private static Seq<AgentRequest> toSeq(List<AgentRequest> l) {
    return scala.jdk.CollectionConverters.ListHasAsScala(l).asScala().toSeq();
  }
}
```

Now we can simply compile the whole project with
```
$ mvn clean generate-sources scala:compile compile
```
and by running our main file we'll get the output: 
```
$ mvn generate-sources scala:compile compile  exec:java -Dexec.mainClass="MainMaven"
...
pinger: pinging ponger
ponger: pinged by pinger
ponger: ponging pinger
pinger: ponged by ponger

```


#### With run-time compiler
For this approach we need the same dependencies as before, the only required dependencies are:
```xml
<dependencies>
    <dependency>
        <groupId>io.github.mostafamohajeri</groupId>
        <artifactId>agentscript-grounds_2.13</artifactId>
        <version>0.47</version>
    </dependency>
    <dependency>
        <groupId>io.github.mostafamohajeri</groupId>
        <artifactId>agentscript-commons_2.13</artifactId>
        <version>0.47</version>
    </dependency>
</dependencies>
```

this time we add a `.json` file to our asc2 source directory defining what type and number of agents are needed:
```json
{
  "agents": [
    {
      "name" : "pinger",
      "script_file" : "pinger.asl",
      "number": 1
    },
    {
      "name" : "ponger",
      "script_file" : "ponger.asl",
      "number": 1
    }
  ]
}
```

and then, the main file will look like:
```java
import infrastructure.MAS;
import scriptcc.ScriptRunner;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        scriptcc.ScriptRunner r = new ScriptRunner();
        File f = new File("src/main/asl/input.json");
        MAS system = r.createMas(f.getAbsolutePath(),false);
    }
}
```
which can be executed by 
```
mvn  exec:java -Dexec.mainClass="Main"
```

This approach as you can see is simpler as there is no need to set-up the compiler and its plugins and also there is no generated sources.
However, there are many downsides, as there is no access to compiler's static analyzer, debugging break points, and caching that is already provided by maven.