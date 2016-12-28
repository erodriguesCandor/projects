import com.zink.fly.FlyPrime
import com.zink.fly.kit.FlyFactory

import scala.collection.mutable

/**
  * Created by #ScalaTeam on 28/12/2016.
  */
class Cashier extends Thread {

  val space: FlyPrime = FlyFactory.makeFly()
  val intervalTIME = 10 * 1000
  val PROCESSTIME = 2 * 60 * 1000
  val TAKETIME = 60 * 1000
  val SENDMONEY = "SENDMONEY"
  val RECEIPT = "RECEIPT"
  val REQUEST = "REQUEST"
  val MANAGER = "MANAGER"
  val CASH = "CASH"
  val infoTemplate = new Information(null, null, null, CASH)
  val dataBase = new mutable.HashMap[String,Int]

  override def run(): Unit = {
    while (true) {
      readInfoFromSpace(infoTemplate) match {
        case Information(id, SENDMONEY, args, _) =>
          println("sendmoneny")
          val price = dataBase.get(id)
          if(price!=args.toInt){
            getInfoFromSpace(new Information(id,null,null,CASH))
            println("Delivery Sent")
          }
          writeInfoToSpace(new Information(id, "READY","coffee",MANAGER))

        case Information(id, REQUEST, args, _) =>
          println("request")
          getInfoFromSpace(new Information(id,null,null,null))
          getInfoFromSpace(new Information(id,null,null,null))
          dataBase.put(id.toString,50)
          writeInfoToSpace(new Information(id, RECEIPT, "50", MANAGER))
        case _ => ()
      }
    }
  }

  def writeInfoToSpace(info: Information) = space.write(info, PROCESSTIME)

  def getInfoFromSpace(infoTemplate: Information) = space.take(infoTemplate, TAKETIME)

  def readInfoFromSpace(infoTemplate: Information) = space.read(infoTemplate, TAKETIME)

}
