package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Checkable;
import android.widget.CompoundButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class SwitchPreferenceCompat extends TwoStatePreference {
    public final Listener mListener;
    public CharSequence mSwitchOff;
    public CharSequence mSwitchOn;

    /* loaded from: classes.dex */
    public class Listener implements CompoundButton.OnCheckedChangeListener {
        public Listener() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            if (!SwitchPreferenceCompat.this.callChangeListener(Boolean.valueOf(z))) {
                compoundButton.setChecked(!z);
            } else {
                SwitchPreferenceCompat.this.setChecked(z);
            }
        }
    }

    public SwitchPreferenceCompat(Context context, AttributeSet attributeSet, Object obj) {
        super(context, attributeSet, 2130969915, 0);
        this.mListener = new Listener();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SwitchPreferenceCompat, 2130969915, 0);
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
        boolean z = view instanceof SwitchCompat;
        if (z) {
            ((SwitchCompat) view).setOnCheckedChangeListener(null);
        }
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(this.mChecked);
        }
        if (z) {
            SwitchCompat switchCompat = (SwitchCompat) view;
            CharSequence charSequence = this.mSwitchOn;
            Objects.requireNonNull(switchCompat);
            switchCompat.setTextOnInternal(charSequence);
            switchCompat.requestLayout();
            if (switchCompat.isChecked()) {
                Object obj = switchCompat.mTextOn;
                if (obj == null) {
                    obj = switchCompat.getResources().getString(2131951625);
                }
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                new ViewCompat.AnonymousClass3(CharSequence.class).set(switchCompat, obj);
            }
            switchCompat.setTextOffInternal(this.mSwitchOff);
            switchCompat.requestLayout();
            if (!switchCompat.isChecked()) {
                Object obj2 = switchCompat.mTextOff;
                if (obj2 == null) {
                    obj2 = switchCompat.getResources().getString(2131951624);
                }
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                new ViewCompat.AnonymousClass3(CharSequence.class).set(switchCompat, obj2);
            }
            switchCompat.setOnCheckedChangeListener(this.mListener);
        }
    }

    @Override // androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        syncSwitchView(preferenceViewHolder.findViewById(2131428997));
        syncSummaryView(preferenceViewHolder.findViewById(16908304));
    }

    @Override // androidx.preference.Preference
    public final void performClick(View view) {
        performClick();
        if (((AccessibilityManager) this.mContext.getSystemService("accessibility")).isEnabled()) {
            syncSwitchView(view.findViewById(2131428997));
            syncSummaryView(view.findViewById(16908304));
        }
    }

    public SwitchPreferenceCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, null);
    }
}
