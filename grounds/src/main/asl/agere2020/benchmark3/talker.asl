
!ask_yourself(100000).

+!ask_yourself(0) => #println("done: " + #executionContext.agentLogger.timeTaken).

+!ask_yourself(X) =>
    !ask_yourself(X)
   .