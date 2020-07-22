package accountingApp.view

import accountingApp.model.Product
import accountingApp.MainApp
import scalafx.scene.control.{TableView, TableColumn, Label, Alert}
import scalafxml.core.macros.sfxml
import scalafx.beans.property.{StringProperty} 
import accountingApp.util.DateUtil._
import scalafx.Includes._
import scalafx.event.ActionEvent
import java.util.UUID
@sfxml
class ProductOverviewController(
  
    private val productTable : TableView[Product],
   
    private val productNameColumn : TableColumn[Product, String],

    private val supplierColumn : TableColumn[Product, String],

    private val unitPriceColumn : TableColumn[Product, Double],

    private val productNameLabel : Label,

    private val supplierLabel : Label,
    
    private val descriptionLabel : Label,
  
    private val priceLabel : Label,

    ){
  // initialize Table View display contents model
  productTable.items = MainApp.productData
  // initialize columns's cell values
  productNameColumn.cellValueFactory = {_.value.productName}
  supplierColumn.cellValueFactory = {_.value.supplier}
  unitPriceColumn.cellValueFactory = {_.value.unitPrice}

  
  showProductDetails(None);
  
  productTable.selectionModel().selectedItem.onChange(
      (_, _, newValue) => if(newValue != null)showProductDetails(Some(newValue))
  )
  
  private def showProductDetails (product : Option[Product]) = {
    product match {
      case Some(product) =>
      // Fill the labels with info from the product object.
      productNameLabel.text = product.productName.value
      descriptionLabel.text  <== product.description
      supplierLabel.text <== product.supplier
      priceLabel.text = product.unitPrice.value.toString
      case None =>
        // Product is null, remove all the text.
      productNameLabel.text = ""
      descriptionLabel.text  = ""
      supplierLabel.text  = ""
      priceLabel.text= ""
    }    
  }
  def handleNewProduct(action : ActionEvent) = {
    val product = new Product(UUID.randomUUID().toString)
    val okClicked = MainApp.showProductEditDialog(product);
        if (okClicked) {
            Product.insertRecord(product)
            MainApp.productData += product
        }
  }
  def handleEditProduct(action : ActionEvent) = {
    val selectedProduct = productTable.selectionModel().selectedItem.value
    if (selectedProduct != null) {
        val okClicked = MainApp.showProductEditDialog(selectedProduct)

        if (okClicked) showProductDetails(Some(selectedProduct))
          Product.editRecord(selectedProduct)
    } else {
        // Nothing selected.
        val alert = new Alert(Alert.AlertType.Warning){
          initOwner(MainApp.stage)
          title       = "No Selection"
          headerText  = "No Product Selected"
          contentText = "Please select a product in the table."
        }.showAndWait()
    }
  }
  def handleDeleteProduct(action : ActionEvent) = {
      val selectedIndex = productTable.selectionModel().selectedIndex.value
      if (selectedIndex >= 0) {

        Product.deleteRecord(productTable.getSelectionModel().getSelectedItem())
        productTable.items().remove(selectedIndex)


      } 
   }
   def handleSwapMenu() = {
      MainApp.showTransactionOverview();
   }

    }