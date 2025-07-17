package example3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BadQueue<E> {
  final int BUFFER_SIZE = 10;
  private E[] data = (E[])new Object[BUFFER_SIZE];
  private int count = 0;
//  private Object rendezvous = new Object();
  private ReentrantLock lock = new ReentrantLock();
  private Condition notFull = lock.newCondition();
  private Condition notEmpty = lock.newCondition();

  public void put(E e) throws InterruptedException {
//    synchronized (this.rendezvous) {
    lock.lock();
    try {
      while (count >= BUFFER_SIZE) {
//        this.rendezvous.wait();
        this.notFull.await();
      }
//      this.rendezvous.notify(); // ODD!!! But not wrong :) Not sensible/readable!!!
//      this.rendezvous.notifyAll(); // ODD!!! But not wrong :) Not sensible/readable!!!
      this.notEmpty.signal(); // ODD!!! But not wrong :) Not sensible/readable!!!
      data[count++] = e;
    } finally {
      lock.unlock();
    }
  }

  public E take() throws InterruptedException {
//    synchronized (this.rendezvous) {
    lock.lock();
  try {
      while (count == 0) {
//        this.rendezvous.wait();
        this.notEmpty.await();
      }
      E rv = data[0];
      System.arraycopy(data, 1, data, 0, --count);
//      this.rendezvous.notify();
//      this.rendezvous.notifyAll();
      this.notFull.signal();
      return rv;
    } finally {
      lock.unlock();
    }
  }
}
