# A3 — Discover the Data-Access Design Pattern (5 marks)

## A3.1 Repeated pattern
All classes open a connection, create statements, bind values, execute SQL, map ResultSet rows to objects, handle exceptions and close resources. Entity-specific differences are table/column names, SQL, key types and mapping logic.

## A3.2 Generic interface
```java
public interface Dao<T,K> {
  K insert(T entity) throws SQLException;
  Optional<T> findById(K id) throws SQLException;
  List<T> findAll() throws SQLException;
  boolean update(T entity) throws SQLException;
  boolean delete(K id) throws SQLException;
}
```
`StudentDao implements Dao<Student,Integer>` owns student SQL and row mapping. A common helper can manage connection lifecycle. Use PreparedStatement for values; SQL identifiers remain fixed/allow-listed.

## A3.3 Object-table mapping
`Student(id, registerNo, name, email)` maps to `student(student_id PRIMARY KEY, register_no UNIQUE, student_name, email)`. id maps to primary key student_id; remaining fields map to corresponding columns. Retrieve generated key with RETURN_GENERATED_KEYS.

## Alternative: SQL in GUI/service
If SQL is copied into GUI and service classes, a renamed column must be changed in every occurrence. DAO centralizes mapping in one class. Asymptotic DB-operation time and row memory remain the same; DAO adds negligible O(1) dispatch overhead. Its benefit is maintainability, testability and reduced coupling.