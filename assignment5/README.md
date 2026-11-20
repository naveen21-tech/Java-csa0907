# Programming in Java — Assignment 5 (CO5)
**Student:** Ms. Naveen | **Register No.:** 192371002 | **Java:** JDK 17+
**Course:** CSA09 Programming in Java — Data Persistence Engineering (Files and Databases)
**Repository:** https://github.com/naveen21-tech/Java-csa0907/tree/main/assignment5

## Contents
Task01–Task05: Part A design/pseudocode and complexity answers. Task06: B1 text pass store, completed TODO, damaged-line tests and I/O benchmark. Task07: B2 adversarial SQL and file-input tests. Task08: B3 JDBC insert strategy comparison, transaction batch loader and schema. Task09: B4 migration and DatabaseMetaData inspection.

## Compile/run
From repository root:
```bash
javac -d out assignment5/Task06/*.java
java -cp out Task06Demo
```
For database tasks, install MySQL Connector/J and include its jar in the compile and run classpaths. Set environment variables DB_URL (default jdbc:mysql://localhost:3306/campus_passes), DB_USER and DB_PASSWORD. Create the schema:
```bash
mysql -u root -p < assignment5/Task08/schema.sql
```
Example (Linux/macOS): `javac -cp mysql-connector-j.jar -d out assignment5/Task08/*.java assignment5/Task09/*.java`; use `java -cp out:mysql-connector-j.jar ...` to run. On Windows, use semicolon as classpath separator.

## Execution evidence
Run every program on your own computer. Benchmark values depend on JVM, OS, filesystem, cache and MySQL configuration, so this repository deliberately does not invent measured timings. Record actual outputs in each task's evidence table and add genuine screenshots to the task folder. Task08 and Task09 require a configured MySQL instance. The code and design are committed; GUI/database execution on your machine remains to be completed.

## Summary
A1 file formats and recovery; A2 buffered I/O; A3 DAO; A4 serialization/versioning; A5 transactions/injection; B1 file store and measurements; B2 adversarial tests; B3 JDBC strategies/batch atomicity; B4 migration and production decision.