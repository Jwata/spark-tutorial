import org.apache.spark.{SparkContext, SparkConf}

object SimpleSparkApp {
  def main(args: Array[String]) = {
    val conf = new SparkConf().setAppName("Simple Spark Application")
    val sc = new SparkContext(conf)
    val txtFile = "sample.txt"
    val txtData = sc.textFile(txtFile, 2).cache()
    val numAs = txtData.filter(line => line.contains("a")).count()
    val numBs = txtData.filter(line => line.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
  }
}