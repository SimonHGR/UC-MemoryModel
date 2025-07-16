package example2;

public class Volatility {
  public static long counter = 0;

  public static void main(String[] args) throws Throwable {
    Runnable incrementer = () -> {
      for (int x = 0; x < 1_000_000_000; x++)
        counter++;
    };
    Thread t1 = new Thread(incrementer);
    Thread t2 = new Thread(incrementer);
    Thread t3 = new Thread(incrementer);
    Thread t4 = new Thread(incrementer);
    long start = System.nanoTime();
    t1.start();
    t2.start();
    t3.start();

    // ...

    long time = System.nanoTime() - start;
    System.out.println("counter value is now " + counter);
    System.out.printf("Time elapsed is %f\n", (time / 1_000_000_000.0));
  }
}

//    t1.join();
//    t2.join();
//    t3.join();
