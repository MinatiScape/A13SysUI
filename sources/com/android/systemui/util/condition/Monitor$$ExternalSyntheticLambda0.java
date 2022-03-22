package com.android.systemui.util.condition;

import android.content.Context;
import com.android.systemui.ImageWallpaper;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.condition.Condition;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.phone.PipController;
import com.android.wm.shell.pip.phone.PipResizeGestureHandler;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class Monitor$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ Monitor$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                Monitor monitor = (Monitor) this.f$0;
                Condition condition = (Condition) this.f$1;
                Objects.requireNonNull(monitor);
                monitor.mConditions.add(condition);
                if (monitor.mHaveConditionsStarted) {
                    condition.addCallback((Condition.Callback) monitor.mConditionCallback);
                    monitor.updateConditionMetState();
                    return;
                }
                return;
            case 1:
                ImageWallpaper.GLEngine gLEngine = (ImageWallpaper.GLEngine) this.f$0;
                List list = (List) this.f$1;
                int i = ImageWallpaper.GLEngine.MIN_SURFACE_WIDTH;
                Objects.requireNonNull(gLEngine);
                ImageWallpaper.this.mColorAreas.removeAll(list);
                ImageWallpaper.this.mLocalColorsToAdd.removeAll(list);
                if (ImageWallpaper.this.mLocalColorsToAdd.size() + ImageWallpaper.this.mColorAreas.size() == 0) {
                    gLEngine.setOffsetNotificationsEnabled(false);
                    return;
                }
                return;
            case 2:
                UserSwitcherController userSwitcherController = (UserSwitcherController) this.f$0;
                ArrayList<UserSwitcherController.UserRecord> arrayList = (ArrayList) this.f$1;
                if (arrayList != null) {
                    userSwitcherController.mUsers = arrayList;
                    userSwitcherController.notifyAdapters();
                    return;
                }
                Objects.requireNonNull(userSwitcherController);
                return;
            case 3:
                BubblesManager.AnonymousClass5 r0 = (BubblesManager.AnonymousClass5) this.f$0;
                Objects.requireNonNull(r0);
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry((String) this.f$1);
                if (entry != null && entry.getImportance() >= 4) {
                    entry.interruption = true;
                    return;
                }
                return;
            default:
                PipController.PipImpl pipImpl = (PipController.PipImpl) this.f$0;
                Objects.requireNonNull(pipImpl);
                PipController pipController = PipController.this;
                Objects.requireNonNull(pipController);
                PipBoundsAlgorithm pipBoundsAlgorithm = pipController.mPipBoundsAlgorithm;
                Context context = pipController.mContext;
                Objects.requireNonNull(pipBoundsAlgorithm);
                pipBoundsAlgorithm.reloadResources(context);
                PipTouchHandler pipTouchHandler = pipController.mTouchHandler;
                Objects.requireNonNull(pipTouchHandler);
                PipResizeGestureHandler pipResizeGestureHandler = pipTouchHandler.mPipResizeGestureHandler;
                Objects.requireNonNull(pipResizeGestureHandler);
                pipResizeGestureHandler.reloadResources();
                pipTouchHandler.mMotionHelper.synchronizePinnedStackBounds();
                pipTouchHandler.reloadResources();
                pipTouchHandler.mPipDismissTargetHandler.createOrUpdateDismissTarget();
                PipBoundsState pipBoundsState = pipController.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState);
                pipBoundsState.mStashOffset = pipBoundsState.mContext.getResources().getDimensionPixelSize(2131166804);
                return;
        }
    }
}
