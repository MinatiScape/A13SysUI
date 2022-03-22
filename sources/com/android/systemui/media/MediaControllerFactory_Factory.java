package com.android.systemui.media;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.provider.Settings;
import android.view.View;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.doze.DozeFalsingManagerAdapter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.google.android.systemui.assist.uihints.ConfigurationHandler;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaControllerFactory_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;

    public /* synthetic */ MediaControllerFactory_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new MediaControllerFactory((Context) this.contextProvider.mo144get());
            case 1:
                return new AccessibilityButtonTargetsObserver((Context) this.contextProvider.mo144get());
            case 2:
                DisplayManager displayManager = (DisplayManager) ((Context) this.contextProvider.mo144get()).getSystemService(DisplayManager.class);
                Objects.requireNonNull(displayManager, "Cannot return null from a non-@Nullable @Provides method");
                return displayManager;
            case 3:
                return new DozeFalsingManagerAdapter((FalsingCollector) this.contextProvider.mo144get());
            case 4:
                NodeController nodeController = ((SectionHeaderControllerSubcomponent) this.contextProvider.mo144get()).getNodeController();
                Objects.requireNonNull(nodeController, "Cannot return null from a non-@Nullable @Provides method");
                return nodeController;
            case 5:
                View findViewById = ((PhoneStatusBarView) this.contextProvider.mo144get()).findViewById(2131428514);
                Objects.requireNonNull(findViewById, "Cannot return null from a non-@Nullable @Provides method");
                return findViewById;
            case FalsingManager.VERSION /* 6 */:
                return new ConfigurationHandler((Context) this.contextProvider.mo144get());
            default:
                String string = Settings.Global.getString(((Context) this.contextProvider.mo144get()).getContentResolver(), "device_name");
                Objects.requireNonNull(string, "Cannot return null from a non-@Nullable @Provides method");
                return string;
        }
    }
}
