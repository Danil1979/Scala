package accountingApp

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}
import scalafx.collections.{ObservableBuffer}
import accountingApp.model.{TransactionRecord, Product}
import accountingApp.util.Database
import accountingApp.view.{TransactionEditDialogController, ProductEditDialogController}
import scalafx.stage.{ Stage, Modality }
import scalafx.beans.property.ObjectProperty

object MainApp extends JFXApp {
  // the data as an observable list of Transactions
  Database.connect()
  val productData = new ObservableBuffer[Product]()
  productData ++= Product.retrieveData 

  val transactionData = new ObservableBuffer[TransactionRecord]()
  transactionData ++= TransactionRecord.retrieveData


  
  // transform path of RootLayout.fxml to URI for resource location.
  val rootResource = getClass.getResourceAsStream("view/RootLayout.fxml")
  // initialize the loader object.
  val loader = new FXMLLoader(null, NoDependencyResolver)
  // Load root layout from fxml file.
  loader.load(rootResource);
  // retrieve the root component BorderPane from the FXML 
  val roots = loader.getRoot[jfxs.layout.BorderPane]
  // initialize stage
  stage = new PrimaryStage {
    title = "AccountingApp"
    scene = new Scene {
      root = roots
    }
  }
  // actions for display transaction overview window 
  def showTransactionOverview() = {
    val resource = getClass.getResourceAsStream("view/TransactionOverview.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
    this.transactionData.foreach(data => data.totalPrice = ObjectProperty[Double](data.calculateTotalPrice))
  } 
  
   def showTransactionEditDialog(transaction: TransactionRecord): Boolean = {
    val resource = getClass.getResourceAsStream("view/TransactionEditDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[TransactionEditDialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    control.dialogStage = dialog
    control.transaction = transaction
    dialog.showAndWait()
    control.okClicked
  } 
    def showProductEditDialog(product: Product): Boolean = {
      val resource = getClass.getResourceAsStream("view/ProductEditDialog.fxml")
      val loader = new FXMLLoader(null, NoDependencyResolver)
      loader.load(resource);
      val roots2  = loader.getRoot[jfxs.Parent]
      val control = loader.getController[ProductEditDialogController#Controller]

      val dialog = new Stage() {
        initModality(Modality.ApplicationModal)
        initOwner(stage)
        scene = new Scene {
          root = roots2
      }
    }
      control.dialogStage = dialog
      control.product = product
      dialog.showAndWait()
      control.okClicked
  }

  def showProductOverview() = {
    val resource = getClass.getResourceAsStream("view/ProductOverview.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }  
  // call to display PersonOverview when app start
  showProductOverview()
}