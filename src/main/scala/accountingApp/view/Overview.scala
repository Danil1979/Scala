package accountingApp.view

import scalafx.event.ActionEvent 
trait Overview[T] {
    def showDetails (t : Option[T]): Unit 
    def handleNewRecord (action : ActionEvent): Unit
    def handleEditRecord (action : ActionEvent): Unit 
    def handleDeleteRecord (action : ActionEvent): Unit
    def handleSwapMenu : Unit
}