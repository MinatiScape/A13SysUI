package com.android.systemui.statusbar;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.ArraySet;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class SmartReplyController implements Dumpable {
    public final IStatusBarService mBarService;
    public Callback mCallback;
    public final NotificationClickNotifier mClickNotifier;
    public final ArraySet mSendingKeys = new ArraySet();
    public final NotificationVisibilityProvider mVisibilityProvider;

    /* loaded from: classes.dex */
    public interface Callback {
        void onSmartReplySent(NotificationEntry notificationEntry, CharSequence charSequence);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("mSendingKeys: ");
        m.append(this.mSendingKeys.size());
        printWriter.println(m.toString());
        Iterator it = this.mSendingKeys.iterator();
        while (it.hasNext()) {
            printWriter.println(" * " + ((String) it.next()));
        }
    }

    public final void stopSending(NotificationEntry notificationEntry) {
        if (notificationEntry != null) {
            this.mSendingKeys.remove(notificationEntry.mSbn.getKey());
        }
    }

    public SmartReplyController(DumpManager dumpManager, NotificationVisibilityProvider notificationVisibilityProvider, IStatusBarService iStatusBarService, NotificationClickNotifier notificationClickNotifier) {
        this.mBarService = iStatusBarService;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mClickNotifier = notificationClickNotifier;
        dumpManager.registerDumpable(this);
    }
}
