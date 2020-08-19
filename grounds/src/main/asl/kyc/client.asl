my_sib(#kyc.data_gen.customer_data(#myName).sib).
my_name(#kyc.data_gen.customer_data(#myName).name).
my_country(#kyc.data_gen.customer_data(#myName).country).

acceptable_purpose("KYC").
acceptable_purpose("research").


!apply_to_bank(#kyc.data_gen.customer_data(#myName).preferred_bank).

+!apply_to_bank(Bank) : #myName == #executionContext.sender.name =>
    #println(#myName + " applying to " + Bank);
    +applying_to(Bank);
    #achieve(Bank,register)
.


+!give_info(B) : my_name(M) && my_sib(S) && my_country(C) && (applying_to(B) || client_of(B)) =>
    #achieve(#executionContext.sender.ref,info(M,S,C))
.

+!give_consent(B,Purpose) : (applying_to(B) || client_of(B)) && acceptable_purpose(Purpose) =>
    #achieve(#executionContext.sender.ref,consent(B,Purpose,true))
.

+!be_informed_of_acceptance(Bank) =>
    +client_of(Bank);
    -applying_to(Bank);
    #println("Yaay! I am now a client of " + Bank)
.

+!change_data(SIB,Country) : my_name(M) =>
    -my_sib(_);
    -my_country(_);
    +my_sib(SIB);
    +my_country(Country);
    !need_to_update_data
.

+!need_to_update_data =>
    for(B in client_of(B)) {
        #achieve(B,need_to_update_data);
    }
.
