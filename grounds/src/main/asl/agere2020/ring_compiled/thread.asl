

!init.

+!init =>
    N = "thread" + ((#executionContext.name.replaceAll("thread","").toInt % #benchmark.ring_data.nb_agents) + 1);
    +neighbor(N).

+!token(0) =>
    #achieve("master1",done).

+!token(T) : neighbor(N) =>
      #achieve(N,token(T - 1))

  .

+!token(T) : not (neighbor(N)) =>
       #println("problem" + #executionContext.agentLogger.timeTaken)
  .








