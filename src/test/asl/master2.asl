agents(10).
tokens(250).
token_value(10).

!init.

+!init : agents(W) && tokens(T) && token_value(V) =>
    #println("start at:");
    #println(#System.currentTimeMillis());
    +not_done(T);
    for(I in between(1,T,I)) {
          J = #std.math.ceil(I * ( W / T ));
          #coms.achieve("master1",token(V));
          #println(J);
        }
   .

@atomic
+!done : not_done(T) =>
    if(T == 1) {
        #println("done at:");
        #println(#System.currentTimeMillis());
        #std.utils.exit();
    };
    -not_done(T);
    +not_done(T - 1)
.



