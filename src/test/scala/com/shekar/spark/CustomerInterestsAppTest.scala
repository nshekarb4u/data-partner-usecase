package com.shekar.spark

import org.apache.commons.cli.MissingArgumentException
import org.apache.spark.sql.SparkSession
import org.scalatest._

class CustomerInterestsAppTest extends FlatSpec with Matchers {
  "The App " should "exit with exception when arguments are empty!" in {
    assertThrows[MissingArgumentException] {
      CustomerInterestsApp.main(Array())
    }
  }

  "The App " should "exit with exception when invalid arguments are supplied" in {
    assertThrows[IllegalArgumentException] {
      CustomerInterestsApp.main(Array("4"))
    }
  }

  "The App " should "return aggregated category metrics by zipCode when input is '2' " in {
    CustomerInterestsApp.main(Array("2"))
  }

  "The App " should "create csv output of kvPairs of users to interest categories" in {
    CustomerInterestsApp.main(Array("1"))
  }

  "The App " should "create csvOutput and aggregated metrics when commandline argument is 'Both' " in {
    CustomerInterestsApp.main(Array("Both"))
  }
}
