package ch.makery.address.view

import ch.makery.address.model.{Transaction}
import ch.makery.address.MainApp
import scalafx.scene.control.{TableView, TableColumn, Label, Alert}
import scalafxml.core.macros.sfxml
import scalafx.beans.property.{StringProperty} 
import ch.makery.address.util.DateUtil._
import scalafx.Includes._
import scalafx.event.ActionEvent
import java.time.{LocalDate,LocalDateTime};
import java.util.UUID
@sfxml
class TransactionOverviewController(
  
    private val transactionTable : TableView[Transaction],

    private val productNameColumn : TableColumn[Transaction, String],

    private val quantityColumn : TableColumn[Transaction, Integer],

    private val totalPriceColumn : TableColumn[Transaction, Double],

    private val dateColumn : TableColumn[Transaction, LocalDate],

    private val addedOnColumn : TableColumn[Transaction, String],

    private val productNameLabel : Label,
    
    private val descriptionLabel : Label,
  
    private val priceLabel : Label,

    private val quantityLabel : Label,

    private val totalPriceLabel : Label,

    private val  dateLabel : Label  
    
    ) {
  // initialize Table View display contents model
  transactionTable.items = MainApp.transactionData
  // initialize columns's cell values
  productNameColumn.cellValueFactory = {_.value.product.productName}
  quantityColumn.cellValueFactory = {_.value.quantity}
  totalPriceColumn.cellValueFactory = {_.value.totalPrice}
  dateColumn.cellValueFactory = {_.value.date}
  addedOnColumn.cellValueFactory =  {_.value.addedOn}
  
  showTransactionDetails(None);
  
  transactionTable.selectionModel().selectedItem.onChange(
      (_, _, newValue) => showTransactionDetails(Some(newValue))
  )
  
  private def showTransactionDetails (transaction : Option[Transaction]) = {
    transaction match {
      case Some(transaction) =>
      // Fill the labels with info from the Transaction object.

      productNameLabel.text <== transaction.product.productName
      descriptionLabel.text  <== transaction.product.description
      priceLabel.text = transaction.product.unitPrice.value.toString
      quantityLabel.text = transaction.quantity.value.toString
      totalPriceLabel.text = transaction.totalPrice.value.toString
      dateLabel.text   = transaction.date.value.asString
      case None =>
        // Transaction is null, remove all the text.
      productNameLabel.text = ""
      descriptionLabel.text  = ""
      priceLabel.text= ""
      dateLabel.text  = ""
      quantityLabel.text = ""
      totalPriceLabel.text = ""
    }    
  }
  def handleNewTransaction(action : ActionEvent) = {
    val transaction = new Transaction(UUID.randomUUID().toString,"")
    val okClicked = MainApp.showTransactionEditDialog(transaction);
        if (okClicked) {
            MainApp.transactionData += transaction
        }
  }
  def handleEditTransaction(action : ActionEvent) = {
    val selectedTransaction = transactionTable.selectionModel().selectedItem.value
    if (selectedTransaction != null) {
        val okClicked = MainApp.showTransactionEditDialog(selectedTransaction)

        if (okClicked) showTransactionDetails(Some(selectedTransaction))

    } else {
        // Nothing selected.
        val alert = new Alert(Alert.AlertType.Warning){
          initOwner(MainApp.stage)
          title       = "No Selection"
          headerText  = "No Transaction Selected"
          contentText = "Please select a transaction in the table."
        }.showAndWait()
    }
  }
  def handleDeleteTransaction(action : ActionEvent) = {
      val selectedIndex = transactionTable.selectionModel().selectedIndex.value
      if (selectedIndex >= 0) {
          transactionTable.items().remove(selectedIndex);
      } 
   }
    def handleSwapMenu() : Unit = {
      MainApp.showProductOverview();
   } 

}