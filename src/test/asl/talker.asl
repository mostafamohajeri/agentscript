
+!say_hi =>
    #println("hi")
.

+!ask_me_something =>
    #achieve(#executionContext.sender.ref,say_hi)
.

