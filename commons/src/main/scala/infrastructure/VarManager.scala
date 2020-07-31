package infrastructure



import bb.exp.{GenericTerm, VarTerm}
import it.unibo.tuprolog.core.{Struct, Term, Var}

import scala.collection.mutable



object VarManager {

  def bindVar(var_name: String, variables: mutable.HashMap[String, GenericTerm]): GenericTerm = {
    if(var_name.equals("_"))
      VarTerm(Var.ANONYMOUS_VAR_NAME);
    variables.getOrElseUpdate(var_name, VarTerm(var_name))
  }

//  def bindVarNative(var_name: String, variables: mutable.HashMap[String, Any]): Any = {
//    if(var_name.equals("_"))
//      Var.underscore();
//    variables.getOrElseUpdate(var_name, Var.of(var_name))
//  }
}