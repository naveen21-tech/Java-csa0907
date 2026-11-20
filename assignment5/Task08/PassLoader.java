import java.sql.*;
import java.util.List;
public class PassLoader {
 public static int loadAll(Connection con,List<String[]> passes)throws SQLException{
  String sql="INSERT INTO bus_pass(pass_id,reg_no,name,route_no,amount) VALUES(?,?,?,?,?)";
  boolean old=con.getAutoCommit();con.setAutoCommit(false);
  try(PreparedStatement ps=con.prepareStatement(sql)){
   int n=0;
   for(String[] p:passes){
    if(p==null||p.length!=5)throw new SQLException("Expected five fields");
    ps.setString(1,p[0]);ps.setString(2,p[1]);ps.setString(3,p[2]);
    ps.setInt(4,Integer.parseInt(p[3]));ps.setBigDecimal(5,new java.math.BigDecimal(p[4]));
    ps.addBatch();n++;
    if(n%1000==0)ps.executeBatch();
   }
   ps.executeBatch();con.commit();return n;
  }catch(SQLException|RuntimeException ex){
   try{con.rollback();}catch(SQLException rollback){ex.addSuppressed(rollback);}
   if(ex instanceof SQLException se)throw se;
   throw new SQLException("Batch failed; transaction rolled back",ex);
  }finally{con.setAutoCommit(old);}
 }
}
