import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ResultProcessor {
    record Result(String file, boolean success, int score, String message) {}
    static Result process(Path file) {
        try {
            String raw = Files.readString(file).trim();
            int score = Integer.parseInt(raw);
            if (score < 0 || score > 100) throw new IllegalArgumentException("Score outside 0..100");
            return new Result(file.getFileName().toString(), true, score, "OK");
        } catch (IOException | IllegalArgumentException e) {
            return new Result(file.getFileName().toString(), false, 0, e.getMessage());
        }
    }
    static void run(Path directory, int poolSize) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        ConcurrentLinkedQueue<Result> results = new ConcurrentLinkedQueue<>();
        long start = System.nanoTime();
        try {
            List<Callable<Result>> jobs = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.txt")) {
                for (Path file : stream) jobs.add(() -> process(file));
            } catch (IOException e) {
                System.out.println("Cannot list input directory: " + e.getMessage());
                return;
            }
            List<Future<Result>> futures = pool.invokeAll(jobs);
            for (Future<Result> future : futures) {
                try { results.add(future.get()); }
                catch (ExecutionException e) { results.add(new Result("unknown", false, 0, e.getCause().toString())); }
            }
            long success = results.stream().filter(Result::success).count();
            long failure = results.size() - success;
            long total = results.stream().filter(Result::success).mapToLong(Result::score).sum();
            System.out.printf("Threads=%d, files=%d, success=%d, failed=%d, totalScore=%d, timeMs=%.3f%n",
                    poolSize, results.size(), success, failure, total, (System.nanoTime()-start)/1_000_000.0);
            results.stream().filter(r -> !r.success()).forEach(r -> System.out.println("FAILED " + r.file() + ": " + r.message()));
        } finally { pool.shutdown(); }
    }
    public static void main(String[] args) throws Exception {
        Path dir = Path.of("scores"); Files.createDirectories(dir);
        Files.writeString(dir.resolve("student1.txt"), "85");
        Files.writeString(dir.resolve("student2.txt"), "92");
        Files.writeString(dir.resolve("student3.txt"), "corrupt");
        Files.writeString(dir.resolve("student4.txt"), "140");
        for (int threads : new int[]{1,2,4,8}) run(dir, threads);
    }
}