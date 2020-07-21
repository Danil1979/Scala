package ch.makery.address.model

import scalafx.beans.property.{StringProperty, ObjectProperty, IntegerProperty}
import java.time.LocalDate;
import scalikejdbc._

class Product(productID : String) extends TableObject(productID){
    var productName  = new StringProperty() 
	var description     = new StringProperty("some description")
	var supplier = new StringProperty("some Supplier Name")
	var unitPrice = ObjectProperty[Double](00.00)
	override def toString() : String = { 
		productName.value
	} 
}
object Product extends DatabaseTable[Product] with ObservableBufferModelList[Product] {
	  def apply(p: ResultName[Product])(rs: WrappedResultSet): Product = {
    new Product(rs.string(p.productID))
  }
  
  private def insertRecord(model: Product): Unit = {
    withSQL {
      insert
        .into(this)
        .namedValues(
          c.productID -> model.objectUid.toString,
        )
    }.update.apply
  }
    private def deleteRecord(model: Product): Unit = {
    withSQL {
      deleteFrom(this).where.eq(c.productID, model.objectUid.toString)
    }.update.apply
  }
}