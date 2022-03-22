package com.android.systemui.qs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.UserAwareController;
import com.android.systemui.util.settings.SecureSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.EmptyList;
import kotlin.collections.EmptySet;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;
/* compiled from: AutoAddTracker.kt */
/* loaded from: classes.dex */
public final class AutoAddTracker implements UserAwareController, Dumpable {
    public static final IntentFilter FILTER = new IntentFilter("android.os.action.SETTING_RESTORED");
    public final Executor backgroundExecutor;
    public final BroadcastDispatcher broadcastDispatcher;
    public final AutoAddTracker$contentObserver$1 contentObserver;
    public final DumpManager dumpManager;
    public final QSHost qsHost;
    public Set<String> restoredTiles;
    public final SecureSettings secureSettings;
    public int userId;
    public final ArraySet<String> autoAdded = new ArraySet<>();
    public final AutoAddTracker$restoreReceiver$1 restoreReceiver = new BroadcastReceiver() { // from class: com.android.systemui.qs.AutoAddTracker$restoreReceiver$1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            Set<String> set;
            List list;
            String join;
            if (Intrinsics.areEqual(intent.getAction(), "android.os.action.SETTING_RESTORED")) {
                AutoAddTracker autoAddTracker = AutoAddTracker.this;
                Objects.requireNonNull(autoAddTracker);
                String stringExtra = intent.getStringExtra("setting_name");
                Unit unit = null;
                Set<String> set2 = null;
                List list2 = null;
                if (Intrinsics.areEqual(stringExtra, "sysui_qs_tiles")) {
                    String stringExtra2 = intent.getStringExtra("new_value");
                    if (stringExtra2 != null) {
                        set2 = CollectionsKt___CollectionsKt.toSet(StringsKt__StringsKt.split$default(stringExtra2, new String[]{","}));
                    }
                    if (set2 == null) {
                        Log.w("AutoAddTracker", Intrinsics.stringPlus("Null restored tiles for user ", Integer.valueOf(autoAddTracker.userId)));
                        set2 = EmptySet.INSTANCE;
                    }
                    autoAddTracker.restoredTiles = set2;
                } else if (Intrinsics.areEqual(stringExtra, "qs_auto_tiles")) {
                    if (autoAddTracker.restoredTiles != null) {
                        String stringExtra3 = intent.getStringExtra("new_value");
                        if (stringExtra3 == null) {
                            list = null;
                        } else {
                            list = StringsKt__StringsKt.split$default(stringExtra3, new String[]{","});
                        }
                        if (list == null) {
                            list = EmptyList.INSTANCE;
                        }
                        String stringExtra4 = intent.getStringExtra("previous_value");
                        if (stringExtra4 != null) {
                            list2 = StringsKt__StringsKt.split$default(stringExtra4, new String[]{","});
                        }
                        if (list2 == null) {
                            list2 = EmptyList.INSTANCE;
                        }
                        ArrayList arrayList = new ArrayList();
                        for (Object obj : list) {
                            if (!set.contains((String) obj)) {
                                arrayList.add(obj);
                            }
                        }
                        if (!arrayList.isEmpty()) {
                            autoAddTracker.qsHost.removeTiles(arrayList);
                        }
                        synchronized (autoAddTracker.autoAdded) {
                            autoAddTracker.autoAdded.clear();
                            autoAddTracker.autoAdded.addAll(CollectionsKt___CollectionsKt.plus(list, list2));
                            join = TextUtils.join(",", autoAddTracker.autoAdded);
                        }
                        autoAddTracker.secureSettings.putStringForUser$1("qs_auto_tiles", join, autoAddTracker.userId);
                        unit = Unit.INSTANCE;
                    }
                    if (unit == null) {
                        Log.w("AutoAddTracker", Intrinsics.stringPlus("qs_auto_tiles restored before sysui_qs_tiles for user ", Integer.valueOf(autoAddTracker.userId)));
                    }
                }
            }
        }
    };

    /* compiled from: AutoAddTracker.kt */
    /* loaded from: classes.dex */
    public static final class Builder {
        public final BroadcastDispatcher broadcastDispatcher;
        public final DumpManager dumpManager;
        public final Executor executor;
        public final Handler handler;
        public final QSHost qsHost;
        public final SecureSettings secureSettings;
        public int userId;

        public Builder(SecureSettings secureSettings, BroadcastDispatcher broadcastDispatcher, QSHost qSHost, DumpManager dumpManager, Handler handler, Executor executor) {
            this.secureSettings = secureSettings;
            this.broadcastDispatcher = broadcastDispatcher;
            this.qsHost = qSHost;
            this.dumpManager = dumpManager;
            this.handler = handler;
            this.executor = executor;
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("Current user: ", Integer.valueOf(this.userId)));
        printWriter.println(Intrinsics.stringPlus("Added tiles: ", this.autoAdded));
    }

    public final boolean isAdded(String str) {
        boolean contains;
        synchronized (this.autoAdded) {
            contains = this.autoAdded.contains(str);
        }
        return contains;
    }

    public final void loadTiles() {
        Collection<? extends String> collection;
        synchronized (this.autoAdded) {
            this.autoAdded.clear();
            ArraySet<String> arraySet = this.autoAdded;
            String stringForUser = this.secureSettings.getStringForUser("qs_auto_tiles", this.userId);
            if (stringForUser == null) {
                collection = null;
            } else {
                collection = StringsKt__StringsKt.split$default(stringForUser, new String[]{","});
            }
            if (collection == null) {
                collection = EmptySet.INSTANCE;
            }
            arraySet.addAll(collection);
        }
    }

    public final void setTileAdded(String str) {
        String str2;
        synchronized (this.autoAdded) {
            if (this.autoAdded.add(str)) {
                str2 = TextUtils.join(",", this.autoAdded);
            } else {
                str2 = null;
            }
        }
        if (str2 != null) {
            this.secureSettings.putStringForUser$1("qs_auto_tiles", str2, this.userId);
        }
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.qs.AutoAddTracker$contentObserver$1] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.qs.AutoAddTracker$restoreReceiver$1] */
    public AutoAddTracker(SecureSettings secureSettings, BroadcastDispatcher broadcastDispatcher, QSHost qSHost, DumpManager dumpManager, final Handler handler, Executor executor, int i) {
        this.secureSettings = secureSettings;
        this.broadcastDispatcher = broadcastDispatcher;
        this.qsHost = qSHost;
        this.dumpManager = dumpManager;
        this.backgroundExecutor = executor;
        this.userId = i;
        this.contentObserver = new ContentObserver(handler) { // from class: com.android.systemui.qs.AutoAddTracker$contentObserver$1
            public final void onChange(boolean z, Collection<? extends Uri> collection, int i2, int i3) {
                AutoAddTracker autoAddTracker = AutoAddTracker.this;
                if (i3 == autoAddTracker.userId) {
                    autoAddTracker.loadTiles();
                }
            }
        };
    }

    @Override // com.android.systemui.util.UserAwareController
    public final void changeUser(UserHandle userHandle) {
        if (userHandle.getIdentifier() != this.userId) {
            this.broadcastDispatcher.unregisterReceiver(this.restoreReceiver);
            this.userId = userHandle.getIdentifier();
            this.restoredTiles = null;
            loadTiles();
            BroadcastDispatcher.registerReceiver$default(this.broadcastDispatcher, this.restoreReceiver, FILTER, this.backgroundExecutor, UserHandle.of(this.userId), 16);
        }
    }
}
