package ch.makery.address.model

import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import java.time.{LocalDate,LocalDateTime}
import java.time.format.DateTimeFormatter
import ch.makery.address.util.Database
import ch.makery.address.util.DateUtil._
import scalikejdbc._
import java.util.UUID
import scala.util.{ Try, Success, Failure }

class Transaction(transactionID : String,var productID : String ) extends TableObject(transactionID) {
	var product : Product = selectProduct
	var quantity : ObjectProperty[Integer] = ObjectProperty[Integer](0)
	var totalPrice : ObjectProperty[Double] = ObjectProperty[Double](0)
	var date     = ObjectProperty[LocalDate](LocalDate.now())
	val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	var addedOn = ObjectProperty[String](LocalDateTime.now().format(formatter))

     	def calculateTotalPrice : Double = {
      try {
		val total : Double = product.unitPrice.value * quantity.value
		total
      } catch {
          case e : NumberFormatException =>
		0
      }
	  
	  }
	  def selectProduct : Product = {
		  if(productID!= ""){
			 Product.getWrappedModel(UUID.fromString(productID)).get
		  } else   new Product("")

	  }
}
object Transaction extends DatabaseTable[Transaction] with ObservableBufferModelList[Transaction] {
	  def apply(t: ResultName[Transaction])(rs: WrappedResultSet): Transaction = {
		new Transaction(rs.string(t.transactionID), rs.string(t.productID))
  }
    private def insertRecord(transaction: Transaction): Unit = {
    withSQL {
      insert
        .into(this)
        .namedValues(
          c.transactionID -> transaction.objectUid.toString,
          c.productID -> transaction.product.objectUid.toString,
        )
    }.update.apply
  }
}
