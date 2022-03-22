package com.android.systemui.hdmi;

import com.android.systemui.util.settings.SecureSettings;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class HdmiCecSetMenuLanguageHelper {
    public final Executor mBackgroundExecutor;
    public HashSet<String> mDenylist;
    public Locale mLocale;
    public final SecureSettings mSecureSettings;

    public HdmiCecSetMenuLanguageHelper(Executor executor, SecureSettings secureSettings) {
        Collection collection;
        this.mBackgroundExecutor = executor;
        this.mSecureSettings = secureSettings;
        String string = secureSettings.getString("hdmi_cec_set_menu_language_denylist");
        if (string == null) {
            collection = Collections.EMPTY_SET;
        } else {
            collection = Arrays.asList(string.split(","));
        }
        this.mDenylist = new HashSet<>(collection);
    }
}
