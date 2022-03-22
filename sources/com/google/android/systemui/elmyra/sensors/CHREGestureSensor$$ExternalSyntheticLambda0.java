package com.google.android.systemui.elmyra.sensors;

import android.view.MotionEvent;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.listbuilder.PipelineState;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable;
import com.android.systemui.util.Assert;
import com.google.android.systemui.elmyra.SnapshotController;
import com.google.android.systemui.smartspace.InterceptingViewPager;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CHREGestureSensor$$ExternalSyntheticLambda0 implements Pluggable.PluggableListener, SnapshotController.Listener, InterceptingViewPager.EventProxy {
    public final /* synthetic */ Object f$0;

    @Override // com.google.android.systemui.smartspace.InterceptingViewPager.EventProxy
    public final boolean delegateEvent(MotionEvent motionEvent) {
        return Objects.requireNonNull((InterceptingViewPager) this.f$0);
    }

    public /* synthetic */ CHREGestureSensor$$ExternalSyntheticLambda0(Object obj) {
        this.f$0 = obj;
    }

    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable.PluggableListener
    public final void onPluggableInvalidated(Object obj) {
        ShadeListBuilder shadeListBuilder = (ShadeListBuilder) this.f$0;
        NotifPromoter notifPromoter = (NotifPromoter) obj;
        Objects.requireNonNull(shadeListBuilder);
        Assert.isMainThread();
        ShadeListBuilderLogger shadeListBuilderLogger = shadeListBuilder.mLogger;
        Objects.requireNonNull(notifPromoter);
        String str = notifPromoter.mName;
        PipelineState pipelineState = shadeListBuilder.mPipelineState;
        Objects.requireNonNull(pipelineState);
        shadeListBuilderLogger.logPromoterInvalidated(str, pipelineState.mState);
        shadeListBuilder.rebuildListIfBefore(5);
    }
}
