import userPackage._ ;
import scala.collection.mutable.ArrayBuffer;
import java.sql.{Connection, DriverManager, ResultSet}

object DBUtil  {
  private val loadUserToDB = new DBManager();
  
  def  insertUserToDB(userData: UserData):Int={
    val userName:String = userData.name;
    val userPassword:String = userData.password;
    val userEmail:String = userData.email;
    val userStatus:String = userData.status;
    val queryForInsertUser = (s"INSERT INTO socialschema.userdata (name, password, email,status) VALUES ('$userName', '$userPassword', '$userEmail','$userStatus')");
    var idValue=loadUserToDB.insert(queryForInsertUser);
    idValue;
 }
  
  def insertPostToDB(postData:PostData){
    val postMsg:String = postData.postMsg;
    val date:java.util.Date = postData.date;
    val userId:Int = postData.userId;
    val queryForInsertPost = (s"INSERT INTO socialschema.postdata(post_msg,date,user_id) VALUES ('$postMsg','$date','$userId')");
    loadUserToDB.insert(queryForInsertPost);
  }
  
  def getUserPost(id:Int):ResultSet = {
    var getUserPostById = (s"SELECT * FROM socialschema.postdata WHERE user_id='$id'");
    var dataInDB = loadUserToDB.get(getUserPostById);
    dataInDB;
  }
  
  def getUserNameList():ResultSet={
    var getUserList=("select * from socialschema.userdata");
    var nameList=loadUserToDB.get(getUserList);
    nameList;
  }
 

  def getUserName(id:Int):ResultSet={
    val queryForCurrentUser = (s"SELECT * FROM socialschema.userdata WHERE user_id='$id'");
    var nameFromDB = loadUserToDB.get(queryForCurrentUser);
    nameFromDB;
    
  }
  
  def insertFollowerToDB(user:Int,followee:Int,status:Int){
    var queryForFollower =(s"INSERT INTO socialschema.userLinkTable(follower_id,followee_id,status) VALUES ('$user','$followee',$status)");
    var returnIdForFollow = loadUserToDB.insert(queryForFollower);
  }
  
  def getUserFolloweeList(userId:Int):ResultSet={
    var queryForFolloweeList = (s"SELECT * FROM socialschema.userLinkTable WHERE follower_id='$userId'");
    var followeelist = loadUserToDB.get(queryForFolloweeList);
    followeelist;
  }
  
  def unFollowUser(userId:Int,unfollowId:Int){
    var queryForDeleteFollower = (s"DELETE FROM socialschema.userLinkTable WHERE follower_id='$userId' AND followee_id='$unfollowId'");
    loadUserToDB.delete(queryForDeleteFollower);
  }
  
  def deleteUserFromDB(id:Int,name:String){
    var querForDeleteUserTable = (s"DELETE FROM socialschema.userdata WHERE user_id='$id'");
    var queryForDeleteUserPost = (s"DELETE FROM socialschema.postdata WHERE user_id='$id'");
    var queryForDeleteUserFollower = (s"DELETE FROM socialschema.userLinkTable WHERE follower_id='$id'");
    loadUserToDB.delete(queryForDeleteUserPost);
    loadUserToDB.delete(queryForDeleteUserFollower);
    loadUserToDB.delete(querForDeleteUserTable);
    
  }
  
  def getAllPost(value:Int):ResultSet={
    var queryForGetAllPost = (s"SELECT * FROM socialschema.postdata LIMIT 5 OFFSET $value");
    var allPost = loadUserToDB.get(queryForGetAllPost);
    allPost;
  }
  
  def updateNameInDB(id:Int,newName:String){
    var queryForUpdateUserName=(s"UPDATE socialschema.userdata SET name='$newName' WHERE user_id='$id'");
    var returnUserId= loadUserToDB.update(queryForUpdateUserName);
  }
  
  def getBlockList(id:Int):ResultSet={
    var queryForGetBlockList = (s"SELECT * FROM socialschema.userLinkTable WHERE follower_id='$id'AND status='2'");
    var blockList = loadUserToDB.get(queryForGetBlockList);
    blockList;
  }
  
  def getAllUserDetail():ResultSet={
    var queryForGetAllUser=(s"SELECT * FROM socialschema.userdata");
    var allUserDetail = loadUserToDB.get(queryForGetAllUser);
    allUserDetail;
  }
  
  def updatePassword(newPassword:String,id:Int){
    var queryForUpdateUserPassword =(s"UPDATE socialschema.userdata SET name='$newPassword' WHERE user_id='$id'");
    loadUserToDB.update(queryForUpdateUserPassword);
  }
  
  def insertReactionForPost(userId:Int,postId:Int,react:Int){
    var queryForInsertPostReact = (s"INSERT INTO socialschema.postreactiontable(user_id,post_id,reaction) VALUES ('$userId','$postId','$react')");
    var reactId = loadUserToDB.insert(queryForInsertPostReact);
  }
  
}
 