# A2 — Don’t Read What You Don’t Need (5 marks)

## A2.1 Predict cost
50 MiB = 52,428,800 bytes. Calling FileInputStream.read() once per byte makes 52,428,800 Java-level read calls. This does not necessarily mean one physical disk syscall per byte because native/filesystem layers may buffer, but the repeated method/native boundary overhead is expensive.

## A2.2 Buffered redesign
```java
long lines = 0;
try (BufferedInputStream in = new BufferedInputStream(
        Files.newInputStream(path), 64 * 1024)) {
    int value;
    while ((value = in.read()) != -1) if (value == '\\n') lines++;
}
```
A 64 KiB buffer refills about 50 MiB / 64 KiB = 800 times, rather than 52,428,800 reads at the underlying stream. A byte-array loop with read(byte[]) is also suitable. Both remain O(N) time, with O(B) auxiliary memory for buffer size B.

## A2.3 When buffering does not help
For a tiny in-memory byte array, or when CPU processing dominates and data already arrives in large chunks, another buffer may add copying without useful I/O savings.

## Alternative: Files.readAllLines
Files.readAllLines loads all decoded lines into memory: O(N) time and O(N) memory plus per-String/list overhead; 50 MiB input can require substantially more than 50 MiB heap. Streaming with a fixed buffer is O(N) time and O(B) memory. readAllLines is convenient for small files that fit comfortably in heap and need repeated line access.