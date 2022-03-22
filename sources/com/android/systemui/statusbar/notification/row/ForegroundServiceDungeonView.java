package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
/* compiled from: ForegroundServiceDungeonView.kt */
/* loaded from: classes.dex */
public final class ForegroundServiceDungeonView extends StackScrollerDecorView {
    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    public final View findSecondaryView() {
        return null;
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    public final void setVisible(boolean z, boolean z2) {
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    public final View findContentView() {
        return findViewById(2131427988);
    }

    public ForegroundServiceDungeonView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
