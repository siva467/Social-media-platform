import scala.io.StdIn;
import userPackage._
import java.util.Date;
import java.sql.{Connection, DriverManager, ResultSet};
import scala.collection.mutable.ArrayBuffer

class ConnectWorld {
   private var userId:Int=0;
   private var currentUserName:String="name";
   private var currentPassword:String="password";
   private var currentEmail:String="email";
   def registerUser():UserData={
     var data = collectUserData()
     userId = DBUtil.insertUserToDB(data);
     var userDetails = DBUtil.getUserName(userId);
     userDetails.next();
     currentUserName = userDetails.getString("name");
     currentPassword = userDetails.getString("password");
     currentEmail = userDetails.getString("email");
     println("your are successfully stored Name : "+data.name+" Password : "+data.password+ "Email : "+data.email+" User ID : "+userId);
     data;
   
    }
   
     def showUserDetails(userArray:ArrayBuffer[Int]){
        for( x <- userArray ){
       var userName=DBUtil.getUserName(x);
       userName.next();
       println(userName.getString("name"),userName.getInt("user_id"));
       
      }
    }

   
   def collectUserData():UserData={
     var user_name = readLine("\nEnter your Name: ");
     var user_password = readLine("\nEnter your Password: ");
     var user_email = readLine("\nEnter your email: ");
     var userDetails = UserData(user_name,user_password,user_email,"true");
     userDetails;
   }
   
  def postFeed(){
    var postData = collectPostData()
    DBUtil.insertPostToDB(postData)
   }
   
   def collectPostData():PostData = {
     var date:Date = new Date();
     var postMessage = readLine("\nEnter your Post: ");
     var postDetails = PostData(postMessage,date,userId);
     postDetails;
   }
   
   def getMyFeeds(){
     var dataInDB = DBUtil.getUserPost(userId);
     println("your post :");
     while (dataInDB.next()) {
       println(dataInDB.getString("post_msg"));
      }
   }
   
   def followUser(){
     var userName = DBUtil.getUserNameList();
      while (userName.next()) {
       println(userName.getString("name"),userName.getInt("user_id"));
      }
      println("\nEnter your Followee id");
      var follweeId=readInt(); 
       var status:Int=1;
      DBUtil.insertFollowerToDB(userId,follweeId,status);
   }
  
   def unfollowUser(){
     var followeeList = DBUtil.getUserFolloweeList(userId);
     var eachIdForName = ArrayBuffer[Int]() ;
     while (followeeList.next()) {
       var eachId:Int=followeeList.getInt("followee_id");
       eachIdForName += eachId; 
      }
     showUserDetails(eachIdForName);
     println("Enter your unfollowee id")
     var unFolloweeId:Int = readInt();
     DBUtil.unFollowUser(userId,unFolloweeId)
   }
   
   def getUserFeeds(){
     println("Enter other user Id for view post");
     var otherUserId = readInt();
     var dataInDB = DBUtil.getUserPost(otherUserId);
     println("your post :");
     while (dataInDB.next()) {
       println(dataInDB.getString("post_msg"));
      }
   }
   def blockOrUnblock(){
     println("\nPress 1 for block \nPress 2 for un block");
     var numForBlkOrUnblk=readInt();
     
     numForBlkOrUnblk match{
       case 1=>{
         println("enter id for block user");
         var blockUserId=readInt();
         var status:Int=2;
         DBUtil.insertFollowerToDB(userId,blockUserId,status);
       }
       case 2=>{
         var eachIdForName = ArrayBuffer[Int]();
         var blockList=DBUtil.getBlockList(userId);
         while (blockList.next()) {
         var eachId:Int=blockList.getInt("followee_id");
         eachIdForName += eachId; 
         }
         showUserDetails(eachIdForName);
         println("Enter id for unblock");
         var unBlockUser=readInt();
         DBUtil.unFollowUser(userId,unBlockUser)
       }
     }
   }
   
   def changeUserDetail(){
     println("\nPress 1 for update name\nPress 2 for change Password");
     var numForUserUpdate=readInt();
     if(numForUserUpdate==1){
       var newName=readLine("Enter new name : ");
       DBUtil.updateNameInDB(userId,newName);
       currentUserName=newName;
     }else if(numForUserUpdate==2){
       println("Press 0 forgot Password\nPress 1 for Previous password");
       var numForSelection = readInt();
       if(numForSelection==0){
         var email = readLine("Enter your email");
         if(email == currentEmail){
           var newPassword=readLine("Enter new password");
           DBUtil.updatePassword(newPassword,userId);
           currentPassword=newPassword;
         }
         else{
           println("Wrong Email");
         }
       }
       else if(numForSelection==1){
         var previousPassword=readLine("Enter Previous password");
         if(previousPassword == currentPassword){
           var newPassword=readLine("Enter new password");
           DBUtil.updatePassword(newPassword,userId);
           currentPassword=newPassword;
         }
         else{
           println("Wrong Password");
         }
       }
       else{
         println("Please select proper number");
       }
       
       
     }else{
       println("Please give the correct number for selection");
     }
   }
   
   def viewAllPost(){
     var offsetValue:Int=0;
     var checkValue=true; 
     while(checkValue){
       var allPost=DBUtil.getAllPost(offsetValue);
       while (allPost.next()) {
         println(allPost.getString("post_msg"),allPost.getInt("post_id"));
       }
       println("\nPress 1 view more post\nPress 2 for React\nPress 3 for exit");
       var numValue = readInt();
       if(numValue == 1){
         offsetValue = offsetValue+5;
       }else if(numValue == 2){
         println("Enter Post Id for Reaction")
         var userReactPostId = readInt();
         var userReactOption = showReactOption();
         DBUtil.insertReactionForPost(userId,userReactPostId,userReactOption);
       }else if(numValue == 3){
         checkValue = false;
       }else{
         println("please give the correct option");
       }
     }
     
   }
   
   def showReactOption():Int={
     println("Press 1 for Like\nPress 2 for dislike\nPress 3 for Sad\nPress 4 for happy\nPress 5 for Angry");
     var userOptionForReact = readInt();
     userOptionForReact;
   }
   
   def sigin():Int={
     var userName = readLine("Enter your name : ");
     var password = readLine("Enter your Password :");
     var showWarning = true;
     var allUserDetails = DBUtil.getAllUserDetail();
     while (allUserDetails.next()) {
       if(allUserDetails.getString("name") == userName){
         if(allUserDetails.getString("password") == password){
           userId=allUserDetails.getInt("user_id");
           currentUserName = allUserDetails.getString("name");
           currentPassword = allUserDetails.getString("password");
           currentEmail = allUserDetails.getString("email");
           println("\nsuccessfully signed in");
           showWarning = false ;
         }
       }
     };
     if(showWarning){
         println("\ncouldn't find your account");
       }
     userId;
   }

   def deleteUser(){
     DBUtil.deleteUserFromDB(userId,currentUserName);
   }
}

object SocialMedia{
   def main(args: Array[String]) { 
     var dataInClass = new ConnectWorld();
     var valueForLoop =true;
     while(valueForLoop){
       println("Press 1 for Sigup\nPress 2 for Sigin");
       var optForSigupOrSigin=readInt();
       optForSigupOrSigin match{
         case 1 =>{
           var userdata=dataInClass.registerUser();
           if(userdata.name !="name"){
             valueForLoop = false;
           }
         }
         case 2 =>{
           var id=dataInClass.sigin();
           if(id != 0){
             valueForLoop = false;
           }
         }
         case _ =>{
           println("Please give proper option");
         }
       }
     }
      
     var conditionForLoop=true;
     while(conditionForLoop){
       println("\nPress 1 for post feed\nPress 2 for get my post\nPress 3 for follow other person\nPress 4 for unfollow other Person\nPress 6 for delete user\nPress 5 for particular user post\nPress 7 for block or unblock\nPress 8 for change user details\nPress 9 for exit\nPress 10 view all post"); 
       var numForOption = readInt();
       numForOption match{
         case 1 => {
          dataInClass.postFeed()
         }
         case 2=>{
          dataInClass.getMyFeeds();
         }
         case 3=>{
           dataInClass.followUser();
         }
         case 4=>{
           dataInClass.unfollowUser();
         }
         case 5=>{
           dataInClass.getUserFeeds();
         }
         case 6=>{
           dataInClass.deleteUser();
         }
         case 7=>{
           dataInClass.blockOrUnblock();
         }
         case 8=>{
           dataInClass.changeUserDetail()
         }
         case 9=>{
           conditionForLoop=false;
         }
         case 10=>{
           dataInClass.viewAllPost();
         }
       
        
          
     }
   }
     }
}