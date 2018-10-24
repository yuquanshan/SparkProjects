import org.apache.spark._

object SimpleWC {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("SimpleWC" + args(0))
    // option:
    // 0: optimal repartition
    // 1 or larger: even partitioning
    val option = args(1).toInt
    val sc = new SparkContext(sparkConf)
    val starttime = System.currentTimeMillis

    val dataopt = if (option >= 1) {
      sc.textFile("hdfs://172.31.28.117:54310/bigtext2.txt", option)
    } else {
      sc.textFile("hdfs://172.31.28.117:54310/bigtext2.txt")
    }
    if (option == 0) {
      dataopt.optRepartition()
    }
    dataopt.flatMap(_.split(" ")).filter(x => x.toLowerCase == "trump" || x.toLowerCase == "obama" || x.toLowerCase == "clinton").map(x => (x.toLowerCase, 1)).reduceByKey(_+_).collect()

    val lap = System.currentTimeMillis - starttime
    println(s"Entire workload run time: $lap")
    sc.stop()
  }	
}
