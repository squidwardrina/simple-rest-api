package validation

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.scalatest.{Matchers, WordSpec}

class RequestJsonValidatorTest extends WordSpec with Matchers {
  def validate(json: JValue): ResultMessage = new RequestJsonValidator(json).validateFields

  "Validator" should {
    "Return success on valid JSON" in {
      val result = validate(parse("""{"aid":"aid1","pid":"a111","timestamp":"2000-01-01T00:00:00.9571247Z"}"""))
      result shouldBe a [SuccessMessage]
    }
    "Return error if error found" in {
      val noFieldJson = parse("""{"aid":"aid1","pid":"a111"}""")
      val result = validate(noFieldJson)
      result shouldBe a [ErrorMessage]
    }
  }

}
