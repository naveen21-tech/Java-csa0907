import java.sql.*;import java.util.*;
public class TransactionFailureDemo {
 public static void main(String[] x)throws Exception{
  String url=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/campus_passes");
  String user=System.getenv().getOrDefault("DB_USER","root"),pwd=System.getenv().getOrDefault("DB_PASSWORD","");
  List<String[]> a=new ArrayList<>();for(int i=0;i<10;i++)a.add(new String[]{"TX_"+i,"REG_"+i,"Test"+i,"1","500.00"});
  a.set(5,new String[]{"TX_BAD","REG_BAD","Bad","not-int","500.00"});
  try(Connection c=DriverManager.getConnection(url,user,pwd)){
   try(Statement s=c.createStatement()){s.executeUpdate("DELETE FROM bus_pass WHERE pass_id LIKE 'TX_%'");}
   try{PassLoader.loadAll(c,a);}catch(SQLException e){System.out.println("Expected failure: "+e.getMessage());}
   try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT COUNT(*) FROM bus_pass WHERE pass_id LIKE 'TX_%'")){r.next();System.out.println("Rows after rollback: "+r.getInt(1));}
  }
 }
}
