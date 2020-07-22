package infrastructure

import it.unibo.tuprolog.core.Term


case class QueryResponse(result: Boolean,bindings: Map[String,Term])

