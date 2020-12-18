ponged(0).

!ping_all.

+!ping_all =>
    #broadcast_achieve(ping)
.

+!ping =>
    #achieve(#executionContext.sender.name,pong).


@atomic
+!pong : ponged(X) =>
    +ponged(X + 1) ;
    -ponged(X) ;
    #println(#myName +" : I was Sponged " + X + " at " + #executionContext.agentLogger.timeTaken);
    if ( X < 5 ) {
       #broadcast_achieve(ping);
    }
    .


