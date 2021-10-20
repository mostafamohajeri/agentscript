

!init.

+!init =>
    N = "thread" + ((#executionContext.name.replaceAll("thread","").toInt % 10) + #asInteger(1));
    +neighbor(N).

+!token(0) =>
    #coms.achieve("master1",done).

+!token(T) : neighbor(N) =>
      #coms.achieve(N,token(T - 1))

  .

+!token(T) : not (neighbor(N)) =>
       #println("problem")
  .








