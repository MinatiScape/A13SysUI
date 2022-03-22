package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import java.util.ArrayList;
/* loaded from: classes.dex */
public abstract class NotifSectioner extends Pluggable<NotifSectioner> {
    public final int mBucket;

    public NotifComparator getComparator() {
        return null;
    }

    public NodeController getHeaderNodeController() {
        return null;
    }

    public abstract boolean isInSection(ListEntry listEntry);

    public void onEntriesUpdated(ArrayList arrayList) {
    }

    public NotifSectioner(String str, int i) {
        super(str);
        this.mBucket = i;
    }
}
