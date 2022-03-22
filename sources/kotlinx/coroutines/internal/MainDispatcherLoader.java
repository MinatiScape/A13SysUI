package kotlinx.coroutines.internal;

import java.util.Iterator;
import java.util.List;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt;
import kotlinx.coroutines.MainCoroutineDispatcher;
/* compiled from: MainDispatchers.kt */
/* loaded from: classes.dex */
public final class MainDispatcherLoader {
    public static final MainCoroutineDispatcher dispatcher;

    static {
        String str;
        boolean z;
        MissingMainCoroutineDispatcher missingMainCoroutineDispatcher;
        List list;
        Object obj;
        int i = SystemPropsKt__SystemPropsKt.AVAILABLE_PROCESSORS;
        try {
            str = System.getProperty("kotlinx.coroutines.fast.service.loader");
        } catch (SecurityException unused) {
            str = null;
        }
        if (str == null) {
            z = true;
        } else {
            z = Boolean.parseBoolean(str);
        }
        try {
            if (z) {
                list = FastServiceLoader.loadMainDispatcherFactory$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
            } else {
                list = SequencesKt___SequencesKt.toList(SequencesKt__SequencesKt.asSequence(MainDispatcherLoader$$ExternalSyntheticServiceLoad0.m()));
            }
            Iterator it = list.iterator();
            if (!it.hasNext()) {
                obj = null;
            } else {
                obj = it.next();
                if (it.hasNext()) {
                    ((MainDispatcherFactory) obj).getLoadPriority();
                    do {
                        ((MainDispatcherFactory) it.next()).getLoadPriority();
                    } while (it.hasNext());
                }
            }
            MainDispatcherFactory mainDispatcherFactory = (MainDispatcherFactory) obj;
            if (mainDispatcherFactory == null) {
                missingMainCoroutineDispatcher = null;
            } else {
                missingMainCoroutineDispatcher = mainDispatcherFactory.createDispatcher$1();
            }
            if (missingMainCoroutineDispatcher == null) {
                missingMainCoroutineDispatcher = new MissingMainCoroutineDispatcher(null, null);
            }
        } catch (Throwable th) {
            missingMainCoroutineDispatcher = new MissingMainCoroutineDispatcher(th, null);
        }
        dispatcher = missingMainCoroutineDispatcher;
    }
}
