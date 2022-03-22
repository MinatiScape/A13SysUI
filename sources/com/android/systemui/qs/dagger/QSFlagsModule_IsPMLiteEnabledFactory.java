package com.android.systemui.qs.dagger;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.keyguard.KeyguardHostView;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.log.LogcatEchoTrackerDebug;
import com.android.systemui.log.LogcatEchoTrackerProd;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFlagsModule_IsPMLiteEnabledFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider featureFlagsProvider;
    public final Provider globalSettingsProvider;

    public /* synthetic */ QSFlagsModule_IsPMLiteEnabledFactory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.featureFlagsProvider = provider;
        this.globalSettingsProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        boolean z = false;
        switch (this.$r8$classId) {
            case 0:
                GlobalSettings globalSettings = (GlobalSettings) this.globalSettingsProvider.mo144get();
                if (((FeatureFlags) this.featureFlagsProvider.mo144get()).isEnabled(Flags.POWER_MENU_LITE) && globalSettings.getInt("sysui_pm_lite", 1) != 0) {
                    z = true;
                }
                return Boolean.valueOf(z);
            case 1:
                ViewGroup viewGroup = (ViewGroup) this.featureFlagsProvider.mo144get();
                KeyguardHostView keyguardHostView = (KeyguardHostView) ((LayoutInflater) this.globalSettingsProvider.mo144get()).inflate(2131624160, viewGroup, false);
                viewGroup.addView(keyguardHostView);
                Objects.requireNonNull(keyguardHostView, "Cannot return null from a non-@Nullable @Provides method");
                return keyguardHostView;
            case 2:
                ContentResolver contentResolver = (ContentResolver) this.featureFlagsProvider.mo144get();
                Looper looper = (Looper) this.globalSettingsProvider.mo144get();
                if (!Build.isDebuggable()) {
                    return new LogcatEchoTrackerProd();
                }
                final LogcatEchoTrackerDebug logcatEchoTrackerDebug = new LogcatEchoTrackerDebug(contentResolver);
                Uri uriFor = Settings.Global.getUriFor("systemui/buffer");
                final Handler handler = new Handler(looper);
                contentResolver.registerContentObserver(uriFor, true, new ContentObserver(handler) { // from class: com.android.systemui.log.LogcatEchoTrackerDebug$attach$1
                    @Override // android.database.ContentObserver
                    public final void onChange(boolean z2, Uri uri) {
                        super.onChange(z2, uri);
                        LogcatEchoTrackerDebug.this.cachedBufferLevels.clear();
                    }
                });
                Uri uriFor2 = Settings.Global.getUriFor("systemui/tag");
                final Handler handler2 = new Handler(looper);
                contentResolver.registerContentObserver(uriFor2, true, new ContentObserver(handler2) { // from class: com.android.systemui.log.LogcatEchoTrackerDebug$attach$2
                    @Override // android.database.ContentObserver
                    public final void onChange(boolean z2, Uri uri) {
                        super.onChange(z2, uri);
                        LogcatEchoTrackerDebug.this.cachedTagLevels.clear();
                    }
                });
                return logcatEchoTrackerDebug;
            default:
                return new StatusBarWindowStateController(((Integer) this.featureFlagsProvider.mo144get()).intValue(), (CommandQueue) this.globalSettingsProvider.mo144get());
        }
    }
}
