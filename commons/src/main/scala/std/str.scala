package std

import bb.expstyla.exp.{BooleanTerm, GenericTerm}
import infrastructure.QueryResponse
import prolog.terms.Trail

object str {
  def concat(str1: String, str2: String): String =
    str1.concat(str2)

  def replace(str1: String, oldChars: String, newChars: String): String =
    str1.replace(oldChars, newChars)

  def length(str1: String): Int =
    str1.length

}
