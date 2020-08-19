
bank(#kyc.data_gen.employee_data(#myName).bank).


+!interview(Client) : bank(B) && B == #executionContext.sender.name =>
    #achieve(Client,give_info(B))
.

+!info(Name,SBI,Country) : bank(B) && Client = #executionContext.sender.name =>
    +information(Client,info(Name,SBI,Country));
    #achieve(Client,give_consent(B,"KYC"))
.

+!consent(B,Purpose,true) :
    Client = #executionContext.sender.name &&
       bank(B) &&
        Purpose == "KYC" &&
        information(Client,I)
        =>

    #achieve(B,interview_complete(Client,I,true))
.

+!consent(Bank,Purpose,false) : Client = #executionContext.sender.name && bank(B) && information(Client,I) =>
    #achieve(B,interview_complete(Client,I,true));
    -information(Client,_)
.

+!do_risk_analysis(C,info(Name,SBI,Country)) =>
    R = #kyc.algorithms.risk(#executionContext.sender.name,SBI,Country);
    #achieve(#executionContext.sender.ref,assign_risk(C,R))
.

