# Q4 — Pharmacy Inventory Map Choice (6 marks)

The benchmark constructs each map with 1,000, 10,000 and 100,000 medicine codes, performs 100,000 lookups, times iteration and measures sorting HashMap keys for a code-order report.

| Map | put/get | iteration | ordering |
|---|---|---|---|
| HashMap | expected O(1) | O(n), capacity effects | none |
| TreeMap | O(log n) | O(n) | sorted by key |
| LinkedHashMap | expected O(1) | O(n) | insertion order |

TreeMap naturally prints medicines in code order; LinkedHashMap naturally prints order-added. HashMap needs a copied key list sorted in O(n log n) for the code report. Hashtable is legacy synchronized API; prefer HashMap with suitable concurrency control or ConcurrentHashMap for concurrent lookup.

Define acceptable lookup latency before deployment, for example under 5 ms on target hardware. Compare your measured values against the threshold; remaining costs include hashing/comparison, memory/cache misses, JVM and GC. A high-frequency lookup path can use HashMap; maintain ordered views only if report frequency justifies their additional O(n) storage.

**Sorted array alternative:** binarySearch lookup O(log n), insertion O(n) shifting, code-order report O(n), space O(n). HashMap lookup expected O(1); TreeMap lookup/insert O(log n) with sorted traversal.

Add your own raw timings and screenshots after local execution. Values vary by hardware/JVM and are not prefilled.