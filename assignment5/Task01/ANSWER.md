# A1 — Design a File Format (7 marks)

## A1.1 Record layout
Text UTF-8 CSV field order: passId, registerNo, name, routeNo, boardingStop, validUntil (ISO-8601), amountPaid. Example: `P1001,192371002,Naveen,12,Central Gate,2027-06-30,4500.00`. Apply CSV quoting: quote fields containing comma, quote or newline; double quotes inside quoted fields. A production parser must understand quoted CSV; plain split(",") is insufficient for quoted commas.

Binary DataOutputStream order: writeUTF(passId), writeUTF(registerNo), writeUTF(name), writeInt(routeNo), writeUTF(boardingStop), writeUTF(validUntil), writeDouble(amountPaid). UTF values are length-prefixed modified UTF-8; int is 4 bytes and double is 8 bytes. Add a magic number, format version and record count in a production file.

## A1.2 SIMATS-style pseudocode
```text
ALGORITHM ADD_PASS(file, pass)
INPUT: file and validated pass; OUTPUT: appended record or error
BEGIN
  VALIDATE required fields, routeNo > 0 and amountPaid >= 0
  OPEN file USING BufferedWriter IN APPEND MODE WITH UTF-8
  ENCODE fields using CSV quoting rules
  WRITE encoded record and newline; FLUSH; CLOSE
END

ALGORITHM FIND_PASS(file, targetId)
INPUT: file and ID; OUTPUT: matching pass or NOT_FOUND
BEGIN
  IF file does not exist THEN RETURN FILE_NOT_FOUND
  OPEN file USING BufferedReader WITH UTF-8
  WHILE line is available
    PARSE line as CSV
    IF parse fails OR field count != 7 OR validation fails THEN
      LOG line number and reason; CONTINUE
    ENDIF
    IF passId equals targetId THEN CLOSE; RETURN record
  ENDWHILE
  CLOSE; RETURN NOT_FOUND
END

ALGORITHM LIST_ROUTE(file, targetRoute)
INPUT: file and route; OUTPUT: all valid passes for route
BEGIN
  OPEN file USING BufferedReader WITH UTF-8
  WHILE line is available
    PARSE and validate line
    IF valid AND routeNo = targetRoute THEN DISPLAY record
    ELSE IF invalid THEN LOG damaged line
    ENDIF
  ENDWHILE
  CLOSE
END
```
BufferedReader/BufferedWriter reduce character-level overhead and support text. Prefer Files.newBufferedReader/writer with UTF-8 over platform-default FileReader/FileWriter. FileInputStream/FileOutputStream are for raw byte/binary formats.

## A1.3 Recovery
Detect damaged lines through parse failure, wrong field count, invalid numeric/date values or checksum mismatch. Quarantine with line number and reason; continue reading. For a missing file, distinguish first-time creation from failed lookup. For safe updates, write a complete temporary file, flush and force it to storage, then atomically rename; after a crash the old file remains or the complete new file appears. Backups/checksums improve recovery.

## Alternative: RandomAccessFile
If record index i is known, seek to headerSize + i × recordSize: O(1). Searching by ID still takes O(n) unless an index maps IDs to offsets. Fixed-width fields waste width minus actual encoded length. Delimited scan is O(n) time, O(1) auxiliary memory; fixed record seek is O(1) for known index, but fixed-width padding and index maintenance cost space.