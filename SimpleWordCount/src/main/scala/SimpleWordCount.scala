import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
//import java.io._
/**
* Simple wordcount program that counts words frequency 
*
*/

object SimpleWordCount {
	def main(args: Array[String]){
		if (args.length < 2){
			System.err.println("Usage: SimpleWordCount <input filename> <num of partitions> <executors (id) to be killed>")
			System.exit(1)
		}
		//val start = System.nanoTime
		val filename = args(0)
		val sparkConf = new SparkConf().setAppName("SimpleWordCountwith"+args(1)+"Partitions")
		val sc = new SparkContext(sparkConf)
		
		/*if (args.length > 2){
			for(i <- 2 until args.length){
				sc.killExecutor(args(i))
			}
		}*/

		val data0 = sc.textFile(filename)
		val data = data0.repartition(args(1).toInt)
		val wordspool = data.flatMap(_.split(" ")).map(x=>(x,1))
		val reducedpool = wordspool.reduceByKey(_+_)
        //val numwords = reducedpool.count()
        //System.out.println("The number of words is "+numwords.toString)
        reducedpool.saveAsTextFile(args(2))
		//val end = System.nanoTime
		//val out = new File(dest+"/runningtime.txt")
		//val fw = new FileWriter(out)
		//fw.write("running time: "+((end-start)/10E9).toString)
		//fw.close()
	}	
}
