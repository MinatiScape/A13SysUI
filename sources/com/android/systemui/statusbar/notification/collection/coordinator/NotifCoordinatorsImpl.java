package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.Flags;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifCoordinators.kt */
/* loaded from: classes.dex */
public final class NotifCoordinatorsImpl implements NotifCoordinators {
    public final ArrayList mCoordinators;
    public final ArrayList mOrderedSections;

    public NotifCoordinatorsImpl(DumpManager dumpManager, NotifPipelineFlags notifPipelineFlags, DataStoreCoordinator dataStoreCoordinator, HideLocallyDismissedNotifsCoordinator hideLocallyDismissedNotifsCoordinator, HideNotifsForOtherUsersCoordinator hideNotifsForOtherUsersCoordinator, KeyguardCoordinator keyguardCoordinator, RankingCoordinator rankingCoordinator, AppOpsCoordinator appOpsCoordinator, DeviceProvisionedCoordinator deviceProvisionedCoordinator, BubbleCoordinator bubbleCoordinator, HeadsUpCoordinator headsUpCoordinator, GutsCoordinator gutsCoordinator, CommunalCoordinator communalCoordinator, ConversationCoordinator conversationCoordinator, DebugModeCoordinator debugModeCoordinator, GroupCountCoordinator groupCountCoordinator, MediaCoordinator mediaCoordinator, PreparationCoordinator preparationCoordinator, RemoteInputCoordinator remoteInputCoordinator, RowAppearanceCoordinator rowAppearanceCoordinator, StackCoordinator stackCoordinator, ShadeEventCoordinator shadeEventCoordinator, SmartspaceDedupingCoordinator smartspaceDedupingCoordinator, ViewConfigCoordinator viewConfigCoordinator, VisualStabilityCoordinator visualStabilityCoordinator, SensitiveContentCoordinator sensitiveContentCoordinator) {
        boolean z;
        ArrayList arrayList = new ArrayList();
        this.mCoordinators = arrayList;
        ArrayList arrayList2 = new ArrayList();
        this.mOrderedSections = arrayList2;
        dumpManager.registerDumpable("NotifCoordinators", this);
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            arrayList.add(dataStoreCoordinator);
        }
        arrayList.add(hideLocallyDismissedNotifsCoordinator);
        arrayList.add(hideNotifsForOtherUsersCoordinator);
        arrayList.add(keyguardCoordinator);
        arrayList.add(rankingCoordinator);
        arrayList.add(appOpsCoordinator);
        arrayList.add(deviceProvisionedCoordinator);
        arrayList.add(bubbleCoordinator);
        arrayList.add(communalCoordinator);
        arrayList.add(debugModeCoordinator);
        arrayList.add(conversationCoordinator);
        arrayList.add(groupCountCoordinator);
        arrayList.add(mediaCoordinator);
        arrayList.add(rowAppearanceCoordinator);
        arrayList.add(stackCoordinator);
        arrayList.add(shadeEventCoordinator);
        arrayList.add(viewConfigCoordinator);
        arrayList.add(visualStabilityCoordinator);
        arrayList.add(sensitiveContentCoordinator);
        if (!notifPipelineFlags.featureFlags.isEnabled(Flags.SMARTSPACE) || !notifPipelineFlags.featureFlags.isEnabled(Flags.SMARTSPACE_DEDUPING)) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            arrayList.add(smartspaceDedupingCoordinator);
        }
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            arrayList.add(headsUpCoordinator);
            arrayList.add(gutsCoordinator);
            arrayList.add(preparationCoordinator);
            arrayList.add(remoteInputCoordinator);
        }
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            arrayList2.add(headsUpCoordinator.sectioner);
        }
        arrayList2.add(appOpsCoordinator.mNotifSectioner);
        arrayList2.add(conversationCoordinator.sectioner);
        arrayList2.add(rankingCoordinator.mAlertingNotifSectioner);
        arrayList2.add(rankingCoordinator.mSilentNotifSectioner);
        arrayList2.add(rankingCoordinator.mMinimizedNotifSectioner);
    }

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        Iterator it = this.mCoordinators.iterator();
        while (it.hasNext()) {
            ((Coordinator) it.next()).attach(notifPipeline);
        }
        notifPipeline.mShadeListBuilder.setSectioners(this.mOrderedSections);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println();
        printWriter.println("NotifCoordinators:");
        Iterator it = this.mCoordinators.iterator();
        while (it.hasNext()) {
            printWriter.println(Intrinsics.stringPlus("\t", ((Coordinator) it.next()).getClass()));
        }
        Iterator it2 = this.mOrderedSections.iterator();
        while (it2.hasNext()) {
            NotifSectioner notifSectioner = (NotifSectioner) it2.next();
            Objects.requireNonNull(notifSectioner);
            printWriter.println(Intrinsics.stringPlus("\t", notifSectioner.mName));
        }
    }
}
