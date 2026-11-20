import java.sql.*;
import java.util.*;
public class InsertComparison {
 static final String URL=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/campus_passes");
 static final String USER=System.getenv().getOrDefault("DB_USER","root"),PWD=System.getenv().getOrDefault("DB_PASSWORD","");
 static List<String[]> rows(int n){List<String[]> a=new ArrayList<>(n);for(int i=0;i<n;i++)a.add(new String[]{"P"+n+"_"+i,"R"+i,"Student"+i,""+(1+i%20),""+(1000+i)});return a;}
 static void clear(Connection c)throws SQLException{try(Statement s=c.createStatement()){s.executeUpdate("DELETE FROM bus_pass");}}
 static long stmt(Connection c,List<String[]> a)throws SQLException{long t=System.nanoTime();try(Statement s=c.createStatement()){for(String[] p:a)s.executeUpdate("INSERT INTO bus_pass VALUES('"+p[0]+"','"+p[1]+"','"+p[2]+"',"+p[3]+","+p[4]+")");}return System.nanoTime()-t;}
 static long prep(Connection c,List<String[]> a,boolean batch)throws SQLException{
  long t=System.nanoTime();try(PreparedStatement p=c.prepareStatement("INSERT INTO bus_pass VALUES(?,?,?,?,?)")){
   for(String[] x:a){p.setString(1,x[0]);p.setString(2,x[1]);p.setString(3,x[2]);p.setInt(4,Integer.parseInt(x[3]));p.setBigDecimal(5,new java.math.BigDecimal(x[4]));
    if(batch)p.addBatch();else p.executeUpdate();}
   if(batch)p.executeBatch();
  }return System.nanoTime()-t;
 }
 static void verify(Connection c,int n)throws SQLException{
  try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT COUNT(*) FROM bus_pass")){r.next();if(r.getInt(1)!=n)throw new SQLException("Wrong row count");}
  try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT * FROM bus_pass LIMIT 1")){ResultSetMetaData m=r.getMetaData();for(int i=1;i<=m.getColumnCount();i++)System.out.println(m.getColumnName(i)+": "+m.getColumnTypeName(i));}
 }
 public static void main(String[] z)throws Exception{try(Connection c=DriverManager.getConnection(URL,USER,PWD)){
  for(int n:new int[]{10,1000,10000,100000}){List<String[]> a=rows(n);
   clear(c);long x=stmt(c,a);verify(c,n);clear(c);long y=prep(c,a,false);verify(c,n);clear(c);long b=prep(c,a,true);verify(c,n);
   System.out.printf("n=%d Statement %.3fs Prepared %.3fs Batch %.3fs%n",n,x/1e9,y/1e9,b/1e9);
  }
 }}
}
