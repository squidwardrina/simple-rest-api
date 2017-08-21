package utils

import com.typesafe.config.ConfigFactory
import org.apache.logging.log4j.{LogManager, Logger}
import utils.MaskChar.MaskChar

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

/** Lazy-fashion configurations reader */
object ConfigReader {
  private val logger: Logger = LogManager.getLogger()

  val configFile = "application.conf"

  private lazy val config = {
    val mbConf = Try(ConfigFactory.load(ConfigFactory.parseResources(configFile)))
    mbConf match {
      case Success(conf) =>
        logger.info(s"Found config file in resources: $configFile")
        conf
      case Failure(e) =>
        logger.error(s"No config file '$configFile' found in resources.")
        throw e
    }
  }

  /** List of enabled AIDs */
  lazy val enabledAids: Set[String] = config.getStringList("enabled_aids").asScala.toSet

  /** A mask for a PID. Specifies digits & letters positions in the PID string */
  lazy val pidMask: List[MaskChar] = config.getStringList("pid_mask").asScala.toList.map(MaskChar.withName)
}

