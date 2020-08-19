employee(#kyc.data_gen.bank_data(#myName).employee(0)).


+!register : employee(E) =>
    #achieve(E,interview(#executionContext.sender.name))
.

+!interview_complete(Client,I,true) : E = #executionContext.sender.name && employee(E) && not client(Client) =>
    #println("interview complete for " + Client);
    !register_client(Client,I)
.

+!interview_complete(Client,I,true) : E = #executionContext.sender.name && employee(E) && client(Client) =>
    #println("interview complete for " + Client);
    !update_client(Client,I)
.

+!register_client(C,I) : employee(E) =>
    +information(C,I);
    +client(Client);
    #achieve(E,do_risk_analysis(C,I));
    #achieve(C,be_informed_of_acceptance(#executionContext.name))
.

+!update_client(C,I) : employee(E) =>
    -information(C,_);
    +information(C,I);
    #achieve(E,do_risk_analysis(C,I))
.


+!assign_risk(C,R) =>
    -risk(C,_);
    +risks(C,R);
    #println("risk " + R + " was assigned to "+ C)
.


+!need_to_update_data : client(#executionContext.sender.name) && employee(E) =>
    #achieve(E,interview(#executionContext.sender.name))
.