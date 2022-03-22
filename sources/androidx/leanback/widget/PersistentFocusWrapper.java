package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.ArrayList;
/* loaded from: classes.dex */
class PersistentFocusWrapper extends FrameLayout {
    public int mSelectedPosition = -1;
    public boolean mPersistFocusVertical = true;

    @Override // android.view.ViewGroup, android.view.View
    public final boolean requestFocus(int i, Rect rect) {
        int i2;
        int i3 = 0;
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup != null && (i2 = this.mSelectedPosition) >= 0) {
            ViewGroup viewGroup2 = (ViewGroup) getChildAt(0);
            if (viewGroup2 != null) {
                i3 = viewGroup2.getChildCount();
            }
            if (i2 < i3 && viewGroup.getChildAt(this.mSelectedPosition).requestFocus(i, rect)) {
                return true;
            }
        }
        return super.requestFocus(i, rect);
    }

    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.leanback.widget.PersistentFocusWrapper.SavedState.1
            @Override // android.os.Parcelable.Creator
            public final SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public int mSelectedPosition;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mSelectedPosition = parcel.readInt();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.mSelectedPosition);
        }
    }

    @Override // android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        this.mSelectedPosition = savedState.mSelectedPosition;
        super.onRestoreInstanceState(savedState.getSuperState());
    }

    @Override // android.view.View
    public final Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mSelectedPosition = this.mSelectedPosition;
        return savedState;
    }

    public PersistentFocusWrapper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void addFocusables(ArrayList<View> arrayList, int i, int i2) {
        int i3;
        if (!hasFocus()) {
            boolean z = false;
            ViewGroup viewGroup = (ViewGroup) getChildAt(0);
            if (viewGroup == null) {
                i3 = 0;
            } else {
                i3 = viewGroup.getChildCount();
            }
            if (i3 != 0) {
                boolean z2 = this.mPersistFocusVertical;
                if ((z2 && (i == 33 || i == 130)) || (!z2 && (i == 17 || i == 66))) {
                    z = true;
                }
                if (z) {
                    arrayList.add(this);
                    return;
                }
            }
        }
        super.addFocusables(arrayList, i, i2);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void requestChildFocus(View view, View view2) {
        int i;
        super.requestChildFocus(view, view2);
        while (view2 != null && view2.getParent() != view) {
            view2 = (View) view2.getParent();
        }
        if (view2 == null) {
            i = -1;
        } else {
            i = ((ViewGroup) view).indexOfChild(view2);
        }
        this.mSelectedPosition = i;
    }
}
