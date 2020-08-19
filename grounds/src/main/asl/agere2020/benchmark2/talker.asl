
!give_speech(10000).

+!give_speech(X) =>
    for(B in between(0,X,B)) {
          #broadcast_achieve(listen(B));
        };
    #println("done")
   .