package bb

import bb.expstyla.exp.{GenericTerm, StructTerm}
import infrastructure.{IAgent, QueryResponse}

abstract class IBeliefBase[T <: GenericTerm] {

  def assertOne(term: T): Boolean

  def query(term: T): QueryResponse

  def bufferedQuery(term: T): Iterator[QueryResponse]

  def query(): QueryResponse

  def matchTerms(term1: T, term2: T): QueryResponse

  def matchTerms(): QueryResponse

  def retractOne(term: T): Boolean

  def assert(terms: List[T]): Unit
}
