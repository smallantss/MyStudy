package com.example.mystudy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test2 {
    boolean a;

    public static void main(String[] args) throws Exception {
        testSemaphore();
    }

    public static void testSemaphore() {
        Semaphore semaphore = new Semaphore(0);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                print("thread a start release");
                semaphore.release();
                print("thread a end release");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            print("thread b start release");
            semaphore.release();
            print("thread b end release");
        }).start();
        try {
            semaphore.acquire(2);
            print("semaphore all release");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static int x = 0;
    static int y = 0;

    public static void testCyclic2() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
            print("cyclicBarrier finish work");
        });
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                print("thread a finish work 1");
                cyclicBarrier.await();
                Thread.sleep(2000);
                print("thread a finish work 2");
                cyclicBarrier.await();
                print("thread a finish work 3");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                Thread.sleep(100);
                print("thread b finish work 1");
                cyclicBarrier.await();
                Thread.sleep(1000);
                print("thread b finish work 2");
                cyclicBarrier.await();
                print("thread b finish work 3");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    public static void testCyclic() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, new Runnable() {
            @Override
            public void run() {
                print(x + y + "");
            }
        });
        int sum = 0;
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                x = 10;
                print("set x 10");
                cyclicBarrier.await();
                print("go on a");
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                y = 1;
                print("set y 1");
                Thread.sleep(500);
                cyclicBarrier.await();
                print("go on b");
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void testCountDownLaunch() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            print("thread1 do");
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            print("thread2 do");
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            print("thread3 do");
            countDownLatch.countDown();
        }).start();
        try {
            countDownLatch.await();
            print("end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void testIterator() {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        Iterator<Integer> iterator = list.iterator();
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (iterator.hasNext()) {
                print(iterator.next() + "");
            }
        }).start();
        new Thread(() -> {
            list.remove(0);
            list.remove(0);
            list.remove(0);
            print(list.toString());
        }).start();
    }

    public static void testList() {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        new Thread(() -> {
            print(list.get(0) + "");
        }).start();
        new Thread(() -> {
            list.set(0, 10);
        }).start();
    }

    public static void getCount2() {
        AtomicInteger ai = new AtomicInteger(5);
        for (int i = 1; i < 6; i++) {
            new Thread(() -> {
                //线程安全，可以在多线程下使用。读取完减一
                try {
                    int a = new Random().nextInt(10);
                    Thread.sleep((10 - a) * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int andGet = ai.decrementAndGet();
                if (andGet == 0) {
                    System.out.println("抢完了");
                } else {
                    System.out.println("剩余" + andGet);
                }
            }).start();
        }
    }

    public static void getCount() {
        Integer[] arr1 = new Integer[]{0, 1, 2, 3, 0, 1, 0, 2};
        Integer[] arr2 = new Integer[]{1, 1, 1, 3, 0, 1, 0, 0};
        AtomicInteger count = new AtomicInteger();
        Thread a = new Thread(() -> {
            for (Integer integer : arr1) {
                if (integer == 0) {
                    count.incrementAndGet();
                }
            }
        });
        Thread b = new Thread(() -> {
            for (Integer integer : arr2) {
                if (integer == 0) {
                    count.incrementAndGet();
                }
            }
        });
        a.start();
        b.start();
        try {
            a.join();
            b.join();
            print(count + "");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    volatile int count = 0;

    public static void testSafe() {
        AtomicInteger integer = new AtomicInteger();
        Test2 test2 = new Test2();
        for (int i = 0; i < 100000; i++) {
            new Thread(() -> {
//                synchronized (Test2.class){
                test2.count++;
//                }
            }).start();
        }
        try {
            Thread.sleep(1000);
            print(test2.count + "");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void testDaemon() {
        Thread a = new Thread(() -> {
            for (; ; ) {

            }
        });
        a.setDaemon(true);
        a.start();
        ThreadLocal t = new ThreadLocal();
        t.set(1);
    }

    public static void testDeadlock() {
        Object i = new Object();
        Object j = new Object();
        Thread a = new Thread(() -> {
            synchronized (i) {
                print("thread a get i");
                try {
                    Thread.sleep(2000);
                    print("thread a request j");
                    synchronized (j) {
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread b = new Thread(() -> {
            synchronized (j) {
                print("thread b get j");
                try {
                    Thread.sleep(2000);
                    print("thread b request i");
                    synchronized (i) {

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        a.start();
        b.start();
        try {
            a.join();
            b.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void testInterrupt1() {
        Thread a = new Thread(() -> {
            for (; ; ) {
            }
        });
        a.start();
        a.interrupt();
        try {
            print("isInterrupted:" + a.isInterrupted());
            print("interrupted:" + a.interrupted());
            print("interrupted:" + Thread.interrupted());
            print("isInterrupted:" + a.isInterrupted());
            a.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void testYield() {
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                if (i % 5 == 0) {
                    print(Thread.currentThread().getName() + " yield cpu");
                    Thread.yield();
                }
            }
            print(Thread.currentThread().getName() + " is over");
        }, "a").start();
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                if (i % 5 == 0) {
                    print(Thread.currentThread().getName() + " yield cpu");
                    Thread.yield();
                }
            }
            print(Thread.currentThread().getName() + " is over");
        }, "b").start();
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                if (i % 5 == 0) {
                    print(Thread.currentThread().getName() + " yield cpu");
                    Thread.yield();
                }
            }
            print(Thread.currentThread().getName() + " is over");
        }, "c").start();
    }

    private static void testSleep() throws Exception {
        Lock lock = new ReentrantLock();
        Thread a = new Thread(() -> {
            lock.lock();
            try {
                print("a get lock start sleep");
                //就算睡眠2s，仍然占有锁
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                print("a release lock");
                lock.unlock();
            }
        });
        Thread b = new Thread(() -> {
            lock.lock();
            try {
                print("b get lock start sleep");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                print("b release lock");
                lock.unlock();
            }
        });
        a.start();
        Thread.sleep(1000);
        b.start();
    }

    private static void testInterrupt() {
        Thread a = new Thread(() -> {
            for (; ; ) {

            }
        });
        final Thread main = Thread.currentThread();
        Thread b = new Thread(() -> {
            try {
                Thread.sleep(1000);
                main.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        a.start();
        b.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testLock() {
        Object resA = new Object();
        Thread a = new Thread(() -> {
            synchronized (resA) {
                print("进入线程a的synchronized");
                try {
                    Thread.sleep(2000);
                    resA.wait();
                    print("结束线程a的synchronized");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread b = new Thread(() -> {
            print("进入线程b");
            synchronized (resA) {
                print("进入线程b的synchronized");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                a.interrupt();
            }
        });
        try {
            a.start();
            Thread.sleep(1000);
            b.start();
            a.join();
            b.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void print(String msg) {
        System.out.println(msg);
    }

}

