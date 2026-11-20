import java.io.*;import java.nio.file.*;import java.sql.*;import java.util.*;
public class MigrationTool {
 static final String URL=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/campus_passes");
 static final String USER=System.getenv().getOrDefault("DB_USER","root"),PWD=System.getenv().getOrDefault("DB_PASSWORD","");
 public static void main(String[] args)throws Exception{
  Path file=Path.of(args.length>0?args[0]:"passes.csv");if(!Files.exists(file))throw new FileNotFoundException(file.toString());
  List<String[]> valid=new ArrayList<>();List<String> rejected=new ArrayList<>();
  try(BufferedReader r=Files.newBufferedReader(file)){String line;int no=0;while((line=r.readLine())!=null){no++;String[] f=line.split(",",-1);
   try{if(f.length!=5)throw new IllegalArgumentException("expected 5 fields");
    if(f[0].isBlank()||f[1].isBlank()||f[2].isBlank())throw new IllegalArgumentException("required field empty");
    int route=Integer.parseInt(f[3].trim());double amount=Double.parseDouble(f[4].trim());
    if(route<=0||!Double.isFinite(amount)||amount<0)throw new IllegalArgumentException("invalid route/amount");
    valid.add(new String[]{f[0],f[1],f[2],""+route,""+amount});
   }catch(RuntimeException e){rejected.add("line "+no+": "+e.getMessage()+" | "+line);}
  }}
  try(Connection c=DriverManager.getConnection(URL,USER,PWD)){
   System.out.println("Rows migrated: "+PassLoader.loadAll(c,valid));inspect(c);
  }
  Files.write(Path.of("migration-rejections.txt"),rejected);System.out.println("Rejected: "+rejected.size());
 }
 static void inspect(Connection c)throws SQLException{
  DatabaseMetaData m=c.getMetaData();System.out.println("Database: "+m.getDatabaseProductName()+" "+m.getDatabaseProductVersion());
  System.out.println("Driver: "+m.getDriverName()+" "+m.getDriverVersion());
  try(ResultSet r=m.getTables(c.getCatalog(),null,"%",new String[]{"TABLE"})){while(r.next())System.out.println("Table: "+r.getString("TABLE_NAME"));}
 }
}
