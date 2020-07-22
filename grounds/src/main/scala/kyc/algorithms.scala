package kyc

import it.unibo.tuprolog.core.Term

object algorithms {
  def risk(bank: String,  sbi : Term, country:Term) : Int = {
    if(country.toString == "wonderland")
      0
    else if(sbi.toString == "queen")
      1
    else 3
  }
}
