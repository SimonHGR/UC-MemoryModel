package example2;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;

public class Volatility {
//  public static volatile long counter = 0;
//  public static AtomicLong counter = new AtomicLong(0);
  public static LongAccumulator counter = new LongAccumulator(
      (left, right) -> left + right, 0L);

  public static void main(String[] args) throws Throwable {
    Runnable incrementer = () -> {
      for (int x = 0; x < 1_000_000_000; x++)
//        counter++;
//        counter.incrementAndGet();
        counter.accumulate(1);
    };
    Thread t1 = new Thread(incrementer);
    Thread t2 = new Thread(incrementer);
    Thread t3 = new Thread(incrementer);
    long start = System.nanoTime();
    t1.start();
    t2.start();
    t3.start();

    // ...

    t1.join();
    t2.join();
    t3.join();
    long time = System.nanoTime() - start;
//    System.out.println("counter value is now " + counter);
    System.out.println("counter value is now " + counter.get());
    System.out.printf("Time elapsed is %f\n", (time / 1_000_000_000.0));
  }
}

