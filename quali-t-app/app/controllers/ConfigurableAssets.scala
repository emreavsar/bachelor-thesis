package controllers

import play.api.Play
import play.api.Play.current
import play.api.mvc.{Action, AnyContent}

object ConfigurableAssets extends AssetsBuilder {
  private val assetsPath = Play.configuration.getString("assets.path").getOrElse("/public")

  def at(path: String, file: String): Action[AnyContent] = {
    var local_path = path

    // Play 2.3.8 does not support routing for different environments
    // Self implementation with some special cases

    // map /app/bower_components to /public/bower_components
    if (local_path == "/app/bower_components" && assetsPath == "/public") {
      local_path = "/"
    }
    // map /app/*file to /public/dist/*file
    else if (local_path == "/app" && assetsPath == "/public/dist") {
      local_path = "/"
    }
    else {
      local_path = local_path.concat("/")
    }

    val fullPath = assetsPath.concat(local_path)
    super.at(fullPath, file)
  }
}