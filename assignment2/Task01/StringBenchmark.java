package Task01;
public class StringBenchmark {
 static final int RUNS=7;
 static long plus(int tickets,int fields){long chars=0;for(int t=0;t<tickets;t++){String ticket="";for(int f=0;f<fields;f++)ticket+="F"+f+"=value;";chars+=ticket.length();}return chars;}
 static long builder(int tickets,int fields){long chars=0;for(int t=0;t<tickets;t++){StringBuilder ticket=new StringBuilder();for(int f=0;f<fields;f++)ticket.append("F").append(f).append("=value;");chars+=ticket.length();}return chars;}
 static long buffer(int tickets,int fields){long chars=0;for(int t=0;t<tickets;t++){StringBuffer ticket=new StringBuffer();for(int f=0;f<fields;f++)ticket.append("F").append(f).append("=value;");chars+=ticket.length();}return chars;}
 interface Work{long run();}
 static void test(String label,int tickets,int fields){Work[] w={()->plus(tickets,fields),()->builder(tickets,fields),()->buffer(tickets,fields)};String[] names={"String +","StringBuilder","StringBuffer"};for(int k=0;k<3;k++)for(Work x:w)x.run();System.out.println(label+" tickets="+tickets+" fields="+fields);for(int i=0;i<3;i++){long sum=0;for(int r=0;r<RUNS;r++){long t=System.nanoTime();long n=w[i].run();sum+=System.nanoTime()-t;if(n<=0)throw new AssertionError();}System.out.printf("%s average_ms=%.3f%n",names[i],sum/RUNS/1e6);}}
 public static void main(String[]a){test("short",1,6);test("large",10000,50);test("custom",1000,100);}
}