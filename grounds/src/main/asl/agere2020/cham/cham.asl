
nb_meetings(0).
nb_meetings_same_color(0).

complement(blue,   red,    yellow).
complement(blue,   yellow, red).
complement(red,    blue,   yellow).
complement(red,    yellow, blue).
complement(yellow, blue,   red).
complement(yellow, red,    blue).
complement(C, C, C).

!set_and_print_color.

+!set_and_print_color =>
    C = #chams.ChameneosData.color(#myName.replaceAll("cham","").toInt);
    #println(C);
    #achieve("broker1",ready);
    +color(C).

+!go_mall : color(C) => #achieve("broker1",meet(C)).

+!go_mall_again(C) => #achieve("broker1",meet(C)).

+!mutate(A,C2) : color(C1) && nb_meetings(N) && complement(C1,C2,C) =>
      -color(C1);
      +color(C);
      -nb_meetings(N);
      +nb_meetings(N+1);
      if(#myName == A) {
         !same_agent;
      };
      !go_mall_again(C).

+!same_agent : nb_meetings_same_color(N) =>
    -nb_meetings_same_color(_);
    +nb_meetings_same_color(N + 1).

+!print_result : nb_meetings_same_color(N) && nb_meetings(N2) =>
    #println("meetings: " + N2 + " | same name: " + N);
    #achieve("broker1",done).