package com.android.settingslib.inputmethod;

import android.content.Context;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Locale;
/* loaded from: classes.dex */
public class InputMethodSubtypePreference extends SwitchWithNoTextPreference {
    @VisibleForTesting
    public InputMethodSubtypePreference(Context context, String str, CharSequence charSequence, Locale locale, Locale locale2) {
        super(context);
        this.mPersistent = false;
        setKey(str);
        setTitle(charSequence);
        if (locale != null && !locale.equals(locale2)) {
            TextUtils.equals(locale.getLanguage(), locale2.getLanguage());
        }
    }
}
