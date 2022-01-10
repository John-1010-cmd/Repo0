## 一 java.lang.Object类

Object类属于java.lang包，此包下的所有类在使用时无需手动导入，系统会在程序编译期间自动导入。Object类是所有类的基类。

```java
/*
 * Copyright (c) 1994, 2012, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package java.lang;

/**
 * Class {@code Object} is the root of the class hierarchy.
 * Every class has {@code Object} as a superclass. All objects,
 * including arrays, implement the methods of this class.
 *
 * @author  unascribed
 * @see     java.lang.Class
 * @since   JDK1.0
 */
public class Object {

    private static native void registerNatives();
    static {
        registerNatives();
    }

    public final native Class<?> getClass();

    public native int hashCode();

    public boolean equals(Object obj) {
        return (this == obj);
    }

    protected native Object clone() throws CloneNotSupportedException;

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    public final native void notify();

    public final native void notifyAll();

    public final native void wait(long timeout) throws InterruptedException;

    public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }
        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                    "nanosecond timeout value out of range");
        }
        if (nanos > 0) {
            timeout++;
        }
        wait(timeout);
    }

    public final void wait() throws InterruptedException {
        wait(0);
    }

    protected void finalize() throws Throwable { }
}
```

**为什么java.lang包下的类不需要手动导入？**

编译器自动导入java.lang包，因为用的多，提前加载，省资源。

Java中两种导包方式

- 单类型导入，例如 import java.util.Date
- 按需类型导入，例如 import java.util.*

按需类型导入是绝对不会降低Java代码执行效率，但会影响到Java代码的编译速度。

**类构造器**

在JDK的Object类源码中是看不到构造器的，系统会自动添加一个无参构造器。

**equals方法**

```java
	public boolean equals(Object obj) {
        return (this == obj);
    }
```

 == 运算符用于比较基本类型的值是否相同，或者比较两个对象的引用是否相等。而equals用于比较两个对象是否相等。在Object类中，== 运算符和equals方法是等价的，都是比较两个对象的引用是否相等。

在Java规范中，对equals方法的使用必须遵循以下几个原则：

1. 自反性：对于任何非空引用值 x，x.equals(x) 都应返回 true。
2. 对称性：对于任何非空引用值 x 和 y，当且仅当 y.equals(x) 返回 true 时，x.equals(y) 才应返回 true。 
3. 传递性：对于任何非空引用值 x、y 和 z，如果 x.equals(y) 返回 true，并且 y.equals(z) 返回 true，那么 x.equals(z) 应返回 true。
4. 一致性：对于任何非空引用值 x 和 y，多次调用 x.equals(y) 始终返回 true 或始终返回 false，前提是对象上 equals 比较中所用的信息没有被修改。
5. 对于任何非空引用值x,x.equals(null)都应返回false。

一个完美的 equals 方法的建议：

1. 显示参数命名为 otherObject，稍后会将它转换成另一个叫做 other 的变量。
2. 判断比较的两个对象引用是否相等，如果引用相等那么表示是同一个对象，那么当然相等。
3. 如果 otherObject 为 null，直接返回false，表示不相等。
4. 比较 this 和 otherObject 是否是同一个类：如果 equals 的语义在每个子类中有所改变，就使用 getClass 检测；如果所有的子类都有统一的定义，那么使用 instanceof 检测。
5. 将 otherObject 转换成对应的类类型变量。
6. 最后对对象的属性进行比较。使用 == 比较基本类型，使用 equals 比较对象。如果都相等则返回true，否则返回false。注意如果是在子类中定义equals，则要包含 super.equals(other)。

```java
@Override
    public boolean equals(Object otherObject) {
        //1、判断比较的两个对象引用是否相等，如果引用相等那么表示是同一个对象，那么当然相等
        if(this == otherObject){
            return true;
        }
        //2、如果 otherObject 为 null，直接返回false，表示不相等
        if(otherObject == null ){//对象为空或者不是Person类的实例
            return false;
        }
        //3、比较 this 和 otherObject 是否是同一个类（注意下面两个只能使用一种）
        //3.1：如果 equals 的语义在每个子类中所有改变，就使用 getClass 检测
        if(this.getClass() != otherObject.getClass()){
            return false;
        }
        //3.2：如果所有的子类都有统一的定义，那么使用 instanceof 检测
        if(!(otherObject instanceof Person)){
            return false;
        }

        //4、将 otherObject 转换成对应的类类型变量
        Person other = (Person) otherObject;

        //5、最后对对象的属性进行比较。使用 == 比较基本类型，使用 equals 比较对象。如果都相等则返回true，否则返回false
        //   使用 Objects 工具类的 equals 方法防止比较的两个对象有一个为 null而报错，因为 null.equals() 是会抛异常的
        return Objects.equals(this.pname,other.pname) && this.page == other.page;

        //6、注意如果是在子类中定义equals，则要包含 super.equals(other)
        //return super.equals(other) && Objects.equals(this.pname,other.pname) && this.page == other.page;

    }
```

无论何时重写equals方法，通常都必须重写hashCode方法，以维护hashCode方法的一般约定，该方法声明相等对象必须具有相同的哈希代码。

**getClass方法**

getClass()在Object类中的作用是，返回对象运行时类。

```java
public final native Class<?> getClass();
```

class是一个类的属性，能获取该类编译时的类对象，而getClass()是一个类的方法，它是获取该类运行时的对象。

```java
public void testClass(){
    Parent p = new Son();
    System.out.println(p.getClass());  //Son
    System.out.println(Parent.class);//Parent
}
```

类型为T的变量getClass方法的返回值类型其实是Class<? extend T>而非getClass方法声明中的Class<？>。

```java
Class <? extends String> c="".getClass();
```

**hashCode方法**

```java
public native int hashCode();
```

这也是一个用native声明的本地方法，作用是返回对象的散列码，是int类型的数值。

hashCode要求：

- 两个对象相等，其hashCode一定相同
- 两个对象不相等，其hashCode有可能相同
- hashCode相同的两个对象，不一定相等
- hashCode不相同的两个对象，一定不相等

hashCode编写指导：

- 不同对象的hash码应该尽量不同，避免hash冲突，也就是算法获得元素要尽量均匀分布。
- hash值是一个int类型，在Java中占用4个字节，也就是2<sup>32</sup>次方，要避免溢出。

**toString方法**

```java
public String toString(){
    return getClass().getName()+"@"+Integer.toHexString(hashCode());//16进制无符号整数型式返回此哈希码的字符串表示形式。
}
```

打印某个对象时，默认是调用toString方法，比如System.out.println(person)等价于System.out.println(person.toString())。

**registerNatives方法**

```java
private static native void registerNatives();
    static {
        registerNatives();
    }
```

在类加载的时候执行静态代码块中的代码，通过该方法来注册本地方法。

## 二 java.lang.Integer类

```java
public final class Integer extends Number implements Comparable<Integer>{}
```

Integer是用final声明的常量类，不能被任何类继承。并且Integer类继承了Number类和实现Comparable接口。Number类是一个抽象类。八种基本数据类型的包装类除了Character和Boolean没有继承该类外，剩下的都继承了Number类，该类的方法用于各种数据类型的转换。Comparable接口就一个compareTo方法，用于元素之间的大小比较。

int类型在Java中占据4个字节，所以其可表示大小的范围是-2<sup>31</sup>——2<sup>31</sup>-1即-2147483648——2147483647。

**构造方法Integer(int)，Integer(String)**

```java
	public Integer(int value) {
        this.value = value;
    }

	public Integer(String s) throws NumberFormatException {
        this.value = parseInt(s, 10);
    }

	public static int parseInt(String s, int radix) throws NumberFormatException{
        if (s == null) {
            throw new NumberFormatException("null");
        }

        if (radix < Character.MIN_RADIX) {
            throw new NumberFormatException("radix " + radix +
                                            " less than Character.MIN_RADIX");
        }

        if (radix > Character.MAX_RADIX) {
            throw new NumberFormatException("radix " + radix +
                                            " greater than Character.MAX_RADIX");
        }

        int result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Integer.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Integer.MIN_VALUE;
                } else if (firstChar != '+')
                    throw NumberFormatException.forInputString(s);

                if (len == 1) // Cannot have lone "+" or "-"
                    throw NumberFormatException.forInputString(s);
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++),radix);
                if (digit < 0) {
                    throw NumberFormatException.forInputString(s);
                }
                if (result < multmin) {
                    throw NumberFormatException.forInputString(s);
                }
                result *= radix;
                if (result < limit + digit) {
                    throw NumberFormatException.forInputString(s);
                }
                result -= digit;
            }
        } else {
            throw NumberFormatException.forInputString(s);
        }
        return negative ? result : -result;
	}
```

**toString()，toString(int i)，toString(int i,int radix)**

```java
public String toString() {
    return toString(value);
}

public static String toString(int i) { //默认十进制
    if (i == Integer.MIN_VALUE)
        return "-2147483648";
    int size = (i < 0) ? stringSize(-i) + 1 : stringSize(i);//包括符号位
    char[] buf = new char[size];
    getChars(i, size, buf);
    return new String(buf, true);
}
```

toString(int) 方法内部调用了 stringSize() 和 getChars() 方法，stringSize() 它是用来计算参数 i 的位数也就是转成字符串之后的字符串的长度，内部结合一个已经初始化好的int类型的数组sizeTable来完成这个计算。getChar方法，第一个if判断，如果i<0,sign记下它的符号“-”，同时将i转成整数。下面所有的操作也就只针对整数了，最后在判断sign如果不等于零将 sign 你的值放在char数组的首位buf [--charPos] = sign;。

**自动拆箱和装箱**

```java
public static Integer valueOf(int i) {
    assert IntegerCache.high >= 127;
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```

自动拆箱和自动装箱是 JDK1.5 以后才有的功能，也就是java当中众多的语法糖之一，它的执行是在编译期，会根据代码的语法，在生成class文件的时候，决定是否进行拆箱和装箱动作。

```java
Integer a = 10;//编译后 Integer a = new Integer(10); -127~128会预先加入常量池
int m = a;     //编译后 int m = a.intValue();
```

简单说，自动装箱就是Integer.valueOf(int i);自动拆箱就是i.intValue();

**equals(Object obj)方法**

```java
public boolean equals(Object obj) {
    if (obj instanceof Integer) {
        return value == ((Integer)obj).intValue();
    }
    return false;
}
```

**hashCode()方法**

```java
public int hashCode(){
    return value;
}
```

**compareTo(Integer anotherInteger)和compare(int,int)**

```java
public int compareTo(Integer anotherInteger){
    return compare(this.value,anotherInteger.value);
}
public static int compare(int x,int y){
    return (x<y)? -1 : ((x == y) ? 0 : 1)
}
```

## 三 java.lang.String类

