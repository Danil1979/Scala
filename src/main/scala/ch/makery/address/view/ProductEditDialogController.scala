package ch.makery.address.view

import ch.makery.address.model.Product
import ch.makery.address.MainApp
import scalafx.scene.control.{TextField, TableColumn, Label, Alert}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import ch.makery.address.util.DateUtil._
import scalafx.event.ActionEvent

@sfxml
class ProductEditDialogController (

    private val productNameField : TextField,
    private val descriptionField : TextField,
    private val supplierField : TextField,
    private val priceField : TextField
){
  var         dialogStage : Stage  = null
  private var _product     : Product = null
  var         okClicked            = false

  def product = _product
  def product_=(x : Product) {
        _product = x

        productNameField.text = _product.productName.value
        descriptionField.text  = _product.description.value
        supplierField.text = _product.supplier.value
        priceField.text= _product.unitPrice.value.toString
    
  }

  def handleOk(action :ActionEvent){

     if (isInputValid()) {
        _product.productName <== productNameField.text
        _product.description    <== descriptionField.text
        _product.supplier <== supplierField.text
        _product.unitPrice.value = priceField.getText().toDouble

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

    if (nullChecking(productNameField.text.value))
      errorMessage += "No valid product name!\n"
    if (nullChecking(descriptionField.text.value))
      errorMessage += "No valid description!\n"
    if (nullChecking(descriptionField.text.value))
      errorMessage += "No valid supplier!\n"
    if (nullChecking(priceField.text.value))
      errorMessage += "No valid price!\n"
    else {
      try {
        (priceField.getText().toDouble);
      } catch {
          case e : NumberFormatException =>
            errorMessage += "No valid price (must be a double)!\n"
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
}