package kyc

object data_gen {

  val customer_data : Map[String,customer] = Map (
    "client1" -> customer("alice","trader","england","ing1"),
    "client2" -> customer("mad hatter","hatter","wonderland","ing1"),
    "client3" -> customer("mary elizabeth heart","queen","castle","abn1"),
    "client4" -> customer("cheshire cat","prophet","roof","abn1"),
  )


  val employee_data : Map[String,employee] = Map (
    "employee1" -> employee("ing1"),
    "employee2" -> employee("abn1")
  )

  val bank_data : Map[String,bank] = Map (
    "ing1"-> bank(Seq("employee1")),
    "abn1"-> bank(Seq("employee2"))
  )

  case class customer(name: String, sib: String, country:String, preferred_bank:String) {}
  case class employee(bank:String) {}
  case class bank(employee:Seq[String]) {}
}
