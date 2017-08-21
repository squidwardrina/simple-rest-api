package server

import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._
import org.scalatra.swagger.SwaggerSupportSyntax.OperationBuilder
import org.scalatra.swagger._
import validation._

import scala.util.Try

/** The controller of the server, contains handlers for HTTP requests */
class Controller(implicit val swagger: Swagger) extends ScalatraServlet with JacksonJsonSupport with SwaggerSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  val postRequest: OperationBuilder = apiOperation[ResultMessage]("request").summary("Sends a JSON to the server")

  // Content type will be set as json before each action
  before() {
    contentType = formats("json")
  }

  post("/", operation(postRequest)) {
    // Check if json in body is OK, then validate it's fields
    val validationResult =
      if (request.body.isEmpty) ErrorMessage(List(EmptyRequestBody))
      else if (!isValidJsonSyntax(request.body)) ErrorMessage(List(JsonSyntaxError))
      else new RequestJsonValidator(parsedBody).validateFields

    val responseJson = validationResult.generateResponseJson

    validationResult match {
      case _: SuccessMessage => Ok(responseJson)
      case _: ErrorMessage => BadRequest(responseJson)
    }
  }

  override protected def applicationDescription: String =
    "Simple REST API application, handling POST requests with a JSON & responding with a JSON"

  private def isValidJsonSyntax(jsonStr: String) = {
    import org.json4s._
    Try(parse(jsonStr)).isSuccess
  }
}
