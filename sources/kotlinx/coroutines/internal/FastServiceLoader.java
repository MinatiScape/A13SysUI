package kotlinx.coroutines.internal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import kotlin.ExceptionsKt;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt__ReversedViewsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;
/* compiled from: FastServiceLoader.kt */
/* loaded from: classes.dex */
public final class FastServiceLoader {
    public static List loadMainDispatcherFactory$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        List list;
        MainDispatcherFactory mainDispatcherFactory;
        if (!FastServiceLoaderKt.ANDROID_DETECTED) {
            ClassLoader classLoader = MainDispatcherFactory.class.getClassLoader();
            try {
                return loadProviders$external__kotlinx_coroutines__android_common__kotlinx_coroutines(classLoader);
            } catch (Throwable unused) {
                return CollectionsKt___CollectionsKt.toList(ServiceLoader.load(MainDispatcherFactory.class, classLoader));
            }
        } else {
            try {
                ArrayList arrayList = new ArrayList(2);
                MainDispatcherFactory mainDispatcherFactory2 = null;
                try {
                    mainDispatcherFactory = (MainDispatcherFactory) MainDispatcherFactory.class.cast(Class.forName("kotlinx.coroutines.android.AndroidDispatcherFactory", true, MainDispatcherFactory.class.getClassLoader()).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
                } catch (ClassNotFoundException unused2) {
                    mainDispatcherFactory = null;
                }
                if (mainDispatcherFactory != null) {
                    arrayList.add(mainDispatcherFactory);
                }
                try {
                    mainDispatcherFactory2 = (MainDispatcherFactory) MainDispatcherFactory.class.cast(Class.forName("kotlinx.coroutines.test.internal.TestMainDispatcherFactory", true, MainDispatcherFactory.class.getClassLoader()).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
                } catch (ClassNotFoundException unused3) {
                }
                if (mainDispatcherFactory2 == null) {
                    return arrayList;
                }
                arrayList.add(mainDispatcherFactory2);
                return arrayList;
            } catch (Throwable unused4) {
                ClassLoader classLoader2 = MainDispatcherFactory.class.getClassLoader();
                try {
                    list = loadProviders$external__kotlinx_coroutines__android_common__kotlinx_coroutines(classLoader2);
                } catch (Throwable unused5) {
                    list = CollectionsKt___CollectionsKt.toList(ServiceLoader.load(MainDispatcherFactory.class, classLoader2));
                }
                return list;
            }
        }
    }

    public static ArrayList loadProviders$external__kotlinx_coroutines__android_common__kotlinx_coroutines(ClassLoader classLoader) {
        List list;
        ArrayList<URL> list2 = Collections.list(classLoader.getResources(Intrinsics.stringPlus("META-INF/services/", MainDispatcherFactory.class.getName())));
        ArrayList arrayList = new ArrayList();
        for (URL url : list2) {
            String url2 = url.toString();
            th = null;
            if (url2.startsWith("jar")) {
                String substringAfter$default = StringsKt__StringsKt.substringAfter$default(url2, "jar:file:");
                int indexOf$default = StringsKt__StringsKt.indexOf$default(substringAfter$default, '!', 0, 6);
                if (indexOf$default != -1) {
                    substringAfter$default = substringAfter$default.substring(0, indexOf$default);
                }
                String substringAfter$default2 = StringsKt__StringsKt.substringAfter$default(url2, "!/");
                JarFile jarFile = new JarFile(substringAfter$default, false);
                try {
                    list = parseFile(new BufferedReader(new InputStreamReader(jarFile.getInputStream(new ZipEntry(substringAfter$default2)), "UTF-8")));
                    jarFile.close();
                } catch (Throwable th) {
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        try {
                            jarFile.close();
                            throw th2;
                        } catch (Throwable th3) {
                            ExceptionsKt.addSuppressed(th, th3);
                            throw th;
                        }
                    }
                }
            } else {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                try {
                    List parseFile = parseFile(bufferedReader);
                    CloseableKt.closeFinally(bufferedReader, th);
                    list = parseFile;
                } finally {
                    try {
                        throw th;
                    } finally {
                    }
                }
            }
            CollectionsKt__ReversedViewsKt.addAll(arrayList, list);
        }
        Set<String> set = CollectionsKt___CollectionsKt.toSet(arrayList);
        if (!set.isEmpty()) {
            ArrayList arrayList2 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(set, 10));
            for (String str : set) {
                Class<?> cls = Class.forName(str, false, classLoader);
                if (MainDispatcherFactory.class.isAssignableFrom(cls)) {
                    arrayList2.add(MainDispatcherFactory.class.cast(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0])));
                } else {
                    throw new IllegalArgumentException(("Expected service of class " + MainDispatcherFactory.class + ", but found " + cls).toString());
                }
            }
            return arrayList2;
        }
        throw new IllegalArgumentException("No providers were loaded with FastServiceLoader".toString());
    }

    public static List parseFile(BufferedReader bufferedReader) {
        boolean z;
        boolean z2;
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                return CollectionsKt___CollectionsKt.toList(linkedHashSet);
            }
            boolean z3 = false;
            int indexOf$default = StringsKt__StringsKt.indexOf$default(readLine, "#", 0, false, 6);
            if (indexOf$default != -1) {
                readLine = readLine.substring(0, indexOf$default);
            }
            String obj = StringsKt__StringsKt.trim(readLine).toString();
            int i = 0;
            while (true) {
                if (i >= obj.length()) {
                    z = true;
                    break;
                }
                char charAt = obj.charAt(i);
                i++;
                if (charAt == '.' || Character.isJavaIdentifierPart(charAt)) {
                    z2 = true;
                    continue;
                } else {
                    z2 = false;
                    continue;
                }
                if (!z2) {
                    z = false;
                    break;
                }
            }
            if (z) {
                if (obj.length() > 0) {
                    z3 = true;
                }
                if (z3) {
                    linkedHashSet.add(obj);
                }
            } else {
                throw new IllegalArgumentException(Intrinsics.stringPlus("Illegal service provider class name: ", obj).toString());
            }
        }
    }
}
