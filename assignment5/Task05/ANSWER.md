# A5 — Design for Correctness and Safety (4 marks)

## A5.1 Transfer transaction pseudocode
```text
ALGORITHM TRANSFER(sourceId, destinationId, amount)
INPUT: wallet IDs and positive amount
OUTPUT: committed transfer or reported failure
BEGIN
  IF amount <= 0 THEN RETURN INVALID_AMOUNT
  OPEN JDBC CONNECTION; SET autoCommit = FALSE
  TRY
    UPDATE wallet SET balance=balance-amount
      WHERE wallet_id=sourceId AND balance>=amount
    IF affected rows != 1 THEN RAISE INSUFFICIENT_FUNDS
    UPDATE wallet SET balance=balance+amount WHERE wallet_id=destinationId
    IF affected rows != 1 THEN RAISE INVALID_DESTINATION
    COMMIT
  CATCH ERROR
    ROLLBACK; REPORT failure
  FINALLY CLOSE connection
  ENDTRY
END
```
Use row locks/constraints to prevent concurrent overdrafts. Both updates commit or neither does.

## A5.2 SQL injection
Concatenated SQL can be bypassed with input such as `' OR '1'='1' -- ` because it changes the predicate and comments out the password clause (exact comment syntax is DB-specific). Use `SELECT user_id FROM users WHERE id=? AND pwd_hash=?`, bind parameters using PreparedStatement and verify a salted password hash with a password-hashing algorithm. Never store plaintext passwords.

## Alternative: separate auto-commit updates
If debit commits then failure occurs before credit, money disappears; if credit commits first and debit fails, money is created. Transaction adds constant coordination/locking/logging overhead but preserves correctness. Both perform two indexed updates, approximately O(1) statement count relative to table size; integrity risk makes independent commits unacceptable.