package com.android.systemui.media;

import android.content.Context;
import android.content.res.Configuration;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.android.systemui.media.MediaHost;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.stack.MediaContainerView;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.Utils;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
/* compiled from: KeyguardMediaController.kt */
/* loaded from: classes.dex */
public final class KeyguardMediaController {
    public final KeyguardBypassController bypassController;
    public final Context context;
    public final MediaHost mediaHost;
    public final NotificationLockscreenUserManager notifLockscreenUserManager;
    public MediaContainerView singlePaneContainer;
    public ViewGroup splitShadeContainer;
    public final SysuiStatusBarStateController statusBarStateController;
    public boolean useSplitShade;
    public Function1<? super Boolean, Unit> visibilityChangedListener;

    public static /* synthetic */ void getUseSplitShade$annotations() {
    }

    public final void attachSinglePaneContainer(MediaContainerView mediaContainerView) {
        boolean z;
        if (this.singlePaneContainer == null) {
            z = true;
        } else {
            z = false;
        }
        this.singlePaneContainer = mediaContainerView;
        if (z) {
            MediaHost mediaHost = this.mediaHost;
            KeyguardMediaController$attachSinglePaneContainer$1 keyguardMediaController$attachSinglePaneContainer$1 = new KeyguardMediaController$attachSinglePaneContainer$1(this);
            Objects.requireNonNull(mediaHost);
            mediaHost.visibleChangedListeners.add(keyguardMediaController$attachSinglePaneContainer$1);
        }
        reattachHostView();
        boolean visible = this.mediaHost.getVisible();
        refreshMediaPosition();
        if (visible) {
            ViewGroup.LayoutParams layoutParams = this.mediaHost.getHostView().getLayoutParams();
            layoutParams.height = -2;
            layoutParams.width = -1;
        }
    }

    public final void reattachHostView() {
        ViewGroup viewGroup;
        ViewGroup viewGroup2;
        boolean z;
        ViewGroup viewGroup3;
        if (this.useSplitShade) {
            viewGroup2 = this.splitShadeContainer;
            viewGroup = this.singlePaneContainer;
        } else {
            viewGroup = this.splitShadeContainer;
            viewGroup2 = this.singlePaneContainer;
        }
        boolean z2 = true;
        if (viewGroup != null && viewGroup.getChildCount() == 1) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            viewGroup.removeAllViews();
        }
        if (viewGroup2 == null || viewGroup2.getChildCount() != 0) {
            z2 = false;
        }
        if (z2) {
            ViewParent parent = this.mediaHost.getHostView().getParent();
            if (parent != null) {
                if (parent instanceof ViewGroup) {
                    viewGroup3 = (ViewGroup) parent;
                } else {
                    viewGroup3 = null;
                }
                if (viewGroup3 != null) {
                    viewGroup3.removeView(this.mediaHost.getHostView());
                }
            }
            viewGroup2.addView(this.mediaHost.getHostView());
        }
    }

    public final void refreshMediaPosition() {
        boolean z;
        boolean z2 = true;
        if (this.statusBarStateController.getState() == 1) {
            z = true;
        } else {
            z = false;
        }
        if (!this.mediaHost.getVisible() || this.bypassController.getBypassEnabled() || !z || !this.notifLockscreenUserManager.shouldShowLockscreenNotifications()) {
            z2 = false;
        }
        if (!z2) {
            setVisibility(this.splitShadeContainer, 8);
            setVisibility(this.singlePaneContainer, 8);
        } else if (this.useSplitShade) {
            setVisibility(this.splitShadeContainer, 0);
            setVisibility(this.singlePaneContainer, 8);
        } else {
            setVisibility(this.singlePaneContainer, 0);
            setVisibility(this.splitShadeContainer, 8);
        }
    }

    public final void setVisibility(ViewGroup viewGroup, int i) {
        Integer num;
        Function1<? super Boolean, Unit> function1;
        boolean z;
        if (viewGroup == null) {
            num = null;
        } else {
            num = Integer.valueOf(viewGroup.getVisibility());
        }
        if (viewGroup != null) {
            viewGroup.setVisibility(i);
        }
        if ((num == null || num.intValue() != i) && (function1 = this.visibilityChangedListener) != null) {
            if (i == 0) {
                z = true;
            } else {
                z = false;
            }
            function1.invoke(Boolean.valueOf(z));
        }
    }

    public KeyguardMediaController(MediaHost mediaHost, KeyguardBypassController keyguardBypassController, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationLockscreenUserManager notificationLockscreenUserManager, Context context, ConfigurationController configurationController, MediaFlags mediaFlags) {
        float f;
        this.mediaHost = mediaHost;
        this.bypassController = keyguardBypassController;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.notifLockscreenUserManager = notificationLockscreenUserManager;
        this.context = context;
        sysuiStatusBarStateController.addCallback(new StatusBarStateController.StateListener() { // from class: com.android.systemui.media.KeyguardMediaController.1
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public final void onStateChanged(int i) {
                KeyguardMediaController.this.refreshMediaPosition();
            }
        });
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.media.KeyguardMediaController.2
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public final void onConfigChanged(Configuration configuration) {
                KeyguardMediaController keyguardMediaController = KeyguardMediaController.this;
                Objects.requireNonNull(keyguardMediaController);
                boolean shouldUseSplitNotificationShade = Utils.shouldUseSplitNotificationShade(keyguardMediaController.context.getResources());
                if (keyguardMediaController.useSplitShade != shouldUseSplitNotificationShade) {
                    keyguardMediaController.useSplitShade = shouldUseSplitNotificationShade;
                    keyguardMediaController.reattachHostView();
                    keyguardMediaController.refreshMediaPosition();
                }
            }
        });
        if (mediaFlags.useMediaSessionLayout()) {
            f = 1.0f;
        } else {
            f = 0.0f;
        }
        mediaHost.setExpansion(f);
        MediaHost.MediaHostStateHolder mediaHostStateHolder = mediaHost.state;
        Objects.requireNonNull(mediaHostStateHolder);
        if (!Boolean.TRUE.equals(Boolean.valueOf(mediaHostStateHolder.showsOnlyActiveMedia))) {
            mediaHostStateHolder.showsOnlyActiveMedia = true;
            Function0<Unit> function0 = mediaHostStateHolder.changedListener;
            if (function0 != null) {
                function0.invoke();
            }
        }
        MediaHost.MediaHostStateHolder mediaHostStateHolder2 = mediaHost.state;
        Objects.requireNonNull(mediaHostStateHolder2);
        if (!mediaHostStateHolder2.falsingProtectionNeeded) {
            mediaHostStateHolder2.falsingProtectionNeeded = true;
            Function0<Unit> function02 = mediaHostStateHolder2.changedListener;
            if (function02 != null) {
                function02.invoke();
            }
        }
        mediaHost.init(2);
        boolean shouldUseSplitNotificationShade = Utils.shouldUseSplitNotificationShade(context.getResources());
        if (this.useSplitShade != shouldUseSplitNotificationShade) {
            this.useSplitShade = shouldUseSplitNotificationShade;
            reattachHostView();
            refreshMediaPosition();
        }
    }
}
