package infrastructure

case class AgentLogger() {

  var startTime : Long = 0;

  def start() : Unit = {startTime = System.nanoTime()}

  def timeTaken () = (System.nanoTime() - startTime) / 1e9

}
