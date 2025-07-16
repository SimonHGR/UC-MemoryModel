package example1;

public class Visibility {
  private static boolean stop = false;

  public static void main(String[] args) throws Throwable {
    Runnable stopper = () -> {
      System.out.println("Stopper thread started");
      while (!stop)
        ;
      System.out.println("Stopper thread ended");
    };
    new Thread(stopper).start();
    System.out.println("Stopper thread launched");
    Thread.sleep(1_000);
    System.out.println("Setting stop flag");
    stop = true;
    System.out.println("main method has set top to " + stop + " and is exiting");
  }
}
