//------------------BELIEFS----------------------
// decisions/targets
d(x).
d(y).
d(z).
d(w).

// parameters of targets (not used yet)
par(d(x),type(air_strike)).
par(d(x),weapon(air_force)).

par(d(y),type(ground_advance)).
par(d(y),weapon(tanks)).

par(d(z),type(ground_advance)).
par(d(z),weapon(foot_soldier)).

par(d(w),type(missile)).
par(d(w),weapon(big)).

// possible outcomes with params (target, vciv, vma, probability)
outcome(d(x),vciv(32),vma(2),p(1)).
outcome(d(y),vciv(32),vma(4),p(1)).
outcome(d(z),vciv(32),vma(4),p(1)).
outcome(d(w),vciv(64),vma(2),p(1)).

// proportionality coefficient
prp(1).

//==================RULES=====================
// calculations and rules
evciv(D,V) :-
    findall(A*Y,outcome(d(D),vciv(A),X,p(Y)),AA) &&
    sumlist(AA,V).

evma(D,V) :-
    findall(A*Y,outcome(d(D),X,vma(A),p(Y)),AA) &&
    sumlist(AA,V).

eqma(X,Y) :-
    evma(X,VMX) &&
    evma(Y,VMY) &&
    VX == VY.

lessciv(X,Y) :-
    d(X) && d(Y) &&
    evciv(X,VCX) &&
    evciv(Y,VCY) && VCX < VCY.

prop(X) :-
    evma(X,VMX) &&
    evciv(X,VCX) &&
    prp(Prp) &&
    PVCX is VCX * Prp &&
    VMX =< PVCX.

morerel(X,Y) :-
    evma(X,VMX) && evma(Y,VMY) &&
    evciv(X,VCX) && evciv(Y,VCY) &&
    SATX is VMX * VCX &&
    SATY is VMY * VCY &&
    SATX >= SATY.

dt(X) :-
    d(X) &&
    forall(d(Y), (X !== Y && eqma(X,Y) && lessciv(X,Y)) -> fail || true).

dp(D) :- d(D) && prop(D).

dmh(X) :- d(X) &&
    forall(d(Y), (X !== Y && not morerel(X,Y)) -> fail || true).

dav(D) :- dt(D) && dp(D) && dmh(D).

//=======================PREFERENCES==================
// preferences: A target D1 is preferred to D2 of the evma of D1 is more than D2
+!target(D1) >> +!target(D2) :- d(D1) >> d(D2).
d(D1) >> d(D2) :- evma(D1,V1) && evma(D2,V2) && V1 > V2.


//========================PLANS=======================

// MAIN PLAN
// target plan: if the parameter D is already bound (e.g. !target(y)) then checks if it is legal and performs it (prints it)
//              if the parameter D is not bound (free var, e.g. !target(D)) finds the most preferred legal option and performs it.
@preferences
+!target(D)
    : d(D) && dav(D)
        => #println("TARGETING:" + D).

// OTHERS
+?is_legal(D)
    : d(D) && dav(D)
        => #coms.respond(true).
+?is_legal(D)
        => #coms.respond(false).

+?get_all_legal
    : findall(X,dav(X),R)
        => #coms.respond(R).

+?get_all_most_preferred_legal
    : findall(X,(most_preferred(d(X)) && dav(X)),R)
        => #coms.respond(R).



