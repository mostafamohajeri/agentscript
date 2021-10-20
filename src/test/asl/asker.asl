+!do =>
    Response = #coms.ask("responder",how_are_you(X));
    #std.terms.unify(M,Response);
    #println(M);
    Response2 = #coms.ask("responder",how_are_you(X),how_are_you(Y));
    #println(Response2);
    #println(Y);
    Response3 = #coms.ask("responder",what_do_you_want_to_do);
    #println("yaay, let's all !"+Response3)
    .
