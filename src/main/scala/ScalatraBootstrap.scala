import javax.servlet.ServletContext

import server.{Controller, ResourcesApp, SwaggerController}
import org.scalatra.LifeCycle

/** Scalatra framework bootstrap - mounts the controller & swagger ui */
class ScalatraBootstrap extends LifeCycle {
  implicit val swagger = new SwaggerController

  override def init(context: ServletContext) {
    context.initParameters("org.scalatra.cors.allowCredentials") = "false"
    context.mount(new Controller, "/*", "*")
    context.mount(new ResourcesApp, "/api-docs")
  }
}