package validation

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.scalatest.{Matchers, WordSpec}

class MandatoryFieldValidatorTest extends WordSpec with Matchers {
  def generateJson(aid: String = "aid1",
                   pid: String = "a111",
                   timestamp: String = "2000-01-01T00:00:00.9571247Z"): JValue =
    parse(s"""{"aid":"$aid","pid":"$pid","timestamp":"$timestamp"}""")

  "AID field validator" should {
    "return None if no errors" in {
      val result = new AidFieldValidator(generateJson()).findError
      result should not be 'defined
    }
    "error if no aid" in {
      val result = new AidFieldValidator(parse(s"""{"pid":"a111","timestamp":"2000-01-01T00:00:00.9571247Z"}""")).findError
      result shouldBe 'defined
      result.get shouldBe a[FieldMissing]
    }
    "error if aid" in {
      val result = new AidFieldValidator(generateJson(aid = "bu")).findError
      result shouldBe 'defined
      result.get shouldBe a[UnknownAid]
    }
  }

  "PID field validator" should {
    "return None if no errors" in {
      val result = new PidFieldValidator(generateJson()).findError
      result should not be 'defined
    }
    "error if no pid" in {
      val result = new PidFieldValidator(parse(s"""{"aid":"aid1","timestamp":"2000-01-01T00:00:00.9571247Z"}""")).findError
      result shouldBe 'defined
      result.get shouldBe a[FieldMissing]
    }
    "error if PID has wrong length" in {
      val result = new PidFieldValidator(generateJson(pid = "a1")).findError
      result shouldBe 'defined
      result.get shouldBe a[BadPidLength]
    }
    "error if PID doesn't suit mask" in {
      val result = new PidFieldValidator(generateJson(pid = "ab11")).findError
      result shouldBe 'defined
      result.get shouldBe a[UnmatchedPidMask]
    }
  }

  "Timestamp field validator" should {
    "return None if no errors" in {
      val result = new TimestampFieldValidator(generateJson()).findError
      result should not be 'defined
    }
    "error if no timestamp" in {
      val result = new TimestampFieldValidator(parse(s"""{"pid":"a111","aid":"aid1"}""")).findError
      result shouldBe 'defined
      result.get shouldBe a[FieldMissing]
    }
    "error if timestamp invalid" in {
      val result = new TimestampFieldValidator(generateJson(timestamp = "bu")).findError
      result shouldBe 'defined
      result.get shouldBe a[WrongTimeFormat]
    }
    "error if timestamp has bad format" in {
      val result = new TimestampFieldValidator(generateJson(timestamp = "2000/01/01T00:00:00.9571247Z")).findError
      result shouldBe 'defined
      result.get shouldBe a[WrongTimeFormat]
    }
    "error if timestamp has no time zone" in {
      val result = new TimestampFieldValidator(generateJson(timestamp = "2000-01-01T00:00:00")).findError
      result shouldBe 'defined
      result.get shouldBe a[WrongTimeFormat]
    }

  }

}
