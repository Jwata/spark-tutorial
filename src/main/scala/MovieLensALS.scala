import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.rdd.RDD

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.recommendation.{MatrixFactorizationModel, ALS, Rating}

object MovieLensALS {
  def main(args: Array[String]) = {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("application").setLevel(Level.INFO)

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

    // train
    val (training, validation, test) = splitRatings(ratings, myRatingsRDD)
    val (model, rank, iterations, lambda) = train(training, validation)

    // test
    val testRmse = time("Computing RMSE for test ...") { computeRmse(model, test, test.count) }
    Logger.getLogger("application").info("The best model was trained with rank = " + rank + ", and numIter = "
      + iterations + ", and lambda = " + lambda + ", and its RMSE on the test set is " + testRmse + ".")

    // compare to a naive baseline
    val meanRating = training.map(r => r.rating).mean
    val baseLineRmse = math.sqrt(test.map(r => (r.rating - meanRating) * (r.rating - meanRating)).mean)
    Logger.getLogger("application").info("The baseline RMSE is " + baseLineRmse)
    val improvement = (baseLineRmse - testRmse) / baseLineRmse * 100
    Logger.getLogger("application").info("The best model improves the baseline by " + "%1.2f".format(improvement) + "%.")

    // recommend
    val recommendations = time("Getting recommendations ...") { model.recommendProducts(user = 0, num = 10) }
    recommendations.foreach(r =>
      Logger.getLogger("application").info(movies(r.product) + r.rating)
    )

    sc.stop()
  }

  def train(training: RDD[Rating], validation: RDD[Rating]): (MatrixFactorizationModel, Int, Int, Double) = {
    val ranks = List(8, 12)
    val iterations = List(10, 20)
    val lambdas = List(0.001, 0.01, 0.1)

    var bestModel: Option[MatrixFactorizationModel] = None
    var bestValidationRmse = Double.MaxValue
    var bestRank = 0
    var bestNumIter = -1
    var bestLambda = -1.0

    for (r <- ranks; i <- iterations; l <- lambdas) {
      val model = time("Training model ...") {
        new ALS()
          .setRank(r)
          .setLambda(l)
          .setIterations(i)
          .run(training)
      }
      val validationRmse = time ("Computing RMSE for validation ...") {
        computeRmse(model, validation, validation.count)
      }
      Logger.getLogger("application").info("RMSE (validation) = " + validationRmse + " for the model trained with rank = "
        + r + ", and numIter = " + i + ", and lambda = " + l + ".")
      if (validationRmse < bestValidationRmse) {
        bestModel = Some(model)
        bestValidationRmse = validationRmse
        bestRank = r
        bestNumIter = i
        bestLambda = l
      }
    }

    return (bestModel.get, bestRank, bestNumIter, bestLambda)
  }

  def computeRmse(model: MatrixFactorizationModel, data: RDD[Rating], n: Long): Double = {
    val predictions = model.predict(data.map { d => (d.user, d.product) })
    val predictAndRatingPairs = predictions.map { p =>
      ((p.user, p.product), p.rating)
    }.join(data.map { d =>
      ((d.user, d.product), d.rating)
    }).values
    math.sqrt(predictAndRatingPairs.map(x => (x._1 - x._2) * (x._1 - x._2)).mean)
  }

  def splitRatings(ratings: RDD[(Long, Rating)], myRatings: RDD[Rating]): (RDD[Rating], RDD[Rating], RDD[Rating]) = {
    val numPartitions = 4
    val training = ratings
      .filter(x => x._1 < 6)
      .values
      .union(myRatings)
      .repartition(numPartitions)
      .cache()

    val validation = ratings
      .filter(x => x._1 >= 6 && x._1 < 8)
      .values
      .repartition(numPartitions)
      .cache()

    val test = ratings
      .filter(x => x._1 >= 8)
      .values
      .cache()

    (training, validation, test)
  }

  def loadMyRatings: Seq[Rating] = {
    Seq(
      Rating(0, 242, 1),
      Rating(0, 302, 5)
    )
  }

  def loadRatings(sc: SparkContext, dir: String): RDD[(Long, Rating)] = {
    val path = dir + "/ratings.dat"
    time("Loading ratings file ...") {
      sc.textFile(path).map { line =>
        val fields = line.split("::")
        (fields(3).toLong % 10, Rating(fields(0).toInt, fields(1).toInt, fields(2).toDouble))
      }
    }
  }

  def loadMovies(sc: SparkContext, dir: String): Map[Int, String] = {
    val path = dir + "/movies.dat"
    time("Loading movies file...") {
      sc.textFile(path).map { line =>
        val fields = line.split("::")
        (fields(0).toInt, fields(1))
      }.collect().toMap
    }
  }

  def time[R](message: String)(block: => R) = {
    val start = System.currentTimeMillis()
    val result = block
    val end = System.currentTimeMillis()
    Logger.getLogger("application").debug(message + " Elapsed Time: %d ms".format(end - start))
    result
  }
}