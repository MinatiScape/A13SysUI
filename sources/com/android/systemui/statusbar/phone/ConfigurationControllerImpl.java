package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.LocaleList;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.util.ArrayList;
import java.util.Iterator;
/* compiled from: ConfigurationControllerImpl.kt */
/* loaded from: classes.dex */
public final class ConfigurationControllerImpl implements ConfigurationController {
    public final Context context;
    public int density;
    public float fontScale;
    public final boolean inCarMode;
    public int layoutDirection;
    public LocaleList localeList;
    public Rect maxBounds;
    public int smallestScreenWidth;
    public int uiMode;
    public final ArrayList listeners = new ArrayList();
    public final Configuration lastConfig = new Configuration();

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(ConfigurationController.ConfigurationListener configurationListener) {
        ConfigurationController.ConfigurationListener configurationListener2 = configurationListener;
        this.listeners.add(configurationListener2);
        configurationListener2.onDensityOrFontScaleChanged();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController
    public final boolean isLayoutRtl() {
        if (this.layoutDirection == 1) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController
    public final void notifyThemeChanged() {
        Iterator it = new ArrayList(this.listeners).iterator();
        while (it.hasNext()) {
            ConfigurationController.ConfigurationListener configurationListener = (ConfigurationController.ConfigurationListener) it.next();
            if (this.listeners.contains(configurationListener)) {
                configurationListener.onThemeChanged();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0047, code lost:
        if (r4 == false) goto L_0x0069;
     */
    /* JADX WARN: Removed duplicated region for block: B:31:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x011a  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x014e  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x016a A[ORIG_RETURN, RETURN] */
    @Override // com.android.systemui.statusbar.policy.ConfigurationController
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onConfigurationChanged(android.content.res.Configuration r11) {
        /*
            Method dump skipped, instructions count: 363
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.ConfigurationControllerImpl.onConfigurationChanged(android.content.res.Configuration):void");
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(ConfigurationController.ConfigurationListener configurationListener) {
        this.listeners.remove(configurationListener);
    }

    public ConfigurationControllerImpl(Context context) {
        boolean z;
        Configuration configuration = context.getResources().getConfiguration();
        this.context = context;
        this.fontScale = configuration.fontScale;
        this.density = configuration.densityDpi;
        this.smallestScreenWidth = configuration.smallestScreenWidthDp;
        int i = configuration.uiMode;
        if ((i & 15) == 3) {
            z = true;
        } else {
            z = false;
        }
        this.inCarMode = z;
        this.uiMode = i & 48;
        this.localeList = configuration.getLocales();
        this.layoutDirection = configuration.getLayoutDirection();
    }
}
