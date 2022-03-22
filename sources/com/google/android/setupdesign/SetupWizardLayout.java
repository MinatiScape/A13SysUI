package com.google.android.setupdesign;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.template.SystemNavBarMixin;
import com.google.android.setupdesign.template.DescriptionMixin;
import com.google.android.setupdesign.template.HeaderMixin;
import com.google.android.setupdesign.template.NavigationBarMixin;
import com.google.android.setupdesign.template.ProgressBarMixin;
import com.google.android.setupdesign.template.RequireScrollMixin;
import com.google.android.setupdesign.view.BottomScrollView;
import com.google.android.setupdesign.view.Illustration;
import java.util.Objects;
/* loaded from: classes.dex */
public class SetupWizardLayout extends TemplateLayout {

    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.google.android.setupdesign.SetupWizardLayout.SavedState.1
            @Override // android.os.Parcelable.Creator
            public final SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public boolean isProgressBarShown;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
            this.isProgressBarShown = false;
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            boolean z = false;
            this.isProgressBarShown = false;
            this.isProgressBarShown = parcel.readInt() != 0 ? true : z;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.isProgressBarShown ? 1 : 0);
        }
    }

    @Override // com.google.android.setupcompat.internal.TemplateLayout
    public ViewGroup findContainer(int i) {
        if (i == 0) {
            i = 2131428973;
        }
        return super.findContainer(i);
    }

    @Override // com.google.android.setupcompat.internal.TemplateLayout
    public View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        if (i == 0) {
            i = 2131624593;
        }
        return inflateTemplate(layoutInflater, 2132017796, i);
    }

    @Override // android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            Log.w("SetupWizardLayout", "Ignoring restore instance state " + parcelable);
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        ((ProgressBarMixin) getMixin(ProgressBarMixin.class)).setShown(savedState.isProgressBarShown);
    }

    public SetupWizardLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            ScrollView scrollView = null;
            registerMixin(SystemNavBarMixin.class, new SystemNavBarMixin(this, null));
            registerMixin(HeaderMixin.class, new HeaderMixin(this, attributeSet, 2130969880));
            registerMixin(DescriptionMixin.class, new DescriptionMixin(this, attributeSet, 2130969880));
            registerMixin(ProgressBarMixin.class, new ProgressBarMixin(this, null, 0));
            registerMixin(NavigationBarMixin.class, new NavigationBarMixin());
            registerMixin(RequireScrollMixin.class, new RequireScrollMixin());
            View findManagedViewById = findManagedViewById(2131428955);
            scrollView = findManagedViewById instanceof ScrollView ? (ScrollView) findManagedViewById : scrollView;
            if (scrollView != null) {
                if (scrollView instanceof BottomScrollView) {
                    BottomScrollView bottomScrollView = (BottomScrollView) scrollView;
                } else {
                    Log.w("ScrollViewDelegate", "Cannot set non-BottomScrollView. Found=" + scrollView);
                }
            }
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.SudSetupWizardLayout, 2130969880, 0);
            Drawable drawable = obtainStyledAttributes.getDrawable(0);
            if (drawable != null) {
                View findManagedViewById2 = findManagedViewById(2131428974);
                if (findManagedViewById2 != null) {
                    findManagedViewById2.setBackgroundDrawable(drawable);
                }
            } else {
                Drawable drawable2 = obtainStyledAttributes.getDrawable(1);
                if (drawable2 != null) {
                    if (drawable2 instanceof BitmapDrawable) {
                        Shader.TileMode tileMode = Shader.TileMode.REPEAT;
                        ((BitmapDrawable) drawable2).setTileModeXY(tileMode, tileMode);
                    }
                    View findManagedViewById3 = findManagedViewById(2131428974);
                    if (findManagedViewById3 != null) {
                        findManagedViewById3.setBackgroundDrawable(drawable2);
                    }
                }
            }
            Drawable drawable3 = obtainStyledAttributes.getDrawable(3);
            if (drawable3 != null) {
                View findManagedViewById4 = findManagedViewById(2131428974);
                if (findManagedViewById4 instanceof Illustration) {
                    Illustration illustration = (Illustration) findManagedViewById4;
                    Objects.requireNonNull(illustration);
                    if (drawable3 != illustration.illustration) {
                        illustration.illustration = drawable3;
                        illustration.invalidate();
                        illustration.requestLayout();
                    }
                }
            } else {
                Drawable drawable4 = obtainStyledAttributes.getDrawable(6);
                Drawable drawable5 = obtainStyledAttributes.getDrawable(5);
                if (!(drawable4 == null || drawable5 == null)) {
                    View findManagedViewById5 = findManagedViewById(2131428974);
                    if (findManagedViewById5 instanceof Illustration) {
                        Illustration illustration2 = (Illustration) findManagedViewById5;
                        if (getContext().getResources().getBoolean(2131034225)) {
                            if (drawable5 instanceof BitmapDrawable) {
                                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable5;
                                bitmapDrawable.setTileModeX(Shader.TileMode.REPEAT);
                                bitmapDrawable.setGravity(48);
                            }
                            if (drawable4 instanceof BitmapDrawable) {
                                ((BitmapDrawable) drawable4).setGravity(51);
                            }
                            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawable5, drawable4});
                            layerDrawable.setAutoMirrored(true);
                            drawable4 = layerDrawable;
                        } else {
                            drawable4.setAutoMirrored(true);
                        }
                        Objects.requireNonNull(illustration2);
                        if (drawable4 != illustration2.illustration) {
                            illustration2.illustration = drawable4;
                            illustration2.invalidate();
                            illustration2.requestLayout();
                        }
                    }
                }
            }
            int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(2, -1);
            dimensionPixelSize = dimensionPixelSize == -1 ? getResources().getDimensionPixelSize(2131167113) : dimensionPixelSize;
            View findManagedViewById6 = findManagedViewById(2131428974);
            if (findManagedViewById6 != null) {
                findManagedViewById6.setPadding(findManagedViewById6.getPaddingLeft(), dimensionPixelSize, findManagedViewById6.getPaddingRight(), findManagedViewById6.getPaddingBottom());
            }
            float f = obtainStyledAttributes.getFloat(4, -1.0f);
            if (f == -1.0f) {
                TypedValue typedValue = new TypedValue();
                getResources().getValue(2131167165, typedValue, true);
                f = typedValue.getFloat();
            }
            View findManagedViewById7 = findManagedViewById(2131428974);
            if (findManagedViewById7 instanceof Illustration) {
                Illustration illustration3 = (Illustration) findManagedViewById7;
                Objects.requireNonNull(illustration3);
                illustration3.aspectRatio = f;
                illustration3.invalidate();
                illustration3.requestLayout();
            }
            obtainStyledAttributes.recycle();
        }
    }

    @Override // android.view.View
    public final Parcelable onSaveInstanceState() {
        int i;
        boolean z;
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        ProgressBarMixin progressBarMixin = (ProgressBarMixin) getMixin(ProgressBarMixin.class);
        Objects.requireNonNull(progressBarMixin);
        TemplateLayout templateLayout = progressBarMixin.templateLayout;
        if (progressBarMixin.useBottomProgressBar) {
            i = 2131428960;
        } else {
            i = 2131428981;
        }
        View findManagedViewById = templateLayout.findManagedViewById(i);
        if (findManagedViewById == null || findManagedViewById.getVisibility() != 0) {
            z = false;
        } else {
            z = true;
        }
        savedState.isProgressBarShown = z;
        return savedState;
    }
}
