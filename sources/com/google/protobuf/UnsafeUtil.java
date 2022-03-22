package com.google.protobuf;

import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import java.util.Objects;
import java.util.logging.Logger;
import sun.misc.Unsafe;
/* loaded from: classes.dex */
public final class UnsafeUtil {
    public static final long BYTE_ARRAY_BASE_OFFSET;
    public static final boolean HAS_UNSAFE_ARRAY_OPERATIONS;
    public static final boolean HAS_UNSAFE_BYTEBUFFER_OPERATIONS;
    public static final JvmMemoryAccessor MEMORY_ACCESSOR;
    public static final Unsafe UNSAFE;
    public static final Logger logger = Logger.getLogger(UnsafeUtil.class.getName());

    /* renamed from: com.google.protobuf.UnsafeUtil$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 implements PrivilegedExceptionAction<Unsafe> {
        @Override // java.security.PrivilegedExceptionAction
        public final Unsafe run() throws Exception {
            Field[] declaredFields;
            for (Field field : Unsafe.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object obj = field.get(null);
                if (Unsafe.class.isInstance(obj)) {
                    return (Unsafe) Unsafe.class.cast(obj);
                }
            }
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class MemoryAccessor {
        public Unsafe unsafe;

        public MemoryAccessor(Unsafe unsafe) {
            this.unsafe = unsafe;
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(20:2|61|3|5|(1:7)(1:8)|9|(1:11)(10:59|12|13|73|14|71|15|(4:18|69|19|(2:21|(14:27|28|63|29|30|39|(3:67|41|42)|45|65|46|(1:52)(1:51)|(1:56)|57|58)))(1:23)|24|(0))|26|38|39|(0)|45|65|46|(1:49)|52|(2:54|56)|57|58|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0259, code lost:
        r0 = null;
     */
    /* JADX WARN: Removed duplicated region for block: B:27:0x009b A[Catch: all -> 0x00fa, TRY_LEAVE, TryCatch #5 {all -> 0x00fa, blocks: (B:19:0x0081, B:27:0x009b), top: B:69:0x0081 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x025c  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0126 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    static {
        /*
            Method dump skipped, instructions count: 633
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.UnsafeUtil.<clinit>():void");
    }

    public static <T> T allocateInstance(Class<T> cls) {
        try {
            return (T) UNSAFE.allocateInstance(cls);
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static int arrayBaseOffset(Class<?> cls) {
        if (!HAS_UNSAFE_ARRAY_OPERATIONS) {
            return -1;
        }
        JvmMemoryAccessor jvmMemoryAccessor = MEMORY_ACCESSOR;
        Objects.requireNonNull(jvmMemoryAccessor);
        return jvmMemoryAccessor.unsafe.arrayBaseOffset(cls);
    }

    public static int arrayIndexScale(Class<?> cls) {
        if (!HAS_UNSAFE_ARRAY_OPERATIONS) {
            return -1;
        }
        JvmMemoryAccessor jvmMemoryAccessor = MEMORY_ACCESSOR;
        Objects.requireNonNull(jvmMemoryAccessor);
        return jvmMemoryAccessor.unsafe.arrayIndexScale(cls);
    }

    public static boolean getBoolean(Object obj, long j) {
        JvmMemoryAccessor jvmMemoryAccessor = MEMORY_ACCESSOR;
        Objects.requireNonNull(jvmMemoryAccessor);
        return jvmMemoryAccessor.unsafe.getBoolean(obj, j);
    }

    public static byte getByte(byte[] bArr, long j) {
        JvmMemoryAccessor jvmMemoryAccessor = MEMORY_ACCESSOR;
        long j2 = BYTE_ARRAY_BASE_OFFSET + j;
        Objects.requireNonNull(jvmMemoryAccessor);
        return jvmMemoryAccessor.unsafe.getByte(bArr, j2);
    }

    public static double getDouble(Object obj, long j) {
        JvmMemoryAccessor jvmMemoryAccessor = MEMORY_ACCESSOR;
        Objects.requireNonNull(jvmMemoryAccessor);
        return jvmMemoryAccessor.unsafe.getDouble(obj, j);
    }

    public static float getFloat(Object obj, long j) {
        JvmMemoryAccessor jvmMemoryAccessor = MEMORY_ACCESSOR;
        Objects.requireNonNull(jvmMemoryAccessor);
        return jvmMemoryAccessor.unsafe.getFloat(obj, j);
    }

    public static int getInt(Object obj, long j) {
        JvmMemoryAccessor jvmMemoryAccessor = MEMORY_ACCESSOR;
        Objects.requireNonNull(jvmMemoryAccessor);
        return jvmMemoryAccessor.unsafe.getInt(obj, j);
    }

    public static long getLong(Object obj, long j) {
        JvmMemoryAccessor jvmMemoryAccessor = MEMORY_ACCESSOR;
        Objects.requireNonNull(jvmMemoryAccessor);
        return jvmMemoryAccessor.unsafe.getLong(obj, j);
    }

    public static Object getObject(Object obj, long j) {
        JvmMemoryAccessor jvmMemoryAccessor = MEMORY_ACCESSOR;
        Objects.requireNonNull(jvmMemoryAccessor);
        return jvmMemoryAccessor.unsafe.getObject(obj, j);
    }

    public static void putInt(Object obj, long j, int i) {
        JvmMemoryAccessor jvmMemoryAccessor = MEMORY_ACCESSOR;
        Objects.requireNonNull(jvmMemoryAccessor);
        jvmMemoryAccessor.unsafe.putInt(obj, j, i);
    }

    public static void putLong(Object obj, long j, long j2) {
        JvmMemoryAccessor jvmMemoryAccessor = MEMORY_ACCESSOR;
        Objects.requireNonNull(jvmMemoryAccessor);
        jvmMemoryAccessor.unsafe.putLong(obj, j, j2);
    }

    public static void putObject(Object obj, long j, Object obj2) {
        JvmMemoryAccessor jvmMemoryAccessor = MEMORY_ACCESSOR;
        Objects.requireNonNull(jvmMemoryAccessor);
        jvmMemoryAccessor.unsafe.putObject(obj, j, obj2);
    }

    /* loaded from: classes.dex */
    public static final class JvmMemoryAccessor extends MemoryAccessor {
        public JvmMemoryAccessor(Unsafe unsafe) {
            super(unsafe);
        }
    }
}
