

!init.

+!init =>
    N = "thread" + ((#executionContext.name.replaceAll("thread","").toInt % 500) + 1);
    #println(N);
    +neighbor(N).

+!token(0) =>
    #println("done");
    #std.coms.exit().

+!token(T) : neighbor(N) =>
      #achieve(N,token(T - 1))

  .

+!token(T) : not (neighbor(N)) =>
       #println("problem" + #executionContext.agentLogger.timeTaken)
  .








