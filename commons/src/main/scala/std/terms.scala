package std

import bb.expstyla.exp.{BooleanTerm, GenericTerm}

object terms {

  def unify(term1: GenericTerm, term2: GenericTerm): BooleanTerm = {
    term1.unify(term2)
  }

}
