import sbt._
import Keys._

object ApplicationBuild extends Build {

    val appName         = "ws-chat"
    val appVersion      = "1.0-SNAPSHOT"
 
    val appDependencies = Seq(
      // Add your project dependencies here,
    )

    val main = Project(appName, file(".")).enablePlugins(play.PlayScala).settings(
      // Add your own project settings here      
      scalaVersion := "2.11.4",
      libraryDependencies ++= appDependencies,
      version := appVersion
    )

}
