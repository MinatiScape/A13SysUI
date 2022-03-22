package com.android.systemui.navigationbar;

import android.view.View;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.dagger.SysUIComponent$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.notification.row.FeedbackInfo;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NavigationBar$$ExternalSyntheticLambda2 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ NavigationBar$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                NavigationBar navigationBar = (NavigationBar) this.f$0;
                Objects.requireNonNull(navigationBar);
                if (LatencyTracker.isEnabled(navigationBar.mContext)) {
                    LatencyTracker.getInstance(navigationBar.mContext).onActionStart(1);
                }
                navigationBar.mStatusBarOptionalLazy.get().ifPresent(SysUIComponent$$ExternalSyntheticLambda3.INSTANCE$1);
                navigationBar.mCommandQueue.toggleRecentApps();
                return;
            default:
                FeedbackInfo feedbackInfo = (FeedbackInfo) this.f$0;
                int i = FeedbackInfo.$r8$clinit;
                Objects.requireNonNull(feedbackInfo);
                feedbackInfo.mGutsContainer.closeControls(view, false);
                feedbackInfo.handleFeedback(true);
                return;
        }
    }
}
