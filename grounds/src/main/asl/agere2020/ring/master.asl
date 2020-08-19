agents(500).
tokens(1).
token_value(50000).

!init.

+!init : agents(W) && tokens(T) && token_value(V) =>
    for(I in between(1,T,I)) {
          J = #std.math.round(I * ( W / T ));
          #achieve("thread"+J,token(V));
        };
    #println("done")
   .
