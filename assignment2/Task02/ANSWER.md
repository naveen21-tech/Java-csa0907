# Q2 — ArrayList Engineering Choice (4 marks)

CollectionBenchmark compares 1,000, 10,000 and 100,000 elements. It measures append-at-end, front insertion/removal, middle indexed access, full iteration and ArrayDeque end operations. Run it repeatedly and record averages; for rigorous JVM benchmarking use JMH.

| Operation | ArrayList | LinkedList | ArrayDeque |
|---|---|---|---|
| add end | amortized O(1) | O(1) | amortized O(1) |
| insert/remove front | O(n) shift | O(1) | O(1) |
| insert middle by index | O(n) | O(n) locate, O(1) link | not indexed |
| get k-th | O(1) | O(n) | no indexed access |
| iterate | O(n) | O(n) | O(n) |

ArrayList uses a contiguous reference array and is cache-friendly. LinkedList uses node objects and previous/next links, with higher memory overhead. ArrayDeque uses a resizable circular array.

ArrayList is suitable when random access and iteration are frequent, with append-at-end and few front/middle edits. Stop recommending it when front edits dominate. The changed Up Next condition (add at both ends, remove from front) favours ArrayDeque.

A plain array has O(1) indexed access and iteration O(n), but front/middle insertion/removal is O(n); append is O(1) while capacity remains and O(n) when resized. Space is O(n). Record your own measured output and screenshot.