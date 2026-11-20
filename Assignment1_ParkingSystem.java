import java.time.*;import java.util.*;
public class ParkingSystem {
 enum UserType{STUDENT,STAFF,VISITOR}
 interface ParkingPass{boolean isValid(LocalDateTime t);boolean givesFreeParking();}
 static class MonthlyPass implements ParkingPass{final LocalDateTime until;MonthlyPass(LocalDateTime u){until=u;}public boolean isValid(LocalDateTime t){return !t.isAfter(until);}public boolean givesFreeParking(){return true;}}
 static class OneDayPass implements ParkingPass{final LocalDate date;OneDayPass(LocalDate d){date=d;}public boolean isValid(LocalDateTime t){return date.equals(t.toLocalDate());}public boolean givesFreeParking(){return false;}}
 interface Chargeable{double chargingFee(int k);}
 interface PricingRule{double fee(Vehicle v,long minutes,LocalDateTime entry);}
 abstract static class Vehicle{final String reg;Vehicle(String r){if(r==null||r.isBlank())throw new IllegalArgumentException();reg=r;}abstract double rate();}
 static class TwoWheeler extends Vehicle{TwoWheeler(String r){super(r);}double rate(){return 10;}}
 static class Car extends Vehicle{Car(String r){super(r);}double rate(){return 30;}}
 static class ElectricCar extends Car implements Chargeable{ElectricCar(String r){super(r);}public double chargingFee(int k){return k*12.0;}}
 static class EBike extends Vehicle implements Chargeable{EBike(String r){super(r);}double rate(){return 8;}public double chargingFee(int k){return 20;}}
 static class DeliveryVan extends Vehicle{DeliveryVan(String r){super(r);}double rate(){return 30;}}
 static class StandardPricing implements PricingRule{public double fee(Vehicle v,long m,LocalDateTime in){double x=((m+59)/60)*v.rate();if(v instanceof DeliveryVan&&!in.toLocalTime().isBefore(LocalTime.of(9,0))&&in.toLocalTime().isBefore(LocalTime.of(17,0)))x*=2;return x;}}
 static class Ticket{final Vehicle v;final int floor,slot;final LocalDateTime entry;final ParkingPass pass;Ticket(Vehicle v,int f,int s,LocalDateTime e,ParkingPass p){this.v=v;floor=f;slot=s;entry=e;pass=p;}}
 static class Facility{final String[][] slots;final Map<String,Ticket> active=new HashMap<>();final PricingRule rule=new StandardPricing();Facility(int[] c){slots=new String[c.length][];for(int i=0;i<c.length;i++)slots[i]=new String[c[i]];}
 Ticket enter(Vehicle v,ParkingPass p,LocalDateTime t){String k=v.reg.toUpperCase();if(active.containsKey(k))throw new IllegalArgumentException("Duplicate vehicle");for(int f=0;f<slots.length;f++)for(int s=0;s<slots[f].length;s++)if(slots[f][s]==null){slots[f][s]=k;Ticket z=new Ticket(v,f,s,t,p);active.put(k,z);return z;}throw new IllegalStateException("Parking full");}
 double exit(String reg,LocalDateTime t,int kWh){String k=reg.toUpperCase();Ticket z=active.get(k);if(z==null||t.isBefore(z.entry))throw new IllegalArgumentException("Invalid exit");long m=Duration.between(z.entry,t).toMinutes();double fee=z.pass!=null&&z.pass.isValid(t)&&z.pass.givesFreeParking()?0:rule.fee(z.v,m,z.entry);if(z.pass instanceof OneDayPass&&z.pass.isValid(t))fee*=.9;if(z.v instanceof Chargeable)fee+=((Chargeable)z.v).chargingFee(kWh);slots[z.floor][z.slot]=null;active.remove(k);return fee;}}
 public static void main(String[] a){Facility f=new Facility(new int[]{2,1,2});LocalDateTime t=LocalDateTime.of(2026,11,20,10,0);f.enter(new TwoWheeler("TN01AA1111"),null,t);f.enter(new ElectricCar("TN02BB2222"),null,t);System.out.println("Bike fee Rs "+f.exit("TN01AA1111",t.plusMinutes(90),0));System.out.println("EV fee Rs "+f.exit("TN02BB2222",t.plusHours(1),2));}
}