import java.sql.{Connection, DriverManager, ResultSet};
import userPackage.UserData ;
import java.sql.{PreparedStatement,Statement,ResultSet};


class DBManager  {
  var key:Int=0;
  Class.forName("org.postgresql.Driver");
  val url = "jdbc:postgresql://localhost:5432/socialmedia"
  val user = "postgres"
  val password = "7xMdkkp-"
  val db_connection:Connection = DriverManager.getConnection(url, user, password)
  var statement:Statement=db_connection.createStatement()
  
  def insert(data:String):Int= {
    statement.executeUpdate(data,Statement.RETURN_GENERATED_KEYS);
    var result:ResultSet = statement.getGeneratedKeys();
    result.next()
    key = result.getInt(1);
    key;
  }
  
  def get(data:String):ResultSet = {
     var rs:ResultSet = statement.executeQuery(data);
     rs;
  }

  def delete(data:String){
   statement.executeUpdate(data);
  }
  
  def update(data:String){
    statement.executeUpdate(data);
  }
  

  
  
}