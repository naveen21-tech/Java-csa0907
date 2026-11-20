# Q7 — Diagnose, Redesign and Validate (13 marks)

## A. Diagnose the bottleneck (2)
Selected limitation: Q1 railway e-ticket generation. Repeated String concatenation inside nested loops repeatedly copies accumulated text and creates short-lived immutable objects. Work grows superlinearly with the number of fields. Task01 is the original baseline.

## B. Design objective (2)
For identical ticket data, preserve byte-for-byte output while reducing character copying from quadratic-in-field-count to linear in output length. Measure elapsed time over seven runs after warm-up and verify the outputs are equal.

## C. Revised solution (4)
Use local StringBuilder objects for each ticket and the overall output. The builder's mutable backing buffer allows appends without rebuilding the entire immutable string each time.

```text
FOR each ticket
  ticketBuilder = new StringBuilder
  FOR each field
    ticketBuilder.append(field)
  END FOR
  outputBuilder.append(ticketBuilder)
END FOR
RETURN outputBuilder.toString()
```

The builder uses O(L) auxiliary capacity for output length L. It avoids repeated intermediate String copies. Keep the builder local to a request; do not share mutable builders between threads.

## D. Prediction (1)
For F similarly sized fields, repeated concatenation copies O(F²) characters in the simplified model; StringBuilder is O(L) amortized in total characters. Large tickets should improve substantially; tiny tickets may show negligible difference.

## E. Implement and validate (2)
Run `java -cp out Task07.RedesignBenchmark`. It checks equality on a small test case, warms both implementations three times, then prints seven paired timings for 2,000 tickets × 100 fields. Save raw output and a genuine terminal screenshot. Repeat on an idle machine.

## F. Defend the design (2)
The redesign should reduce time and temporary allocation for large workloads, at the cost of a mutable growable buffer and occasional capacity expansion. Plain concatenation remains readable for a tiny fixed expression. StringBuilder is the defensible high-throughput choice. Do not invent a speed-up percentage; calculate it from your own runs.

| Measure | Repeated + | StringBuilder |
|---|---|---|
| Time for F similar fields | O(F²) character copying | O(L) amortized |
| Working memory | many intermediate strings; cumulative transient allocation | O(L) builder capacity |
| Output | concatenated fields | same, verified by equality check |

The measured magnitude depends on JDK, CPU, heap and garbage collector. A simple nanoTime harness is educational evidence; JMH is preferred for rigorous JVM benchmarking.