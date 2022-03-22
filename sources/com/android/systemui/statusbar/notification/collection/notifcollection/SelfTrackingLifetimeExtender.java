package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.statusbar.notification.collection.NotifCollection$$ExternalSyntheticLambda2;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SelfTrackingLifetimeExtender.kt */
/* loaded from: classes.dex */
public abstract class SelfTrackingLifetimeExtender implements NotifLifetimeExtender, Dumpable {
    public final boolean debug;
    public NotifLifetimeExtender.OnEndLifetimeExtensionCallback mCallback;
    public boolean mEnding;
    public final Handler mainHandler;
    public final String name;
    public final String tag = "RemoteInputCoordinator";
    public final ArrayMap<String, NotificationEntry> mEntriesExtended = new ArrayMap<>();

    public void onCanceledLifetimeExtension(NotificationEntry notificationEntry) {
    }

    public void onStartedLifetimeExtension(NotificationEntry notificationEntry) {
    }

    public abstract boolean queryShouldExtendLifetime(NotificationEntry notificationEntry);

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender
    public final void cancelLifetimeExtension(NotificationEntry notificationEntry) {
        if (this.debug) {
            String str = this.tag;
            Log.d(str, this.name + ".cancelLifetimeExtension(key=" + notificationEntry.mKey + ") isExtending=" + isExtending(notificationEntry.mKey));
        }
        warnIfEnding();
        this.mEntriesExtended.remove(notificationEntry.mKey);
        onCanceledLifetimeExtension(notificationEntry);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("LifetimeExtender: ");
        m.append(this.name);
        m.append(':');
        printWriter.println(m.toString());
        printWriter.println(Intrinsics.stringPlus("  mEntriesExtended: ", Integer.valueOf(this.mEntriesExtended.size())));
        for (Map.Entry<String, NotificationEntry> entry : this.mEntriesExtended.entrySet()) {
            printWriter.println(Intrinsics.stringPlus("  * ", entry.getKey()));
        }
    }

    public final void endLifetimeExtension(String str) {
        if (this.debug) {
            String str2 = this.tag;
            Log.d(str2, this.name + ".endLifetimeExtension(key=" + str + ") isExtending=" + isExtending(str));
        }
        warnIfEnding();
        this.mEnding = true;
        NotificationEntry remove = this.mEntriesExtended.remove(str);
        if (remove != null) {
            NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback = this.mCallback;
            if (onEndLifetimeExtensionCallback == null) {
                onEndLifetimeExtensionCallback = null;
            }
            ((NotifCollection$$ExternalSyntheticLambda2) onEndLifetimeExtensionCallback).onEndLifetimeExtension(this, remove);
        }
        this.mEnding = false;
    }

    public final void endLifetimeExtensionAfterDelay(final String str, long j) {
        if (this.debug) {
            String str2 = this.tag;
            Log.d(str2, this.name + ".endLifetimeExtensionAfterDelay(key=" + str + ", delayMillis=" + j + ") isExtending=" + isExtending(str));
        }
        if (isExtending(str)) {
            this.mainHandler.postDelayed(new Runnable() { // from class: com.android.systemui.statusbar.notification.collection.notifcollection.SelfTrackingLifetimeExtender$endLifetimeExtensionAfterDelay$1
                @Override // java.lang.Runnable
                public final void run() {
                    SelfTrackingLifetimeExtender.this.endLifetimeExtension(str);
                }
            }, j);
        }
    }

    public final boolean isExtending(String str) {
        return this.mEntriesExtended.containsKey(str);
    }

    public final void warnIfEnding() {
        if (this.debug && this.mEnding) {
            Log.w(this.tag, "reentrant code while ending a lifetime extension");
        }
    }

    public SelfTrackingLifetimeExtender(String str, boolean z, Handler handler) {
        this.name = str;
        this.debug = z;
        this.mainHandler = handler;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender
    public final boolean maybeExtendLifetime(NotificationEntry notificationEntry, int i) {
        boolean queryShouldExtendLifetime = queryShouldExtendLifetime(notificationEntry);
        if (this.debug) {
            String str = this.tag;
            Log.d(str, this.name + ".shouldExtendLifetime(key=" + notificationEntry.mKey + ", reason=" + i + ") isExtending=" + isExtending(notificationEntry.mKey) + " shouldExtend=" + queryShouldExtendLifetime);
        }
        warnIfEnding();
        if (queryShouldExtendLifetime && this.mEntriesExtended.put(notificationEntry.mKey, notificationEntry) == null) {
            onStartedLifetimeExtension(notificationEntry);
        }
        return queryShouldExtendLifetime;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender
    public final void setCallback(NotifCollection$$ExternalSyntheticLambda2 notifCollection$$ExternalSyntheticLambda2) {
        this.mCallback = notifCollection$$ExternalSyntheticLambda2;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender
    public final String getName() {
        return this.name;
    }
}
