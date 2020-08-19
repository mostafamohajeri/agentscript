agents(#benchmark.ring_data.nb_agents).
tokens(#benchmark.ring_data.nb_tokens).
token_value(#benchmark.ring_data.nb_hops).

!init.

+!init : agents(W) && tokens(T) && token_value(V) =>
    #println("start at:");
    #println(#System.currentTimeMillis());
    +not_done(T);
    for(I in between(1,T,I)) {
          J = #std.math.ceil(I * ( W / T ));
          #achieve("thread"+J,token(V));
        }
   .

@atomic
+!done : not_done(T) =>
    if(T == 1) {
        #println("done at:");
        #println(#System.currentTimeMillis());
        #std.coms.exit();
    };
    -not_done(T);
    +not_done(T - 1)
.

