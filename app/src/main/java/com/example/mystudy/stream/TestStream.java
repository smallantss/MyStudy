package com.example.mystudy.stream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.SequenceInputStream;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class TestStream {

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    static AtomicInteger ai = new AtomicInteger(5);

    public static void main(String[] args) {
//        threadLocal.set(100);
//        new MyRun().start();
//        System.out.println(Thread.currentThread().getName()+":"+threadLocal.get());

        for (int i = 0; i < 5; i++) {
            new Thread(new Thread(){
                @Override
                public void run() {
                    int andGet = ai.decrementAndGet();
                    if (andGet==0){
                        System.out.println("抢完了");
                    }else {
                        System.out.println("剩余"+andGet);
                    }
                }
            }).start();
        }


//        writeFile();

//        readFile();

//        readBAOS();

//        writeBAOS();

//        testDecorate();

//        writeDis();

//        randomFile();

//        sequenceStream();

//        consumerList();

//        consumerFlag();




    }


    static class MyRun extends Thread{
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+":"+threadLocal.get());
        }
    }

    private static void consumerFlag() {
        Tv tv = new Tv();
        new Person(tv).start();
        new Actor(tv).start();
    }

    private static void consumerList() {
        Container container = new Container();
        new Producer(container).start();
        new Consumer(container).start();
    }

    private static void sequenceStream() {
        //输出流
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream("RxJava2Activity--r.txt", true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Vector<InputStream> vi = new Vector<>();

        //1.创建源
        File file0 = new File("D:/Projects/MyStudy/RxJava2Activity--0.txt");
        File file1 = new File("D:/Projects/MyStudy/RxJava2Activity--1.txt");
        File file2 = new File("D:/Projects/MyStudy/RxJava2Activity--2.txt");
        //2.选择流
        InputStream is = null;
        //3.操作
        try {
            is = new BufferedInputStream(new FileInputStream(file0));
            vi.add(is);
            is = new BufferedInputStream(new FileInputStream(file1));
            vi.add(is);
            is = new BufferedInputStream(new FileInputStream(file2));
            vi.add(is);
            SequenceInputStream sis = new SequenceInputStream(vi.elements());
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = sis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (is != null) {
                    //4.释放资源
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void randomFile() {
        File src = new File("D:/Projects/MyStudy/app/src/main/java/com/example/mystudy/RxJava2Activity.kt");
        //总长度
        long len = src.length();
        //每一块的长度
        int blockSize = 1024;
        //块数
        int blockNum = (int) Math.ceil(len * 1.0 / blockSize);
        System.out.println(blockNum);
        int beginPos = 0;
        int actualSize = blockSize > len ? (int) len : blockSize;
        for (int i = 0; i < blockNum; i++) {
            if (i == blockNum - 1) {
                //最后一块的长度
                actualSize = (int) len;
            } else {
                actualSize = blockSize;
                len -= actualSize;
            }
            //开始读取的位置
            beginPos = i * blockSize;
            readRandomFile(i, beginPos, actualSize);
        }
    }

    private static void readRandomFile(int i, int beginPos, int actualSize) {
        try {
            RandomAccessFile raf = new RandomAccessFile("D:/Projects/MyStudy/app/src/main/java/com/example/mystudy/RxJava2Activity.kt", "rw");
            //移动到指定位置
            raf.seek(beginPos);
            //分段读取

            //1.创建源
            File file = new File("RxJava2Activity--" + i + ".txt");
            //2.选择流
            OutputStream os = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(file));

                byte[] flush = new byte[1024];
                int len = -1;
                while ((len = raf.read(flush)) != -1) {
                    if (actualSize > len) {
                        //读取所有
                        actualSize -= len;
                        os.write(flush, 0, len);
                    } else {
                        //只读取最后一部分
                        os.write(flush, 0, actualSize);
                        break;
                    }

                }
                os.flush();
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os != null) {
                        //4.释放资源
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void writeDis() {
        //写入
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(baos));
        try {
            dos.writeUTF("辛汪洋");
            dos.writeChar('i');
            dos.writeChar('s');
            dos.writeInt(24);
            dos.writeBoolean(true);
            dos.flush();
            byte[] byteArray = baos.toByteArray();
            //读取
            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            DataInputStream dis = new DataInputStream(new BufferedInputStream(bis));
            System.out.println(dis.readUTF());
            System.out.println(dis.readChar());
            System.out.println(dis.readChar());
            System.out.println(dis.readInt());
            System.out.println(dis.readBoolean());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testDecorate() {
        Drink coffee = new Coffee(5);
        Decorate sugar = new Sugar(coffee);
        System.out.println(sugar.info() + "---" + sugar.cost());
        Decorate milk = new Milk(sugar);
        System.out.println(milk.info() + "---" + milk.cost());
    }


    private static void writeBAOS() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String msg = "我同意你的看法";
        try {
            baos.write(msg.getBytes());
            baos.flush();
            byte[] bytes = baos.toByteArray();
            System.out.println(new String(bytes, 0, bytes.length));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readBAOS() {
        ByteArrayInputStream bais = null;

        String msg = "辛汪洋确实很帅";
        bais = new ByteArrayInputStream(msg.getBytes());
        byte[] flush = new byte[1024];
        int len = -1;
        try {
            while ((len = bais.read(flush)) != -1) {
                System.out.println(new String(flush, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFile() {
        File file = new File("xwy.txt");
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            byte[] flush = new byte[1024];
            int len = -1;
            while ((len = is.read(flush)) != -1) {
                String s = new String(flush, 0, len);
                System.out.println(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeFile() {
        //1.创建源
        File file = new File("xwy.txt");
        //2.选择流
        OutputStream os = null;
        String msg = "世界那么大，为何帅的偏偏是我？";
        //3.操作
        byte[] msgBytes = msg.getBytes();
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            os.write(msgBytes, 0, msgBytes.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    //4.释放资源
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

//抽象组件 如：可以喝的东西
interface Drink {
    //花费
    double cost();

    //信息
    String info();
}

//具体组件 如：咖啡是喝的
class Coffee implements Drink {
    private double money;

    public Coffee(double money) {
        this.money = money;
    }

    @Override
    public double cost() {
        return money;
    }

    @Override
    public String info() {
        return "咖啡的价格是" + money;
    }
}

//抽象装饰类 包含对抽象组件的引用，装饰共有的方法 如：可以给咖啡加东西
abstract class Decorate implements Drink {

    private Drink drink;

    public Decorate(Drink drink) {
        this.drink = drink;
    }

    @Override
    public double cost() {
        return drink.cost();
    }

    @Override
    public String info() {
        return drink.info();
    }
}

//具体装饰类  装饰具体组件 如：咖啡可以加糖，牛奶
class Sugar extends Decorate {

    public Sugar(Drink drink) {
        super(drink);
    }

    @Override
    public double cost() {
        return super.cost() + 2;
    }

    @Override
    public String info() {
        return super.info() + "加入了Sugar";
    }
}

class Milk extends Decorate {

    public Milk(Drink drink) {
        super(drink);
    }

    @Override
    public double cost() {
        return super.cost() + 3;
    }

    @Override
    public String info() {
        return super.info() + "加入了Milk";
    }
}


//管程法
//生产者
class Producer extends Thread {
    private Container container;

    public Producer(Container container) {
        this.container = container;
    }

    @Override
    public void run() {
        //开始生产100个
        for (int i = 0; i < 100; i++) {
            container.push(new Bread(i));
            System.out.println("生产第" + i + "个馒头");
        }
    }
}

//消费者
class Consumer extends Thread {
    private Container container;

    public Consumer(Container container) {
        this.container = container;
    }

    @Override
    public void run() {
        //消费200个
        for (int i = 0; i < 200; i++) {
            Bread bread = container.pop();
            System.out.println("消费第" + bread.id + "个馒头");
        }
    }
}

//缓冲区
class Container {
    //最大生产数据数量
    private Bread[] breads = new Bread[10];
    //计数器
    int count = 0;

    //生产
    public synchronized void push(Bread bread) {
        //不能生产，阻塞，消费者通知生产，阻塞解除
        if (count == breads.length) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        breads[count] = bread;
        count++;
        //唤醒消费
        notifyAll();
    }

    //消费
    public synchronized Bread pop() {
        //存在数据才可以消费
        if (count == 0) {
            try {
                //线程阻塞，生产者通知消费解除阻塞
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        count--;
        //唤醒生产
        notifyAll();
        return breads[count];
    }
}

//数据 面包
class Bread {
    int id;

    public Bread(int id) {
        this.id = id;
    }
}

//信号灯法
class Tv {

    String voice;
    //是否表演过
    boolean flag = false;

    public synchronized void act(String voice) {
        if (flag){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("先表演了：" + voice);
        this.voice = voice;
        flag = !flag;
        notifyAll();
    }

    public synchronized void play() {
        //没有表演过，先等待表演
        if(!flag){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("后播放了：" + voice);
        flag = !flag;
        notifyAll();
    }
}

class Person extends Thread {
    Tv tv;

    public Person(Tv tv) {
        this.tv = tv;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                tv.act("铁人" + i);
            } else {
                tv.act("罗密欧" + i);
            }
        }
    }
}

class Actor extends Thread {
    Tv tv;

    public Actor(Tv tv) {
        this.tv = tv;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            tv.play();
        }
    }
}









