package com.android.systemui.statusbar.notification.collection.render;

import java.util.ArrayList;
/* compiled from: NodeController.kt */
/* loaded from: classes.dex */
public final class NodeSpecImpl implements NodeSpec {
    public final ArrayList children = new ArrayList();
    public final NodeController controller;
    public final NodeSpec parent;

    public NodeSpecImpl(NodeSpecImpl nodeSpecImpl, NodeController nodeController) {
        this.parent = nodeSpecImpl;
        this.controller = nodeController;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeSpec
    public final ArrayList getChildren() {
        return this.children;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeSpec
    public final NodeController getController() {
        return this.controller;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeSpec
    public final NodeSpec getParent() {
        return this.parent;
    }
}
