ended(0).
t_total(100).

!init.

+!init : t_total(T) =>
    #println("done at:");
    #println(#System.currentTimeMillis());
    for(I in between(1,T,I)) {
          W = #math.random() * 2;
          #achieve("ponger1",pong(W));
        }
   .

@atomic
+!finished : ended(I) && t_total(T) =>
    if(T == I + 1) {
        #println("done at:");
        #println(#System.currentTimeMillis());
        #std.coms.exit();
    };
    -ended(I);
    +ended(I + 1)
.

