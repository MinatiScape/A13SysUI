package com.android.systemui.wmshell;

import android.app.smartspace.SmartspaceTarget;
import android.graphics.Rect;
import android.os.Parcelable;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.statusbar.policy.ZenModeControllerImpl;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.compatui.CompatUIWindowManagerAbstract;
import com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection;
import com.google.android.systemui.smartspace.CardPagerAdapter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WMShell$$ExternalSyntheticLambda7 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WMShell$$ExternalSyntheticLambda7(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                ((ShellCommandHandler) obj).dump((PrintWriter) this.f$0);
                return;
            case 1:
                int i = ZenModeControllerImpl.$r8$clinit;
                Objects.requireNonNull((ZenModeController.Callback) obj);
                return;
            case 2:
                CompatUIWindowManagerAbstract compatUIWindowManagerAbstract = (CompatUIWindowManagerAbstract) obj;
                Objects.requireNonNull(compatUIWindowManagerAbstract);
                ((List) this.f$0).add(Integer.valueOf(compatUIWindowManagerAbstract.mTaskId));
                return;
            case 3:
                PipAccessibilityInteractionConnection pipAccessibilityInteractionConnection = (PipAccessibilityInteractionConnection) this.f$0;
                Rect rect = (Rect) obj;
                Objects.requireNonNull(pipAccessibilityInteractionConnection);
                pipAccessibilityInteractionConnection.mMotionHelper.synchronizePinnedStackBounds();
                pipAccessibilityInteractionConnection.mUpdateMovementBoundCallback.run();
                return;
            default:
                CardPagerAdapter cardPagerAdapter = (CardPagerAdapter) this.f$0;
                Objects.requireNonNull(cardPagerAdapter);
                SmartspaceTarget smartspaceTarget = (Parcelable) obj;
                if (smartspaceTarget.getFeatureType() == 34) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    cardPagerAdapter.mHolidayAlarmsTarget = smartspaceTarget;
                    return;
                } else {
                    cardPagerAdapter.mTargetsExcludingMediaAndHolidayAlarms.add(smartspaceTarget);
                    return;
                }
        }
    }
}
