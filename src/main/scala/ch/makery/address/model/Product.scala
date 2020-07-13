package ch.makery.address.model

import scalafx.beans.property.{StringProperty, DoubleProperty, ObjectProperty}
import java.time.LocalDate;

class Product ( productNames : String ){
  var productName  = new StringProperty(productNames) 
	var description     = new StringProperty("some description")
	var price = DoubleProperty(00.00)
	var date       = ObjectProperty[LocalDate](LocalDate.of(1999, 2, 21))
}