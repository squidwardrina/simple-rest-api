package server

import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{ApiInfo, NativeSwaggerBase, Swagger}

class SwaggerController extends Swagger(Swagger.SpecVersion, "1.0.0", RestApiInfo)

class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with NativeSwaggerBase

object RestApiInfo extends ApiInfo(
  "Simple RESTful API application",
  "A dummy server",
  "",
  "rinafrid@gmail.com",
  "",
  "")
