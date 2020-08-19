package benchmark

import bb.expstyla.exp.{GenericTerm, StringTerm}
import std.converters._

object cham_data {

  def color(id: Int): String = {
    id % 3 match {
      case 0 => "red"
      case 1 => "blue"
      case 2 => "yellow"
    }
  }


  var nb_chameneos = 1000
  var nb_meetings = 1000




}


