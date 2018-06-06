import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object SimplePi {
  def main(args: Array[String]) {
    if (args.length < 2){
      System.err.println("Usage: SimplePi <num_trials> <num_mappers>")
      System.exit(1)
    }
    val trials = args(0).toInt
    val mappers = args(1).toInt
    assert(mappers <= trials, "Mappers should be less than trials!")
    val sparkConf = new SparkConf().setAppName("SimplePi"+args(1)+"Partitions")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.parallelize(0 until trials, mappers)
    val scale = 1000000
    def inCircle(seed: Int, countDown: Int): Double = {
      util.Random.setSeed(seed)
      (0 until countDown).map { _ =>
        val a = util.Random.nextDouble()
        val b = util.Random.nextDouble()
        if ((a - 0.5) * (a - 0.5) + (b - 0.5) * (b - 0.5) < 0.25) 1 else 0
      }.reduce(_+_).toDouble / countDown
    }
    System.out.println("Estimated Pi is " + rdd.map(inCircle(_, scale)).reduce(_+_)/trials/0.25)
  }	
}
