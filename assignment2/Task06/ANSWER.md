# Q6 — Priority, Order and Fairness in a Queue (8 marks)

FIFO Queue cannot choose a more severe later arrival. PriorityQueue compares severity ascending (1 is critical) and arrival counter ascending. The counter makes equal-severity patients stable in arrival order.

Completed-code expected output:
```
Meena(S1) Kumar(S1) Lata(S2) Ravi(S3) John(S3) Asha(S5)
```
Printing PriorityQueue directly does not guarantee treatment order; its iterator exposes heap layout. Copy and sort using the same comparator to display a sorted waiting list.

The six patients manually verify: Meena, Kumar, Lata, Ravi, John, Asha. offer and poll are O(log n), peek O(1), space O(n). QueueBenchmark measures combined add/remove at 1k, 10k and 100k entries, seven-run average.

A severity-5 patient can starve if more severe cases continually arrive. Aging can improve effective priority after a waiting threshold, with bounded promotion and periodic re-queuing. Sorted LinkedList insertion costs O(n) admit and O(1) remove-next. Five FIFO queues (one per severity) offer O(1) admit and O(1) next by checking levels 1–5. Both use O(n) space.

5,000 arrivals/day is a modest data-structure load, but real hospital deployment also requires durable audit, concurrent access, clinical governance, monitoring and validated escalation rules; this educational in-memory example is not a clinical triage product. Record your actual timings and screenshot.