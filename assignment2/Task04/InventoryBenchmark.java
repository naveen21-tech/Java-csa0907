package Task04;
import java.util.*;
public class InventoryBenchmark{
 static volatile int sink;
 static long time(Runnable r){long t=System.nanoTime();r.run();return System.nanoTime()-t;}
 static long lookup(Map<String,Integer>m,List<String>keys){return time(()->{int v=0;for(int i=0;i<100000;i++)v+=m.get(keys.get(i%keys.size()));sink=v;});}
 static long iterate(Map<String,Integer>m){return time(()->{int v=0;for(int x:m.values())v+=x;sink=v;});}
 static void run(int n){Map<String,Integer>h=new HashMap<>(),t=new TreeMap<>(),l=new LinkedHashMap<>();for(int i=0;i<n;i++){String k=String.format("MED%07d",i);h.put(k,i);t.put(k,i);l.put(k,i);}List<String>keys=new ArrayList<>(h.keySet());Collections.shuffle(keys,new Random(42));
 long a=lookup(h,keys),b=lookup(t,keys),c=lookup(l,keys),ih=iterate(h),it=iterate(t),il=iterate(l),sort=time(()->{List<String>x=new ArrayList<>(h.keySet());Collections.sort(x);sink=x.size();});
 System.out.printf("n=%d lookup100k H=%d T=%d L=%d iteration H=%d T=%d L=%d sortHash=%d ns%n",n,a,b,c,ih,it,il,sort);
 System.out.println("TreeMap order "+t.keySet().stream().limit(3).toList()+" LinkedHashMap insertion "+l.keySet().stream().limit(3).toList());}
 public static void main(String[]a){for(int n:new int[]{1000,10000,100000})run(n);}
}