package accountingApp.view
import scalafx.scene.control.Alert
import accountingApp.MainApp
import scalafxml.core.macros.sfxml
@sfxml
class MenuBarController {
     def close = {
    System.exit(0)
  }
  def showHelp = {
    val alert = new Alert(Alert.AlertType.Warning){
    initOwner(MainApp.stage)
    title       = "About"
    headerText  = "Instruction"
    contentText = "The purpose of this application is to record transaction for finance management. First, add a product from the Product Menu first using the new Button. Then click on the transaction menu button to move to Transaction Menu. Then use the new button in the Transaction Menu to add a new transaction menu. The edit and delete button in both menu can be use to edit or delete existing records."
    }.showAndWait()
  }
}