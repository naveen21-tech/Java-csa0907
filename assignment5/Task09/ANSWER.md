# B4 — Final Engineering Decision (5 marks)

MigrationTool reads the legacy five-field file, validates field count, required values, route and amount, collects valid rows and records rejected line numbers/reasons in migration-rejections.txt. It calls the transactional batch loader; database failure rolls back and is reported. For very large migration, stream bounded batches rather than retaining all valid rows (current teaching implementation uses O(n) memory).

DatabaseMetaData reports product/version, JDBC driver/version and table names. This helps detect SQL dialect, driver capability and schema differences when moving to another server.

**Production decision:** use a shared relational database, not text files or Java serialization. Twelve campuses, 100× volume and concurrent issuing counters need transactions, uniqueness constraints, indexed searches, isolation, centralized backup and auditability. Files need custom locking/index/recovery; serialization is Java-specific and not a multi-writer database.

**Alternative:** twelve campus-local serialized files avoid network dependency but global search scans all files, O(N) total work; total storage O(N) plus backup copies. Local concurrent counters still need locks or single-writer queues, and global duplicate IDs/consistency are difficult. A shared database with transactions and indexes is preferable. Insert measured benchmark results from your machine before final submission; this solution does not fabricate timings.