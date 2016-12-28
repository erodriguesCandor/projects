/**
  * Created by #EduardoRodrigues on 28/12/2016.
  */
object APP {
  def main(args: Array[String]): Unit = {
    val id = Manager.makeRequest("coffee")
    Thread.sleep(3*1000)
    println(Manager.getInfo(id))
    println(Manager.deliveryMoney(id,50))
    Thread.sleep(3*1000)
    println(Manager.getInfo(id))
  }
}
