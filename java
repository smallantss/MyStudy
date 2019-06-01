一.逻辑运算符
    1.异或 ^
        相同为false，不同为true

    2.位运算符
        &: 3 & 4 = 0011 & 0100 = 0000 = 0 （与）
        |: 3 | 4 = 0011 & 0100 = 0111 = 7 （或）
        ^: 3 | 4 = 0011 & 0100 = 0111 = 7 （异或）
        <<: 3*2 = 3<<1 = 6                 (左移)*2
        >>: 10/2 = 10>>1 = 5               (右移)/2


    3.方法区：
        存放类信息（代码），静态变量，静态方法，字符串常量

    4.栈：
        每调用一次方法，就开辟空间存放方法的栈帧，栈帧里面有方法的局部变量

    5.静态变量和静态方法从属于类，不属于对象

    6.构造方法用于对象的初始化，静态代码块用于类的初始化，静态代码块不能直接访问非静态属性

    7.Java中，方法中所有的参数都是 "值传递" ，即传递的是值的副本，因此复印件不会改变原件，如
      基本数据类型参数的传递

    8.引用类型参数的传值传递的是值的副本，引用类型指的是 "对象的地址"。

    9.Integer自动装箱拆箱机制
      由于Integer会自动缓存[-128,127]之间的对象数组，在此之间取的对象均为缓存中的同一个对象
      因此:
      Integer a = -128;
      Integer b = -128;
      a==b  true  a.equals(b) true
      而1234不在范围内，因此会 new Integer创建新对象:
      Integer a = 1234;
      Integer b = 1234;
      a==b  false  a.equals(b) true


    10.String类优化：
      String a = "hello " + "world";
      String b = "hello world";
      编译器会做优化，因此 a == b true
      而：
      String a = "hello ";
      String b = "world";
      String c = a + b;
      String d = "hello world";
      c == d false ,这种编译器不会优化

    11.双亲委托机制保证了Java核心类库的安全

    12.自定义类加载器
      继承java.lang.ClassLoader
        MyClassLoader(String rootsDir)
      传入 com.xwy.test.user d:/myjava com/xwy/test/User.class
      重写findClass(String name)方法，判断是否已经被加载过
        Class<?> c = findLoadedClass(name);
        if(c!=null){
            //被加载过，直接返回
            return c;
        }else{
            //父类加载
            c = getParent().loadClass(name);
            if(c!=null){
                return c;
            }else{
                //传入类名，返回字节数组
                byte[] classData = getClassData(name);
                if(classData){
                    //throws ClassNotFindException
                }else{
                    c = defineClass(name,classData,0,classData.length)
                }
            }
        }

        private fun getClassData(name:String){
            String path = rootDir+name.replace('.','/')+".class";
            InputStream stream = FileInputStream(path)
            ByteArrayOutputStream baos = ByteArrayOutputStream()
            val temp =0
            byte[] buffer = new byte[1024]
            while((temp=stream.read(buffer))!=-1){
                baos.write(buffer,0,temp)
            }
            return baos.toByteArray();
        }

    13.只有是同一个类加载器加载同一个类，才被认为是相同的类
