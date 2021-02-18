import bb.expstyla.exp.{BooleanTerm, DoubleTerm, IntTerm, StringTerm, StructTerm}
import com.google.gson.Gson
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import serialize.terms.JsonTermSerializer

class SerializerSpec extends AnyWordSpec with BeforeAndAfterAll {

  val serializer = JsonTermSerializer(new Gson())

  override def beforeAll(): Unit = {

  }

  "A Serialized StructTerm" when {

    "has no terms" should {
      "produce 0 terms in Json" in {
        assert(serializer.serialize(StructTerm("fun", Seq())).getAsJsonObject.get("terms").getAsJsonArray.size() equals 0)
      }

      "have the proper functor" in {
        assert(serializer.serialize(StructTerm("fun", Seq())).getAsJsonObject.get("functor").getAsString == "fun")
      }
    }

    "has multiple/nested terms" should {
      "serialize the terms" in {
        assert(serializer.serialize(StructTerm("fun", Seq(BooleanTerm(true), StringTerm("term_2")))).getAsJsonObject.get("terms").getAsJsonArray.size() == 2)
      }

      "serialize the terms recursively" in {
        assert(serializer.serialize(StructTerm("fun", Seq(StructTerm("fun_inner", Seq(BooleanTerm(true)))))).getAsJsonObject.get("terms").getAsJsonArray.get(0).getAsJsonObject.get("functor").getAsString equals "fun_inner")
      }
    }
  }

  "A Serialized BooleanTerm" when {
    "is true/false" should {
      "produce true/false" in {
        assert(serializer.serialize(BooleanTerm(true)).getAsJsonObject.get("value").getAsBoolean equals true)
        assert(serializer.serialize(BooleanTerm(false)).getAsJsonObject.get("value").getAsBoolean equals  false)
      }
    }
  }

  "An IntTerm" when {
    "is serialized" should {
      "produce the same number" in {

        assert(serializer.serialize(IntTerm(10)).getAsJsonObject.get("value").getAsNumber equals 10)
      }
    }
  }

  "A DoubleTerm" when {
    "is serialized" should {
      "produce the same number" in {
        assert(serializer.serialize(DoubleTerm(10.22)).getAsJsonObject.get("value").getAsNumber equals 10.22)
      }
    }
  }
}