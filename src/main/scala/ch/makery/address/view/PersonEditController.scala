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
class PersonEditDialogController (

    private val  productNameField : TextField,
    private val   descriptionField : TextField,
    private val priceField : TextField,
    private val   birthdayField : TextField

){
  var         dialogStage : Stage  = null
  private var _product     : Product = null
  var         okClicked            = false

  def product = _product
  def product_=(x : Product) {
        _product = x

        productNameField.text = _product.productName.value
        descriptionField.text  = _product.description.value
        priceField.text= _product.price.value.toString
        birthdayField.text  = _product.date.value.asString
        birthdayField.setPromptText("dd.mm.yyyy");
  }

  def handleOk(action :ActionEvent){

     if (isInputValid()) {
        _product.productName <== productNameField.text
        _product.description    <== descriptionField.text
        _product.price.value = priceField.getText().toInt
        _product.date.value       = birthdayField.text.value.parseLocalDate;

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
    if (nullChecking(priceField.text.value))
      errorMessage += "No valid postal code!\n"
    else {
      try {
        Integer.parseInt(priceField.getText());
      } catch {
          case e : NumberFormatException =>
            errorMessage += "No valid postal code (must be an integer)!\n"
      }
    }
    if (nullChecking(birthdayField.text.value))
      errorMessage += "No valid birtday!\n"
    else {
      if (!birthdayField.text.value.isValid) {
          errorMessage += "No valid birthday. Use the format dd.mm.yyyy!\n";
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