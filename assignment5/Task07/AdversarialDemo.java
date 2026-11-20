import java.sql.*;
import java.nio.file.*;
import java.util.Arrays;
public class AdversarialDemo {
 public static void main(String[] args)throws Exception{
  String url=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/campus_passes");
  String user=System.getenv().getOrDefault("DB_USER","root"),pwd=System.getenv().getOrDefault("DB_PASSWORD","");
  try(Connection c=DriverManager.getConnection(url,user,pwd)){
   try(Statement s=c.createStatement()){
    s.executeUpdate("CREATE TABLE IF NOT EXISTS login_demo(id VARCHAR(80) PRIMARY KEY,pwd VARCHAR(80) NOT NULL)");
    s.executeUpdate("DELETE FROM login_demo");
    s.executeUpdate("INSERT INTO login_demo VALUES('naveen','correct-password')");
   }
   String id="nobody' OR '1'='1' -- ",wrong="incorrect";
   String sql="SELECT COUNT(*) FROM login_demo WHERE id='"+id+"' AND pwd='"+wrong+"'";
   try(Statement s=c.createStatement();ResultSet r=s.executeQuery(sql)){r.next();System.out.println("Unsafe accepts: "+(r.getInt(1)>0));}
   try(PreparedStatement p=c.prepareStatement("SELECT COUNT(*) FROM login_demo WHERE id=? AND pwd=?")){
    p.setString(1,id);p.setString(2,wrong);
    try(ResultSet r=p.executeQuery()){r.next();System.out.println("Prepared accepts: "+(r.getInt(1)>0));}
   }
  }
  Path f=Files.createTempFile("damaged",".csv");
  Files.writeString(f,"P1,R1,Name,1,10.0\\nP2,too,few\\nP3,R3,Name,X,10.0\\nP4,R4,Name,4,NaN\\n\\n");
  PassStore s=new PassStore(f);
  System.out.println("P1: "+Arrays.toString(s.find("P1")));
  System.out.println("P2: "+s.find("P2"));System.out.println("P3: "+s.find("P3"));System.out.println("P4: "+s.find("P4"));
  Files.deleteIfExists(f);
 }
}
