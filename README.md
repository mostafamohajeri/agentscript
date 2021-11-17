# AgentScript (ASC2)
## What is ASC2?
ASC2 is a Multi-Agent System framework mainly for the agents created with [AgentScript agent programming language](https://github.com/mostafamohajeri/scriptcc-translator). The language of ASC2 is based on [AgentSpeak(L)/Jason](https://github.com/jason-lang/jason).

## How does it work?
ASC2 works as a cross-compiler that translates the high level language of AgentScript into lower level executable languages and in the current version the lower level language is Scala. ASC2 heavily utilizes Akka actor framework, meaning at run-time, each ASC2 agent becomes an actor-based micro-system. For more information please refer to this [paper](https://dl.acm.org/doi/abs/10.1145/3427760.3428339)

As ASC2 agents are cross-compiled into Scala, all the available mainstream development and deployment tools for JVM including online CI/CD tools are usable with ASC2 out-of-the-box. For more information about this please refer to this [paper](https://www.semanticscholar.org/paper/Seamless-Integration-and-Testing-for-MA-Engineering-Mohajeri/ac5c18ee097ee13f81f86c6456bf36dd0ef876e8).


## How to use?
ASC2 works in two modes, compile-time cross-compilation and online compilation.

In compile-time cross-compilation, a sbt plugin is used to translate the AgentScript language to Scala as part of sbt's build cycle and then the rest of compilation is done by the normal build process of sbt with these translated agents as `managed sources`. An example of this approach can be found [here](https://github.com/mostafamohajeri/asc2-liccam)

The runtime cross-compilation gets the agent programs as text (or text files) at runtime and does the compilation at runtime then returns a factory object that the program can use to create agents from the script.