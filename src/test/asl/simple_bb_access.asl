name(#executionContext.name).
v(1).

+!read(W) : name(A) => #println(A+W); #coms.achieve(#executionContext.src,#asString(W)).

+!do : name(M) =>
    #std.terms.unify(v(X),v(1));
    #println(2+#asInteger(X));
    Response = #coms.ask(#executionContext.src,name(X));
    #println(Response);
    #println(Response).


+newinfo(X) => #println(oh(X)); !test.

+!test : newinfo(X) => #println(ohoh(X)).

+?hello(X) : v(X) => +v(X); M = v(X); #coms.respond(hello(X)).


