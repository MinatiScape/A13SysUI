package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.AbsSavedState;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import java.util.Objects;
/* loaded from: classes.dex */
public class EditTextPreference extends DialogPreference {
    public String mText;

    /* loaded from: classes.dex */
    public static final class SimpleSummaryProvider implements Preference.SummaryProvider<EditTextPreference> {
        public static SimpleSummaryProvider sSimpleSummaryProvider;

        @Override // androidx.preference.Preference.SummaryProvider
        public final CharSequence provideSummary(EditTextPreference editTextPreference) {
            EditTextPreference editTextPreference2 = editTextPreference;
            Objects.requireNonNull(editTextPreference2);
            if (TextUtils.isEmpty(editTextPreference2.mText)) {
                return editTextPreference2.mContext.getString(2131952877);
            }
            return editTextPreference2.mText;
        }
    }

    public EditTextPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, 0);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.EditTextPreference, i, 0);
        if (obtainStyledAttributes.getBoolean(0, obtainStyledAttributes.getBoolean(0, false))) {
            if (SimpleSummaryProvider.sSimpleSummaryProvider == null) {
                SimpleSummaryProvider.sSimpleSummaryProvider = new SimpleSummaryProvider();
            }
            this.mSummaryProvider = SimpleSummaryProvider.sSimpleSummaryProvider;
            notifyChanged();
        }
        obtainStyledAttributes.recycle();
    }

    @Override // androidx.preference.Preference
    public final Parcelable onSaveInstanceState() {
        this.mBaseMethodCalled = true;
        AbsSavedState absSavedState = AbsSavedState.EMPTY_STATE;
        if (this.mPersistent) {
            return absSavedState;
        }
        SavedState savedState = new SavedState(absSavedState);
        savedState.mText = this.mText;
        return savedState;
    }

    /* loaded from: classes.dex */
    public static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.preference.EditTextPreference.SavedState.1
            @Override // android.os.Parcelable.Creator
            public final SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public String mText;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mText = parcel.readString();
        }

        public SavedState(AbsSavedState absSavedState) {
            super(absSavedState);
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeString(this.mText);
        }
    }

    @Override // androidx.preference.Preference
    public final void onSetInitialValue(Object obj) {
        setText(getPersistedString((String) obj));
    }

    @Override // androidx.preference.Preference
    public final boolean shouldDisableDependents() {
        if (TextUtils.isEmpty(this.mText) || super.shouldDisableDependents()) {
            return true;
        }
        return false;
    }

    @Override // androidx.preference.Preference
    public final Object onGetDefaultValue(TypedArray typedArray, int i) {
        return typedArray.getString(i);
    }

    @Override // androidx.preference.Preference
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (!parcelable.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setText(savedState.mText);
    }

    public final void setText(String str) {
        boolean shouldDisableDependents = shouldDisableDependents();
        this.mText = str;
        persistString(str);
        boolean shouldDisableDependents2 = shouldDisableDependents();
        if (shouldDisableDependents2 != shouldDisableDependents) {
            notifyDependencyChange(shouldDisableDependents2);
        }
        notifyChanged();
    }

    public EditTextPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, 2130968990, 16842898), 0);
    }
}
