
package ch.makery.address.model
import scalikejdbc._
import java.util.UUID
import scalafx.collections.ObservableBuffer
import scalafx.collections.ObservableBuffer.{Add, Remove}
import scalafx.event.subscriptions.Subscription

trait DatabaseTable[T <: TableObject] extends SQLSyntaxSupport[T] {
  implicit val session: AutoSession.type = AutoSession
  val c: ColumnName[T] = column

  def apply(rn: ResultName[T])(rs: WrappedResultSet): T

  def hasInitialized: Boolean = {
    DB getTable tableName match {
      case None => false
      case _ => true
    }
  }

  val querySyntax: QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[T], T] = syntax(tableName.substring(0, 1))
  val modelList: List[T] = {
    withSQL {
      select.from(this as querySyntax)
    }.map(this(querySyntax.resultName)).list.apply
  }

  def modelCount: Int = modelList.size

  def getModel(predicate: T => Boolean): Option[T] = {
    modelList.find(predicate)
  }

  def getModel(modelUid: UUID): Option[T] = {
    modelList.find(_.objectUid.equals(modelUid))
  }
}

trait ObservableBufferModelList[T <: TableObject] {
  this: DatabaseTable[T] =>
  val wrappedModelList: ObservableBuffer[T] = ObservableBuffer(modelList)
  val bufferModelList: ObservableBuffer[T] = ObservableBuffer()

  val modelSubscription: Subscription = wrappedModelList.onChange {
    (_, changes) => for (change <- changes) {
      change match {
        case Add(_, added) => bufferModelList.appendAll(added)
        case Remove(_, removed) => removed.foreach { bufferModelList.remove(_) }
        case _ =>
      }
    }
  }

  def registerModel(model: T): Unit = {
    wrappedModelList.append(model)
  }

  def unregisterModel(model: T): Unit = {
    wrappedModelList.remove(model)
  }

  def wrappedModelCount: Int = wrappedModelList.size
  def bufferModelCount: Int = bufferModelList.size

  def getWrappedModel(predicate: T => Boolean): Option[T] = {
    wrappedModelList.find(predicate)
  }

  def getWrappedModel(modelUid: UUID): Option[T] = {
    wrappedModelList.find(_.objectUid.equals(modelUid))
  }

//   def createModel(withArgument: U)
}