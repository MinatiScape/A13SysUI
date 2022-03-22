package com.android.settingslib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.preference.R$styleable;
import com.android.settingslib.utils.BuildCompatUtils;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public class MainSwitchBar extends LinearLayout implements CompoundButton.OnCheckedChangeListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public int mBackgroundActivatedColor;
    public int mBackgroundColor;
    public Drawable mBackgroundDisabled;
    public Drawable mBackgroundOff;
    public Drawable mBackgroundOn;
    public View mFrameView;
    public Switch mSwitch;
    public final ArrayList mSwitchChangeListeners;
    public TextView mTextView;

    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.android.settingslib.widget.MainSwitchBar.SavedState.1
            @Override // android.os.Parcelable.Creator
            public final SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public boolean mChecked;
        public boolean mVisible;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mChecked = ((Boolean) parcel.readValue(null)).booleanValue();
            this.mVisible = ((Boolean) parcel.readValue(null)).booleanValue();
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("MainSwitchBar.SavedState{");
            m.append(Integer.toHexString(System.identityHashCode(this)));
            m.append(" checked=");
            m.append(this.mChecked);
            m.append(" visible=");
            m.append(this.mVisible);
            m.append("}");
            return m.toString();
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeValue(Boolean.valueOf(this.mChecked));
            parcel.writeValue(Boolean.valueOf(this.mVisible));
        }
    }

    public MainSwitchBar(Context context) {
        this(context, null);
    }

    public MainSwitchBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        int i;
        MainSwitchBar mainSwitchBar;
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mSwitch.setChecked(savedState.mChecked);
        boolean z = savedState.mChecked;
        Switch r1 = this.mSwitch;
        if (r1 != null) {
            r1.setChecked(z);
        }
        setBackground(z);
        setBackground(savedState.mChecked);
        if (savedState.mVisible) {
            i = 0;
        } else {
            i = 8;
        }
        setVisibility(i);
        Switch r0 = this.mSwitch;
        if (savedState.mVisible) {
            mainSwitchBar = this;
        } else {
            mainSwitchBar = null;
        }
        r0.setOnCheckedChangeListener(mainSwitchBar);
        requestLayout();
    }

    @Override // android.view.View
    public final boolean performClick() {
        return this.mSwitch.performClick();
    }

    public MainSwitchBar(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        setBackground(z);
        int size = this.mSwitchChangeListeners.size();
        for (int i = 0; i < size; i++) {
            ((OnMainSwitchChangeListener) this.mSwitchChangeListeners.get(i)).onSwitchChanged(z);
        }
    }

    @Override // android.view.View
    public final Parcelable onSaveInstanceState() {
        boolean z;
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mChecked = this.mSwitch.isChecked();
        if (getVisibility() == 0) {
            z = true;
        } else {
            z = false;
        }
        savedState.mVisible = z;
        return savedState;
    }

    public final void setBackground(boolean z) {
        Drawable drawable;
        int i;
        if (!BuildCompatUtils.isAtLeastS()) {
            if (z) {
                i = this.mBackgroundActivatedColor;
            } else {
                i = this.mBackgroundColor;
            }
            setBackgroundColor(i);
            return;
        }
        View view = this.mFrameView;
        if (z) {
            drawable = this.mBackgroundOn;
        } else {
            drawable = this.mBackgroundOff;
        }
        view.setBackground(drawable);
    }

    @Override // android.view.View
    public final void setEnabled(boolean z) {
        Drawable drawable;
        super.setEnabled(z);
        this.mTextView.setEnabled(z);
        this.mSwitch.setEnabled(z);
        if (!BuildCompatUtils.isAtLeastS()) {
            return;
        }
        if (z) {
            View view = this.mFrameView;
            if (this.mSwitch.isChecked()) {
                drawable = this.mBackgroundOn;
            } else {
                drawable = this.mBackgroundOff;
            }
            view.setBackground(drawable);
            return;
        }
        this.mFrameView.setBackground(this.mBackgroundDisabled);
    }

    @Override // android.view.View
    public final void setOnClickListener(View.OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        this.mSwitch.setOnClickListener(onClickListener);
    }

    public MainSwitchBar(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        ArrayList arrayList = new ArrayList();
        this.mSwitchChangeListeners = arrayList;
        LayoutInflater.from(context).inflate(2131624483, this);
        if (!BuildCompatUtils.isAtLeastS()) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16843829});
            this.mBackgroundActivatedColor = obtainStyledAttributes.getColor(0, 0);
            this.mBackgroundColor = context.getColor(2131100355);
            obtainStyledAttributes.recycle();
        }
        setFocusable(true);
        setClickable(true);
        this.mFrameView = findViewById(2131427993);
        this.mTextView = (TextView) findViewById(2131429000);
        this.mSwitch = (Switch) findViewById(16908352);
        if (BuildCompatUtils.isAtLeastS()) {
            this.mBackgroundOn = getContext().getDrawable(2131232651);
            this.mBackgroundOff = getContext().getDrawable(2131232650);
            this.mBackgroundDisabled = getContext().getDrawable(2131232649);
        }
        OnMainSwitchChangeListener mainSwitchBar$$ExternalSyntheticLambda0 = new OnMainSwitchChangeListener() { // from class: com.android.settingslib.widget.MainSwitchBar$$ExternalSyntheticLambda0
            @Override // com.android.settingslib.widget.OnMainSwitchChangeListener
            public final void onSwitchChanged(boolean z) {
                MainSwitchBar mainSwitchBar = MainSwitchBar.this;
                int i3 = MainSwitchBar.$r8$clinit;
                Objects.requireNonNull(mainSwitchBar);
                Switch r0 = mainSwitchBar.mSwitch;
                if (r0 != null) {
                    r0.setChecked(z);
                }
                mainSwitchBar.setBackground(z);
            }
        };
        if (!arrayList.contains(mainSwitchBar$$ExternalSyntheticLambda0)) {
            arrayList.add(mainSwitchBar$$ExternalSyntheticLambda0);
        }
        if (this.mSwitch.getVisibility() == 0) {
            this.mSwitch.setOnCheckedChangeListener(this);
        }
        boolean isChecked = this.mSwitch.isChecked();
        Switch r7 = this.mSwitch;
        if (r7 != null) {
            r7.setChecked(isChecked);
        }
        setBackground(isChecked);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.Preference, 0, 0);
            CharSequence text = obtainStyledAttributes2.getText(4);
            TextView textView = this.mTextView;
            if (textView != null) {
                textView.setText(text);
            }
            obtainStyledAttributes2.recycle();
        }
        setBackground(this.mSwitch.isChecked());
    }
}
