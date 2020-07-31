package infrastructure

import bb.exp.GenericTerm
import it.unibo.tuprolog.core.Term


case class QueryResponse(result: Boolean,bindings: Map[String,GenericTerm])

