import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val sparkCore = "org.apache.spark" %% "spark-core" % "3.1.2"
  lazy val sparkSql = "org.apache.spark" %% "spark-sql" % "3.1.2"
  lazy val sparkCatalyst = "org.apache.spark" %% "spark-catalyst" % "3.1.2"
}
