import java.io.*;
import java.nio.file.*;
public final class LineCounter {
  private LineCounter(){}
  public static long unbuffered(Path p)throws IOException {
    long n=0; try(InputStream in=Files.newInputStream(p)){int b;while((b=in.read())!=-1)if(b=='\\n')n++;} return n;
  }
  public static long buffered(Path p)throws IOException {
    long n=0; try(InputStream in=new BufferedInputStream(Files.newInputStream(p),65536)){int b;while((b=in.read())!=-1)if(b=='\\n')n++;} return n;
  }
  public static long arrayBuffered(Path p)throws IOException {
    long n=0; byte[] b=new byte[65536];
    try(InputStream in=Files.newInputStream(p)){int k;while((k=in.read(b))!=-1)for(int i=0;i<k;i++)if(b[i]=='\\n')n++;}
    return n;
  }
}
