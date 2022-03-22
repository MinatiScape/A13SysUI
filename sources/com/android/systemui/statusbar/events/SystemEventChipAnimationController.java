package com.android.systemui.statusbar.events;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.window.StatusBarWindowController;
/* compiled from: SystemEventChipAnimationController.kt */
/* loaded from: classes.dex */
public final class SystemEventChipAnimationController {
    public View animationDotView;
    public FrameLayout animationWindowView;
    public final Context context;
    public View currentAnimatedView;
    public boolean initialized;
    public final StatusBarLocationPublisher locationPublisher;
    public final StatusBarWindowController statusBarWindowController;

    public SystemEventChipAnimationController(Context context, StatusBarWindowController statusBarWindowController, StatusBarLocationPublisher statusBarLocationPublisher) {
        this.context = context;
        this.statusBarWindowController = statusBarWindowController;
        this.locationPublisher = statusBarLocationPublisher;
    }
}
