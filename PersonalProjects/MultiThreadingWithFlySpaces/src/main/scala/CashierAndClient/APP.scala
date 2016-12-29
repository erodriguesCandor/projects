package CashierAndClient

/**
  * Created by #EduardoRodrigues on 28/12/2016.
  */
object APP {
  def main(args: Array[String]): Unit = {
    for{
      x <- 1 to 300
    }{
    val id = Manager.makeRequest("coffee")
    Thread.sleep(1000)
    println(Manager.getInfo(id))
    println(Manager.deliveryMoney(id,50))
    Thread.sleep(1000)
    println(Manager.getInfo(id))}
  }
}
