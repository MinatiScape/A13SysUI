package com.android.systemui.statusbar.phone.userswitcher;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/* compiled from: StatusBarUserSwitcherContainer.kt */
/* loaded from: classes.dex */
public final class StatusBarUserSwitcherContainer extends LinearLayout {
    public ImageView avatar;
    public TextView text;

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.text = (TextView) findViewById(2131427788);
        this.avatar = (ImageView) findViewById(2131427787);
    }

    public StatusBarUserSwitcherContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
