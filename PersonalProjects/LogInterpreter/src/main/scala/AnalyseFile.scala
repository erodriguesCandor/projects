import java.nio.file.{Files, Paths}
import java.util.concurrent.{BlockingDeque, LinkedBlockingDeque}

import scala.annotation.tailrec
import scala.collection.immutable.HashMap

/**
  * Created by #ScalaTeam on 16/02/2017.
  */


case class InfoNode(graphID: String, actIDTo: String, actIDFrom: String, injID: String, processing: Boolean) {
  def setMessage(value: Boolean) = InfoNode(graphID, actIDTo, actIDFrom, injID, value)

  def setChangeAct(valueFrom: String, valueTo: String) = InfoNode(graphID, valueFrom, valueTo, injID, processing)
}

case class LevelCount(info: Int, error: Int, warn: Int) {
  def incInfo: LevelCount = LevelCount(info + 1, error, warn)

  def incError: LevelCount = LevelCount(info, error + 1, warn)

  def incWarn: LevelCount = LevelCount(info, error, warn + 1)
}

case class Counters(processing: Int, recovered: Int, processed: Int) {

  def incProcessing: Counters = Counters(processing + 1, recovered, processed)

  def decProcessing: Counters = Counters(processing - 1, recovered, processed + 1)

  def incRecovered: Counters = Counters(processing, recovered + 1, processed)
}

//just for testing purposes!!! REMOVE
case class LoggingSignal(code: Int, level: String, nodeID: String,
                         graphID: String, actIDfrom: String, actIDto: String,
                         injID: String, message: String) extends Serializable {

  def setMessage(value: String): LoggingSignal = LoggingSignal(code, level, nodeID, graphID, actIDfrom, actIDto, injID, value)

  def setInjID(value: String): LoggingSignal = LoggingSignal(code, level, nodeID, graphID, actIDfrom, actIDto, value, message)

  def setActTo(value: String): LoggingSignal = LoggingSignal(code, level, nodeID, graphID, actIDfrom, value, injID, message)

  def setActFrom(value: String): LoggingSignal = LoggingSignal(code, level, nodeID, graphID, value, actIDto, injID, message)

  def setGraphID(value: String): LoggingSignal = LoggingSignal(code, level, nodeID, value, actIDfrom, actIDto, injID, message)

  def setNodeID(value: String): LoggingSignal = LoggingSignal(code, level, value, graphID, actIDfrom, actIDto, injID, message)

  def setLevel(value: String): LoggingSignal = LoggingSignal(code, value, nodeID, graphID, actIDfrom, actIDto, injID, message)

  def setCode(value: Int): LoggingSignal = LoggingSignal(value, level, nodeID, graphID, actIDfrom, actIDto, injID, message)

}

case class DataBase(nodesDB: HashMap[String, InfoNode], actDB: HashMap[String, Int], countLevels: LevelCount, otherCounters: Counters) {
  def setNodeID(nDB: HashMap[String, InfoNode]) = DataBase(nDB, actDB, countLevels, otherCounters)

  def setActID(aDB: HashMap[String, Int]) = DataBase(nodesDB, aDB, countLevels, otherCounters)

  def setCountLev(cLevel: LevelCount) = DataBase(nodesDB, actDB, cLevel, otherCounters)

  def setOtherCounters(oCount: Counters) = DataBase(nodesDB, actDB, countLevels, oCount)

  def setNodesAndAct(nDB: HashMap[String, InfoNode], aDB: HashMap[String, Int]) = DataBase(nDB, aDB, countLevels, otherCounters)

  def setNodesActandCount(nDB: HashMap[String, InfoNode], aDB: HashMap[String, Int], cLevel: LevelCount) = DataBase(nDB, aDB, cLevel, otherCounters)

  def setNodesCountandOthers(nDB: HashMap[String, InfoNode], cLevel: LevelCount, oCount: Counters) = DataBase(nDB, actDB, cLevel, oCount)

  def setNodesAndCount(nDB: HashMap[String, InfoNode], cLevel: LevelCount) = DataBase(nDB, actDB, cLevel, otherCounters)

  def setCounters(cLevel: LevelCount, oCount: Counters) = DataBase(nodesDB, actDB, cLevel, oCount)
}

case class Graphics(processing : LineChart, processed : LineChart, levels : BarChart)

class AnalyseFile(updateTime: Int = 2, logs: LinkedBlockingDeque[LoggingSignal]) extends Thread {

  val SLEEPTIME = updateTime * 1000

  override def run(): Unit = {
    processingLog(DataBase(new HashMap[String, InfoNode](), new HashMap[String, Int](), LevelCount(0, 0, 0), Counters(0, 0, 0)))
  }

  @tailrec
  private def processingLog(db: DataBase): DataBase = {
    val start: Long = System.currentTimeMillis()
    val database: DataBase = {
      processLogs(db)
    }
    val spentTime: Long = System.currentTimeMillis() - start
    if (spentTime > 0)
      Thread.sleep(SLEEPTIME - spentTime)
    //updateGraphics(database)
    processingLog(database)
  }

  @tailrec
  private def processLogs(data: DataBase): DataBase = {
    if (!logs.isEmpty) {
      processLogs(processLog(data, logs.poll()))
    } else
      data
  }


  def processLog(data: DataBase, log: LoggingSignal): DataBase = {
    log.code match {
      case 0 =>
        val nDB = data.nodesDB + (log.nodeID -> InfoNode(log.graphID, log.actIDfrom, log.actIDto, log.injID, false))
        val levelCount = processLevel(log.level, data)
        data.setNodesAndCount(nDB, levelCount)
      //not utility defined yet
      case 1 =>
        ???
      case 2 =>
        //update nodeDB
        val oldNodeInfo = data.nodesDB(log.nodeID)
        val newNDB = data.nodesDB.updated(log.nodeID, oldNodeInfo.setChangeAct(log.actIDfrom, log.actIDto))
        //update actDB
        val newActDB = {
          val lastValueFrom = data.actDB(log.actIDfrom)
          val lastValueTo = data.actDB(log.actIDto)
          val updateIntermediate = data.actDB.updated(log.actIDfrom, lastValueFrom - 1)
          updateIntermediate.updated(log.actIDto, lastValueTo + 1)
        }
        val levelCount = processLevel(log.level, data)

        data.setNodesActandCount(newNDB, newActDB, levelCount)
      case 3 =>
        //update nodeDB
        val oldNodeInfo = data.nodesDB(log.nodeID)
        val newNDB = data.nodesDB.updated(log.nodeID, oldNodeInfo.setMessage(true))
        val levelCount = processLevel(log.level, data)
        val otherCount = data.otherCounters.incProcessing
        data.setNodesCountandOthers(newNDB, levelCount, otherCount)
      case 4 =>
        val oldNodeInfo = data.nodesDB(log.nodeID)
        val newNDB = data.nodesDB.updated(log.nodeID, oldNodeInfo.setMessage(false))
        val levelCount = processLevel(log.level, data)
        val otherCount = data.otherCounters.decProcessing
        data.setNodesCountandOthers(newNDB, levelCount, otherCount)
      case 5 =>
        val levelCount = processLevel(log.level, data)
        data.setCountLev(levelCount)
      case 6 =>
        val levelCount = processLevel(log.level, data)
        val otherCount = data.otherCounters.incRecovered
        data.setCounters(levelCount, otherCount)
    }
  }

  def processLevel(logLevel: String, data: DataBase): LevelCount = logLevel match {
    case "ERROR" => data.countLevels.incError
    case "WARN" => data.countLevels.incWarn
    case "INFO" => data.countLevels.incInfo
  }
}

