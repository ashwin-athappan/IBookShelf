package LibraryModule.UI.Database;

import LibraryModule.UI.DisplayBook.BookListController;
import LibraryModule.UI.DisplayMember.DisplayMemberController;

import javax.swing.*;
import java.io.File;
import java.sql.*;
/*
*
* Class Consists of all the database operations
*
* */
public class DatabaseHandler {
    private final String url = "jdbc:mysql://localhost:3306/library";
    private final String userName = "root";
    private final String password = "1379";
    private Connection myConnection = null;
    private Statement myStatement= null;
    private static DatabaseHandler handler;

    private DatabaseHandler(){
        createConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                setUpBookTable();
                setUpMemberTable();
                IssueTable();
                bookImageTable();
            }
        }).start();
    }

    public static DatabaseHandler getInstance(){
        if(handler == null){
            handler = new DatabaseHandler();
        }
        return handler;
    }

    public void createConnection(){
       try {
           myConnection = DriverManager.getConnection(url, userName, password);
       }catch(SQLException e){
           e.printStackTrace();
       }
    }

    public void setUpBookTable(){
       String Table_Name = "BOOK";
       try{
           myStatement = myConnection.createStatement();
           DatabaseMetaData dbm = myConnection.getMetaData();
           ResultSet rs = dbm.getTables(null,null,Table_Name.toUpperCase(),null);

           if(rs.next()){
               System.out.println("Table "+Table_Name+" already exists.");
           }else{
                String query = "CREATE table "+Table_Name+" (id VARCHAR(20) primary key,title VARCHAR(200),author VARCHAR(200),publisher VARCHAR(200),isAvailable boolean default true);";
                myStatement.executeUpdate(query);
           }

       }catch(SQLException e){
           System.err.println(e.getMessage());
        }
    }


    public void setUpMemberTable(){
        String Table_Name = "MemberList";
        try {
            myStatement = myConnection.createStatement();
            DatabaseMetaData dbm = myConnection.getMetaData();
            ResultSet rs = dbm.getTables(null,null,Table_Name.toUpperCase(),null);

            if(rs.next()){
                System.out.println("Table "+Table_Name+" already exists.");
            }else{
                String query = "CREATE table "+Table_Name+" (id VARCHAR(20) primary key, name VARCHAR(200), mobile VARCHAR(200), email VARCHAR(200));";
                myStatement.executeUpdate(query);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void bookImageTable(){
        String Table_Name = "img";
        try {
            myStatement = myConnection.createStatement();
            DatabaseMetaData dbm = myConnection.getMetaData();
            ResultSet rs = dbm.getTables(null,null,Table_Name,null);

            if(rs.next()){
                System.out.println("Table "+Table_Name+" already exists.");
            }else{
                String query = "Create table "+Table_Name+" (id varchar(20) PRIMARY KEY, ImagePath Varchar (225),foreign key (id) references book(id));";
                myStatement.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void IssueTable(){
        String Table_Name = "Issue";
        try {
            myStatement = myConnection.createStatement();
            DatabaseMetaData dbm = myConnection.getMetaData();
            ResultSet rs = dbm.getTables(null,null,Table_Name,null);

            if(rs.next()){
                System.out.println("Table "+Table_Name+" already exists.");
            }else{
                String query = "Create table "+Table_Name+" (BookId VARCHAR(20) PRIMARY KEY, MemberId Varchar (20),renewCount integer default 0,issueTime timestamp default CURRENT_TIMESTAMP,FOREIGN  KEY (BookId) references BOOK(id),FOREIGN  KEY (MemberId) references MEMBERLIST(id) )";
                myStatement.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet execQuery(String query){
        ResultSet rs = null;
        try {
            Statement queryStatement = myConnection.createStatement();
            rs = queryStatement.executeQuery(query);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error :"+e.getMessage(),"Error Occurred",JOptionPane.ERROR_MESSAGE);
        }
        return rs;
    }

    public boolean execAction(String Action){

        try {
            Statement actionStatement = myConnection.createStatement();
            actionStatement.execute(Action);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error :"+e.getMessage(),"Error Occurred",JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    public boolean DeleteBookOperation(String ObjectID){
        String deleteStatement = "DELETE FROM book WHERE id = ?";
        String getBookImage = "Select * from img where id = '"+ObjectID+"'";
        String deleteS = "DELETE FROM img WHERE id = ?";
        try {
            Statement Statement = myConnection.createStatement();
            ResultSet rs = Statement.executeQuery(getBookImage);
            if(rs.next()){
                String imgPath = rs.getString("ImagePath");
                String subPath = imgPath.substring(8).replace("/", "\\");
                File oldImage = new File(subPath);
                if (oldImage.exists()){
                    boolean deletion = oldImage.delete();
                    if(deletion){
                        System.out.println("Image Deleted");
                    }else{
                        System.out.println("Image Not Deleted");
                    }
                }
            }


            PreparedStatement pStatement = myConnection.prepareStatement(deleteS);
            pStatement.setString(1,ObjectID);
            pStatement.executeUpdate();

            pStatement = myConnection.prepareStatement(deleteStatement);
            pStatement.setString(1,ObjectID);
            pStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean DeleteMemberOperation(String ObjectID){
        String deleteStatement = "DELETE FROM memberlist WHERE id = ?";
        try {
            PreparedStatement pStatement = myConnection.prepareStatement(deleteStatement);
            pStatement.setString(1,ObjectID);
            pStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isBookIssued(String bID){
        String statement = "Select count(*) from issue where BookId = ?";
        try {
            PreparedStatement s = myConnection.prepareStatement(statement);
            s.setString(1,bID);
            ResultSet rs = s.executeQuery();
            if(rs.next()){
                int count = rs.getInt(1);
                if(count > 0){
                    return true;
                }else{
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateBook(BookListController.Book book, String newImgLoc){
        String statement = "UPDATE BOOK SET title=? ,author=? ,publisher=? where id=?";
        try {
            PreparedStatement s = myConnection.prepareStatement(statement);
            s.setString(1,book.getTitle());
            s.setString(2,book.getAuthor());
            s.setString(3,book.getPublisher());
            s.setString(4,book.getId());

            int res = s.executeUpdate();
            if(updateImage(book.getId(),newImgLoc)){
                if(res > 0){
                    return true;
                }
            }else{
                return false;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean  updateMember(DisplayMemberController.Member member){
        String statement = "UPDATE memberlist SET name=? ,mobile=? ,email=? where id=?";
        try {
            PreparedStatement s = myConnection.prepareStatement(statement);
            s.setString(1,member.getName());
            s.setString(2,member.getMobile());
            s.setString(3,member.getEmail());
            s.setString(4,member.getId());

            int res = s.executeUpdate();
            if(res > 0){
                return true;
            }else{
                return false;
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateImage(String BookID,String newLoc){
        String q = "update img set ImagePath=? where id=?;";
        try {
            PreparedStatement s = myConnection.prepareStatement(q);
            s.setString(1,newLoc);
            s.setString(2,BookID);

            int res = s.executeUpdate();
            if(res > 0){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

