package com.android.keyguard.clock;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import com.android.keyguard.clock.ClockManager;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.media.dialog.MediaOutputController;
import com.android.systemui.plugins.ClockPlugin;
import com.android.systemui.screenshot.DeleteScreenshotReceiver;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ClockManager$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ ClockManager$$ExternalSyntheticLambda1(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ClockPlugin clockPlugin = null;
        switch (this.$r8$classId) {
            case 0:
                ClockManager.ClockChangedListener clockChangedListener = (ClockManager.ClockChangedListener) this.f$0;
                ClockPlugin clockPlugin2 = (ClockPlugin) this.f$1;
                if (!(clockPlugin2 instanceof DefaultClockController)) {
                    clockPlugin = clockPlugin2;
                }
                clockChangedListener.onClockChanged(clockPlugin);
                return;
            case 1:
                MediaOutputController mediaOutputController = (MediaOutputController) this.f$0;
                boolean z = MediaOutputController.DEBUG;
                Objects.requireNonNull(mediaOutputController);
                mediaOutputController.mLocalMediaManager.connectDevice((MediaDevice) this.f$1);
                return;
            case 2:
                int i = DeleteScreenshotReceiver.$r8$clinit;
                ((Context) this.f$0).getContentResolver().delete((Uri) this.f$1, null, null);
                return;
            case 3:
                VisualStabilityCoordinator visualStabilityCoordinator = (VisualStabilityCoordinator) this.f$0;
                Objects.requireNonNull(visualStabilityCoordinator);
                visualStabilityCoordinator.mEntriesThatCanChangeSection.remove((String) this.f$1);
                return;
            default:
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) this.f$0;
                ExpandableNotificationRow.AnonymousClass2 r1 = ExpandableNotificationRow.TRANSLATE_CONTENT;
                Objects.requireNonNull(expandableNotificationRow);
                ((View) this.f$1).setVisibility(4);
                expandableNotificationRow.resetAllContentAlphas();
                return;
        }
    }
}
