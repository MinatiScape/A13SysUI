package androidx.emoji2.viewsintegration;

import android.annotation.SuppressLint;
import android.text.Editable;
import androidx.emoji2.text.SpannableBuilder;
/* loaded from: classes.dex */
public final class EmojiEditableFactory extends Editable.Factory {
    public static final Object INSTANCE_LOCK = new Object();
    public static volatile EmojiEditableFactory sInstance;
    public static Class<?> sWatcherClass;

    @Override // android.text.Editable.Factory
    public final Editable newEditable(CharSequence charSequence) {
        Class<?> cls = sWatcherClass;
        if (cls != null) {
            return new SpannableBuilder(cls, charSequence);
        }
        return super.newEditable(charSequence);
    }

    @SuppressLint({"PrivateApi"})
    public EmojiEditableFactory() {
        try {
            sWatcherClass = Class.forName("android.text.DynamicLayout$ChangeWatcher", false, EmojiEditableFactory.class.getClassLoader());
        } catch (Throwable unused) {
        }
    }
}
