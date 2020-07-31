
!give_speech.

+!give_speech =>
    for(B in (num(X) && between(0,X,B))) {
          #broadcast_achieve(listen(B));
        };
    #println("done")
   .