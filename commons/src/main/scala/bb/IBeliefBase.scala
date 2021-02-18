package bb

import bb.expstyla.exp.StructTerm
import infrastructure.QueryResponse

abstract class IBeliefBase[T <: IGenericTerm] {

  def assert(term: T): Boolean

  def assert(terms: List[T]): Unit

  def query(term: T): QueryResponse

  def bufferedQuery(term: T): Iterator[QueryResponse]

  def query(): QueryResponse

  def matchTerms(term1: T, term2: T): QueryResponse

  def matchTerms(): QueryResponse

  def retract(term: T): Boolean
}
