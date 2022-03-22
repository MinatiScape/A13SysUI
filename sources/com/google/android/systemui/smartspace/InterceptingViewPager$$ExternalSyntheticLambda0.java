package com.google.android.systemui.smartspace;

import android.view.MotionEvent;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.listbuilder.PipelineState;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable;
import com.android.systemui.util.Assert;
import com.google.android.systemui.smartspace.InterceptingViewPager;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class InterceptingViewPager$$ExternalSyntheticLambda0 implements Pluggable.PluggableListener, InterceptingViewPager.EventProxy {
    public final /* synthetic */ Object f$0;

    @Override // com.google.android.systemui.smartspace.InterceptingViewPager.EventProxy
    public final boolean delegateEvent(MotionEvent motionEvent) {
        return Objects.requireNonNull((InterceptingViewPager) this.f$0);
    }

    public /* synthetic */ InterceptingViewPager$$ExternalSyntheticLambda0(Object obj) {
        this.f$0 = obj;
    }

    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable.PluggableListener
    public final void onPluggableInvalidated(Object obj) {
        ShadeListBuilder shadeListBuilder = (ShadeListBuilder) this.f$0;
        NotifComparator notifComparator = (NotifComparator) obj;
        Objects.requireNonNull(shadeListBuilder);
        Assert.isMainThread();
        ShadeListBuilderLogger shadeListBuilderLogger = shadeListBuilder.mLogger;
        Objects.requireNonNull(notifComparator);
        String str = notifComparator.mName;
        PipelineState pipelineState = shadeListBuilder.mPipelineState;
        Objects.requireNonNull(pipelineState);
        shadeListBuilderLogger.logNotifComparatorInvalidated(str, pipelineState.mState);
        shadeListBuilder.rebuildListIfBefore(7);
    }
}
