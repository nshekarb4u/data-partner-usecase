import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.shekar"
ThisBuild / organizationName := "shekar"

lazy val root = (project in file("."))
  .settings(
    name := "data-partner-usecase",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += sparkCore,
    libraryDependencies += sparkSql,
    libraryDependencies += sparkCatalyst
  )

ThisBuild / description := "This data-engineer exercise to screen candidates."
ThisBuild / homepage    := Some(url("https://github.com/nshekarb4u/data-partner-usecase"))

ThisBuild / developers := List(
  Developer(
     id    = "nshekarb4u",
     name  = "Shekar Nyala",
     email = "nshekarb4u@gmail.com",
     url   = url("https://github.com/nshekarb4u")
   )
)