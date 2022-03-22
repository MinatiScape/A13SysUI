package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.listbuilder.PipelineState;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Invalidator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable;
import com.android.systemui.statusbar.phone.FoldStateListener;
import com.android.systemui.util.Assert;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda8 implements Pluggable.PluggableListener, FoldStateListener.OnFoldStateChangeListener {
    public final /* synthetic */ Object f$0;

    public /* synthetic */ StatusBar$$ExternalSyntheticLambda8(Object obj) {
        this.f$0 = obj;
    }

    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable.PluggableListener
    public final void onPluggableInvalidated(Object obj) {
        ShadeListBuilder shadeListBuilder = (ShadeListBuilder) this.f$0;
        Invalidator invalidator = (Invalidator) obj;
        Objects.requireNonNull(shadeListBuilder);
        Assert.isMainThread();
        ShadeListBuilderLogger shadeListBuilderLogger = shadeListBuilder.mLogger;
        Objects.requireNonNull(invalidator);
        String str = invalidator.mName;
        PipelineState pipelineState = shadeListBuilder.mPipelineState;
        Objects.requireNonNull(pipelineState);
        shadeListBuilderLogger.logPreRenderInvalidated(str, pipelineState.mState);
        shadeListBuilder.rebuildListIfBefore(9);
    }
}
