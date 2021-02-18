package serialize.terms

import bb.expstyla.exp._
import com.google.gson.{Gson, JsonArray, JsonElement, JsonObject}

case class JsonTermSerializer(gson: Gson) {

  def serialize(t: GenericTerm): JsonElement =
    t match {
      case StructTerm(functor, terms) =>
        val jsonObject = new JsonObject()
        jsonObject.addProperty("functor", functor)
        val termsArray = new JsonArray()
        terms.map(serialize).foreach(termsArray.add)
        jsonObject.add("terms", termsArray)
        jsonObject
      case BooleanTerm(value) =>
        val jsonObject = new JsonObject()
        jsonObject.addProperty("value", value)
        jsonObject

      case StringTerm(value) =>
        val jsonObject = new JsonObject()
        jsonObject.addProperty("value", value)
        jsonObject

      case DoubleTerm(value) =>
        val jsonObject = new JsonObject()
        jsonObject.addProperty("value", value)
        jsonObject

      case IntTerm(value) =>
        val jsonObject = new JsonObject()
        jsonObject.addProperty("value", value)
        jsonObject

      case _ => throw new TypeNotPresentException(t.getClass.toString, new Throwable)
    }

}
