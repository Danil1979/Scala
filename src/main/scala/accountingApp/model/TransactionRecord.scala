package accountingApp.model
import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import java.time.{LocalDate,LocalDateTime}
import accountingApp.util.Database
import accountingApp.util.DateUtil._
import scalikejdbc._
import scala.util.{ Try, Success, Failure }
import accountingApp.MainApp

class TransactionRecord(transactionID : String) extends TableObject(transactionID) {
	var product : Product = null

	var quantity : ObjectProperty[Integer] = ObjectProperty[Integer](0)
	var totalPrice : ObjectProperty[Double] = ObjectProperty[Double](0)
  var dateOfPurchase = ObjectProperty[String](LocalDate.now().asString)
	var addedOn = ObjectProperty[String](LocalDateTime.now().asString)

     	def calculateTotalPrice : Double = {
      try {
		val total : Double = product.unitPrice.value * quantity.value
		total
      } catch {
          case e : NumberFormatException => 		0

      }
	  
	  }

}
object TransactionRecord extends DatabaseTable[TransactionRecord]  {
	def apply(transactionID : String, productID : String, quantityI : Int, dateOfPurchaseS :String, addedOnS : String): TransactionRecord = {
    new TransactionRecord(transactionID){
      product = (MainApp.productData.find{_.objectUid.toString == productID}).get
      quantity.value = quantityI
      dateOfPurchase.value = dateOfPurchaseS
      addedOn.value = addedOnS
    }
  }

  def insertRecord(model: TransactionRecord): Unit = {
	      withSQL {
      insert
        .into(this)
        .values(
      model.objectUid.toString,
      model.product.objectUid.toString,
      model.quantity.value,
      model.dateOfPurchase.value,
      model.addedOn.value
        )
    }.update.apply
  }
  def editRecord(model: TransactionRecord): Unit = {
	sql"update transaction_record set product_id = ${model.product.objectUid.toString}, quantity = ${model.quantity.value}, date_Of_Purchase = ${model.dateOfPurchase.value} where transaction_id = ${model.objectUid.toString}".update.apply()
  }
  def deleteRecord(model: TransactionRecord): Unit = {
	sql"delete from transaction_record where transaction_id = ${model.objectUid.toString}".update.apply()
  }
  def retrieveData : List[TransactionRecord] = {
	val transactions : List[TransactionRecord]= sql"select * from transaction_record".map(rs => TransactionRecord(rs.string("transaction_id"),rs.string("product_id"),rs.int("quantity"),rs.string("date_Of_Purchase"),rs.string("added_On"))).list.apply()
    transactions
  }

}
