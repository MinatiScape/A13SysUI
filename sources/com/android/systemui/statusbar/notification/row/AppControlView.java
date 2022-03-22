package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import java.util.Objects;
/* compiled from: ChannelEditorListView.kt */
/* loaded from: classes.dex */
public final class AppControlView extends LinearLayout {
    public TextView channelName;
    public ImageView iconView;

    /* renamed from: switch  reason: not valid java name */
    public Switch f6switch;

    @Override // android.view.View
    public final void onFinishInflate() {
        this.iconView = (ImageView) findViewById(2131428102);
        this.channelName = (TextView) findViewById(2131427505);
        this.f6switch = (Switch) findViewById(2131429065);
        setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.AppControlView$onFinishInflate$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AppControlView appControlView = AppControlView.this;
                Objects.requireNonNull(appControlView);
                Switch r0 = appControlView.f6switch;
                if (r0 == null) {
                    r0 = null;
                }
                r0.toggle();
            }
        });
    }

    public AppControlView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
