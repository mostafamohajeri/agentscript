package infrastructure

import bb.IGenericTerm
import bb.expstyla.exp.GenericTerm

case class QueryResponse(result: Boolean, bindings: Map[String, GenericTerm], answer: Option[GenericTerm] = Option.empty)
