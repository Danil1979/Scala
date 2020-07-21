package ch.makery.address.util
import scalikejdbc._
import ch.makery.address.model.Transaction


object Database  {
   val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"

  val dbURL = "jdbc:derby:myDB;create=true;";
  // initialize JDBC driver & connection pool
  Class.forName(derbyDriverClassname)

  def connect(): Unit = {
  ConnectionPool.singleton(dbURL, "me", "mine")
  }

  def disconnect(): Unit = {
    ConnectionPool.close()
  }
}