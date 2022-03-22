package com.android.systemui.settings;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.UserInfo;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import com.android.keyguard.KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.EmptyList;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
/* compiled from: UserTrackerImpl.kt */
/* loaded from: classes.dex */
public final class UserTrackerImpl extends BroadcastReceiver implements UserTracker, Dumpable {
    public static final /* synthetic */ KProperty<Object>[] $$delegatedProperties;
    public final Handler backgroundHandler;
    public final Context context;
    public final DumpManager dumpManager;
    public boolean initialized;
    public final SynchronizedDelegate userContext$delegate;
    public final SynchronizedDelegate userHandle$delegate;
    public final SynchronizedDelegate userId$delegate;
    public final UserManager userManager;
    public final Object mutex = new Object();
    public final SynchronizedDelegate userProfiles$delegate = new SynchronizedDelegate(EmptyList.INSTANCE);
    public final ArrayList callbacks = new ArrayList();

    /* compiled from: UserTrackerImpl.kt */
    /* loaded from: classes.dex */
    public static final class SynchronizedDelegate<T> {
        public T value;

        public final T getValue(UserTrackerImpl userTrackerImpl, KProperty<?> kProperty) {
            T t;
            if (userTrackerImpl.initialized) {
                synchronized (userTrackerImpl.mutex) {
                    t = this.value;
                }
                return t;
            }
            throw new IllegalStateException(Intrinsics.stringPlus("Must initialize before getting ", kProperty.getName()));
        }

        public SynchronizedDelegate(T t) {
            this.value = t;
        }
    }

    static {
        MutablePropertyReference1Impl mutablePropertyReference1Impl = new MutablePropertyReference1Impl(UserTrackerImpl.class, "userId", "getUserId()I");
        Objects.requireNonNull(Reflection.factory);
        $$delegatedProperties = new KProperty[]{mutablePropertyReference1Impl, new MutablePropertyReference1Impl(UserTrackerImpl.class, "userHandle", "getUserHandle()Landroid/os/UserHandle;"), new MutablePropertyReference1Impl(UserTrackerImpl.class, "userContext", "getUserContext()Landroid/content/Context;"), new MutablePropertyReference1Impl(UserTrackerImpl.class, "userProfiles", "getUserProfiles()Ljava/util/List;")};
    }

    @Override // com.android.systemui.settings.UserTracker
    public final void addCallback(UserTracker.Callback callback, Executor executor) {
        synchronized (this.callbacks) {
            this.callbacks.add(new DataItem(new WeakReference(callback), executor));
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        List<DataItem> list;
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.initialized, "Initialized: ", printWriter);
        if (this.initialized) {
            printWriter.println(Intrinsics.stringPlus("userId: ", Integer.valueOf(getUserId())));
            List<UserInfo> userProfiles = getUserProfiles();
            ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(userProfiles, 10));
            for (UserInfo userInfo : userProfiles) {
                arrayList.add(Integer.valueOf(userInfo.id));
            }
            printWriter.println(Intrinsics.stringPlus("userProfiles: ", arrayList));
        }
        synchronized (this.callbacks) {
            list = CollectionsKt___CollectionsKt.toList(this.callbacks);
        }
        printWriter.println("Callbacks:");
        for (DataItem dataItem : list) {
            Objects.requireNonNull(dataItem);
            UserTracker.Callback callback = dataItem.callback.get();
            if (callback != null) {
                printWriter.println(Intrinsics.stringPlus("  ", callback));
            }
        }
    }

    @Override // com.android.systemui.settings.UserContextProvider
    public final Context getUserContext() {
        return (Context) this.userContext$delegate.getValue(this, $$delegatedProperties[2]);
    }

    @Override // com.android.systemui.settings.UserTracker
    public final UserHandle getUserHandle() {
        return (UserHandle) this.userHandle$delegate.getValue(this, $$delegatedProperties[1]);
    }

    @Override // com.android.systemui.settings.UserTracker
    public final int getUserId() {
        return ((Number) this.userId$delegate.getValue(this, $$delegatedProperties[0])).intValue();
    }

    @Override // com.android.systemui.settings.UserTracker
    public final List<UserInfo> getUserProfiles() {
        return (List) this.userProfiles$delegate.getValue(this, $$delegatedProperties[3]);
    }

    @Override // com.android.systemui.settings.UserTracker
    public final void removeCallback(final UserTracker.Callback callback) {
        synchronized (this.callbacks) {
            this.callbacks.removeIf(new Predicate() { // from class: com.android.systemui.settings.UserTrackerImpl$removeCallback$1$1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    UserTracker.Callback callback2 = UserTracker.Callback.this;
                    UserTracker.Callback callback3 = ((DataItem) obj).callback.get();
                    if (callback3 == null) {
                        return true;
                    }
                    return callback3.equals(callback2);
                }
            });
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [T, android.os.UserHandle] */
    /* JADX WARN: Type inference failed for: r2v1, types: [android.content.Context, T, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r8v1, types: [T, java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r8v4, types: [T, java.util.ArrayList] */
    /* JADX WARN: Unknown variable types count: 4 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final kotlin.Pair<android.content.Context, java.util.List<android.content.pm.UserInfo>> setUserIdInternal(int r8) {
        /*
            r7 = this;
            android.os.UserManager r0 = r7.userManager
            java.util.List r0 = r0.getProfiles(r8)
            android.os.UserHandle r1 = new android.os.UserHandle
            r1.<init>(r8)
            android.content.Context r2 = r7.context
            r3 = 0
            android.content.Context r2 = r2.createContextAsUser(r1, r3)
            java.lang.Object r4 = r7.mutex
            monitor-enter(r4)
            com.android.systemui.settings.UserTrackerImpl$SynchronizedDelegate r5 = r7.userId$delegate     // Catch: all -> 0x008b
            kotlin.reflect.KProperty<java.lang.Object>[] r6 = com.android.systemui.settings.UserTrackerImpl.$$delegatedProperties     // Catch: all -> 0x008b
            r3 = r6[r3]     // Catch: all -> 0x008b
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch: all -> 0x008b
            java.util.Objects.requireNonNull(r5)     // Catch: all -> 0x008b
            java.lang.Object r3 = r7.mutex     // Catch: all -> 0x008b
            monitor-enter(r3)     // Catch: all -> 0x008b
            r5.value = r8     // Catch: all -> 0x0088
            monitor-exit(r3)     // Catch: all -> 0x008b
            com.android.systemui.settings.UserTrackerImpl$SynchronizedDelegate r8 = r7.userHandle$delegate     // Catch: all -> 0x008b
            r3 = 1
            r3 = r6[r3]     // Catch: all -> 0x008b
            java.util.Objects.requireNonNull(r8)     // Catch: all -> 0x008b
            java.lang.Object r3 = r7.mutex     // Catch: all -> 0x008b
            monitor-enter(r3)     // Catch: all -> 0x008b
            r8.value = r1     // Catch: all -> 0x0085
            monitor-exit(r3)     // Catch: all -> 0x008b
            com.android.systemui.settings.UserTrackerImpl$SynchronizedDelegate r8 = r7.userContext$delegate     // Catch: all -> 0x008b
            r1 = 2
            r1 = r6[r1]     // Catch: all -> 0x008b
            java.util.Objects.requireNonNull(r8)     // Catch: all -> 0x008b
            java.lang.Object r1 = r7.mutex     // Catch: all -> 0x008b
            monitor-enter(r1)     // Catch: all -> 0x008b
            r8.value = r2     // Catch: all -> 0x0082
            monitor-exit(r1)     // Catch: all -> 0x008b
            java.util.ArrayList r8 = new java.util.ArrayList     // Catch: all -> 0x008b
            r1 = 10
            int r1 = kotlin.collections.CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(r0, r1)     // Catch: all -> 0x008b
            r8.<init>(r1)     // Catch: all -> 0x008b
            java.util.Iterator r1 = r0.iterator()     // Catch: all -> 0x008b
        L_0x0053:
            boolean r3 = r1.hasNext()     // Catch: all -> 0x008b
            if (r3 == 0) goto L_0x0068
            java.lang.Object r3 = r1.next()     // Catch: all -> 0x008b
            android.content.pm.UserInfo r3 = (android.content.pm.UserInfo) r3     // Catch: all -> 0x008b
            android.content.pm.UserInfo r5 = new android.content.pm.UserInfo     // Catch: all -> 0x008b
            r5.<init>(r3)     // Catch: all -> 0x008b
            r8.add(r5)     // Catch: all -> 0x008b
            goto L_0x0053
        L_0x0068:
            com.android.systemui.settings.UserTrackerImpl$SynchronizedDelegate r1 = r7.userProfiles$delegate     // Catch: all -> 0x008b
            kotlin.reflect.KProperty<java.lang.Object>[] r3 = com.android.systemui.settings.UserTrackerImpl.$$delegatedProperties     // Catch: all -> 0x008b
            r5 = 3
            r3 = r3[r5]     // Catch: all -> 0x008b
            java.util.Objects.requireNonNull(r1)     // Catch: all -> 0x008b
            java.lang.Object r7 = r7.mutex     // Catch: all -> 0x008b
            monitor-enter(r7)     // Catch: all -> 0x008b
            r1.value = r8     // Catch: all -> 0x007f
            monitor-exit(r7)     // Catch: all -> 0x008b
            monitor-exit(r4)
            kotlin.Pair r7 = new kotlin.Pair
            r7.<init>(r2, r0)
            return r7
        L_0x007f:
            r8 = move-exception
            monitor-exit(r7)     // Catch: all -> 0x008b
            throw r8     // Catch: all -> 0x008b
        L_0x0082:
            r7 = move-exception
            monitor-exit(r1)     // Catch: all -> 0x008b
            throw r7     // Catch: all -> 0x008b
        L_0x0085:
            r7 = move-exception
            monitor-exit(r3)     // Catch: all -> 0x008b
            throw r7     // Catch: all -> 0x008b
        L_0x0088:
            r7 = move-exception
            monitor-exit(r3)     // Catch: all -> 0x008b
            throw r7     // Catch: all -> 0x008b
        L_0x008b:
            r7 = move-exception
            monitor-exit(r4)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.settings.UserTrackerImpl.setUserIdInternal(int):kotlin.Pair");
    }

    public UserTrackerImpl(Context context, UserManager userManager, DumpManager dumpManager, Handler handler) {
        this.context = context;
        this.userManager = userManager;
        this.dumpManager = dumpManager;
        this.backgroundHandler = handler;
        this.userId$delegate = new SynchronizedDelegate(Integer.valueOf(context.getUserId()));
        this.userHandle$delegate = new SynchronizedDelegate(context.getUser());
        this.userContext$delegate = new SynchronizedDelegate(context);
    }

    @Override // com.android.systemui.settings.UserContentResolverProvider
    public final ContentResolver getUserContentResolver() {
        return getUserContext().getContentResolver();
    }

    @Override // com.android.systemui.settings.UserTracker
    public final UserInfo getUserInfo() {
        boolean z;
        int userId = getUserId();
        for (UserInfo userInfo : getUserProfiles()) {
            if (userInfo.id == userId) {
                z = true;
                continue;
            } else {
                z = false;
                continue;
            }
            if (z) {
                return userInfo;
            }
        }
        throw new NoSuchElementException("Collection contains no element matching the predicate.");
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [T, java.util.ArrayList] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // android.content.BroadcastReceiver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onReceive(android.content.Context r5, android.content.Intent r6) {
        /*
            Method dump skipped, instructions count: 296
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.settings.UserTrackerImpl.onReceive(android.content.Context, android.content.Intent):void");
    }
}
