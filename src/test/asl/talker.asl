
+!say_hi =>
    #println("hi")
.

+!ask_me_something =>
    #achieve(#executionContext.sender.ref,kill_me)
.

