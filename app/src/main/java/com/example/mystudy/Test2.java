package com.example.mystudy;

import com.example.mystudy.aop.CheckNet;
import com.example.mystudy.aop.NetType;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test2 {

    int a = 1;
    int b = 2;

    private void change() {
        b = 33333;
        a = b;
    }

    private void print() {
        System.out.println("a = " + a + " b = " + b);
    }

    static class MyRun implements Runnable {
        private boolean flag = false;

        @Override
        public void run() {
            try {
                Thread.sleep(200);
                flag = true;
                System.out.println("flag = " + flag);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public boolean isFlag() {
            return flag;
        }
    }

    public static void main(String[] args) {
        Person person = new Man();
        person.eat();
//        int sum = 100;
//        int count = 15;
//        //根据线程数计算每个线程需要下载多少
//        for (int i = 0; i < count; i++) {
//            int part = sum / count;
//            int start = i * part;
//            int end = start;
//            if (i == count-1) {
//                end = sum;
//            } else {
//                end = start + part;
//            }
//            System.out.println("第"+i+"下载 start->"+start+",end->"+end);
//        }


//        new Thread(new MyRun()).start();

//        String s = new String("我");
//        testString(s);
//        System.out.println(s);
//        int a = 0;
//        testInt(a);
//        System.out.println(a);
//        Person p = new Person();
//        p.name = "xwy";
//        testObj(p);
//        System.out.println(p.name);

//        Method[] methods = Test2.class.getDeclaredMethods();
//        for (Method method : methods) {
//            method.setAccessible(true);
//            Class<?>[] parameterTypes = method.getParameterTypes();
//            System.out.println("方法名:"+method.getName()+"------------");
//            for (Class<?> parameterType : parameterTypes) {
//                String paramName = parameterType.getName();
//                if (parameterType.equals(boolean.class)){
//                    System.out.println(paramName);
//                }
//            }
//        }


//        System.out.println("start");
//        Observable.empty().subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(Object o) throws Exception {
//                System.out.println("accept");
//            }
//        });

//        try {
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder().url("https://www.wanandroid.com/wxarticle/chapters/json").build();
//            Response response = client.newCall(request).execute();
//            System.out.println(response.body().string());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 3, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(3), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

            }
        });
//        for (int i = 0; i < 10; i++) {
//            int index = i;
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.currentThread().setName("thread----" + index);
//                        Thread.sleep(5000);
//                        System.out.println(Thread.currentThread().getName());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            executor.execute(runnable);
//        }


//        for (int i = 5; i < 10; i++) {
//            int index = i;
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.currentThread().setName("thread----" + index);
//                        Thread.sleep(5000);
//                        System.out.println(Thread.currentThread().getName());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            executor.execute(runnable);
//        }
    }


    @CheckNet
    public void testAnnotation(int a, String b, NetType netType) {

    }

    private void testBool(boolean b) {

    }

    private static void testString(String s) {
        s = "真帅";
    }

    private static void testInt(int s) {
        s++;
    }

    private static void testObj(Person p) {
        p.name = "aaa";
    }
}

class Person {
    String name;

    public void eat(){
        System.out.println("Person eat");
    }
}

class Man extends Person{

    @Override
    public void eat() {
        System.out.println("Man eat");
    }
}
