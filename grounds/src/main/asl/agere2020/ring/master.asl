agents(16).
tokens(16).
token_value(128).



+!init : agents(W) && tokens(T) && token_value(V) =>
    #println("start at:");
    #println(#System.currentTimeMillis());
    +not_done(T);
    for(I in between(1,T,I)) {
          J = #std.math.ceil(I * ( W / T ));
          //#println(J);
          #coms.achieve("thread"+J,token(V));
          #coms.inform("thread"+J,name("thread"+J));
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

