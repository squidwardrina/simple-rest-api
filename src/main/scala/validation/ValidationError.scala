package validation

import utils.ConfigReader
import utils.MaskChar.MaskChar

trait ValidationError

case class FieldMissing(field: String) extends ValidationError {
  override def toString = s"Required field '$field' is not provided in request"
}

case class NotString(field: String) extends ValidationError {
  override def toString = s"Required field $field is expected to be a string"
}

case class UnknownAid(aid: String) extends ValidationError {
  override def toString =
    s"The provided AID $aid is unknown. Only the following AIDs are allowed: ${ConfigReader.enabledAids.mkString(", ")}"
}

case class BadPidLength(expected: Int, got: Int) extends ValidationError {
  override def toString = s"The provided PID has expected length of $expected, but received $got"
}

case class UnmatchedPidMask(expected: MaskChar, char: Char) extends ValidationError {
  override def toString =
    s"PID should match the defined mask [${ConfigReader.pidMask.mkString(", ")}]. Encountered '$char' when expected a $expected"
}

case class WrongTimeFormat(e: Throwable) extends ValidationError {
  override def toString =
    s"Timestamp expected in ISO 8601 format with time zone, like: 2000-01-01T00:00:00.9571247Z. Error cause: ${e.getMessage}"
}

case object EmptyRequestBody extends ValidationError {
  override def toString = "The request body should contain a JSON"
}
case object JsonSyntaxError extends ValidationError {
  override def toString = "The received JSON has wrong syntax"
}
