package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.AbsSavedState;
import androidx.collection.SimpleArrayMap;
import androidx.preference.Preference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class PreferenceGroup extends Preference {
    public boolean mAttachedToHierarchy;
    public final AnonymousClass1 mClearRecycleCacheRunnable;
    public int mCurrentPreferenceOrder;
    public final Handler mHandler;
    public final SimpleArrayMap<String, Long> mIdRecycleCache;
    public int mInitialExpandedChildrenCount;
    public boolean mOrderingAsAdded;
    public ArrayList mPreferences;

    /* JADX WARN: Type inference failed for: r2v0, types: [androidx.preference.PreferenceGroup$1] */
    public PreferenceGroup(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, 0);
        this.mIdRecycleCache = new SimpleArrayMap<>();
        this.mHandler = new Handler();
        this.mOrderingAsAdded = true;
        this.mCurrentPreferenceOrder = 0;
        this.mAttachedToHierarchy = false;
        this.mInitialExpandedChildrenCount = Integer.MAX_VALUE;
        this.mClearRecycleCacheRunnable = new Runnable() { // from class: androidx.preference.PreferenceGroup.1
            @Override // java.lang.Runnable
            public final void run() {
                synchronized (this) {
                    PreferenceGroup.this.mIdRecycleCache.clear();
                }
            }
        };
        this.mPreferences = new ArrayList();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.PreferenceGroup, i, 0);
        this.mOrderingAsAdded = obtainStyledAttributes.getBoolean(2, obtainStyledAttributes.getBoolean(2, true));
        if (obtainStyledAttributes.hasValue(1)) {
            setInitialExpandedChildrenCount(obtainStyledAttributes.getInt(1, obtainStyledAttributes.getInt(1, Integer.MAX_VALUE)));
        }
        obtainStyledAttributes.recycle();
    }

    @Override // androidx.preference.Preference
    public final Parcelable onSaveInstanceState() {
        this.mBaseMethodCalled = true;
        return new SavedState(AbsSavedState.EMPTY_STATE, this.mInitialExpandedChildrenCount);
    }

    public final boolean removePreference(Preference preference) {
        boolean remove;
        synchronized (this) {
            try {
                Objects.requireNonNull(preference);
                preference.unregisterDependency();
                if (preference.mParentGroup == this) {
                    preference.mParentGroup = null;
                }
                remove = this.mPreferences.remove(preference);
                if (remove) {
                    String str = preference.mKey;
                    if (str != null) {
                        this.mIdRecycleCache.put(str, Long.valueOf(preference.getId()));
                        this.mHandler.removeCallbacks(this.mClearRecycleCacheRunnable);
                        this.mHandler.post(this.mClearRecycleCacheRunnable);
                    }
                    if (this.mAttachedToHierarchy) {
                        preference.onDetached();
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        Preference.OnPreferenceChangeInternalListener onPreferenceChangeInternalListener = this.mListener;
        if (onPreferenceChangeInternalListener != null) {
            PreferenceGroupAdapter preferenceGroupAdapter = (PreferenceGroupAdapter) onPreferenceChangeInternalListener;
            preferenceGroupAdapter.mHandler.removeCallbacks(preferenceGroupAdapter.mSyncRunnable);
            preferenceGroupAdapter.mHandler.post(preferenceGroupAdapter.mSyncRunnable);
        }
        return remove;
    }

    /* loaded from: classes.dex */
    public static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.preference.PreferenceGroup.SavedState.1
            @Override // android.os.Parcelable.Creator
            public final SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public int mInitialExpandedChildrenCount;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mInitialExpandedChildrenCount = parcel.readInt();
        }

        public SavedState(AbsSavedState absSavedState, int i) {
            super(absSavedState);
            this.mInitialExpandedChildrenCount = i;
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.mInitialExpandedChildrenCount);
        }
    }

    /* JADX WARN: Finally extract failed */
    public final void addPreference(Preference preference) {
        long j;
        if (!this.mPreferences.contains(preference)) {
            Objects.requireNonNull(preference);
            if (preference.mKey != null) {
                PreferenceGroup preferenceGroup = this;
                while (true) {
                    PreferenceGroup preferenceGroup2 = preferenceGroup.mParentGroup;
                    if (preferenceGroup2 == null) {
                        break;
                    }
                    preferenceGroup = preferenceGroup2;
                }
                String str = preference.mKey;
                if (preferenceGroup.findPreference(str) != null) {
                    Log.e("PreferenceGroup", "Found duplicated key: \"" + str + "\". This can cause unintended behaviour, please use unique keys for every preference.");
                }
            }
            int i = preference.mOrder;
            if (i == Integer.MAX_VALUE) {
                if (this.mOrderingAsAdded) {
                    int i2 = this.mCurrentPreferenceOrder;
                    this.mCurrentPreferenceOrder = i2 + 1;
                    if (i2 != i) {
                        preference.mOrder = i2;
                        Preference.OnPreferenceChangeInternalListener onPreferenceChangeInternalListener = preference.mListener;
                        if (onPreferenceChangeInternalListener != null) {
                            PreferenceGroupAdapter preferenceGroupAdapter = (PreferenceGroupAdapter) onPreferenceChangeInternalListener;
                            preferenceGroupAdapter.mHandler.removeCallbacks(preferenceGroupAdapter.mSyncRunnable);
                            preferenceGroupAdapter.mHandler.post(preferenceGroupAdapter.mSyncRunnable);
                        }
                    }
                }
                if (preference instanceof PreferenceGroup) {
                    ((PreferenceGroup) preference).mOrderingAsAdded = this.mOrderingAsAdded;
                }
            }
            int binarySearch = Collections.binarySearch(this.mPreferences, preference);
            if (binarySearch < 0) {
                binarySearch = (binarySearch * (-1)) - 1;
            }
            boolean shouldDisableDependents = shouldDisableDependents();
            if (preference.mParentDependencyMet == shouldDisableDependents) {
                preference.mParentDependencyMet = !shouldDisableDependents;
                preference.notifyDependencyChange(preference.shouldDisableDependents());
                preference.notifyChanged();
            }
            synchronized (this) {
                this.mPreferences.add(binarySearch, preference);
            }
            PreferenceManager preferenceManager = this.mPreferenceManager;
            String str2 = preference.mKey;
            if (str2 == null || !this.mIdRecycleCache.containsKey(str2)) {
                Objects.requireNonNull(preferenceManager);
                synchronized (preferenceManager) {
                    j = preferenceManager.mNextId;
                    preferenceManager.mNextId = 1 + j;
                }
            } else {
                SimpleArrayMap<String, Long> simpleArrayMap = this.mIdRecycleCache;
                Objects.requireNonNull(simpleArrayMap);
                j = simpleArrayMap.getOrDefault(str2, null).longValue();
                this.mIdRecycleCache.remove(str2);
            }
            preference.mId = j;
            preference.mHasId = true;
            try {
                preference.onAttachedToHierarchy(preferenceManager);
                preference.mHasId = false;
                if (preference.mParentGroup == null) {
                    preference.mParentGroup = this;
                    if (this.mAttachedToHierarchy) {
                        preference.onAttached();
                    }
                    Preference.OnPreferenceChangeInternalListener onPreferenceChangeInternalListener2 = this.mListener;
                    if (onPreferenceChangeInternalListener2 != null) {
                        PreferenceGroupAdapter preferenceGroupAdapter2 = (PreferenceGroupAdapter) onPreferenceChangeInternalListener2;
                        preferenceGroupAdapter2.mHandler.removeCallbacks(preferenceGroupAdapter2.mSyncRunnable);
                        preferenceGroupAdapter2.mHandler.post(preferenceGroupAdapter2.mSyncRunnable);
                        return;
                    }
                    return;
                }
                throw new IllegalStateException("This preference already has a parent. You must remove the existing parent before assigning a new one.");
            } catch (Throwable th) {
                preference.mHasId = false;
                throw th;
            }
        }
    }

    public final <T extends Preference> T findPreference(CharSequence charSequence) {
        T t;
        if (charSequence == null) {
            throw new IllegalArgumentException("Key cannot be null");
        } else if (TextUtils.equals(this.mKey, charSequence)) {
            return this;
        } else {
            int preferenceCount = getPreferenceCount();
            for (int i = 0; i < preferenceCount; i++) {
                T t2 = (T) getPreference(i);
                Objects.requireNonNull(t2);
                if (TextUtils.equals(t2.mKey, charSequence)) {
                    return t2;
                }
                if ((t2 instanceof PreferenceGroup) && (t = (T) ((PreferenceGroup) t2).findPreference(charSequence)) != null) {
                    return t;
                }
            }
            return null;
        }
    }

    public final Preference getPreference(int i) {
        return (Preference) this.mPreferences.get(i);
    }

    public final int getPreferenceCount() {
        return this.mPreferences.size();
    }

    @Override // androidx.preference.Preference
    public final void dispatchRestoreInstanceState(Bundle bundle) {
        super.dispatchRestoreInstanceState(bundle);
        int preferenceCount = getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            getPreference(i).dispatchRestoreInstanceState(bundle);
        }
    }

    @Override // androidx.preference.Preference
    public final void dispatchSaveInstanceState(Bundle bundle) {
        super.dispatchSaveInstanceState(bundle);
        int preferenceCount = getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            getPreference(i).dispatchSaveInstanceState(bundle);
        }
    }

    @Override // androidx.preference.Preference
    public final void notifyDependencyChange(boolean z) {
        super.notifyDependencyChange(z);
        int preferenceCount = getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            Preference preference = getPreference(i);
            Objects.requireNonNull(preference);
            if (preference.mParentDependencyMet == z) {
                preference.mParentDependencyMet = !z;
                preference.notifyDependencyChange(preference.shouldDisableDependents());
                preference.notifyChanged();
            }
        }
    }

    @Override // androidx.preference.Preference
    public final void onAttached() {
        super.onAttached();
        this.mAttachedToHierarchy = true;
        int preferenceCount = getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            getPreference(i).onAttached();
        }
    }

    @Override // androidx.preference.Preference
    public final void onDetached() {
        unregisterDependency();
        this.mAttachedToHierarchy = false;
        int preferenceCount = getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            getPreference(i).onDetached();
        }
    }

    @Override // androidx.preference.Preference
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (!parcelable.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        this.mInitialExpandedChildrenCount = savedState.mInitialExpandedChildrenCount;
        super.onRestoreInstanceState(savedState.getSuperState());
    }

    public final void setInitialExpandedChildrenCount(int i) {
        if (i != Integer.MAX_VALUE && !(!TextUtils.isEmpty(this.mKey))) {
            Log.e("PreferenceGroup", getClass().getSimpleName() + " should have a key defined if it contains an expandable preference");
        }
        this.mInitialExpandedChildrenCount = i;
    }

    public PreferenceGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }
}
