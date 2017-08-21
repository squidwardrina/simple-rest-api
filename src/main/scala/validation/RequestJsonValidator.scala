package validation

import org.json4s.JsonAST.{JString, JValue}

/** Receives the JSON from the request and validates it's fields as specified in spec */
class RequestJsonValidator(json: JValue) {
  private val validators = List(
    new AidFieldValidator(json),
    new PidFieldValidator(json),
    new TimestampFieldValidator(json))

  def validateFields: ResultMessage = {
    val errors = validators.flatMap(_.findError)
    if (errors.isEmpty) SuccessMessage(extract("aid"), extract("pid"), extract("timestamp"))
    else ErrorMessage(errors)
  }

  private def extract(field: String) = (json \ field).asInstanceOf[JString].values
}
