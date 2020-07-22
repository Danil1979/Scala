package accountingApp.model
import java.util.UUID

abstract class  TableObject(uid: String) {
  val objectUid: UUID = UUID.fromString(uid)
}
