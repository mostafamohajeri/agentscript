outcome(d(x),vciv(0.7),vma(0.9),p(0.8)).
outcome(d(x),vciv(0.2),vma(0.2),p(0.2)).

d(x).
d(y).
d().
d(y).

outcome(d(y),vciv(0.9),vma(0.7),p(0.9)).
outcome(d(y),vciv(0.1),vma(0.7),p(0.1)).

evciv(D,V) :- findall(A*Y,outcome(D,vciv(A),X,p(Y)),AA) , sumlist(AA,V).


