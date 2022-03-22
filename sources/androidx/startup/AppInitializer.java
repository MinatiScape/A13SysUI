package androidx.startup;

import android.content.Context;
import android.os.Trace;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
/* loaded from: classes.dex */
public final class AppInitializer {
    public static volatile AppInitializer sInstance;
    public static final Object sLock = new Object();
    public final Context mContext;
    public final HashSet mDiscovered = new HashSet();
    public final HashMap mInitialized = new HashMap();

    public final Object doInitialize(Class cls, HashSet hashSet) {
        Object obj;
        synchronized (sLock) {
            if (Trace.isEnabled()) {
                Trace.beginSection(cls.getSimpleName());
            }
            if (!hashSet.contains(cls)) {
                if (!this.mInitialized.containsKey(cls)) {
                    hashSet.add(cls);
                    try {
                        Initializer initializer = (Initializer) cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                        List<Class<? extends Initializer<?>>> dependencies = initializer.dependencies();
                        if (!dependencies.isEmpty()) {
                            for (Class<? extends Initializer<?>> cls2 : dependencies) {
                                if (!this.mInitialized.containsKey(cls2)) {
                                    doInitialize(cls2, hashSet);
                                }
                            }
                        }
                        obj = initializer.create(this.mContext);
                        hashSet.remove(cls);
                        this.mInitialized.put(cls, obj);
                    } catch (Throwable th) {
                        throw new StartupException(th);
                    }
                } else {
                    obj = this.mInitialized.get(cls);
                }
                Trace.endSection();
            } else {
                throw new IllegalStateException(String.format("Cannot initialize %s. Cycle detected.", cls.getName()));
            }
        }
        return obj;
    }

    public AppInitializer(Context context) {
        this.mContext = context.getApplicationContext();
    }
}
