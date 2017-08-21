package server

import org.scalatra.test.scalatest._

class ControllerTest extends ScalatraWordSpec {
  implicit val swagger = new SwaggerController
  addServlet(new Controller, "/*")

  "Server controller" should {
    "return bad request when no JSON sent" in {
      post("/") {
        status should equal(400)
      }
    }
    "return bad request when JSON has bad format" in {
      post("/", """{"bu":bu}""", Map("Content-Type" -> "application/json")) {
        status should equal(400)
      }
    }
    "return Ok when json is set" in {
      val goodJson ="""{"aid":"aid1","pid":"a111","timestamp":"2000-01-01T00:00:00.9571247Z"}"""
      post("/", goodJson, Map("Content-Type" -> "application/json")) {
        status should equal(200)
      }

    }
  }
}