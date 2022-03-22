package kotlinx.coroutines.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.Result;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Reflection;
/* compiled from: ExceptionsConstuctor.kt */
/* loaded from: classes.dex */
public final class ExceptionsConstuctorKt {
    public static final int throwableFields = fieldsCountOrDefault(Throwable.class, -1);
    public static final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
    public static final WeakHashMap<Class<? extends Throwable>, Function1<Throwable, Throwable>> exceptionCtors = new WeakHashMap<>();

    public static final int fieldsCountOrDefault(Class<?> cls, int i) {
        Object obj;
        Reflection.getOrCreateKotlinClass(cls);
        int i2 = 0;
        do {
            try {
                Field[] declaredFields = cls.getDeclaredFields();
                int length = declaredFields.length;
                int i3 = 0;
                int i4 = 0;
                while (i3 < length) {
                    Field field = declaredFields[i3];
                    i3++;
                    if (!Modifier.isStatic(field.getModifiers())) {
                        i4++;
                    }
                }
                i2 += i4;
                cls = cls.getSuperclass();
            } catch (Throwable th) {
                obj = new Result.Failure(th);
            }
        } while (cls != null);
        obj = Integer.valueOf(i2);
        Object valueOf = Integer.valueOf(i);
        if (obj instanceof Result.Failure) {
            obj = valueOf;
        }
        return ((Number) obj).intValue();
    }
}
