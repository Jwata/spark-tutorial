import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.rdd.RDD

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.recommendation.Rating

object SimpleSparkApp {
  def main(args: Array[String]) = {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    if (args.length != 1) {
      println("Invalid args!!")
      sys.exit(1)
    }
    val movieLensHomeDir = args(0)

    val conf = new SparkConf()
      .setAppName("SimpleSparkApp")
      .set("spark.executor.memory", "2g")
    val sc = new SparkContext(conf)

    val myRatings = loadMyRatings
    val myRatingsRDD = sc.parallelize(myRatings)

    val ratings = loadRatings(sc, movieLensHomeDir)
    val movies = loadMovies(sc, movieLensHomeDir)

    // logic here
    val numRatings = ratings.count
    println("numRatings", numRatings)

    val numUsers = ratings.map(_._2.user).distinct.count
    println("numUsers", numUsers)

    val numMovies = ratings.map(_._2.product).distinct.count
    println("numMovies", numMovies)

    sc.stop()
  }

  def loadMyRatings: Seq[Rating] = {
    Seq(
      Rating(0, 242, 1),
      Rating(0, 302, 5)
    )
  }

  def loadRatings(sc: SparkContext, dir: String): RDD[(Long, Rating)] = {
    val path = dir + "/u.data"
    sc.textFile(path).map { line =>
      val fields = line.split("\t")
      (fields(3).toLong % 10, Rating(fields(0).toInt, fields(1).toInt, fields(2).toDouble))
    }
  }

  def loadMovies(sc: SparkContext, dir: String): Map[Int, String] = {
    val path = dir + "/u.item"
    sc.textFile(path).map { line =>
      val fields = line.split("\\|")
      (fields(0).toInt, fields(1))
    }.collect().toMap
  }
}