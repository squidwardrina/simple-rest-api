package validation

import org.joda.time.format.ISODateTimeFormat
import org.json4s.JString
import org.json4s.JsonAST.{JNothing, JValue}
import utils.MaskChar.MaskChar
import utils.{ConfigReader, MaskChar}

import scala.util.{Failure, Success, Try}

/** Responsible for validating a generic mandatory field in a JSON received by server */
trait MandatoryFieldValidator {
  val json: JValue

  def fieldName: String

  /** Checks for errors in the extracted field value
    *
    * @return Error message or None if value is valid */
  protected def findErrorInValue(value: JString): Option[ValidationError]

  /** Checks whether JSON field is present & correct
    *
    * @return Error message or None if field is valid */
  def findError: Option[ValidationError] = {
    json \ fieldName match {
      case JNothing => Some(FieldMissing(fieldName))
      case properTypeField: JString => findErrorInValue(properTypeField)
      case _ => Some(NotString(fieldName))
    }
  }
}

class PidFieldValidator(override val json: JValue) extends MandatoryFieldValidator {
  override def fieldName = "pid"

  /** Checks for errors in the extracted field value
    *
    * @return Error message or None if value is valid */
  override def findErrorInValue(value: JString): Option[ValidationError] = {
    def isMatching(maskChar: MaskChar, char: Char) = maskChar match {
      case MaskChar.digit => char.isDigit
      case MaskChar.letter => char.isLetter
    }

    val mask = ConfigReader.pidMask
    val pid = value.values
    if (mask.length != pid.length) Some(BadPidLength(mask.length, pid.length))
    else {
      val maybeUnmatched = mask.zip(pid).dropWhile(x => isMatching(x._1, x._2)).headOption
      maybeUnmatched.map { case (expected, char) => UnmatchedPidMask(expected, char) }
    }
  }
}

class AidFieldValidator(override val json: JValue) extends MandatoryFieldValidator {
  override def fieldName = "aid"

  /** Checks for errors in the extracted field value
    *
    * @return Error message or None if value is valid */
  override def findErrorInValue(value: JString): Option[ValidationError] = {
    if (ConfigReader.enabledAids.contains(value.values)) None
    else Some(UnknownAid(value.values))
  }
}

class TimestampFieldValidator(override val json: JValue) extends MandatoryFieldValidator {
  override def fieldName = "timestamp"

  override def findErrorInValue(value: JString): Option[ValidationError] = {
    Try(ISODateTimeFormat.dateTime().parseDateTime(value.values)) match {
      case Success(_) => None
      case Failure(e) => Some(WrongTimeFormat(e))
    }
  }
}