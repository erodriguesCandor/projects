import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

import com.zink.fly.FlyPrime
import com.zink.fly.kit.FlyFactory

/**
  * Created by #EduardoRodrigues on 28/12/2016.
  */
object Manager {

  val idGenerator: AtomicInteger = new AtomicInteger
  val CASH = "CASH"
  val space: FlyPrime = FlyFactory.makeFly()
  val PROCESSTIME = 2 * 60 * 1000
  val TAKETIME = 60 * 1000
  val SENDMONEY = "SENDMONEY"
  val REQUEST = "REQUEST"
  val READY = "READY"
  val DONE = "DONE"
  val MANAGER = "MANAGER"
  val RECEIPT = "RECEIPT"
  val DELIVERED = "DELIVERED"
  val PROCESSING = "PROCESSING"

  val infoReadyTemplate = new Information(null, READY, null, DONE)

  def writeInfoToSpace(info: Information) = space.write(info, PROCESSTIME)

  def getInfoFromSpace(infoTemplate: Information) = space.take(infoTemplate, TAKETIME)

  def readInfoFromSpace(infoTemplate: Information) = space.read(infoTemplate, TAKETIME)

  def makeRequest(ask: String): String = {
    val requestID = UUID.randomUUID().toString
    val info = new Information(requestID, REQUEST, ask, CASH)
    writeInfoToSpace(info)
    val infoProcessing = new Information(requestID, PROCESSING, null, MANAGER)
    writeInfoToSpace(infoProcessing)
    requestID
  }


  def deliveryMoney(id: String, money: Int) : String = {
    val information = new Information(id, RECEIPT, null, MANAGER)
    getInfoFromSpace(information)
    information.ask = SENDMONEY
    information.args = money.toString
    information.to = CASH
    writeInfoToSpace(information)
    "Money Sent! Wait for the confirmation"
  }

  def getInfo(id: String) : String = {
    val info = new Information(id, null, null, MANAGER)
    readInfoFromSpace(info) match{
      case Information(_,RECEIPT,args,MANAGER) => "Your request cost "+args+"€. Make the payment to continue."
      case Information(_,READY,args,MANAGER) =>
        getInfoFromSpace(info)
        writeInfoToSpace(new Information(id,DELIVERED,null,MANAGER))
        "Your request is ready! Enjoy your "+args
      case Information(_,DELIVERED,args,MANAGER) => "Your request has been delivered."
      case _ => "Information Unavailable. Try Again Soon."
    }
  }
}
