package org.pp.socket.netty.api;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

/**
 * ByteBuf缓冲区 Netty中传输数据的载体
 * 数据结构  字节数组 包含两个指针 readerIndex writerIndex在AbstractByteBuf抽象基类中定义
 * 0 <= readerIndex <= writerIndex <= capacity
 * <p>
 * 使用ByteBuf，api简单易操作 read*(...) write*(...)
 * 支持对象池管理：维护Recycler回调引用
 * 引用计数自动管理内存：实现ReferenceCounted接口
 * 自动扩容的（AbstractByteBufAllocator中指定最大默认容量值）
 * 参考:
 * ByteBufAllocator.DEFAULT.buffer();
 * Unpooled.buffer();
 * <p>
 * UML 参考{@link ./AbstractByteBuf.png}
 */
public class ByteBufTest {
    public static void main(String[] args) {
        // UnpooledHeapByteBuf底层就是字节数组，构造方法会调用：
        //        protected byte[] allocateArray(int initialCapacity) {
        //            return new byte[initialCapacity];
        //        }

        // PooledByteBuf池化的会维护缓存，例如PooledHeapByteBuf的构造
        /*private static final Recycler<PooledHeapByteBuf> RECYCLER = new Recycler<PooledHeapByteBuf>() {
            @Override
            protected PooledHeapByteBuf newObject(Handle<PooledHeapByteBuf> handle) {
                return new PooledHeapByteBuf(handle, 0);
            }
        };*/
        /* static PooledHeapByteBuf newInstance(int maxCapacity) {
                PooledHeapByteBuf buf = RECYCLER.get();
                buf.reuse(maxCapacity);
                return buf;
            }*/
        // 其中 RECYCLER是一个线程私有堆栈的轻量级对象池管理Buffer，pop操作，获取不到buffer时就会newObject生成一个Buffer
        /*
        RECYCLER::get
        public final T get() {
            if (maxCapacityPerThread == 0) {
                return newObject((Recycler.Handle<T>) NOOP_HANDLE);
            }
            Recycler.Stack<T> stack = threadLocal.get();
            Recycler.DefaultHandle<T> handle = stack.pop();
            if (handle == null) {
                handle = stack.newHandle();
                handle.value = newObject(handle);
            }
            return (T) handle.value;
        }*/
        // reuse方法会重置引用计数器
        // final void reuse(int maxCapacity) {
        //        maxCapacity(maxCapacity);
        //        resetRefCnt();
        //        setIndex0(0, 0);
        //        discardMarks();
        //    }

        // ByteBuf分配分为：池化、非池化、堆内、直接内存缓冲
        // 通过ByteBufUtil、ByteBufAllocator、Unpooled分配
        // 1、ByteBufAllocator DEFAULT = ByteBufUtil.DEFAULT_ALLOCATOR;
        // 2、String allocType = SystemPropertyUtil.get(
        //                "io.netty.allocator.type", PlatformDependent.isAndroid() ? "unpooled" : "pooled");

        ByteBuf buffer = allocBuf();
        int capacity = buffer.capacity();
        System.out.println("buffer capacity = " + capacity);
        for (int i = 0; i < capacity; i++) {
            byte b = buffer.getByte(i);
            System.out.print(b + "-");
        }
        System.out.println("\nreaderIndex=" + buffer.readerIndex());
        System.out.println("writerIndex=" + buffer.writerIndex());
        System.out.println("readableBytes=" + buffer.readableBytes());
        byte[] bytes = new byte[/*capacity*/buffer.readableBytes()];
        while (buffer.isReadable()) {
//            System.out.print(buffer.readByte() + "-");
            buffer.readBytes(bytes);
        }
        System.out.println("\nreaderIndex=" + buffer.readerIndex());
        System.out.println("writerIndex=" + buffer.writerIndex());

        System.out.println("buffer.isWritable() = " + buffer.isWritable());  // false
        while (buffer.isWritable()) {
            buffer.writeBytes(bytes);
        }
        bytes = new byte[capacity];
        buffer.readBytes(bytes);
        System.out.println(Arrays.toString(bytes));
    }


    private static ByteBuf allocBuf() {
        byte[] bytes = "你hao".getBytes();
        System.out.println("bytes.length=" + bytes.length); // 6 你 UTF-8占了3个字节
        // Unpooled.copiedBuffer(bytes); Unpooled.wrappedBuffer(bytes); 容量固定不可变
        // static final int DEFAULT_INITIAL_CAPACITY = 256;
        //static final int DEFAULT_MAX_CAPACITY = Integer.MAX_VALUE;
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(bytes);
        return buf;
//        return ByteBufAllocator.DEFAULT.buffer(); // 默认缓冲区
    }
}