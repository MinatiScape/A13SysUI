package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
/* compiled from: NotificationSectionHeadersModule.kt */
/* loaded from: classes.dex */
public interface SectionHeaderControllerSubcomponent {

    /* compiled from: NotificationSectionHeadersModule.kt */
    /* loaded from: classes.dex */
    public interface Builder {
        SectionHeaderControllerSubcomponent build();

        /* renamed from: clickIntentAction */
        Builder mo141clickIntentAction(String str);

        Builder headerText(int i);

        /* renamed from: nodeLabel */
        Builder mo142nodeLabel(String str);
    }

    SectionHeaderController getHeaderController();

    NodeController getNodeController();
}
