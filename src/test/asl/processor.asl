

input(data, meta(purpose(spy))).
input(data, meta(purpose(research))).


storage(public_storage, meta(purpose(research))).
storage(private_storage, meta(purpose(ads))).

//
algorithm(fairness_algorithm, meta(purpose(research))).
algorithm(evil_algorithm, meta(purpose(ads))).

poop(
  input(Data,Meta1),
  storage(Storage,Meta2),
  algorithm(Algorithm,Meta3),
  true
) :- input(Data,Meta1) && storage(Storage,Meta2) && algorithm(Algorithm,Meta3).

poop(
  input(Data,Meta1),
  storage(Storage,Meta2),
  algorithm(Algorithm,Meta3),
  false
) :- input(Data,Meta1) && storage(Storage,Meta2) && algorithm(Algorithm,Meta3).

  poop(
  input(_,meta(purpose(P1))),
  storage(_,meta(purpose(P2))),
  algorithm(_,meta(purpose(P3))),
  true) >>
  poop(
  input(_,_),
  storage(_,_),
  algorithm(_,_),
  false) :- P1 == P2 && P1 == P3 && P2 == P3.

  poop(
  input(_,meta(purpose(P1))),
  storage(_,meta(purpose(P2))),
  algorithm(_,meta(purpose(P3))),
  false) >>
  poop(
  input(_,_),
  storage(_,_),
  algorithm(_,_),
  true) :- P1 !== P2 || P1 !== P3 || P2 !== P3.

+!process(
  input(Data,meta(purpose(P1))),
  storage(Storage,meta(purpose(P2))),
  algorithm(Algorithm,meta(purpose(P3))),
  false) >> +!process(
  input(Data,meta(purpose(P1))),
  storage(Storage,meta(purpose(P2))),
  algorithm(Algorithm,meta(purpose(P3))),
  true) :- P1 !== P2 || P1 !== P3 || P2 !== P3.

+!process(Data,Meta1) =>
    #println("starting process");
    I = input(Data,Meta1);
    S = storage(Storage,Meta2);
    A = algorithm(Algorithm,Meta3);
    !process(I,S,A,Legal).


@preferences
+!process(
  input(Data,Meta1),
  storage(Storage,Meta2),
  algorithm(Algorithm,Meta3),
  true) :
  storage(Storage,Meta2) && algorithm(Algorithm,Meta3)
    => #println("running:"+process(Data,Meta1,Storage,Meta2,Algorithm,Meta3)).

@preferences
+!process(
  input(Data,Meta1),
  storage(Storage,Meta2),
  algorithm(Algorithm,Meta3),
  false) :
  storage(Storage,Meta2) && algorithm(Algorithm,Meta3)
    => #println("not running:"+process(Data,Meta1,Storage,Meta2,Algorithm,Meta3)).


+!get_all_most_preferred_poop
    : findall(poop(I,S,A,L),(poop(I,S,A,L)),R)
        => #println(R).


