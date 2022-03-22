package com.google.android.material.slider;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.AbsSavedState;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class RangeSlider extends BaseSlider<RangeSlider, Object, Object> {
    public float minSeparation;
    public int separationUnit;

    /* loaded from: classes.dex */
    public static class RangeSliderState extends AbsSavedState {
        public static final Parcelable.Creator<RangeSliderState> CREATOR = new Parcelable.Creator<RangeSliderState>() { // from class: com.google.android.material.slider.RangeSlider.RangeSliderState.1
            @Override // android.os.Parcelable.Creator
            public final RangeSliderState createFromParcel(Parcel parcel) {
                return new RangeSliderState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final RangeSliderState[] newArray(int i) {
                return new RangeSliderState[i];
            }
        };
        public float minSeparation;
        public int separationUnit;

        public RangeSliderState(Parcelable parcelable) {
            super(parcelable);
        }

        public RangeSliderState(Parcel parcel) {
            super(parcel.readParcelable(RangeSliderState.class.getClassLoader()));
            this.minSeparation = parcel.readFloat();
            this.separationUnit = parcel.readInt();
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeFloat(this.minSeparation);
            parcel.writeInt(this.separationUnit);
        }
    }

    public RangeSlider(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R$styleable.RangeSlider, 2130969744, 2132018702, new int[0]);
        if (obtainStyledAttributes.hasValue(1)) {
            TypedArray obtainTypedArray = obtainStyledAttributes.getResources().obtainTypedArray(obtainStyledAttributes.getResourceId(1, 0));
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < obtainTypedArray.length(); i++) {
                arrayList.add(Float.valueOf(obtainTypedArray.getFloat(i, -1.0f)));
            }
            setValuesInternal(new ArrayList<>(arrayList));
        }
        this.minSeparation = obtainStyledAttributes.getDimension(0, 0.0f);
        obtainStyledAttributes.recycle();
    }

    @Override // com.google.android.material.slider.BaseSlider, android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        RangeSliderState rangeSliderState = (RangeSliderState) parcelable;
        super.onRestoreInstanceState(rangeSliderState.getSuperState());
        this.minSeparation = rangeSliderState.minSeparation;
        int i = rangeSliderState.separationUnit;
        this.separationUnit = i;
        super.separationUnit = i;
        this.dirtyConfig = true;
        postInvalidate();
    }

    @Override // com.google.android.material.slider.BaseSlider
    public final ArrayList getValues() {
        return super.getValues();
    }

    @Override // com.google.android.material.slider.BaseSlider, android.view.View
    public final Parcelable onSaveInstanceState() {
        RangeSliderState rangeSliderState = new RangeSliderState(super.onSaveInstanceState());
        rangeSliderState.minSeparation = this.minSeparation;
        rangeSliderState.separationUnit = this.separationUnit;
        return rangeSliderState;
    }

    @Override // com.google.android.material.slider.BaseSlider
    public final void setValues(Float... fArr) {
        super.setValues(fArr);
    }

    @Override // com.google.android.material.slider.BaseSlider
    public final float getMinSeparation() {
        return this.minSeparation;
    }
}
