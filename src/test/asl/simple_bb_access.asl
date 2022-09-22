name(#executionContext.name).
v(1).

@atomic @preferences
+!read(W)=> #coms.achieve(#executionContext.src,#asString(W)).


-hello(X) => #println(X).

+!do : name(M) =>
    #std.terms.unify(v(X),v(1));
    #println(2+#asInteger(X));
    Response = #coms.ask(#executionContext.src,name(X));
    #println(Response);
    #println(Response).


+newinfo(X) => #println(oh(X)); !test.

+!test : newinfo(X) => #println(ohoh(X)).

+!test => #println(ohoh(X)).




