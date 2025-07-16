package example3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProdCons {
  public static void main(String[] args) throws Throwable {
    final int DATA_COUNT = 30_000_000;
    BlockingQueue<int[]> queue = new ArrayBlockingQueue<>(10);

    Runnable producer = () -> {
      System.out.println("Producer starting");
      try {
        for (int i = 0; i < DATA_COUNT; i++) {
          int[] data = {-1, i}; // "transactionally unsound", i.e. not the same value yet
          if (i < 500) {
            Thread.sleep(1); // consumer overruns lazy producer draining queue
          }
          data[0] = i; // now we're "transactionally sound"
          if (i == DATA_COUNT / 2) {
            data[0] = -999; // test the test--should see one failure
          }
          queue.put(data);
          data = null; // we don't own it any more, don't touch!!!
        }
      } catch (InterruptedException ie) {
        System.out.println("Strange? Producer shutdown requested");
      }
      System.out.println("Producer completed");
    };
    Runnable consumer = () -> {
      System.out.println("Consumer starting");
      try {
        for (int i = 0; i < DATA_COUNT; i++) {
          int[] data = queue.take();
          if (i > DATA_COUNT - 500) {
            Thread.sleep(1); // producer overruns consumer, filling queue
          }
          if (data[0] != data[1] || data[0] != i) {
            System.out.printf("****Error at index %d, values are %d and %d\n", i, data[0], data[1]);
          }
        }
      } catch (InterruptedException ie) {
        System.out.println("Strange? Consumer shutdown requested");
      }
      System.out.println("Consumer complete");
    };
    Thread p = new Thread(producer);
    Thread c = new Thread(consumer);
    long start = System.nanoTime();
    p.start();
    c.start();
    p.join();
    c.join();
    long time = System.nanoTime() - start;
    System.out.printf("All done, elapsed time for %d items is %f\n", DATA_COUNT, (time / 1_000_000_000.0));
  }
}
