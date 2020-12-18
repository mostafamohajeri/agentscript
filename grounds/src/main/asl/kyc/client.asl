my_sib(#kyc.data_gen.customer_data(#myName).sib).
my_name(#kyc.data_gen.customer_data(#myName).name).
my_country(#kyc.data_gen.customer_data(#myName).country).

acceptable_purpose("KYC").
acceptable_purpose("research").

//!apply_to_bank("BankA").

+!apply_to_bank(Bank) : #myName == #executionContext.sender.name =>
    #println(#myName + " applying to " + Bank);
    +applying_to(Bank);
    #banking.kyc_utils.EFlintEnabledMessaging.send(Bank,register)
.

