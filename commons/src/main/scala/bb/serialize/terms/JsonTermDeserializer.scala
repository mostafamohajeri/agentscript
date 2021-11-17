package bb.serialize.terms

import bb.expstyla.exp._
import com.google.gson.{Gson, JsonElement}
import bb.serialize.DeserializeException

import scala.collection.mutable.ListBuffer

case class JsonTermDeserializer(gson: Gson) {

  import com.google.gson.JsonParser



  def deserialize(string: String): GenericTerm = deserialize(JsonParser.parseString(string))

  def deserialize(json: JsonElement): GenericTerm = {
    if (json.getAsJsonObject.has("functor")) {
      val f                              = json.getAsJsonObject.get("functor").getAsString
      var terms: ListBuffer[GenericTerm] = ListBuffer[GenericTerm]()
      json.getAsJsonObject.get("terms").getAsJsonArray.forEach(e => terms += deserialize(e))
      return StructTerm(f, Seq.from(terms))
    } else if (json.getAsJsonObject.has("value"))
      if (json.getAsJsonObject.getAsJsonPrimitive("value").isBoolean)
        return BooleanTerm(json.getAsJsonObject.getAsJsonPrimitive("value").getAsBoolean)
      else if (json.getAsJsonObject.getAsJsonPrimitive("value").isString)
        return StringTerm(json.getAsJsonObject.getAsJsonPrimitive("value").getAsString)
      else if (json.getAsJsonObject.getAsJsonPrimitive("value").isNumber) {
        val v = json.getAsJsonObject.getAsJsonPrimitive("value").getAsNumber
        if (v.intValue() == v) {
          return IntTerm(v.intValue())
        }
        return DoubleTerm(v.doubleValue())
      }

    throw DeserializeException("not a well formed json")

  }
}
