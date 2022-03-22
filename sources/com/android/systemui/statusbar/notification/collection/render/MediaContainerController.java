package com.android.systemui.statusbar.notification.collection.render;

import android.view.LayoutInflater;
import android.view.View;
import com.android.systemui.statusbar.notification.stack.MediaContainerView;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaContainerController.kt */
/* loaded from: classes.dex */
public final class MediaContainerController implements NodeController {
    public final LayoutInflater layoutInflater;
    public MediaContainerView mediaContainerView;

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final int getChildCount() {
        return 0;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final String getNodeLabel() {
        return "MediaContainer";
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void onViewAdded() {
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void onViewMoved() {
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void onViewRemoved() {
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void addChildAt(NodeController nodeController, int i) {
        throw new RuntimeException("Not supported");
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final View getChildAt(int i) {
        throw new RuntimeException("Not supported");
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final View getView() {
        MediaContainerView mediaContainerView = this.mediaContainerView;
        Intrinsics.checkNotNull(mediaContainerView);
        return mediaContainerView;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void moveChildTo(NodeController nodeController, int i) {
        throw new RuntimeException("Not supported");
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void removeChild(NodeController nodeController, boolean z) {
        throw new RuntimeException("Not supported");
    }

    public MediaContainerController(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }
}
