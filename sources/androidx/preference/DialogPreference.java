package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceManager;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class DialogPreference extends Preference {
    public Drawable mDialogIcon;
    public int mDialogLayoutResId;
    public String mDialogMessage;
    public CharSequence mDialogTitle;
    public String mNegativeButtonText;
    public String mPositiveButtonText;

    /* loaded from: classes.dex */
    public interface TargetFragment {
        <T extends Preference> T findPreference(CharSequence charSequence);
    }

    public DialogPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.DialogPreference, i, i2);
        String string = TypedArrayUtils.getString(obtainStyledAttributes, 9, 0);
        this.mDialogTitle = string;
        if (string == null) {
            this.mDialogTitle = this.mTitle;
        }
        this.mDialogMessage = TypedArrayUtils.getString(obtainStyledAttributes, 8, 1);
        Drawable drawable = obtainStyledAttributes.getDrawable(6);
        this.mDialogIcon = drawable == null ? obtainStyledAttributes.getDrawable(2) : drawable;
        this.mPositiveButtonText = TypedArrayUtils.getString(obtainStyledAttributes, 11, 3);
        this.mNegativeButtonText = TypedArrayUtils.getString(obtainStyledAttributes, 10, 4);
        this.mDialogLayoutResId = obtainStyledAttributes.getResourceId(7, obtainStyledAttributes.getResourceId(5, 0));
        obtainStyledAttributes.recycle();
    }

    @Override // androidx.preference.Preference
    public void onClick() {
        PreferenceManager preferenceManager = this.mPreferenceManager;
        Objects.requireNonNull(preferenceManager);
        PreferenceManager.OnDisplayPreferenceDialogListener onDisplayPreferenceDialogListener = preferenceManager.mOnDisplayPreferenceDialogListener;
        if (onDisplayPreferenceDialogListener != null) {
            onDisplayPreferenceDialogListener.onDisplayPreferenceDialog(this);
        }
    }

    public DialogPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DialogPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, 2130968946, 16842897));
    }

    public DialogPreference(Context context) {
        this(context, null);
    }
}
