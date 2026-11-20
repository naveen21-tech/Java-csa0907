import java.nio.file.*;
import java.util.Arrays;
public class Benchmark {
 public static void main(String[] args)throws Exception{
  for(int mb:new int[]{1,10,50}){
   Path p=Files.createTempFile("attendance",".log");
   byte[] block=new byte[1024*1024]; Arrays.fill(block,(byte)'x'); block[block.length-1]=(byte)'\\n';
   for(int i=0;i<mb;i++)Files.write(p,block,StandardOpenOption.APPEND);
   long t=System.nanoTime(),a=LineCounter.unbuffered(p),u=System.nanoTime();
   long b=LineCounter.buffered(p),v=System.nanoTime();
   long c=LineCounter.arrayBuffered(p),w=System.nanoTime();
   if(a!=b||b!=c)throw new AssertionError("Counts differ");
   System.out.printf("%d MiB lines=%d unbuffered=%.3fs buffered=%.3fs array=%.3fs%n",mb,a,(u-t)/1e9,(v-u)/1e9,(w-v)/1e9);
   Files.delete(p);
  }
 }
}
