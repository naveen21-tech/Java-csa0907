import java.nio.file.*;
import java.util.Arrays;
public class Task06Demo {
 public static void main(String[] args)throws Exception{
  Path f=Files.createTempFile("passes",".csv"); PassStore s=new PassStore(f);
  s.add("P1001","192311001","Arun",12,4500);
  Files.writeString(f,"P1002,DAMAGED LINE\\n",StandardOpenOption.APPEND);
  s.add("P1003","192311003","Divya",7,3800);
  System.out.println(Arrays.toString(s.find("P1003")));
  System.out.println(Arrays.toString(s.find("P1002")));
  Files.deleteIfExists(f);
 }
}
