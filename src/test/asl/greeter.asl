i(1).
i(2).

+!hello =>
    #println(Source);
    #println(Parent);
    #println(Self);
    #coms.achieve(#executionContext.src,greetings)
.



+!loop =>
for (I in i(I)) {
    #println(I);
}.


+!loop_this(List) =>
for (I in member(I,List)) {
    #println(I);
}.