package com.android.systemui.dagger;

import android.content.Context;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.WMComponent;
/* loaded from: classes.dex */
public interface GlobalRootComponent {

    /* loaded from: classes.dex */
    public interface Builder {
        GlobalRootComponent build();

        /* renamed from: context */
        Builder mo119context(Context context);
    }

    /* renamed from: getSysUIComponent */
    SysUIComponent.Builder mo117getSysUIComponent();

    /* renamed from: getWMComponentBuilder */
    WMComponent.Builder mo118getWMComponentBuilder();
}
