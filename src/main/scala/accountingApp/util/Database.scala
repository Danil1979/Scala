package accountingApp.util
import scalikejdbc._
import accountingApp.model.TransactionRecord


object Database  {
   val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"

  val dbURL = "jdbc:derby:myDB;create=true;";
  // initialize JDBC driver & connection pool
  Class.forName(derbyDriverClassname)

  def connect(): Unit = {
  ConnectionPool.singleton(dbURL, "", "")
  }

  def disconnect(): Unit = {
    ConnectionPool.close()
  }
}