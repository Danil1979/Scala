package accountingApp.view

import accountingApp.model.{TransactionRecord}
import accountingApp.MainApp
import scalafx.scene.control.{TableView, TableColumn, Label, Alert}
import scalafxml.core.macros.sfxml
import scalafx.beans.property.{StringProperty} 
import accountingApp.util.DateUtil._
import scalafx.Includes._
import scalafx.event.ActionEvent
import java.time.{LocalDate,LocalDateTime};
import java.util.UUID
import javafx.scene.control.cell.PropertyValueFactory
@sfxml
class TransactionOverviewController(
  
    private val transactionTable : TableView[TransactionRecord],

    private val productNameColumn : TableColumn[TransactionRecord, String],

    private val quantityColumn : TableColumn[TransactionRecord, Integer],

    private val totalPriceColumn : TableColumn[TransactionRecord, Double],

    private val dateColumn : TableColumn[TransactionRecord, String],

    private val addedOnColumn : TableColumn[TransactionRecord, String],

    private val productNameLabel : Label,
    
    private val descriptionLabel : Label,

    private val supplierLabel : Label, 
  
    private val priceLabel : Label,

    private val quantityLabel : Label,

    private val totalPriceLabel : Label,

    private val  dateLabel : Label  
    
    ) extends Overview[TransactionRecord]{
  // initialize Table View display contents model
  transactionTable.items = MainApp.transactionData
  // initialize columns's cell values
  productNameColumn.cellValueFactory = {_.value.product.productName}
  quantityColumn.cellValueFactory = {_.value.quantity}
  totalPriceColumn.cellValueFactory = {_.value.totalPrice}
  dateColumn.cellValueFactory = {_.value.dateOfPurchase}
  addedOnColumn.cellValueFactory =  {_.value.addedOn}
  
  showDetails(None);
  
  transactionTable.selectionModel().selectedItem.onChange(
      (_, _, newValue) => if(newValue != null) showDetails(Some(newValue))
  )
  
   def showDetails (transaction : Option[TransactionRecord]) : Unit = {
    transaction match {
      case Some(transaction) =>
      // Fill the labels with info from the TransactionRecord object.

      productNameLabel.text <== transaction.product.productName
      descriptionLabel.text  <== transaction.product.description
      supplierLabel.text  <== transaction.product.supplier
      priceLabel.text = transaction.product.unitPrice.value.toString
      quantityLabel.text = transaction.quantity.value.toString
      totalPriceLabel.text = transaction.totalPrice.value.toString
      dateLabel.text   = transaction.dateOfPurchase.value.toString
      case None =>
        // TransactionRecord is null, remove all the text.
      productNameLabel.text = ""
      descriptionLabel.text  = ""
      priceLabel.text= ""
      supplierLabel.text = ""
      dateLabel.text  = ""
      quantityLabel.text = ""
      totalPriceLabel.text = ""
    }    
  }
  def handleNewRecord(action : ActionEvent) : Unit = {
    val transaction = new TransactionRecord(UUID.randomUUID().toString)
    val okClicked = MainApp.showTransactionEditDialog(transaction);
        if (okClicked) {
            TransactionRecord.insertRecord(transaction)
            MainApp.transactionData += transaction
        }
  }
  def handleEditRecord(action : ActionEvent) : Unit= {
    val selectedTransaction = transactionTable.selectionModel().selectedItem.value
    if (selectedTransaction != null) {
        val okClicked = MainApp.showTransactionEditDialog(selectedTransaction)

        if (okClicked) showDetails(Some(selectedTransaction))
        TransactionRecord.editRecord(selectedTransaction)
        transactionTable.refresh()

    } else {
        // Nothing selected.
        val alert = new Alert(Alert.AlertType.Warning){
          initOwner(MainApp.stage)
          title       = "No Selection"
          headerText  = "No TransactionRecord Selected"
          contentText = "Please select a transaction in the table."
        }.showAndWait()
    }
  }
  def handleDeleteRecord(action : ActionEvent) : Unit = {
      val selectedIndex = transactionTable.selectionModel().selectedIndex.value
      if (selectedIndex >= 0) {
          TransactionRecord.deleteRecord(transactionTable.getSelectionModel().getSelectedItem())
          transactionTable.items().remove(selectedIndex)

      } 
   }
    def handleSwapMenu() : Unit = {
      MainApp.showProductOverview();
   } 

}