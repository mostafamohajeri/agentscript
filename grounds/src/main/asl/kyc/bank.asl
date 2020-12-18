!application_request("KYC").

+!initiate(Name,Employee,SensSetting,RiskSetting) =>
    +name(Name).

+!initiate_sharing(Broker) =>
    +broker(Broker).

+!employ(Employee,Risk) =>
    +employee(Employee).

+!application_request(Consent) =>
    +consents(#executionContext.sender.name,Consent).

+!interview_completed(Profile) =>
    +interview_completed(Profile).

+!risk_assessment_completed(Risk) =>
    +interview_completed(Profile).

+!initiate_compute_risk(Client) =>
    #println(Client).

+!computed_risk(Client,Risk) =>
    #println(Client,Risk).

+!sbi_update(SBI) =>
    #println(SBI).

+!duty_to_share(Client,Attr) =>
    #println("consider sharing").

+!assessment_duty(Bank,Employee,Client_ID) =>
    #println(Bank,Employee,Client_ID).

+!assessment_duty_violation(Bank,Employee,Client_ID) =>
    #println(Bank,Employee,Client_ID).

+!invalid_risk(Employee,Client,Risk) =>
    #println(Employee,Client,Risk).