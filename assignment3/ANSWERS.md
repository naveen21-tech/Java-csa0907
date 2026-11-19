# Assignment 3 – Written Answers and Analysis

> Based on the supplied Assignment 3 (CO3) questions. Update student-specific details and add screenshots and actual measurements after running the programs on your own machine.

## Q1 – Handling Failure in a Bank ATM Simulation (6 marks)

### Failure classification
| Failure point | Exception / throwable | Classification | Handling |
|---|---|---|---|
| Letters entered where a number is expected | NumberFormatException | Unchecked | Numeric-input catch |
| Negative withdrawal | IllegalArgumentException | Unchecked | Invalid-amount catch |
| Amount exceeds balance | IllegalStateException | Unchecked | Withdrawal-rejected catch |
| Card-reader file missing | IOException | Checked | Device-error catch |
| JVM cannot continue due to resource exhaustion | OutOfMemoryError (example) | Error | Normally not caught by application |

Relevant hierarchy:
```text
Throwable
├── Error
└── Exception
    ├── IOException (checked)
    └── RuntimeException (unchecked)
        ├── IllegalArgumentException
        │   └── NumberFormatException
        └── IllegalStateException
```

Catch blocks should be ordered from specific to general. A superclass catch before a subclass makes the subclass catch unreachable. The finally block executes when control leaves the try/catch normally or exceptionally (except abnormal JVM termination); it is appropriate for cleanup. The sample uses try-with-resources for Scanner closure and finally to report session closure.

### Test plan
| Input / condition | Expected path | finally |
|---|---|---|
| account 1001, amount 500, card-reader.txt exists | successful withdrawal | Yes |
| amount entered as abc | NumberFormatException | Yes |
| amount -10 | IllegalArgumentException | Yes |
| amount 6000 with balance 5000 | IllegalStateException | Yes |
| card-reader.txt absent | IOException | Yes |
| Catch Exception before IOException | Compile-time unreachable catch for IOException | Not applicable |

To capture an uncaught stack trace, temporarily remove the relevant catch and run with an invalid value. A Java stack trace's first line names the exception and message; subsequent `at` lines show the call path, with the top frame being the throw location.

Catching everything with `catch (Exception e)` is acceptable only at a top-level application boundary for logging and graceful termination. It loses type-specific recovery, can hide programming mistakes, and makes user messages less meaningful. Errors should not normally be caught.

### Alternative: validation and error codes
Use `Scanner.hasNextDouble()` before reading, validate sign and balance, and return status constants such as INVALID_INPUT, NEGATIVE_AMOUNT and INSUFFICIENT_FUNDS. This avoids stack-trace construction for expected failures but spreads checks across the code. Both approaches are O(1) time and O(1) auxiliary space for one withdrawal. Exception creation/throwing is more expensive in practice because stack traces capture call frames; returning a code is generally cheaper. Complexity does not change asymptotically for a fixed-depth stack.

## Q2 – User-Defined Exceptions and Propagation (7 marks)

### Exception design
- `ExamException extends Exception`: checked base type for expected business rejection.
- `ExamWindowClosedException`: checked; caller must handle a submission outside the permitted window.
- `StudentNotRegisteredException`: checked; caller must handle failed eligibility.
- `SubmissionStorageException`: checked; storage failure must be handled or declared.

Propagation path: Storage catches IOException and throws SubmissionStorageException with the original IOException as its cause. The service declares ExamException and lets it propagate. The controller/main method catches ExamException and presents a safe message. The cause remains available through `getCause()`.

Expected output:
```text
Saved answer01.pdf
Error: Could not store answer02.pdf
Cause: Disk quota exceeded
```

If the cause is not passed to the constructor, `getCause()` is null. The original low-level reason is lost; the supplied demonstration's direct `getCause().getMessage()` would then throw NullPointerException, so robust code checks for a null cause.

A meaningful SubmissionStorageException separates the storage implementation from the user interface. The UI can display a stable message without exposing disk paths or internal I/O details, while logs retain the original cause.

If custom exceptions extend RuntimeException, method signatures need not declare them and callers need not catch them. This reduces compile-time enforcement and is best for programming/precondition defects, not ordinary business failures. Exception objects require memory for fields, cause, and stack trace; stack-trace capture is proportional to call-stack depth. Chaining retains a reference to the cause and its trace, increasing retained memory.

## Q3 – Resource Safety, Debugging and Robust Code (7 marks)

Resources include the report writer and every store-file reader. Each must be closed even when parsing or writing fails. The manual implementation uses nested finally blocks; the try-with-resources implementation closes resources automatically in reverse declaration order. If both the body and close operation fail, the body exception is primary and close failures are available through `getSuppressed()`.

The classic leak occurs when code opens a reader and an exception happens before reaching a later `close()` statement, with no finally block. The supplied manual example uses finally and therefore closes correctly; it is a safe manual-close variant. Try-with-resources is shorter and less error-prone.

Try-with-resources alone does not make a report transactionally complete: output may be partial after a failure, inputs may change during processing, disk space may run out, or data may be malformed. A robust design writes to a temporary file, validates all inputs, then atomically renames the completed report where supported; it also logs the failing filename.

For f input files and N total input characters/bytes, processing is O(N) time. With sequential processing, only one input reader plus the writer is open at a time: O(1) file handles. A careless design that opens all f readers first can hold O(f) handles. Correct finally and try-with-resources versions both close resources; no-close code can leak up to O(f) handles and eventually hit the OS limit.

## Q4 – Thread Creation and Life Cycle (7 marks)

Extending Thread is direct but uses Java's single class-inheritance slot. Implementing Runnable separates the task from the thread and is generally more flexible; the same Runnable can be submitted to different execution mechanisms.

Life-cycle outline:
```text
NEW --start()--> RUNNABLE <--> RUNNING (scheduled)
                       |          |
                       |          +--sleep()/wait()/I/O--> WAITING or TIMED_WAITING
                       |                                      |
                       +--------------------------------------+
RUNNING --run() completes--> TERMINATED
```
Java's official Thread.State combines ready-to-run and running as RUNNABLE. A call to `run()` is an ordinary method call on the current thread; only `start()` asks the JVM to create/schedule a new thread. `join()` blocks the calling thread until the target terminates, so totals printed after joining all stations are final. `isAlive()` is false after termination.

Output order is nondeterministic because scheduling, CPU load, timing and OS/JVM decisions vary. Priority is only a scheduling hint and does not guarantee order. A daemon thread does not keep the JVM alive; if main and all non-daemon threads finish, remaining daemon work may be stopped. Run the program at least ten times and record observed finish orders locally; do not treat one order as guaranteed.

For n independent scripts and p worker threads, ideal work time is approximately O(n/p) when work is balanced, plus scheduling overhead. Creating four station threads uses O(p) thread objects and stack reservations. A fixed ExecutorService pool reuses p workers and is preferable for large task sets; one thread per script can consume excessive stack and scheduling resources.

## Q5 – Race Conditions in Cinema Seat Booking (6 marks)

The critical section is the combined check `if (!booked[seat])` and write `booked[seat] = true` (including the booking counter update). Without a lock, two threads can both read false before either writes true, then both count a sale. This is a check-then-act race.

A synchronized instance method locks the current SeatBooking object. A synchronized block on the same object can protect only the critical section and allow unrelated work outside the lock. Static synchronization locks the Class object and is needed when shared state is static across all instances. The completed code uses an instance monitor and produces `Seats sold: 10`.

For 20 screens, use one independent lock per screen/seat inventory so unrelated screens do not block each other; synchronize the check-and-book operation for a given screen. For a seat-level high-contention implementation, AtomicBoolean.compareAndSet offers a non-blocking atomic claim. A ReentrantLock offers timed/interruptible locking. ConcurrentHashMap.putIfAbsent works when seat IDs are map keys. All require O(s) state for s seats; each booking is expected O(1), though contention increases waiting/retry time.

Record 10 runs of the unsynchronized version and synchronized version as requested. The race is timing-dependent, so an unsynchronized test may occasionally appear correct; a barrier/latch inserted between check and set can make the interleaving reproducible.

## Q6 – Deadlock and Inter-Thread Communication (7 marks)

Deadlock requires all four Coffman conditions: mutual exclusion, hold-and-wait, no preemption, and circular wait. Two transfers can deadlock when thread A locks wallet A then B while thread B locks B then A. A consistent global lock order (for example, ascending unique account ID) prevents circular wait. The example demonstrates the key idea by accepting the same first/second lock order for all transfers; a production implementation should derive that order from stable account IDs.

The bounded counter has capacity 5. Producer waits while full; consumers wait while empty. The wait predicate must be in a while loop because wakeups may be spurious and another thread may consume the condition before the awakened thread reacquires the monitor. wait() releases the monitor; sleep() does not. Both methods must be called while owning the monitor. notifyAll() wakes all waiters so each can re-check its condition; notify() can wake a thread that cannot proceed while a suitable waiter remains asleep.

Expected result is `Sum of tokens collected: 210 (expected 210)`, since 1 + ... + 20 = 210. Changing while to if is unsafe: after waking, the condition may no longer hold or a spurious wakeup may occur, leading to underflow/overflow or incorrect results.

ArrayBlockingQueue<Integer>(5) encapsulates the bounded-buffer synchronization and is less error-prone. put/take are O(1) per item, with blocking under full/empty conditions; storage is O(5). For wallet transfer, tryLock with timeout can avoid indefinite blocking but requires careful rollback/retry and consistent lock release in finally. A timed lock does not itself guarantee a transfer completes.

## Q7 – Concurrent Fault-Tolerant Result Processor (10 marks)

### Requirements and design
Each score file is an independent task and can be processed in parallel. Shared output consists of success/failure records and aggregate counts; ConcurrentLinkedQueue provides thread-safe result collection. Each Callable returns one Result and Future.get retrieves it. The fixed ExecutorService bounds concurrency. Per-file IOException, malformed integer and out-of-range scores are recorded as failures so one bad file does not stop the batch.

Pseudocode:
```text
create fixed pool(p)
for each score file:
    submit Callable<Result>(read, parse, validate, return success/failure)
for each Future:
    collect returned Result; record unexpected execution failure
count successes and failures; sum valid scores
shutdown pool
```

Class roles: Result is the immutable outcome record; ResultProcessor discovers inputs, submits Callable jobs, collects Futures, aggregates results and reports metrics. The implementation uses ConcurrentLinkedQueue for thread-safe result storage.

The sample program creates two valid files, one corrupt file and one out-of-range file, then measures pool sizes 1, 2, 4 and 8. It prints actual elapsed times from the machine running it. Those timings must be captured by the student; no invented benchmark values are included here. More threads do not always help because disk I/O, storage bandwidth, context switching and scheduling overhead can dominate. For file I/O workloads, start with a small pool and select the size based on repeated measurements on the target machine.

A complete summary is guaranteed by waiting for every Future returned by invokeAll and adding one outcome per task, including unexpected execution failures. One limitation is that the example loads the directory's task list and result objects in memory; for very large input sets, process bounded batches and persist results.

Parallel streams use the common ForkJoinPool and offer concise code but less explicit control over pool sizing and per-task failure reporting. One thread per file creates up to 10,000 threads, with excessive stack reservation and scheduling overhead. A bounded executor keeps active worker threads at p and task/result metadata proportional to the number of files. Parallel-stream failures can surface as terminal-operation exceptions unless caught inside each per-file operation; thread-per-file failures likewise need individual handling.

## Measurement and evidence checklist
- [ ] Run each program and attach terminal screenshots in its Task folder.
- [ ] Run Q4 ten times; record station completion order and any priority/daemon experiment.
- [ ] Run Q5 unsynchronized and synchronized versions ten times; record duplicate count and 10,000-operation timing if measured.
- [ ] Run Q3 with a deliberately malformed input and record which file failed; inspect suppressed exceptions if a close failure is simulated.
- [ ] Run Q7 on the same 10,000-file dataset for pool sizes 1, 2, 4 and 8; record raw times and ensure success + failure equals total files.
- [ ] Fill in the individual contribution table using the actual pair members.
