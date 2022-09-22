package std

trait CommunicationLogger {
  def logBeliefMessage(src:String,dest:String,payLoad: String)
  def logUnBeliefMessage(src:String,dest:String,payLoad: String)
  def logAskMessage(src:String,dest:String,payLoad: String)
  def logRespondMessage(src:String,dest:String,payLoad: String)
  def logAchieveMessage(src:String,dest:String,payLoad: String)
}

case class SinkComminicationLogger() extends CommunicationLogger {
  override def logBeliefMessage(src: String, dest: String, payLoad: String): Unit = {

  }

  override def logUnBeliefMessage(src: String, dest: String, payLoad: String): Unit = {

  }

  override def logAskMessage(src: String, dest: String, payLoad: String): Unit = {

  }

  override def logRespondMessage(src: String, dest: String, payLoad: String): Unit = {

  }

  override def logAchieveMessage(src: String, dest: String, payLoad: String): Unit = {

  }


}
