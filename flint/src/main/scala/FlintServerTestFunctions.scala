//
//
//object instance {
//  var uuid: String = "631468ec-1a22-49d5-ad3a-c45d9bf3f1eb"
//  val self_name: String = "Me"
//
//  def findDuties (): Unit = {
//
//  }
//}
//
//object Test1 extends App {
//  var r = requests.post(
//    "http://127.0.0.1:4567/create",
//    data = ujson.Obj("template-name"-> "social-interaction_2.eflint").render(),
//    headers = Map(
//      "Content-Type" -> "application/json"
//    )
//  )
//
//  val parsed = ujson.read(r.text)
//
//  if(parsed("status").str == "SUCCESS") println(parsed("message").str)
//}
//
//
//object Test2 extends App {
//  var r = requests.post(
//    "http://127.0.0.1:4567/command",
//    data = ujson.Obj(
//      "request-type"-> "command",
//      "uuid" -> instance.uuid,
//      "data" -> ujson.Obj(
//        "command" -> "phrase",
//        "text" -> "+person(Me)")).render(),
//    headers = Map(
//      "Content-Type" -> "application/json"
//    )
//  )
//
//  println(r)
//  val parsed = ujson.read(r.text)
//}
//
//
//object Status extends App {
//  var r = requests.post(
//    "http://127.0.0.1:4567/command",
//    data = ujson.Obj(
//      "request-type"-> "command",
//      "uuid" -> instance.uuid,
//      "data" -> ujson.Obj(
//        "command" -> "status"
//      )).render(),
//    headers = Map(
//      "Content-Type" -> "application/json"
//    )
//  )
//
//  val parsed = ujson.read(r.text)
//
//
//  val duties = parsed("data")("response")("new-duties").arr.filter(a => a("arguments")(0)("value").str == instance.self_name).foreach(a => println(
//    a("fact-type").str + "(" + a("arguments")(0)("value").str + ","  + a("arguments")(1)("value").str + ")"
//  ))
//
//
//
////  println(duties)
//}
//
//
//object MetAPerson extends App {
//
//  var r = requests.post(
//    "http://127.0.0.1:4567/command",
//    data = ujson.Obj(
//      "request-type"-> "command",
//      "uuid" -> instance.uuid,
//      "data" -> ujson.Obj(
//        "command" -> "phrase",
//        "text" -> "met_a_person(Me,P)"
//      )).render(),
//    headers = Map(
//      "Content-Type" -> "application/json"
//    )
//  )
//
//  println(r)
//  val parsed = ujson.read(r.text)
//
//  println(parsed)
//  val duties = parsed("data")("response")("new-duties")
//  println(duties)
//
//}
