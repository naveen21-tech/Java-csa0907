package Task03;
import java.util.*;
// Deliberately faulty version for the before-fix experiment.
class BrokenStudent{
 final String regNo,name;BrokenStudent(String r,String n){regNo=r;name=n;}
 @Override public boolean equals(Object o){return this==o||(o instanceof BrokenStudent s&&regNo.equals(s.regNo));}
}
class Student{
 private final String regNo,name;
 Student(String r,String n){regNo=r;name=n;}
 @Override public boolean equals(Object o){if(this==o)return true;if(!(o instanceof Student s))return false;return regNo.equals(s.regNo);}
 // Completed TODO 1: hash code uses exactly the identity field used by equals.
 @Override public int hashCode(){return Objects.hash(regNo);}
 @Override public String toString(){return regNo+" "+name;}
}
public class Attendance{
 public static void main(String[]args){
  Set<BrokenStudent> broken=new HashSet<>();broken.add(new BrokenStudent("R1","A"));broken.add(new BrokenStudent("R1","A"));System.out.println("Broken set size (record actual): "+broken.size());
  Set<Student> present=new HashSet<>();present.add(new Student("192311001","Arun"));present.add(new Student("192311002","Divya"));present.add(new Student("192311001","Arun"));System.out.println("Unique students: "+present.size());
  Student a=new Student("R3","C"),b=new Student("R3","Other");System.out.println("equal="+a.equals(b)+" equalHash="+(a.hashCode()==b.hashCode()));
 }
}