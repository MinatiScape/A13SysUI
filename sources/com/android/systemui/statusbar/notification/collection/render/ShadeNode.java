package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
/* compiled from: ShadeViewDiffer.kt */
/* loaded from: classes.dex */
public final class ShadeNode {
    public final NodeController controller;
    public ShadeNode parent;
    public final View view;

    public final String getLabel() {
        return this.controller.getNodeLabel();
    }

    public ShadeNode(NodeController nodeController) {
        this.controller = nodeController;
        this.view = nodeController.getView();
    }
}
