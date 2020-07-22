// package ch.makery.address.model

// import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
// import java.time.{LocalDate,LocalDateTime}
// import java.time.format.DateTimeFormatter
// import ch.makery.address.util.Database
// import ch.makery.address.util.DateUtil._
// import scalikejdbc._
// import scala.util.{ Try, Success, Failure }

// class Transaction (val transactionNames : String) extends Database {
// 	def this()     = this(null, null)
// 	var product = new Product();
// 	var quantity = ObjectProperty[Integer](0)
// 	var totalPrice : ObjectProperty[Double] = ObjectProperty[Double](0)
// 	var date       = ObjectProperty[LocalDate](LocalDate.now())
// 	val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
// 	var addedOn = ObjectProperty[String](LocalDateTime.now().format(formatter))

//      	def calculateTotalPrice : Double = {
//       try {
// 		val total : Double = product.unitPrice.value * quantity.value
// 		total
//       } catch {
//           case e : NumberFormatException =>
// 		0
//       }
	  
//   	}
	// def save() : Try[Int] = {
	// 	if (!(isExist)) {
	// 		Try(DB autoCommit { implicit session => 
	// 			sql"""
	// 				insert into transaction (product, transactionName,
	// 					quantity, totalPrice, date, addedOn) values 
	// 					(${product}, ${transactionName.value}, ${quantity.value},
	// 						${totalPrice.value},${date.value.asString}, ${addedOn.value.asString})
	// 			""".update.apply()
	// 		})
	// 	} else {
	// 		Try(DB autoCommit { implicit session => 
	// 			sql"""
	// 			update transaction 
	// 			set 
	// 			product  = ${product} ,
	// 			transactionName   = ${transactionName.value},
	// 			quantity     = ${quantity.value},
	// 			totalPrice = ${totalPrice.value},
	// 			date       = ${date.value.asString},
	// 			addedOn       = ${addedOn.value.asString}
	// 			 where addedOn = ${addedOn.value.asString}

	// 			""".update.apply()
	// 		})
	// 	}
			
// 	}
// 	def delete() : Try[Int] = {
// 		if (isExist) {
// 			Try(DB autoCommit { implicit session => 
// 			sql"""
// 				delete from transaction where  
// 					addedOn = ${addedOn.value.asString} 
// 				""".update.apply()
// 			})
// 		} else 
// 			throw new Exception("transaction not Exists in Database")		
// 	}
// 	def isExist : Boolean =  {
// 		DB readOnly { implicit session =>
// 			sql"""
// 				select * from transaction where 
// 				addedOn = ${addedOn.value.asString} 
// 			""".map(rs => rs.string("addedOn")).single.apply()
// 		} match {
// 			case Some(x) => true
// 			case None => false
// 		}

// 	}
// }
// object Transaction extends Database{
// 	def apply (
// 		productP : Product, 
// 		transactionNameS : String,
// 		quantityI : Int,
// 		totalPriceD : Double,
// 		dateS : String,
// 		addedOnS : String
// 		) : Transaction = {

// 		new Transaction(transactionNameS) {
// 			product     = productP
//             quantity.value = quantityI
//             totalPrice.value = totalPriceD
//             date.value       = dateS.parseLocalDate
//             addedOn.value = addedOnS.parseLocalDate
// 		}
		
// 	}
// 	def initializeTable() = {
// 		DB autoCommit { implicit session => 
// 			sql"""
// 			create table product (
//               id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
//               product VARBINARY(MAX)
// 			  transactionName varchar(64), 
// 			  quantity intr, 
// 			  totalPrice double,
// 			  date varchar(100),
// 			  addedOn varchar(64)
// 			)
// 			""".execute.apply()
// 		}
// 	}
	
// 	def getAllTransactions : List[Transaction] = {
// 		DB readOnly { implicit session =>
// 			sql"select * from product".map(rs => Transaction(rs.string("transactionName"),
// 				rs.int("quantity"),rs.double("totalPrice"), 
// 				rs.string("date"),rs.string("addedOn") )).list.apply()
// 		}
// 	}
// }

