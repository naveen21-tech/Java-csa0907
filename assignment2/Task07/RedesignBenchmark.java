package Task07;
public class RedesignBenchmark{
 static volatile int blackhole;
 static String original(int tickets,int fields){String all="";for(int t=0;t<tickets;t++){String ticket="";for(int f=0;f<fields;f++)ticket+="FIELD"+f+"=value;";all+=ticket;}return all;}
 static String redesigned(int tickets,int fields){StringBuilder all=new StringBuilder();for(int t=0;t<tickets;t++){StringBuilder ticket=new StringBuilder();for(int f=0;f<fields;f++)ticket.append("FIELD").append(f).append("=value;");all.append(ticket);}return all.toString();}
 static long measure(boolean newDesign,int tickets,int fields){long start=System.nanoTime();String s=newDesign?redesigned(tickets,fields):original(tickets,fields);blackhole=s.length();return System.nanoTime()-start;}
 public static void main(String[]a){int tickets=2000,fields=100;String old=original(2,3),fresh=redesigned(2,3);if(!old.equals(fresh))throw new AssertionError("Output mismatch");for(int i=0;i<3;i++){measure(false,tickets,fields);measure(true,tickets,fields);}for(int r=1;r<=7;r++)System.out.printf("run=%d original_ms=%.3f redesigned_ms=%.3f%n",r,measure(false,tickets,fields)/1e6,measure(true,tickets,fields)/1e6);}
}