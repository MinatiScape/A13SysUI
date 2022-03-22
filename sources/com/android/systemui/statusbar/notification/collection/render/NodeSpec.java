package com.android.systemui.statusbar.notification.collection.render;

import java.util.ArrayList;
/* compiled from: NodeController.kt */
/* loaded from: classes.dex */
public interface NodeSpec {
    ArrayList getChildren();

    NodeController getController();

    NodeSpec getParent();
}
