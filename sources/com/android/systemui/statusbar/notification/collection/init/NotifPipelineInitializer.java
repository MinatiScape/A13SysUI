package com.android.systemui.statusbar.notification.collection.init;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.coalescer.CoalescedEvent;
import com.android.systemui.statusbar.notification.collection.coalescer.EventBatch;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManagerFactory;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotifPipelineInitializer implements Dumpable {
    public final DumpManager mDumpManager;
    public final GroupCoalescer mGroupCoalescer;
    public final ShadeListBuilder mListBuilder;
    public final NotifCollection mNotifCollection;
    public final NotifInflaterImpl mNotifInflater;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public final NotifCoordinators mNotifPluggableCoordinators;
    public final NotifPipeline mPipelineWrapper;
    public final RenderStageManager mRenderStageManager;
    public final ShadeViewManagerFactory mShadeViewManagerFactory;

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.mNotifPluggableCoordinators.dump(fileDescriptor, printWriter, strArr);
        GroupCoalescer groupCoalescer = this.mGroupCoalescer;
        Objects.requireNonNull(groupCoalescer);
        long uptimeMillis = groupCoalescer.mClock.uptimeMillis();
        printWriter.println();
        printWriter.println("Coalesced notifications:");
        int i = 0;
        for (EventBatch eventBatch : groupCoalescer.mBatches.values()) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("   Batch ");
            m.append(eventBatch.mGroupKey);
            m.append(":");
            printWriter.println(m.toString());
            printWriter.println("       Created " + (uptimeMillis - eventBatch.mCreatedTimestamp) + "ms ago");
            Iterator it = eventBatch.mMembers.iterator();
            while (it.hasNext()) {
                CoalescedEvent coalescedEvent = (CoalescedEvent) it.next();
                StringBuilder sb = new StringBuilder();
                sb.append("       ");
                Objects.requireNonNull(coalescedEvent);
                sb.append(coalescedEvent.key);
                printWriter.println(sb.toString());
                i++;
            }
        }
        if (i != groupCoalescer.mCoalescedEvents.size()) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("    ERROR: batches contain ");
            m2.append(groupCoalescer.mCoalescedEvents.size());
            m2.append(" events but am tracking ");
            m2.append(groupCoalescer.mCoalescedEvents.size());
            m2.append(" total events");
            printWriter.println(m2.toString());
            printWriter.println("    All tracked events:");
            for (CoalescedEvent coalescedEvent2 : groupCoalescer.mCoalescedEvents.values()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("        ");
                Objects.requireNonNull(coalescedEvent2);
                sb2.append(coalescedEvent2.key);
                printWriter.println(sb2.toString());
            }
        }
    }

    public NotifPipelineInitializer(NotifPipeline notifPipeline, GroupCoalescer groupCoalescer, NotifCollection notifCollection, ShadeListBuilder shadeListBuilder, RenderStageManager renderStageManager, NotifCoordinators notifCoordinators, NotifInflaterImpl notifInflaterImpl, DumpManager dumpManager, ShadeViewManagerFactory shadeViewManagerFactory, NotifPipelineFlags notifPipelineFlags) {
        this.mPipelineWrapper = notifPipeline;
        this.mGroupCoalescer = groupCoalescer;
        this.mNotifCollection = notifCollection;
        this.mListBuilder = shadeListBuilder;
        this.mRenderStageManager = renderStageManager;
        this.mNotifPluggableCoordinators = notifCoordinators;
        this.mDumpManager = dumpManager;
        this.mNotifInflater = notifInflaterImpl;
        this.mShadeViewManagerFactory = shadeViewManagerFactory;
        this.mNotifPipelineFlags = notifPipelineFlags;
    }
}
