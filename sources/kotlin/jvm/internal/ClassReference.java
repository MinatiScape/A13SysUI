package kotlin.jvm.internal;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Function;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.collections.MapsKt___MapsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function10;
import kotlin.jvm.functions.Function11;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function14;
import kotlin.jvm.functions.Function15;
import kotlin.jvm.functions.Function16;
import kotlin.jvm.functions.Function17;
import kotlin.jvm.functions.Function18;
import kotlin.jvm.functions.Function19;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function20;
import kotlin.jvm.functions.Function21;
import kotlin.jvm.functions.Function22;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.functions.Function8;
import kotlin.jvm.functions.Function9;
import kotlin.reflect.KClass;
import kotlin.text.StringsKt__StringsKt;
/* compiled from: ClassReference.kt */
/* loaded from: classes.dex */
public final class ClassReference implements KClass<Object>, ClassBasedDeclarationContainer {
    public static final Map<Class<? extends Function<?>>, Integer> FUNCTION_CLASSES;
    public static final LinkedHashMap simpleNames;
    public final Class<?> jClass;

    static {
        String str;
        int i = 0;
        List listOf = SetsKt__SetsKt.listOf(Function0.class, Function1.class, Function2.class, Function3.class, Function4.class, Function5.class, Function6.class, Function7.class, Function8.class, Function9.class, Function10.class, Function11.class, Function12.class, Function13.class, Function14.class, Function15.class, Function16.class, Function17.class, Function18.class, Function19.class, Function20.class, Function21.class, Function22.class);
        ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(listOf, 10));
        for (Object obj : listOf) {
            int i2 = i + 1;
            if (i >= 0) {
                arrayList.add(new Pair((Class) obj, Integer.valueOf(i)));
                i = i2;
            } else {
                SetsKt__SetsKt.throwIndexOverflow();
                throw null;
            }
        }
        FUNCTION_CLASSES = MapsKt___MapsKt.toMap(arrayList);
        HashMap hashMap = new HashMap();
        hashMap.put("boolean", "kotlin.Boolean");
        hashMap.put("char", "kotlin.Char");
        hashMap.put("byte", "kotlin.Byte");
        hashMap.put("short", "kotlin.Short");
        hashMap.put("int", "kotlin.Int");
        hashMap.put("float", "kotlin.Float");
        hashMap.put("long", "kotlin.Long");
        hashMap.put("double", "kotlin.Double");
        HashMap hashMap2 = new HashMap();
        hashMap2.put("java.lang.Boolean", "kotlin.Boolean");
        hashMap2.put("java.lang.Character", "kotlin.Char");
        hashMap2.put("java.lang.Byte", "kotlin.Byte");
        hashMap2.put("java.lang.Short", "kotlin.Short");
        hashMap2.put("java.lang.Integer", "kotlin.Int");
        hashMap2.put("java.lang.Float", "kotlin.Float");
        hashMap2.put("java.lang.Long", "kotlin.Long");
        hashMap2.put("java.lang.Double", "kotlin.Double");
        HashMap hashMap3 = new HashMap();
        hashMap3.put("java.lang.Object", "kotlin.Any");
        hashMap3.put("java.lang.String", "kotlin.String");
        hashMap3.put("java.lang.CharSequence", "kotlin.CharSequence");
        hashMap3.put("java.lang.Throwable", "kotlin.Throwable");
        hashMap3.put("java.lang.Cloneable", "kotlin.Cloneable");
        hashMap3.put("java.lang.Number", "kotlin.Number");
        hashMap3.put("java.lang.Comparable", "kotlin.Comparable");
        hashMap3.put("java.lang.Enum", "kotlin.Enum");
        hashMap3.put("java.lang.annotation.Annotation", "kotlin.Annotation");
        hashMap3.put("java.lang.Iterable", "kotlin.collections.Iterable");
        hashMap3.put("java.util.Iterator", "kotlin.collections.Iterator");
        hashMap3.put("java.util.Collection", "kotlin.collections.Collection");
        hashMap3.put("java.util.List", "kotlin.collections.List");
        hashMap3.put("java.util.Set", "kotlin.collections.Set");
        hashMap3.put("java.util.ListIterator", "kotlin.collections.ListIterator");
        hashMap3.put("java.util.Map", "kotlin.collections.Map");
        hashMap3.put("java.util.Map$Entry", "kotlin.collections.Map.Entry");
        hashMap3.put("kotlin.jvm.internal.StringCompanionObject", "kotlin.String.Companion");
        hashMap3.put("kotlin.jvm.internal.EnumCompanionObject", "kotlin.Enum.Companion");
        hashMap3.putAll(hashMap);
        hashMap3.putAll(hashMap2);
        for (String str2 : hashMap.values()) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("kotlin.jvm.internal.");
            int lastIndexOf = str2.lastIndexOf(46, StringsKt__StringsKt.getLastIndex(str2));
            if (lastIndexOf == -1) {
                str = str2;
            } else {
                str = str2.substring(lastIndexOf + 1, str2.length());
            }
            Pair pair = new Pair(MotionController$$ExternalSyntheticOutline1.m(m, str, "CompanionObject"), Intrinsics.stringPlus(str2, ".Companion"));
            hashMap3.put(pair.getFirst(), pair.getSecond());
        }
        for (Map.Entry<Class<? extends Function<?>>, Integer> entry : FUNCTION_CLASSES.entrySet()) {
            hashMap3.put(entry.getKey().getName(), Intrinsics.stringPlus("kotlin.Function", Integer.valueOf(entry.getValue().intValue())));
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap(MapsKt__MapsJVMKt.mapCapacity(hashMap3.size()));
        for (Map.Entry entry2 : hashMap3.entrySet()) {
            Object key = entry2.getKey();
            String str3 = (String) entry2.getValue();
            int lastIndexOf2 = str3.lastIndexOf(46, StringsKt__StringsKt.getLastIndex(str3));
            if (lastIndexOf2 != -1) {
                str3 = str3.substring(lastIndexOf2 + 1, str3.length());
            }
            linkedHashMap.put(key, str3);
        }
        simpleNames = linkedHashMap;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof ClassReference) || !Intrinsics.areEqual(JvmClassMappingKt.getJavaObjectType(this), JvmClassMappingKt.getJavaObjectType((KClass) obj))) {
            return false;
        }
        return true;
    }

    public final String getSimpleName() {
        String str;
        Class<?> cls = this.jClass;
        String str2 = null;
        if (!cls.isAnonymousClass()) {
            if (cls.isLocalClass()) {
                String simpleName = cls.getSimpleName();
                Method enclosingMethod = cls.getEnclosingMethod();
                if (enclosingMethod != null) {
                    return StringsKt__StringsKt.substringAfter$default(simpleName, Intrinsics.stringPlus(enclosingMethod.getName(), "$"));
                }
                Constructor<?> enclosingConstructor = cls.getEnclosingConstructor();
                if (enclosingConstructor != null) {
                    return StringsKt__StringsKt.substringAfter$default(simpleName, Intrinsics.stringPlus(enclosingConstructor.getName(), "$"));
                }
                int indexOf$default = StringsKt__StringsKt.indexOf$default(simpleName, '$', 0, 6);
                if (indexOf$default == -1) {
                    return simpleName;
                }
                return simpleName.substring(indexOf$default + 1, simpleName.length());
            } else if (cls.isArray()) {
                Class<?> componentType = cls.getComponentType();
                if (componentType.isPrimitive() && (str = (String) simpleNames.get(componentType.getName())) != null) {
                    str2 = Intrinsics.stringPlus(str, "Array");
                }
                if (str2 == null) {
                    return "Array";
                }
            } else {
                String str3 = (String) simpleNames.get(cls.getName());
                if (str3 == null) {
                    return cls.getSimpleName();
                }
                return str3;
            }
        }
        return str2;
    }

    public final String toString() {
        return Intrinsics.stringPlus(this.jClass.toString(), " (Kotlin reflection is not available)");
    }

    public ClassReference(Class<?> cls) {
        this.jClass = cls;
    }

    public final int hashCode() {
        return JvmClassMappingKt.getJavaObjectType(this).hashCode();
    }

    @Override // kotlin.jvm.internal.ClassBasedDeclarationContainer
    public final Class<?> getJClass() {
        return this.jClass;
    }
}
