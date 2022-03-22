package kotlin.jvm;

import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.reflect.KClass;
/* compiled from: JvmClassMapping.kt */
/* loaded from: classes.dex */
public final class JvmClassMappingKt {
    public static final <T> Class<T> getJavaObjectType(KClass<T> kClass) {
        Class<T> cls = (Class<T>) ((ClassBasedDeclarationContainer) kClass).getJClass();
        if (!cls.isPrimitive()) {
            return cls;
        }
        String name = cls.getName();
        switch (name.hashCode()) {
            case -1325958191:
                if (!name.equals("double")) {
                    return cls;
                }
                return Double.class;
            case 104431:
                if (!name.equals("int")) {
                    return cls;
                }
                return Integer.class;
            case 3039496:
                if (!name.equals("byte")) {
                    return cls;
                }
                return Byte.class;
            case 3052374:
                if (!name.equals("char")) {
                    return cls;
                }
                return Character.class;
            case 3327612:
                if (!name.equals("long")) {
                    return cls;
                }
                return Long.class;
            case 3625364:
                if (!name.equals("void")) {
                    return cls;
                }
                return Void.class;
            case 64711720:
                if (!name.equals("boolean")) {
                    return cls;
                }
                return Boolean.class;
            case 97526364:
                if (!name.equals("float")) {
                    return cls;
                }
                return Float.class;
            case 109413500:
                if (!name.equals("short")) {
                    return cls;
                }
                return Short.class;
            default:
                return cls;
        }
    }
}
