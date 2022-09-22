!init.

+!init =>
    N = "thread" + ((#executionContext.name.replaceAll("thread","").toInt % 10) + #asInteger(1));
    +neighbor(N).

+name(T) => #println("starting: " + T);
    +myName(T);
    +myName(T).

+myName(T) => #println("wow today I learned that my name is: " + T).


+!token(0) : name(T) =>
    #println("my name was: " + T);
    #coms.achieve("master1",done).

+!token(T) : neighbor(N) =>
      #coms.achieve(N,token(T - 1))
  .

+!token(T) : not (neighbor(N)) =>
       #println("problem")
  .








