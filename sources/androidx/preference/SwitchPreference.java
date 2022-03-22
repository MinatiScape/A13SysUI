package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.core.content.res.TypedArrayUtils;
/* loaded from: classes.dex */
public class SwitchPreference extends TwoStatePreference {
    public final Listener mListener;
    public CharSequence mSwitchOff;
    public CharSequence mSwitchOn;

    /* loaded from: classes.dex */
    public class Listener implements CompoundButton.OnCheckedChangeListener {
        public Listener() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            if (!SwitchPreference.this.callChangeListener(Boolean.valueOf(z))) {
                compoundButton.setChecked(!z);
            } else {
                SwitchPreference.this.setChecked(z);
            }
        }
    }

    public SwitchPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mListener = new Listener();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SwitchPreference, i, i2);
        this.mSummaryOn = TypedArrayUtils.getString(obtainStyledAttributes, 7, 0);
        if (this.mChecked) {
            notifyChanged();
        }
        this.mSummaryOff = TypedArrayUtils.getString(obtainStyledAttributes, 6, 1);
        if (!this.mChecked) {
            notifyChanged();
        }
        this.mSwitchOn = TypedArrayUtils.getString(obtainStyledAttributes, 9, 3);
        notifyChanged();
        this.mSwitchOff = TypedArrayUtils.getString(obtainStyledAttributes, 8, 4);
        notifyChanged();
        this.mDisableDependentsState = obtainStyledAttributes.getBoolean(5, obtainStyledAttributes.getBoolean(2, false));
        obtainStyledAttributes.recycle();
    }

    public final void syncSwitchView(View view) {
        boolean z = view instanceof Switch;
        if (z) {
            ((Switch) view).setOnCheckedChangeListener(null);
        }
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(this.mChecked);
        }
        if (z) {
            Switch r4 = (Switch) view;
            r4.setTextOn(this.mSwitchOn);
            r4.setTextOff(this.mSwitchOff);
            r4.setOnCheckedChangeListener(this.mListener);
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        syncSwitchView(preferenceViewHolder.findViewById(16908352));
        syncSummaryView(preferenceViewHolder.findViewById(16908304));
    }

    @Override // androidx.preference.Preference
    public final void performClick(View view) {
        performClick();
        if (((AccessibilityManager) this.mContext.getSystemService("accessibility")).isEnabled()) {
            syncSwitchView(view.findViewById(16908352));
            syncSummaryView(view.findViewById(16908304));
        }
    }

    public SwitchPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public SwitchPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, 2130969916, 16843629));
    }

    public SwitchPreference(Context context) {
        this(context, null);
    }
}
