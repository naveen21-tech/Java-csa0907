package Task05;
import java.util.*;
class Team{
 final String name;final int points,goalDiff;
 Team(String n,int p,int g){name=n;points=p;goalDiff=g;}
 public String toString(){return name+"("+points+","+goalDiff+")";}
}
class Leaderboard<T>{
 private final List<T>items=new ArrayList<>();private final Comparator<? super T>order;
 Leaderboard(Comparator<? super T>o){order=Objects.requireNonNull(o);}
 void add(T x){items.add(x);}
 List<T>ranking(){List<T>copy=new ArrayList<>(items);copy.sort(order);return copy;}
}
public class LeaderboardDemo{
 static final Comparator<Team> byRank=Comparator.comparingInt((Team x)->x.points).reversed()
  .thenComparing(Comparator.comparingInt((Team x)->x.goalDiff).reversed()).thenComparing(x->x.name);
 public static void main(String[]a){Leaderboard<Team>b=new Leaderboard<>(byRank);b.add(new Team("Panthers",9,4));b.add(new Team("Eagles",9,6));b.add(new Team("Cobras",6,1));b.add(new Team("Bisons",9,4));System.out.println(b.ranking());}
}