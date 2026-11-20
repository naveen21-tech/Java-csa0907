# B2 — Engineer an Adversarial Input (5 marks)

Run only against a local test database. The demo creates its own login_demo table and inserts a fixture row. Crafted ID `nobody' OR '1'='1' -- ` closes the quoted identifier, adds a true OR predicate and comments out the remaining condition. Unsafe concatenation may accept despite wrong password; PreparedStatement treats the entire input as a value and rejects it.

Damaged file includes wrong field count, empty line, non-numeric route and NaN amount. Task06 store skips invalid records and continues. Production should log line number and reason and quarantine rejected records.

Escaping quote characters is database/charset/SQL-mode dependent and easy to get wrong. Regex validation cannot safely cover all valid values and does not separate SQL syntax from data. PreparedStatement provides parameter binding. Validation is O(L) for input length L; binding also processes O(L) data, but structurally prevents the input from becoming SQL syntax. Use semantic validation and password hashing too. Capture actual output locally; never test against a real user database.