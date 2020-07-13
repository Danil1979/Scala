package ch.makery.address.view

import ch.makery.address.model.Product
import ch.makery.address.MainApp
import scalafx.scene.control.{TableView, TableColumn, Label, Alert}
import scalafxml.core.macros.sfxml
import scalafx.beans.property.{StringProperty} 
import ch.makery.address.util.DateUtil._
import scalafx.Includes._
import scalafx.event.ActionEvent

@sfxml
class PersonOverviewController(
  
    private val productTable : TableView[Product],
   
    private val productNameColumn : TableColumn[Product, String],
  

    private val productNameLabel : Label,
    
    private val descriptionLabel : Label,
  
    private val priceLabel : Label,

    private val  birthdayLabel : Label  
    
    ) {
  // initialize Table View display contents model
  productTable.items = MainApp.productData
  // initialize columns's cell values
  productNameColumn.cellValueFactory = {_.value.productName}

  
  showPersonDetails(None);
  
  productTable.selectionModel().selectedItem.onChange(
      (_, _, newValue) => showPersonDetails(Some(newValue))
  )
  
  private def showPersonDetails (product : Option[Product]) = {
    product match {
      case Some(product) =>
      // Fill the labels with info from the product object.
      productNameLabel.text <== product.productName
      descriptionLabel.text  <== product.description
      priceLabel.text = product.price.value.toString
      birthdayLabel.text   = product.date.value.asString
      case None =>
        // Person is null, remove all the text.
      productNameLabel.text = ""
      descriptionLabel.text  = ""
      priceLabel.text= ""
      birthdayLabel.text  = ""
    }    
  }
  def handleNewPerson(action : ActionEvent) = {
    val product = new Product("")
    val okClicked = MainApp.showPersonEditDialog(product);
        if (okClicked) {
            MainApp.productData += product
        }
  }
  def handleEditPerson(action : ActionEvent) = {
    val selectedPerson = productTable.selectionModel().selectedItem.value
    if (selectedPerson != null) {
        val okClicked = MainApp.showPersonEditDialog(selectedPerson)

        if (okClicked) showPersonDetails(Some(selectedPerson))

    } else {
        // Nothing selected.
        val alert = new Alert(Alert.AlertType.Warning){
          initOwner(MainApp.stage)
          title       = "No Selection"
          headerText  = "No Person Selected"
          contentText = "Please select a product in the table."
        }.showAndWait()
    }
  }
  def handleDeletePerson(action : ActionEvent) = {
      val selectedIndex = productTable.selectionModel().selectedIndex.value
      if (selectedIndex >= 0) {
          productTable.items().remove(selectedIndex);
      } 
   } 
  
}