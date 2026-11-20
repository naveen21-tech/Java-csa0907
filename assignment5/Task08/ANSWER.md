# B3 — Statement vs PreparedStatement vs Batch (8 marks)

InsertComparison generates 10, 1,000, 10,000 and 100,000 rows; runs Statement, PreparedStatement and batch; verifies count and reports ResultSetMetaData column names/types. Run only on the dedicated assignment database.

| Rows | Statement | Prepared | Batch |
|---:|---:|---:|---:|
| 10 | measure locally | measure locally | measure locally |
| 1,000 | measure locally | measure locally | measure locally |
| 10,000 | measure locally | measure locally | measure locally |
| 100,000 | measure locally | measure locally | measure locally |

TransactionFailureDemo injects a non-integer route at row six. PassLoader rolls back the transaction; expected remaining TX_ rows = 0. Compare with auto-commit per-row inserts to observe partial commits after mid-way failure.

Statement concatenation has quoting/injection risks. PreparedStatement separates SQL from values. Batch reduces network round trips; use it for nightly 100k imports. A single counter-issued pass should use one PreparedStatement in a short transaction. All approaches do O(n) row work. Batch client memory is O(b) for batch size b; individual execution needs O(1) row buffer. Multi-row INSERT reduces round trips but creates large SQL/parameter lists; chunk it. MySQL LOAD DATA can be faster for bulk import but requires strict validation, file permissions and server configuration. Timings are machine/server dependent and must be filled with actual runs.