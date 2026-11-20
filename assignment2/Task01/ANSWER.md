# Q1 — Building Text Under Load (6 marks)

String is immutable, so repeated concatenation in a loop creates new strings and recopies the accumulated characters. For n similarly sized fields, copied character work is approximately 1+2+...+n = O(n²). StringBuilder appends to a mutable growable buffer and is O(L) in total output characters (amortized). StringBuffer has similar append complexity but synchronizes its methods.

The benchmark deliberately tests 1 ticket × 6 fields, 10,000 × 50 and 1,000 × 100. It warms each implementation and reports seven-run averages using System.nanoTime. Run on an idle machine and retain the raw output.

**Decision:** use a local StringBuilder for each ticket. For a shared multithreaded booking log, prefer a thread-safe logging service/queue or a lock covering the entire record append. StringBuffer synchronizes individual methods, but a sequence of appends is not automatically one atomic record.

**Alternative:** StringJoiner or String.join can join a collection of fields in O(L), with collection allocation. String.format is expressive but generally has more formatting overhead.

**Space/time:** repeated + can create O(n) intermediate String objects and O(nL) cumulative copied characters (O(n²) for equal-sized fields); builder needs O(L) buffer space and O(L) amortized work. Exact allocation counts depend on JVM optimizations.

| Workload | String + | StringBuilder | StringBuffer |
|---|---|---|---|
| 1 × 6 | measure locally | measure locally | measure locally |
| 10,000 × 50 | measure locally | measure locally | measure locally |
| 1,000 × 100 | measure locally | measure locally | measure locally |

Do not fabricate timings; add your actual repeated measurements and screenshot.