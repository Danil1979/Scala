package accountingApp.model

import scalafx.beans.property.{StringProperty, ObjectProperty, IntegerProperty}
import java.time.LocalDate;
import scalikejdbc._

class Product(productID : String) extends ModelObject(productID){
  var productName  = new StringProperty() 
	var description     = new StringProperty("some description")
	var supplier = new StringProperty("some Supplier Name")
	var unitPrice = ObjectProperty[Double](00.00)
	override def toString() : String = { 
		productName.value
	} 
}
object Product extends ModelTrait[Product]{
  def apply(productID : String, productNameS : String, descriptionS : String, supplierS : String, unitPriceD : Double): Product = {
    new Product(productID){
      productName.value = productNameS
      description.value = descriptionS
      supplier.value = supplierS
      unitPrice.value = unitPriceD
    }
  }

  def insertRecord(model: Product): Unit = {
    withSQL {
      insert
        .into(this)
        .values(
      model.objectUid.toString,
      model.productName.value,
      model.description.value,
      model.supplier.value,
      model.unitPrice.value,
        )
    }.update.apply
  }
  def deleteRecord(model: Product): Unit = {
    sql"delete from product where product_id = ${model.objectUid.toString}".update.apply()
  }
  def editRecord(model: Product) : Unit = {
    sql"update product set product_name = ${model.productName.value}, description = ${model.description.value}, supplier = ${model.supplier.value}, unit_Price = ${model.unitPrice.value} where product_id =${model.objectUid.toString}".update.apply()
  }
  

  def retrieveData : List[Product] = {
    val products : List[Product]= sql"select * from product".map(rs => 
    Product(rs.string("product_id"),rs.string("product_name"),rs.string("description"),rs.string("supplier"),rs.double("unit_Price"))).list.apply()
    products
  }

}
