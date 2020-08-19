nb_meetings(1000).
nb_ready(0).

@atomic
+!ready : nb_ready(N) =>
    -nb_ready(N);
    +nb_ready(N + 1);
    if( (N + 1) == 500 )
    {
        #broadcast_achieve(go_mall);
    }
    .

@atomic
+!meet(C2) : first(A1,C1) && nb_meetings(I) && I > 0
   =>
      A2 = #executionContext.sender.name;
      -first(A1,C1);
      -nb_meetings(I);
      +nb_meetings(I - 1);
      #achieve(A1,mutate(A2,C2));
      #achieve(A2,mutate(A1,C1)).

+!meet(C1) : not nb_meetings(0) => +first(#executionContext.sender.name,C1).

@atomic
+!meet(_) : not finished =>
    #broadcast_achieve(print_result);
    +finished;
    #println("done");
    #std.coms.exit().

+!meet(_) => #print("").

