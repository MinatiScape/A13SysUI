package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.ArraySet;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.NotifCollection$$ExternalSyntheticLambda2;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewListener;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewManager;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
/* compiled from: GutsCoordinator.kt */
/* loaded from: classes.dex */
public final class GutsCoordinator implements Coordinator, Dumpable {
    public final GutsCoordinatorLogger logger;
    public final NotifGutsViewManager notifGutsViewManager;
    public NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback;
    public final ArraySet<String> notifsWithOpenGuts = new ArraySet<>();
    public final ArraySet<String> notifsExtendingLifetime = new ArraySet<>();
    public final GutsCoordinator$mLifetimeExtender$1 mLifetimeExtender = new NotifLifetimeExtender() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinator$mLifetimeExtender$1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender
        public final String getName() {
            return "GutsCoordinator";
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender
        public final void cancelLifetimeExtension(NotificationEntry notificationEntry) {
            GutsCoordinator.this.notifsExtendingLifetime.remove(notificationEntry.mKey);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender
        public final boolean maybeExtendLifetime(NotificationEntry notificationEntry, int i) {
            GutsCoordinator gutsCoordinator = GutsCoordinator.this;
            Objects.requireNonNull(gutsCoordinator);
            ArraySet<String> arraySet = gutsCoordinator.notifsWithOpenGuts;
            Objects.requireNonNull(notificationEntry);
            boolean contains = arraySet.contains(notificationEntry.mKey);
            if (contains) {
                GutsCoordinator.this.notifsExtendingLifetime.add(notificationEntry.mKey);
            }
            return contains;
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender
        public final void setCallback(NotifCollection$$ExternalSyntheticLambda2 notifCollection$$ExternalSyntheticLambda2) {
            GutsCoordinator.this.onEndLifetimeExtensionCallback = notifCollection$$ExternalSyntheticLambda2;
        }
    };
    public final GutsCoordinator$mGutsListener$1 mGutsListener = new NotifGutsViewListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinator$mGutsListener$1
        @Override // com.android.systemui.statusbar.notification.collection.render.NotifGutsViewListener
        public final void onGutsClose(NotificationEntry notificationEntry) {
            GutsCoordinatorLogger gutsCoordinatorLogger = GutsCoordinator.this.logger;
            String str = notificationEntry.mKey;
            Objects.requireNonNull(gutsCoordinatorLogger);
            LogBuffer logBuffer = gutsCoordinatorLogger.buffer;
            LogLevel logLevel = LogLevel.DEBUG;
            GutsCoordinatorLogger$logGutsClosed$2 gutsCoordinatorLogger$logGutsClosed$2 = GutsCoordinatorLogger$logGutsClosed$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("GutsCoordinator", logLevel, gutsCoordinatorLogger$logGutsClosed$2);
                obtain.str1 = str;
                logBuffer.push(obtain);
            }
            GutsCoordinator.access$closeGutsAndEndLifetimeExtension(GutsCoordinator.this, notificationEntry);
        }

        @Override // com.android.systemui.statusbar.notification.collection.render.NotifGutsViewListener
        public final void onGutsOpen(NotificationEntry notificationEntry, NotificationGuts notificationGuts) {
            boolean z;
            GutsCoordinatorLogger gutsCoordinatorLogger = GutsCoordinator.this.logger;
            String str = notificationEntry.mKey;
            Objects.requireNonNull(gutsCoordinatorLogger);
            LogBuffer logBuffer = gutsCoordinatorLogger.buffer;
            LogLevel logLevel = LogLevel.DEBUG;
            GutsCoordinatorLogger$logGutsOpened$2 gutsCoordinatorLogger$logGutsOpened$2 = GutsCoordinatorLogger$logGutsOpened$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            boolean z2 = true;
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("GutsCoordinator", logLevel, gutsCoordinatorLogger$logGutsOpened$2);
                obtain.str1 = str;
                obtain.str2 = Reflection.getOrCreateKotlinClass(notificationGuts.mGutsContent.getClass()).getSimpleName();
                NotificationGuts.GutsContent gutsContent = notificationGuts.mGutsContent;
                if (gutsContent == null || !gutsContent.isLeavebehind()) {
                    z = false;
                } else {
                    z = true;
                }
                obtain.bool1 = z;
                logBuffer.push(obtain);
            }
            Objects.requireNonNull(notificationGuts);
            NotificationGuts.GutsContent gutsContent2 = notificationGuts.mGutsContent;
            if (gutsContent2 == null || !gutsContent2.isLeavebehind()) {
                z2 = false;
            }
            if (z2) {
                GutsCoordinator.access$closeGutsAndEndLifetimeExtension(GutsCoordinator.this, notificationEntry);
            } else {
                GutsCoordinator.this.notifsWithOpenGuts.add(notificationEntry.mKey);
            }
        }
    };

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        this.notifGutsViewManager.setGutsListener(this.mGutsListener);
        notifPipeline.addNotificationLifetimeExtender(this.mLifetimeExtender);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("  notifsWithOpenGuts: ", Integer.valueOf(this.notifsWithOpenGuts.size())));
        Iterator<String> it = this.notifsWithOpenGuts.iterator();
        while (it.hasNext()) {
            printWriter.println(Intrinsics.stringPlus("   * ", it.next()));
        }
        printWriter.println(Intrinsics.stringPlus("  notifsExtendingLifetime: ", Integer.valueOf(this.notifsExtendingLifetime.size())));
        Iterator<String> it2 = this.notifsExtendingLifetime.iterator();
        while (it2.hasNext()) {
            printWriter.println(Intrinsics.stringPlus("   * ", it2.next()));
        }
        printWriter.println(Intrinsics.stringPlus("  onEndLifetimeExtensionCallback: ", this.onEndLifetimeExtensionCallback));
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinator$mLifetimeExtender$1] */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinator$mGutsListener$1] */
    public GutsCoordinator(NotifGutsViewManager notifGutsViewManager, GutsCoordinatorLogger gutsCoordinatorLogger, DumpManager dumpManager) {
        this.notifGutsViewManager = notifGutsViewManager;
        this.logger = gutsCoordinatorLogger;
        dumpManager.registerDumpable("GutsCoordinator", this);
    }

    public static final void access$closeGutsAndEndLifetimeExtension(GutsCoordinator gutsCoordinator, NotificationEntry notificationEntry) {
        NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback;
        Objects.requireNonNull(gutsCoordinator);
        ArraySet<String> arraySet = gutsCoordinator.notifsWithOpenGuts;
        Objects.requireNonNull(notificationEntry);
        arraySet.remove(notificationEntry.mKey);
        if (gutsCoordinator.notifsExtendingLifetime.remove(notificationEntry.mKey) && (onEndLifetimeExtensionCallback = gutsCoordinator.onEndLifetimeExtensionCallback) != null) {
            ((NotifCollection$$ExternalSyntheticLambda2) onEndLifetimeExtensionCallback).onEndLifetimeExtension(gutsCoordinator.mLifetimeExtender, notificationEntry);
        }
    }
}
