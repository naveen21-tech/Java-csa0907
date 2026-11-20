import java.io.*;
import java.nio.file.*;
import java.util.Arrays;

public class PassStore {
    private final Path file;
    PassStore(Path file) { this.file = file; }
    public void add(String passId,String regNo,String name,int route,double amount) throws IOException {
        try(BufferedWriter w=Files.newBufferedWriter(file,StandardOpenOption.CREATE,StandardOpenOption.APPEND)) {
            w.write(String.join(",",passId,regNo,name,String.valueOf(route),String.valueOf(amount)));
            w.newLine();
        }
    }
    /** Returns the five fields, or null if no valid matching record exists. */
    public String[] find(String passId) throws IOException {
        if(!Files.exists(file)) return null;
        try(BufferedReader r=Files.newBufferedReader(file)) {
            String line;
            while((line=r.readLine())!=null) {
                String[] f=line.split(",",-1);
                if(f.length!=5) continue;
                try {
                    Integer.parseInt(f[3].trim());
                    double amount=Double.parseDouble(f[4].trim());
                    if(!Double.isFinite(amount)||amount<0) continue;
                } catch(NumberFormatException ex) { continue; }
                if(f[0].equals(passId)) return f;
            }
        }
        return null;
    }
    public int listByRoute(int route) throws IOException {
        if(!Files.exists(file)) return 0;
        int count=0;
        try(BufferedReader r=Files.newBufferedReader(file)) {
            String line;
            while((line=r.readLine())!=null) {
                String[] f=line.split(",",-1);
                if(f.length!=5) continue;
                try { if(Integer.parseInt(f[3].trim())==route) { System.out.println(Arrays.toString(f)); count++; } }
                catch(NumberFormatException ignored) { }
            }
        }
        return count;
    }
}
