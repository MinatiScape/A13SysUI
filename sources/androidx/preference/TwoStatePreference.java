package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.AbsSavedState;
import androidx.preference.Preference;
/* loaded from: classes.dex */
public abstract class TwoStatePreference extends Preference {
    public boolean mChecked;
    public boolean mCheckedSet;
    public boolean mDisableDependentsState;
    public CharSequence mSummaryOff;
    public CharSequence mSummaryOn;

    public TwoStatePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // androidx.preference.Preference
    public final Object onGetDefaultValue(TypedArray typedArray, int i) {
        return Boolean.valueOf(typedArray.getBoolean(i, false));
    }

    @Override // androidx.preference.Preference
    public final Parcelable onSaveInstanceState() {
        this.mBaseMethodCalled = true;
        AbsSavedState absSavedState = AbsSavedState.EMPTY_STATE;
        if (this.mPersistent) {
            return absSavedState;
        }
        SavedState savedState = new SavedState(absSavedState);
        savedState.mChecked = this.mChecked;
        return savedState;
    }

    /* loaded from: classes.dex */
    public static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.preference.TwoStatePreference.SavedState.1
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

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mChecked = parcel.readInt() != 1 ? false : true;
        }

        public SavedState(AbsSavedState absSavedState) {
            super(absSavedState);
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.mChecked ? 1 : 0);
        }
    }

    public TwoStatePreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // androidx.preference.Preference
    public void onClick() {
        boolean z = !this.mChecked;
        if (callChangeListener(Boolean.valueOf(z))) {
            setChecked(z);
        }
    }

    @Override // androidx.preference.Preference
    public final void onSetInitialValue(Object obj) {
        if (obj == null) {
            obj = Boolean.FALSE;
        }
        boolean booleanValue = ((Boolean) obj).booleanValue();
        if (shouldPersist()) {
            booleanValue = this.mPreferenceManager.getSharedPreferences().getBoolean(this.mKey, booleanValue);
        }
        setChecked(booleanValue);
    }

    public void setChecked(boolean z) {
        boolean z2;
        if (this.mChecked != z) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2 || !this.mCheckedSet) {
            this.mChecked = z;
            this.mCheckedSet = true;
            persistBoolean(z);
            if (z2) {
                notifyDependencyChange(shouldDisableDependents());
                notifyChanged();
            }
        }
    }

    @Override // androidx.preference.Preference
    public final boolean shouldDisableDependents() {
        boolean z;
        if (this.mDisableDependentsState) {
            z = this.mChecked;
        } else if (!this.mChecked) {
            z = true;
        } else {
            z = false;
        }
        if (z || super.shouldDisableDependents()) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0030  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void syncSummaryView(android.view.View r4) {
        /*
            r3 = this;
            boolean r0 = r4 instanceof android.widget.TextView
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            android.widget.TextView r4 = (android.widget.TextView) r4
            r0 = 1
            boolean r1 = r3.mChecked
            r2 = 0
            if (r1 == 0) goto L_0x001c
            java.lang.CharSequence r1 = r3.mSummaryOn
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 != 0) goto L_0x001c
            java.lang.CharSequence r0 = r3.mSummaryOn
            r4.setText(r0)
        L_0x001a:
            r0 = r2
            goto L_0x002e
        L_0x001c:
            boolean r1 = r3.mChecked
            if (r1 != 0) goto L_0x002e
            java.lang.CharSequence r1 = r3.mSummaryOff
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 != 0) goto L_0x002e
            java.lang.CharSequence r0 = r3.mSummaryOff
            r4.setText(r0)
            goto L_0x001a
        L_0x002e:
            if (r0 == 0) goto L_0x003e
            java.lang.CharSequence r3 = r3.getSummary()
            boolean r1 = android.text.TextUtils.isEmpty(r3)
            if (r1 != 0) goto L_0x003e
            r4.setText(r3)
            r0 = r2
        L_0x003e:
            r3 = 8
            if (r0 != 0) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r2 = r3
        L_0x0044:
            int r3 = r4.getVisibility()
            if (r2 == r3) goto L_0x004d
            r4.setVisibility(r2)
        L_0x004d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.preference.TwoStatePreference.syncSummaryView(android.view.View):void");
    }

    public TwoStatePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // androidx.preference.Preference
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (!parcelable.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setChecked(savedState.mChecked);
    }

    public TwoStatePreference(Context context) {
        this(context, null);
    }
}
