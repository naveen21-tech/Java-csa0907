package Task03;
import java.util.*;
class Student{
 final String regNo,name;Student(String r,String n){regNo=r;name=n;}
 @Override public boolean equals(Object o){return this==o||(o instanceof Student s&&regNo.equals(s.regNo));}
 // Intentionally missing hashCode for the faulty experiment.
 @Override public String toString(){return regNo+" "+name;}
}
public class Attendance{
 static final class CorrectStudent{
  final String regNo,name;CorrectStudent(String r,String n){regNo=r;name=n;}
  @Override public boolean equals(Object o){return this==o||(o instanceof CorrectStudent s&&regNo.equals(s.regNo));}
  @Override public int hashCode(){return Objects.hash(regNo);}
 }
 public static void main(String[]args){
  Set<Student> broken=new HashSet<>();broken.add(new Student("R1","A"));broken.add(new Student("R1","A"));System.out.println("Broken set size (record actual): "+broken.size());
  Set<CorrectStudent> fixed=new HashSet<>();fixed.add(new CorrectStudent("192311001","Arun"));fixed.add(new CorrectStudent("192311002","Divya"));fixed.add(new CorrectStudent("192311001","Arun"));System.out.println("Unique students: "+fixed.size());
  CorrectStudent mutableKey=new CorrectStudent("R3","C");Set<CorrectStudent>s=new HashSet<>();s.add(mutableKey);System.out.println("Correct set contains key: "+s.contains(mutableKey));
 }
}