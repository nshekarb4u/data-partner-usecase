package com.shekar.spark

import com.shekar.Customer
import org.apache.commons.cli.MissingArgumentException
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.functions.{explode, split, trim}
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

import java.nio.file.Paths

/***
 *
 * Input-data is provided as a json file on a daily basis, we as an engineering team need to
 * Find out the interest of people based on their submissions.
 */
object CustomerInterestsApp extends App {
  val validArgs = Set("1", "2", "Both")
  if (args.length == 0) {
    println("""
    At least one command line argument is required.
    Please retry with valid 'use case ids (1 or 2 or both)'.
    Exiting the App. """.stripMargin)
    throw new MissingArgumentException("Program arguments are required.")
  } else if(!validArgs.contains(args(0))) {
    println("Invalid option is provided. Please retry with valid 'use case ids (1 or 2 or both)'.")
    throw new IllegalArgumentException("Invalid arguments are supplied!")
  }
  val cmdLineInput = args(0)

  setHadoopHomeforLocalEnv(System.getenv("environment"))

  //1. Build input json file path from the resources folder.
  val dataFile = getClass.getResource("/customerInterests.json").getPath
  val spark = SparkSession.builder()
    .appName("customer-interest-transformer")
    .master("local[*]") //is only for local, remove this if running on cluster
    .getOrCreate()

  import spark.implicits._

  val schema2 =  ScalaReflection.schemaFor[Customer].dataType.asInstanceOf[StructType]
  /*val schema = new StructType()
    .add("id", IntegerType)
    .add("Name", StringType)
    .add("Age", IntegerType)
    .add("Address", StringType)
    .add("ZIP", StringType)
    .add("Interest", StringType)*/

  val inputDf = spark.read.format("json")
    .option("multiline", true)
    .schema(schema2)
    .load(dataFile)

  val kvPairsDf = inputDf
    .select($"id", $"Name", $"Age", $"Address", $"ZIP", explode(split($"Interest", ",")).as("Interest"))
    .withColumn("Interest", trim($"Interest"))

  //kvPairsDf.show(10, false)

  cmdLineInput match {
    case "1" => writeKVPairsToCsv(kvPairsDf)
    case "2" => aggregateIntCategoriesByZip(kvPairsDf)
    case "Both" => writeKVPairsToCsv(kvPairsDf)
      aggregateIntCategoriesByZip(kvPairsDf)
    case _ => println("Invalid option provided. Please retry with valid 'use case ids (1 or 2 or both)'!.")
  }

  /**
   * Generated output can be found project work directory ..
   * output path: <project_work_directory>/output/interestKVPairs.csv
   *
   * @param transformDf --> transformed json DataFrame
   */
  def writeKVPairsToCsv(transformDf: DataFrame): Unit = {
    val outputCsvDf = transformDf
      .select($"id".as("ID"), $"Name".as("NAME"), $"Age".as("\tAGE"), $"Address".as("ADDRESS")
        , $"ZIP".as("\tZIP"), $"Interest".as("INTEREST"))

    outputCsvDf.write
      .format("csv")
      .option("header", true)
      .option("delimiter", "\t")
      .mode(SaveMode.Overwrite)
      .save("output/interestKVPairs.csv")
  }

  /** *
   * compute category aggregations by Zip and display inline
   *
   * @param inputDf --> formatted dataframe
   * @return metricsDf --> aggregated metricsDf of categories by ZipCode
   */
  def aggregateIntCategoriesByZip(inputDf: DataFrame): DataFrame = {
    val metricsDf = inputDf.groupBy($"Interest", $"ZIP").count()
    metricsDf.select($"Interest", $"ZIP".as("Zip"), $"count".as("Count"))
      .show(false)
    metricsDf
  }

  /****
   *
   * if running on windows need to set two things:
   * 1). Set HADOOP_HOME for windows --> this is done by app automatically,
   *     for this to work keep the module directory as work-directory
   * 2). Copy 'hadoop.dll' from web (based on hadoop_version) into 'C:\Windows\System32' folder.
   * hadoop.dll download link: https://github.com/steveloughran/winutils/blob/master/hadoop-3.0.0/bin/hadoop.dll
   */
  def setHadoopHomeforLocalEnv(env: String):Unit = {
    if(env == null||env == "local") {
      val os = System.getProperty("os.name").toLowerCase
      if (os.contains("win")) {
        System.setProperty("hadoop.home.dir", Paths.get("src/main/resources").toAbsolutePath.toString)
        val hadoopHome = System.getProperty("hadoop.home.dir")
        System.setProperty("java.library.path", s"$hadoopHome\\bin;${System.getProperty("java.library.path")}")
      } else {
        System.setProperty("hadoop.home.dir", "/")
      }
    }
  }
}
