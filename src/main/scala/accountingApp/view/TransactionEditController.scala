package accountingApp.view

import accountingApp.model.{TransactionRecord,Product}
import accountingApp.MainApp
import scalafx.scene.control.{TextField, TableColumn, Label, Alert, ChoiceBox}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import accountingApp.util.DateUtil._
import scalafx.event.ActionEvent

@sfxml
class TransactionEditDialogController (

    private val choiceBox : ChoiceBox[Product],
    private val descriptionField : Label,
    private val priceField : Label,
    private val quantityField : TextField,
    private val totalPriceField : Label,
    private val dateField : TextField

){
  var         dialogStage : Stage  = null
  private var _transaction     : TransactionRecord = null
  var         okClicked            = false
  handleChoiceBox

  def transaction = _transaction
  def transaction_=(x : TransactionRecord) {
        _transaction = x
        choiceBox.selectionModel().select(_transaction.product)
        quantityField.text= _transaction.quantity.value.toString
        totalPriceField.text= _transaction.totalPrice.value.toString
        dateField.text  = _transaction.dateOfPurchase.value.toString
        dateField.setPromptText("dd.mm.yyyy");
  }
  def handleChoiceBox : Unit = {

    choiceBox.getItems().removeAll(choiceBox.getItems())
    MainApp.productData.foreach(data => choiceBox.getItems().add(data))
    choiceBox.selectionModel().selectedItem.onChange(
    choiceBoxSelection
  )
  def choiceBoxSelection : Unit = {
    descriptionField.text = choiceBox.selectionModel().selectedItem.value.description.value
    priceField.text = choiceBox.selectionModel().selectedItem.value.unitPrice.value.toString
    calculateTotalPrice
  }

 
  }
  def handleOk(action :ActionEvent){

     if (isInputValid()) {
        _transaction.product = choiceBox.selectionModel().selectedItem.value
        _transaction.quantity.value = quantityField.getText().toInt
        calculateTotalPrice
        _transaction.dateOfPurchase.value = dateField.text.value;

        okClicked = true;
        dialogStage.close()
    }
  }

  def handleCancel(action :ActionEvent) {
        dialogStage.close();
  }
  
  def nullChecking (x : String) = x == null || x.length == 0

  def isInputValid() : Boolean = {
    var errorMessage = ""
    try{
      choiceBox.selectionModel().selectedItem.value.productName.value
    }
    catch {
      case e: NullPointerException => errorMessage += "No valid product! \n(If there are no product to choose, please add a new product from the Product Menu first.)\n"
    }
    if (nullChecking(quantityField.text.value))
      errorMessage += "No valid quantity!\n"
         else {
      try {
        (quantityField.getText().toInt);
      } catch {
          case e : NumberFormatException =>
            errorMessage += "No valid quantity (must be a integer)!\n"
      }
    }
    if (nullChecking(dateField.text.value))
      errorMessage += "No valid date!\n"
    else {
      if (!dateField.text.value.isValid) {
          errorMessage += "No valid date. Use the format dd.mm.yyyy!\n";
      }
    }

    if (errorMessage.length() == 0) {
        return true;
    } else {
        // Show the error message.
        val alert = new Alert(Alert.AlertType.Error){
          initOwner(dialogStage)
          title = "Invalid Fields"
          headerText = "Please correct invalid fields"
          contentText = errorMessage
        }.showAndWait()

        return false;
    }
   }
  def calculateTotalPrice : Unit = {
      try {
        val total : Double = priceField.getText().toDouble * quantityField.getText().toInt

        totalPriceField.text = total.toString
        _transaction.totalPrice.value = total
        
      } catch {
          case e : NumberFormatException =>
        
      }


  }
}