package com.google.android.systemui.smartspace;

import android.content.ComponentName;
import android.content.Context;
import android.view.View;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.util.Objects;
/* compiled from: KeyguardSmartspaceController.kt */
/* loaded from: classes.dex */
public final class KeyguardSmartspaceController {
    public final FeatureFlags featureFlags;

    /* JADX WARN: Type inference failed for: r1v3, types: [com.google.android.systemui.smartspace.KeyguardMediaViewController$init$2] */
    public KeyguardSmartspaceController(Context context, FeatureFlags featureFlags, final KeyguardZenAlarmViewController keyguardZenAlarmViewController, final KeyguardMediaViewController keyguardMediaViewController) {
        this.featureFlags = featureFlags;
        if (!featureFlags.isEnabled(Flags.SMARTSPACE)) {
            context.getPackageManager().setComponentEnabledSetting(new ComponentName(ThemeOverlayApplier.SYSUI_PACKAGE, "com.google.android.systemui.keyguard.KeyguardSliceProviderGoogle"), 1, 1);
            return;
        }
        Objects.requireNonNull(keyguardZenAlarmViewController);
        keyguardZenAlarmViewController.plugin.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$init$1
            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewAttachedToWindow(View view) {
                KeyguardZenAlarmViewController keyguardZenAlarmViewController2 = KeyguardZenAlarmViewController.this;
                Objects.requireNonNull(keyguardZenAlarmViewController2);
                keyguardZenAlarmViewController2.smartspaceViews.add((BcSmartspaceDataPlugin.SmartspaceView) view);
                KeyguardZenAlarmViewController keyguardZenAlarmViewController3 = KeyguardZenAlarmViewController.this;
                Objects.requireNonNull(keyguardZenAlarmViewController3);
                if (keyguardZenAlarmViewController3.smartspaceViews.size() == 1) {
                    KeyguardZenAlarmViewController keyguardZenAlarmViewController4 = KeyguardZenAlarmViewController.this;
                    keyguardZenAlarmViewController4.zenModeController.addCallback(keyguardZenAlarmViewController4.zenModeCallback);
                    KeyguardZenAlarmViewController keyguardZenAlarmViewController5 = KeyguardZenAlarmViewController.this;
                    keyguardZenAlarmViewController5.nextAlarmController.addCallback(keyguardZenAlarmViewController5.nextAlarmCallback);
                }
                KeyguardZenAlarmViewController keyguardZenAlarmViewController6 = KeyguardZenAlarmViewController.this;
                Objects.requireNonNull(keyguardZenAlarmViewController6);
                keyguardZenAlarmViewController6.updateDnd();
                keyguardZenAlarmViewController6.updateNextAlarm();
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewDetachedFromWindow(View view) {
                KeyguardZenAlarmViewController keyguardZenAlarmViewController2 = KeyguardZenAlarmViewController.this;
                Objects.requireNonNull(keyguardZenAlarmViewController2);
                keyguardZenAlarmViewController2.smartspaceViews.remove((BcSmartspaceDataPlugin.SmartspaceView) view);
                KeyguardZenAlarmViewController keyguardZenAlarmViewController3 = KeyguardZenAlarmViewController.this;
                Objects.requireNonNull(keyguardZenAlarmViewController3);
                if (keyguardZenAlarmViewController3.smartspaceViews.isEmpty()) {
                    KeyguardZenAlarmViewController keyguardZenAlarmViewController4 = KeyguardZenAlarmViewController.this;
                    keyguardZenAlarmViewController4.zenModeController.removeCallback(keyguardZenAlarmViewController4.zenModeCallback);
                    KeyguardZenAlarmViewController keyguardZenAlarmViewController5 = KeyguardZenAlarmViewController.this;
                    keyguardZenAlarmViewController5.nextAlarmController.removeCallback(keyguardZenAlarmViewController5.nextAlarmCallback);
                }
            }
        });
        keyguardZenAlarmViewController.updateNextAlarm();
        Objects.requireNonNull(keyguardMediaViewController);
        keyguardMediaViewController.plugin.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.google.android.systemui.smartspace.KeyguardMediaViewController$init$1
            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewAttachedToWindow(View view) {
                KeyguardMediaViewController keyguardMediaViewController2 = KeyguardMediaViewController.this;
                Objects.requireNonNull(keyguardMediaViewController2);
                keyguardMediaViewController2.smartspaceView = (BcSmartspaceDataPlugin.SmartspaceView) view;
                KeyguardMediaViewController keyguardMediaViewController3 = KeyguardMediaViewController.this;
                keyguardMediaViewController3.mediaManager.addCallback(keyguardMediaViewController3.mediaListener);
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewDetachedFromWindow(View view) {
                KeyguardMediaViewController keyguardMediaViewController2 = KeyguardMediaViewController.this;
                Objects.requireNonNull(keyguardMediaViewController2);
                keyguardMediaViewController2.smartspaceView = null;
                KeyguardMediaViewController keyguardMediaViewController3 = KeyguardMediaViewController.this;
                NotificationMediaManager notificationMediaManager = keyguardMediaViewController3.mediaManager;
                KeyguardMediaViewController$mediaListener$1 keyguardMediaViewController$mediaListener$1 = keyguardMediaViewController3.mediaListener;
                Objects.requireNonNull(notificationMediaManager);
                notificationMediaManager.mMediaListeners.remove(keyguardMediaViewController$mediaListener$1);
            }
        });
        final BroadcastDispatcher broadcastDispatcher = keyguardMediaViewController.broadcastDispatcher;
        keyguardMediaViewController.userTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.google.android.systemui.smartspace.KeyguardMediaViewController$init$2
            @Override // com.android.systemui.settings.CurrentUserTracker
            public final void onUserSwitched(int i) {
                KeyguardMediaViewController keyguardMediaViewController2 = KeyguardMediaViewController.this;
                Objects.requireNonNull(keyguardMediaViewController2);
                keyguardMediaViewController2.title = null;
                keyguardMediaViewController2.artist = null;
                BcSmartspaceDataPlugin.SmartspaceView smartspaceView = keyguardMediaViewController2.smartspaceView;
                if (smartspaceView != null) {
                    smartspaceView.setMediaTarget(null);
                }
            }
        };
    }
}
