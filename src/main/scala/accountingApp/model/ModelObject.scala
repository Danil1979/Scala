package accountingApp.model
import java.util.UUID

abstract class  ModelObject(uid: String) {
  val objectUid: UUID = UUID.fromString(uid)
}
