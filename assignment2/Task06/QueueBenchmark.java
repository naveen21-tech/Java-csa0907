package Task06;
import java.util.*;
public class QueueBenchmark{
 static long run(int n){PriorityQueue<Integer>q=new PriorityQueue<>();long t=System.nanoTime();for(int i=0;i<n;i++)q.offer(n-i);while(!q.isEmpty())q.poll();return System.nanoTime()-t;}
 public static void main(String[]a){for(int n:new int[]{1000,10000,100000}){long sum=0;for(int r=0;r<7;r++)sum+=run(n);System.out.printf("n=%d average add+remove ms=%.3f%n",n,sum/7/1e6);}}
}