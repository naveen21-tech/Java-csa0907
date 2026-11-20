# B1 — Implement and Instrument the File Store (7 marks)

The completed find() reads with BufferedReader in try-with-resources, checks exactly five fields, validates route and finite non-negative amount, skips damaged records and returns the matching ID. Demo expected output:
```
[P1003, 192311003, Divya, 7, 3800.0]
null
```
P1002 has only two fields; it is skipped and scanning continues, so damage does not abort the search.

| Test | Expected result | What it verifies |
|---|---|---|
| Find P1003 | matching fields | valid row |
| Find P9999 | null | missing ID |
| P1002 damaged row | skipped | wrong field count |
| non-numeric route/amount | skipped | numeric validation |
| empty line | skipped | malformed record |

Run `Benchmark` for 1/10/50 MiB; it prints unbuffered, buffered and byte-array measurements. Record actual machine results; timings depend on JVM, OS, cache and filesystem.

| Size | Unbuffered | Buffered | Array | Lines |
|---:|---:|---:|---:|---:|
| 1 MiB | measure locally | measure locally | measure locally | 1 |
| 10 MiB | measure locally | measure locally | measure locally | 10 |
| 50 MiB | measure locally | measure locally | measure locally | 50 |

For growth test generate 1k, 10k, 50k, 100k records and repeatedly search an ID near EOF. Linear scan is O(n) per lookup; report measured values rather than invented timings.

Alternative HashMap: load n records once, O(n) build time and O(n) memory; q lookups expected O(q), total O(n+q). Repeated scan costs O(qn), O(1) auxiliary memory. Map is useful for many searches when memory is available. This teaching CSV implementation is a simplified comma-separated format and does not support quoted commas; use a compliant CSV parser for production.