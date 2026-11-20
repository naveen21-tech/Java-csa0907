import java.util.Arrays;
public class ParkingFacility {
 private final String[][] slots;
 public ParkingFacility(int[] capacities){slots=new String[capacities.length][];for(int i=0;i<capacities.length;i++){if(capacities[i]<0)throw new IllegalArgumentException();slots[i]=new String[capacities[i]];}}
 public int[] allocate(String reg){if(reg==null||reg.isBlank())throw new IllegalArgumentException("Invalid registration");for(int f=0;f<slots.length;f++)for(int s=0;s<slots[f].length;s++)if(slots[f][s]==null){slots[f][s]=reg;return new int[]{f,s};}return null;}
 public boolean release(int f,int s){if(f<0||f>=slots.length||s<0||s>=slots[f].length||slots[f][s]==null)return false;slots[f][s]=null;return true;}
 public static void main(String[] a){ParkingFacility p=new ParkingFacility(new int[]{2,1});System.out.println(Arrays.toString(p.allocate("TN01AB1111")));System.out.println(Arrays.toString(p.allocate("TN02CD2222")));System.out.println(p.release(0,0));}
}