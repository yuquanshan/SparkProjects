import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

/**
* Reduce multiple SimpleWordCount results (in a directory) 
*/

object WordCountReduce {
	def string2Tuple(str: String) = { 	// make "(yuquan,1)" to ("yuquan",1)		
		val spltpt = str.lastIndexOf(',')
		val key = str.slice(1,spltpt)
		val value = str.slice(spltpt+1,str.length-1)
		(key,value.toInt)
	}
	def main(args: Array[String]){
		val path = args(0)
		val sparkConf = new SparkConf().setAppName("WordCountReduce_in_"+path)
		val sc = new SparkContext(sparkConf)
		
		val data = sc.wholeTextFiles(path)
		val wordspool = data.flatMap{case(x,y)=>y.split("\n")}.map(string2Tuple(_))
		val reducedpool = wordspool.reduceByKey(_+_)
        reducedpool.saveAsTextFile(path+"/reduceresult")
	}	
}
