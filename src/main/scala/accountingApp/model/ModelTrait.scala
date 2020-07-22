
package accountingApp.model

import scalikejdbc._
import java.util.UUID
import scalafx.collections.ObservableBuffer
import scalafx.collections.ObservableBuffer.{Add, Remove}
import scalafx.event.subscriptions.Subscription

trait ModelTrait[T <: ModelObject] extends SQLSyntaxSupport[T] {

  implicit val session: AutoSession.type = AutoSession

  def insertRecord(model: T): Unit
  def editRecord(model: T): Unit
  def deleteRecord(model: T): Unit
  def retrieveData : List[T]
}