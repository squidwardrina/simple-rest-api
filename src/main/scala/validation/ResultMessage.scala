package validation

/** Represents the validation message. Converts itself to JSON to be sent as server response */
trait ResultMessage {
  def getMessage: String

  def generateResponseJson = s"""{ "message": "$getMessage" }"""
}

case class ErrorMessage(errors: List[ValidationError]) extends ResultMessage {
  override def getMessage: String = errors match {
    case Nil => "unknown error"
    case oneError :: Nil => "Error found: " + oneError
    case multiple =>
      val errorsNumbered = multiple.zipWithIndex.map { case (err, i) => s"\n\t${i + 1}. $err" }
      s"${multiple.size} errors found: ${errorsNumbered.mkString(";")}"
  }
}

case class SuccessMessage(aid: String, pid: String, timestamp: String) extends ResultMessage {
  override def getMessage: String =
    s"Success! Received a request with AID '$aid', PID '$pid' and timestamp $timestamp"
}
