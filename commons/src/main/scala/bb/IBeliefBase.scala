package bb

import bb.expstyla.exp.{GenericTerm, StructTerm}
import infrastructure.{ExecutionContext, IAgent, QueryResponse}

abstract class IBeliefBase[T <: GenericTerm] {

  def assertOne(term: T) (implicit executionContext: ExecutionContext) : Boolean

  def query(term: T) (implicit executionContext: ExecutionContext) : QueryResponse

  def bufferedQuery(term: T) (implicit executionContext: ExecutionContext) : Iterator[QueryResponse]

  def query()  : QueryResponse

  def matchTerms(term1: T, term2: T) : QueryResponse

  def matchTerms() : QueryResponse

  def retractOne(term: T) (implicit executionContext: ExecutionContext) : Boolean

  def assert(terms: List[T]) (implicit executionContext: ExecutionContext) : Unit

  def forceAssertOne(term: GenericTerm) : Unit
}
