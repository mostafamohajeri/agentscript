hello(1).
bank(#kyc.data_gen.employee_data(#myName).bank).
hi(A) :- hello(A) && bank("ING").
!go.

+!go => #println("go").
+!hello(M) => #println(M).
+hello(M) => +bye(M).