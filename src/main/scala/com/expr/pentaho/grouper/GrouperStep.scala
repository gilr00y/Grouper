package com.expr.pentaho.grouper

import java.util.{List => JUList}

import org.pentaho.di.core.RowSet
import org.pentaho.di.core.row._
import org.pentaho.di.trans._
import org.pentaho.di.trans.step._



class GrouperStep(smi: StepMeta, sdi: StepDataInterface, copyNr: Int, transMeta: TransMeta, trans: Trans) extends BaseStep(smi, sdi, copyNr, transMeta, trans) {
  override def safeModeChecking(row: RowMetaInterface): Unit = {
    logError("CHECKING SAFE MODE NOW... Kind of...")
  }
  val rowSets: JUList[RowSet] = getInputRowSets
  logError(rowSets.toString())

  def valOrNull(s: String) = if (s.isEmpty) null else s

  override def init(smi: StepMetaInterface, sdi: StepDataInterface) = {
    super.init(smi, sdi)
  }

  override def processRow(smi: StepMetaInterface, sdi: StepDataInterface): Boolean = {
    val meta = smi.asInstanceOf[GrouperStepMeta]
    val data = sdi.asInstanceOf[GrouperStepData]
    var grp: List[Any] = List()
    logError(this.rowSets.toString)
    var lastRow: Array[Object] = getRow // First row in the group.. need to initialize with something to retrieve `rowMeta`
//    val rowMeta = Option(getInputRowMeta).getOrElse(new RowMeta)
//    val metaList = rowMeta.getValueMetaList
    import scala.collection.JavaConverters._
    for(rowset <- rowSets.asScala) {
      val rowMeta = getInputRowMeta
      logError("Processing Step: " + rowset)
      val row: Array[Object] = getRowFrom(rowset)
      row.foreach(x => logError(Option(x).getOrElse("Null").toString))
    }
    val groupBys = Array("_id")
//    while(true) {
//
//      if(first) {
//        first = false
//      } else {
//        val nextRow = getRow
//        if(nextRow != null) {
//          var grp = true
//
//          // Check if all group-by conditions are met
//          for (i <- groupBys.indices) {
//            val gbIdx = rowMeta.indexOfValue(groupBys(i))
//            grp &= rowMeta.getString(lastRow, gbIdx) == rowMeta.getString(nextRow, gbIdx)
//          }
//          if(grp) {
//          } else {
//            // Write lastRow
//            putRow(rowMeta, lastRow)
//            lastRow = nextRow
//            //
//          }
//        } else {
//          setOutputDone()
//          return false
//        }
//      }
//    }
    false
  }
  
  override def dispose(smi: StepMetaInterface, sdi: StepDataInterface): Unit = {
    super.dispose(smi, sdi)
  }
}

class GrouperStepMeta extends BaseStepMeta with StepMetaInterface {
  override def excludeFromRowLayoutVerification: Boolean = { true }//super.excludeFromRowLayoutVerification()

  def getGroupFields: List[String] = {
    List("a", "b")
  }
  def getStep(smi: StepMeta, sdi: StepDataInterface, copyNr: Int, transMeta: TransMeta, trans: Trans) =
    new GrouperStep(smi, sdi, copyNr, transMeta, trans)

  def getStepData() = new GrouperStepData

  def setDefault(): Unit = {  }

  // override def check(remarks: JList[CheckResultInterface], meta: TransMeta, stepMeta: StepMeta, prev: RowMetaInterface, input: Array[String], output: Array[String], info: RowMetaInterface) = {
  //   // TODO: Implement a check
  // }

  // override def getFields(inputRowMeta: RowMetaInterface, name: String, info: Array[RowMetaInterface], nextStep: StepMeta, space: VariableSpace): Unit = {
  //   val v = valueMeta()
  //   v.setOrigin(name)
  //   inputRowMeta.addValueMeta(v)
  //   logBasic("outgoing valueMeta:" + inputRowMeta.toString())
  // }

  // override def getXML() =
  //   s"<settings><token>${token}</token><projectId>${projectId}</projectId><queue>${queue}</queue><outputField>${outputField}</outputField></settings>"

  // override def loadXML(node: org.w3c.dom.Node, databases: JList[DatabaseMeta], counters: JMap[String, Counter]): Unit = {
  //   println(s"Loading XML from $node")
  //   import javax.xml.xpath._
  //   val xpath = XPathFactory.newInstance.newXPath

  //   token = xpath.evaluate("//settings/token", node)
  //   projectId = xpath.evaluate("//settings/projectId", node)
  //   queue = xpath.evaluate("//settings/queue", node)
  //   outputField = xpath.evaluate("//settings/outputField", node)

  //   println(s"Loaded params: token = $token, projectId = $projectId, queue = $queue, outputField = $outputField")
  // }

  // override def readRep(rep: Repository, stepId: ObjectId, databases: JList[DatabaseMeta], counters: JMap[String, Counter]): Unit = {
  //   throw new UnsupportedOperationException("readRep")
  // }

  // override def saveRep(rep: Repository, transformId: ObjectId, stepId: ObjectId): Unit = {
  //   throw new UnsupportedOperationException("readRep")
  // }
}


class GrouperStepData extends BaseStepData with StepDataInterface
