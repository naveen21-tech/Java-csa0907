# Q3 — hashCode–equals Contract (6 marks)

**Test 1 prediction:** two Student objects with equal regNo compare equal. With no hashCode override, their identity-based hash values are generally different, so HashSet may place them in different buckets and retain both. Exact size is not deterministic because hash collisions are possible.

**Test 2 prediction:** two different registration numbers are unequal and both should remain; expected size 2.

**Test 3:** mutate a field used in equals/hashCode after insertion. The object can remain in the old bucket, so contains/remove using the new hash may fail. Make identity fields final and immutable.

HashSet uses hashCode to select a bucket and equals to compare candidates within it. The contract requires equal objects to have equal hash codes; unequal objects may share a hash.

The corrected class overrides hashCode with Objects.hash(regNo), consistent with equals. The completed-code expected output is:
```
Unique students: 2
```

Alternative TreeSet with Comparator.comparing(regNo) uses O(log n) add/search and O(n) space; comparator result zero defines uniqueness and should agree with equals. HashMap<String,Student> keyed by regNo has expected O(1) average lookup/insert and O(n) space. HashSet has expected O(1) average add/contains and O(n) space. Record the actual faulty test result; do not assume it must always be 2.