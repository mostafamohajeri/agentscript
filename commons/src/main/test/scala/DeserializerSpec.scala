import bb.expstyla.exp._
import com.google.gson.{Gson, JsonObject}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import bb.serialize.DeserializeException
import bb.serialize.terms.{JsonTermDeserializer, JsonTermSerializer}

class DeserializerSpec extends AnyWordSpec with BeforeAndAfterAll {

  val deserializer = JsonTermDeserializer(new Gson())
  val serializer = JsonTermSerializer(new Gson())


  "A Deserializer" when {
    "sent a malformed object" should {
      "throw a bb.serialize.DeserializeException exception" in {
        assertThrows[DeserializeException] {
          deserializer.deserialize(new JsonObject())
        }
      }
    }
  }

  "A Serialized StructTerm" when {
    "deserialized" should {
      "become the same" in {
        assert(deserializer.deserialize(serializer.serialize(StructTerm("func", Seq()))).getStringValue equals "func")
        assert(deserializer.deserialize(serializer.serialize(StructTerm("func", Seq(StringTerm("hello"))))).getStringValue equals "func(hello)")
      }
    }
  }

  "A Serialized DoubleTerm" when {
    "deserialized" should {
      "become the same" in {
        assert(deserializer.deserialize(serializer.serialize(DoubleTerm(10.2))) equals DoubleTerm(10.2))
      }
    }
  }

  "A Serialized BooleanTerm" when {
    "deserialized" should {
      "become the same" in {
        assertResult(BooleanTerm(false))(deserializer.deserialize(serializer.serialize(BooleanTerm(false))))
        assertResult(BooleanTerm(true))(deserializer.deserialize(serializer.serialize(BooleanTerm(true))))
      }
    }
  }

  "A Serialized IntTerm" when {
    "deserialized" should {
      "become the same" in {
        assert(deserializer.deserialize(serializer.serialize(IntTerm(999))) equals IntTerm(999))
      }
    }
  }

  "A Serialized Object" when {
    "deserialized" should {
      "become the same" in {
        assert(deserializer.deserialize("func(1)") equals StructTerm("func", Seq(IntTerm(1))))
      }
    }
  }

}