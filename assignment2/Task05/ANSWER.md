# Q5 — Generic Ranking Component (7 marks)

Use a Comparator rather than making Team implement Comparable because different sports, player and college leaderboards may need different ranking policies. The comparator chain sorts points descending, goal difference descending, and team name ascending. comparingInt avoids overflow risks from subtraction-based comparators.

Leaderboard<T> accepts Comparator<? super T>, stores items and returns a sorted copy. The wildcard allows a comparator of T or a supertype. List<? extends T> is useful when consuming values from a list of a subtype.

Completed TODO 1: copy items and sort the copy with order, leaving the original unchanged. Completed TODO 2:
```java
Comparator.comparingInt((Team t) -> t.points).reversed()
 .thenComparing(Comparator.comparingInt((Team t) -> t.goalDiff).reversed())
 .thenComparing(t -> t.name)
```
Expected:
```
[Eagles(9,6), Bisons(9,4), Panthers(9,4), Cobras(6,1)]
```
Bisons ranks above Panthers because points and goal difference tie, then B comes before P alphabetically.

Each key uses a total order; lexicographic composition is transitive and returns zero when all keys tie. A faulty comparator that ignores a tie-breaker will not meet the specified ordering. Comparable defines one natural order and couples it to Team. Comparator is reusable.

One comparator sort costs O(n log n) and O(n) copy space. Three stable sorts (name, goal difference, points) cost roughly three O(n log n) sorts and temporary space; a single comparator chain is simpler.