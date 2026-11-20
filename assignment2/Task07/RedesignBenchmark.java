package Task07;
public class RedesignBenchmark{
 static volatile long blackhole;
 static long original(int tickets,int fields){long chars=0;for(int t=0;t<tickets;t++){String ticket="";for(int f=0;f<fields;f++)ticket+="FIELD"+f+"=value;";chars+=ticket.length();}return chars;}
 static long redesigned(int tickets,int fields){long chars=0;for(int t=0;t<tickets;t++){StringBuilder ticket=new StringBuilder();for(int f=0;f<fields;f++)ticket.append("FIELD").append(f).append("=value;");chars+=ticket.length();}return chars;}
 static long measure(boolean newDesign,int tickets,int fields){long start=System.nanoTime();long n=newDesign?redesigned(tickets,fields):original(tickets,fields);blackhole=n;return System.nanoTime()-start;}
 public static void main(String[]a){int tickets=2000,fields=100;if(original(2,3)!=redesigned(2,3))throw new AssertionError("Output length mismatch");for(int i=0;i<3;i++){measure(false,tickets,fields);measure(true,tickets,fields);}for(int r=1;r<=7;r++)System.out.printf("run=%d original_ms=%.3f redesigned_ms=%.3f%n",r,measure(false,tickets,fields)/1e6,measure(true,tickets,fields)/1e6);}
}