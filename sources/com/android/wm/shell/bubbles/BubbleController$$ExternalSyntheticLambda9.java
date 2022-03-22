package com.android.wm.shell.bubbles;

import android.net.Uri;
import android.util.Range;
import android.view.GestureDetector;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda0;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda4;
import com.android.wm.shell.recents.RecentTasksController;
import com.google.android.systemui.elmyra.actions.DeskClockAction;
import com.google.android.systemui.elmyra.sensors.config.Adjustment;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda9 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda9(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                BubbleController bubbleController = (BubbleController) this.f$0;
                Objects.requireNonNull(bubbleController);
                bubbleController.mMainExecutor.execute(new PipTaskOrganizer$$ExternalSyntheticLambda4(bubbleController, (List) obj, 4));
                return;
            case 1:
                ((Consumer) this.f$0).accept((GestureDetector.OnGestureListener) obj);
                return;
            case 2:
                NavigationBarView navigationBarView = (NavigationBarView) this.f$0;
                int i = NavigationBarView.$r8$clinit;
                Objects.requireNonNull(navigationBarView);
                navigationBarView.post(new NavigationBarView$$ExternalSyntheticLambda0(navigationBarView, (Boolean) obj, 0));
                return;
            case 3:
                RecentTasksController.IRecentTasksImpl iRecentTasksImpl = (RecentTasksController.IRecentTasksImpl) this.f$0;
                RecentTasksController recentTasksController = (RecentTasksController) obj;
                int i2 = RecentTasksController.IRecentTasksImpl.$r8$clinit;
                Objects.requireNonNull(iRecentTasksImpl);
                RecentTasksController.IRecentTasksImpl.AnonymousClass1 r3 = iRecentTasksImpl.mRecentTasksListener;
                Objects.requireNonNull(recentTasksController);
                if (!recentTasksController.mCallbacks.contains(r3)) {
                    recentTasksController.mCallbacks.add(r3);
                    return;
                }
                return;
            case 4:
                DeskClockAction deskClockAction = (DeskClockAction) this.f$0;
                Uri uri = (Uri) obj;
                Objects.requireNonNull(deskClockAction);
                deskClockAction.updateBroadcastReceiver();
                return;
            default:
                GestureConfiguration gestureConfiguration = (GestureConfiguration) this.f$0;
                Adjustment adjustment = (Adjustment) obj;
                Range<Float> range = GestureConfiguration.SENSITIVITY_RANGE;
                Objects.requireNonNull(gestureConfiguration);
                gestureConfiguration.mSensitivity = gestureConfiguration.getUserSensitivity();
                GestureConfiguration.Listener listener = gestureConfiguration.mListener;
                if (listener != null) {
                    listener.onGestureConfigurationChanged(gestureConfiguration);
                    return;
                }
                return;
        }
    }
}
