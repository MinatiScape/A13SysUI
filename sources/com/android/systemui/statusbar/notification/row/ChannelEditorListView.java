package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;
/* compiled from: ChannelEditorListView.kt */
/* loaded from: classes.dex */
public final class ChannelEditorListView extends LinearLayout {
    public AppControlView appControlRow;
    public Drawable appIcon;
    public String appName;
    public ChannelEditorDialogController controller;
    public List<NotificationChannel> channels = new ArrayList();
    public final ArrayList channelRows = new ArrayList();

    /* JADX WARN: Removed duplicated region for block: B:81:0x0140  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0147  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0149  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateRows() {
        /*
            Method dump skipped, instructions count: 344
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.ChannelEditorListView.updateRows():void");
    }

    public ChannelEditorListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.appControlRow = (AppControlView) findViewById(2131427498);
    }
}
