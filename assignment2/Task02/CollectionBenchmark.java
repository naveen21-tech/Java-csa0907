package Task02;
import java.util.*;
public class CollectionBenchmark{
 static volatile long sink;
 static long time(Runnable r){long t=System.nanoTime();r.run();return System.nanoTime()-t;}
 static void run(int n){List<Integer>a=new ArrayList<>(),l=new LinkedList<>();for(int i=0;i<n;i++){a.add(i);l.add(i);}int reps=Math.max(1000,1000000/n),mid=n/2;
 long ae=time(()->{for(int i=0;i<reps;i++)a.add(-i);});for(int i=0;i<reps;i++)a.remove(a.size()-1);
 long le=time(()->{for(int i=0;i<reps;i++)l.add(-i);});for(int i=0;i<reps;i++)l.remove(l.size()-1);
 long af=time(()->{for(int i=0;i<reps;i++)a.add(0,-i);});for(int i=0;i<reps;i++)a.remove(0);
 long lf=time(()->{for(int i=0;i<reps;i++)l.add(0,-i);});for(int i=0;i<reps;i++)l.remove(0);
 long ag=time(()->{long s=0;for(int i=0;i<reps;i++)s+=a.get(mid);sink=s;});long lg=time(()->{long s=0;for(int i=0;i<reps;i++)s+=l.get(mid);sink=s;});
 long at=time(()->{long s=0;for(int x:a)s+=x;sink=s;}),lt=time(()->{long s=0;for(int x:l)s+=x;sink=s;});
 ArrayDeque<Integer>d=new ArrayDeque<>();for(int i=0;i<n;i++)d.addLast(i);long dq=time(()->{for(int i=0;i<reps;i++){d.addFirst(-i);d.removeLast();}});
 System.out.printf("n=%d reps=%d append A=%d L=%d front A=%d L=%d getMid A=%d L=%d iterate A=%d L=%d dequeEnds=%d ns%n",n,reps,ae,le,af,lf,ag,lg,at,lt,dq);}
 public static void main(String[]x){for(int n:new int[]{1000,10000,100000})run(n);}
}