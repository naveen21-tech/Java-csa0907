# A4 — What Should Be Serialized? (4 marks)

## A4.1 Persisted fields
Mark the live JDBC Connection transient because it is a process/network resource that cannot be meaningfully restored from a file; reconnect after deserialization. Password should be transient and, preferably, plaintext should never be stored in the object graph; persist a salted password hash in a protected credential store. Names, register numbers, room/resident lists and hostel state are durable data. Serialization traverses reachable object references recursively; every reachable non-transient object must implement Serializable or NotSerializableException occurs. Transient references become null after deserialization.

## A4.2 Version changes
Declare an explicit stable `serialVersionUID`. If phone is added while UID stays compatible, an older stream normally gives phone its default value (null), which application migration can handle. Without explicit UID, Java computes one from class structure; structural changes can cause InvalidClassException. A matching UID does not make every incompatible change safe; custom readObject/readResolve or explicit versioned migration may be required.

## Alternative CSV/JSON
CSV/JSON are language-neutral and inspectable; field names/versioning can support schema evolution. Save/load n residents in O(n) time and O(n) file space. JSON has field-name/syntax overhead; CSV is compact but requires escaping and schema documentation. Java serialization handles object graphs conveniently but is Java-specific and sensitive to class evolution. All formats require validation and secure treatment of secrets.