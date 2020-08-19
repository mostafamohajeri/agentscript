ponged(0).

!ping_all.

+!ping_all =>
    #broadcast_achieve(ping)
.

+!ping =>
    #achieve(#executionContext.sender.name,pong).

+!pong : ponged(X) =>
    +ponged(X + 1) ;
    #println(#myName +" : I was ponged " + X + " at " + #executionContext.agentLogger.timeTaken)
    .


