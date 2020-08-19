package bb.exp

import it.unibo.tuprolog.core.Var

import scala.collection.mutable

case class VarMap(vars : mutable.HashMap[String,GenericTerm] = mutable.HashMap()){

  def apply(key: String) : GenericTerm = {
    if(vars.contains(key)) vars(key)
    else {
      val v = VarTerm(key)
      vars += key -> v
      v
    }
  }

  def addOne(elem: (String, GenericTerm)): VarMap.this.type = {vars += elem; this;}

  def #=(elem: (String, GenericTerm)) : VarMap.this.type =
    vars(elem._1) match {
      case VarTerm(_) => this.addOne(elem)
      case _ => throw new RuntimeException("assigning to bounded var?")
    }

  def ++(varMap: VarMap) : VarMap = VarMap(this.vars ++ varMap.vars)

  def +=(elem: (String, GenericTerm)) : VarMap.this.type = this.addOne(elem)
  def +=+(elem: (String, GenericTerm)) : BooleanTerm = { this.addOne(elem); BooleanTerm(true) }

  def clear(): Unit = vars.clear()
}
