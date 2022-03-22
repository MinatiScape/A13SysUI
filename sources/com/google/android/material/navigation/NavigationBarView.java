package com.google.android.material.navigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.customview.view.AbsSavedState;
import androidx.mediarouter.R$bool;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public abstract class NavigationBarView extends FrameLayout {
    public ColorStateList itemRippleColor;
    public final NavigationBarMenu menu;
    public SupportMenuInflater menuInflater;
    public final NavigationBarMenuView menuView;
    public final NavigationBarPresenter presenter;

    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: com.google.android.material.navigation.NavigationBarView.SavedState.1
            @Override // android.os.Parcelable.ClassLoaderCreator
            public final SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            public final Object createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.Creator
            public final Object[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public Bundle menuPresenterState;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.menuPresenterState = parcel.readBundle(classLoader == null ? getClass().getClassLoader() : classLoader);
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mSuperState, i);
            parcel.writeBundle(this.menuPresenterState);
        }
    }

    public abstract NavigationBarMenuView createNavigationBarMenuView(Context context);

    public abstract int getMaxItemCount();

    public NavigationBarView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, i, i2), attributeSet, i);
        Drawable drawable;
        Drawable drawable2;
        Drawable drawable3;
        Drawable drawable4;
        NavigationBarPresenter navigationBarPresenter = new NavigationBarPresenter();
        this.presenter = navigationBarPresenter;
        Context context2 = getContext();
        TintTypedArray obtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(context2, attributeSet, R$styleable.NavigationBarView, i, i2, 10, 9);
        NavigationBarMenu navigationBarMenu = new NavigationBarMenu(context2, getClass(), getMaxItemCount());
        this.menu = navigationBarMenu;
        NavigationBarMenuView createNavigationBarMenuView = createNavigationBarMenuView(context2);
        this.menuView = createNavigationBarMenuView;
        navigationBarPresenter.menuView = createNavigationBarMenuView;
        navigationBarPresenter.id = 1;
        Objects.requireNonNull(createNavigationBarMenuView);
        createNavigationBarMenuView.presenter = navigationBarPresenter;
        navigationBarMenu.addMenuPresenter(navigationBarPresenter, navigationBarMenu.mContext);
        navigationBarPresenter.initForMenu(getContext(), navigationBarMenu);
        if (obtainTintedStyledAttributes.hasValue(5)) {
            ColorStateList colorStateList = obtainTintedStyledAttributes.getColorStateList(5);
            createNavigationBarMenuView.itemIconTint = colorStateList;
            NavigationBarItemView[] navigationBarItemViewArr = createNavigationBarMenuView.buttons;
            if (navigationBarItemViewArr != null) {
                for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                    Objects.requireNonNull(navigationBarItemView);
                    navigationBarItemView.iconTint = colorStateList;
                    if (!(navigationBarItemView.itemData == null || (drawable4 = navigationBarItemView.wrappedIconDrawable) == null)) {
                        drawable4.setTintList(colorStateList);
                        navigationBarItemView.wrappedIconDrawable.invalidateSelf();
                    }
                }
            }
        } else {
            ColorStateList createDefaultColorStateList = createNavigationBarMenuView.createDefaultColorStateList();
            createNavigationBarMenuView.itemIconTint = createDefaultColorStateList;
            NavigationBarItemView[] navigationBarItemViewArr2 = createNavigationBarMenuView.buttons;
            if (navigationBarItemViewArr2 != null) {
                for (NavigationBarItemView navigationBarItemView2 : navigationBarItemViewArr2) {
                    Objects.requireNonNull(navigationBarItemView2);
                    navigationBarItemView2.iconTint = createDefaultColorStateList;
                    if (!(navigationBarItemView2.itemData == null || (drawable3 = navigationBarItemView2.wrappedIconDrawable) == null)) {
                        drawable3.setTintList(createDefaultColorStateList);
                        navigationBarItemView2.wrappedIconDrawable.invalidateSelf();
                    }
                }
            }
        }
        int dimensionPixelSize = obtainTintedStyledAttributes.getDimensionPixelSize(4, getResources().getDimensionPixelSize(2131166513));
        createNavigationBarMenuView.itemIconSize = dimensionPixelSize;
        NavigationBarItemView[] navigationBarItemViewArr3 = createNavigationBarMenuView.buttons;
        if (navigationBarItemViewArr3 != null) {
            for (NavigationBarItemView navigationBarItemView3 : navigationBarItemViewArr3) {
                Objects.requireNonNull(navigationBarItemView3);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) navigationBarItemView3.icon.getLayoutParams();
                layoutParams.width = dimensionPixelSize;
                layoutParams.height = dimensionPixelSize;
                navigationBarItemView3.icon.setLayoutParams(layoutParams);
            }
        }
        if (obtainTintedStyledAttributes.hasValue(10)) {
            int resourceId = obtainTintedStyledAttributes.getResourceId(10, 0);
            NavigationBarMenuView navigationBarMenuView = this.menuView;
            Objects.requireNonNull(navigationBarMenuView);
            navigationBarMenuView.itemTextAppearanceInactive = resourceId;
            NavigationBarItemView[] navigationBarItemViewArr4 = navigationBarMenuView.buttons;
            if (navigationBarItemViewArr4 != null) {
                for (NavigationBarItemView navigationBarItemView4 : navigationBarItemViewArr4) {
                    Objects.requireNonNull(navigationBarItemView4);
                    navigationBarItemView4.smallLabel.setTextAppearance(resourceId);
                    float textSize = navigationBarItemView4.smallLabel.getTextSize();
                    float textSize2 = navigationBarItemView4.largeLabel.getTextSize();
                    navigationBarItemView4.shiftAmount = textSize - textSize2;
                    navigationBarItemView4.scaleUpFactor = (textSize2 * 1.0f) / textSize;
                    navigationBarItemView4.scaleDownFactor = (textSize * 1.0f) / textSize2;
                    ColorStateList colorStateList2 = navigationBarMenuView.itemTextColorFromUser;
                    if (colorStateList2 != null) {
                        navigationBarItemView4.setTextColor(colorStateList2);
                    }
                }
            }
        }
        if (obtainTintedStyledAttributes.hasValue(9)) {
            int resourceId2 = obtainTintedStyledAttributes.getResourceId(9, 0);
            NavigationBarMenuView navigationBarMenuView2 = this.menuView;
            Objects.requireNonNull(navigationBarMenuView2);
            navigationBarMenuView2.itemTextAppearanceActive = resourceId2;
            NavigationBarItemView[] navigationBarItemViewArr5 = navigationBarMenuView2.buttons;
            if (navigationBarItemViewArr5 != null) {
                for (NavigationBarItemView navigationBarItemView5 : navigationBarItemViewArr5) {
                    Objects.requireNonNull(navigationBarItemView5);
                    navigationBarItemView5.largeLabel.setTextAppearance(resourceId2);
                    float textSize3 = navigationBarItemView5.smallLabel.getTextSize();
                    float textSize4 = navigationBarItemView5.largeLabel.getTextSize();
                    navigationBarItemView5.shiftAmount = textSize3 - textSize4;
                    navigationBarItemView5.scaleUpFactor = (textSize4 * 1.0f) / textSize3;
                    navigationBarItemView5.scaleDownFactor = (textSize3 * 1.0f) / textSize4;
                    ColorStateList colorStateList3 = navigationBarMenuView2.itemTextColorFromUser;
                    if (colorStateList3 != null) {
                        navigationBarItemView5.setTextColor(colorStateList3);
                    }
                }
            }
        }
        if (obtainTintedStyledAttributes.hasValue(11)) {
            ColorStateList colorStateList4 = obtainTintedStyledAttributes.getColorStateList(11);
            NavigationBarMenuView navigationBarMenuView3 = this.menuView;
            Objects.requireNonNull(navigationBarMenuView3);
            navigationBarMenuView3.itemTextColorFromUser = colorStateList4;
            NavigationBarItemView[] navigationBarItemViewArr6 = navigationBarMenuView3.buttons;
            if (navigationBarItemViewArr6 != null) {
                for (NavigationBarItemView navigationBarItemView6 : navigationBarItemViewArr6) {
                    navigationBarItemView6.setTextColor(colorStateList4);
                }
            }
        }
        if (getBackground() == null || (getBackground() instanceof ColorDrawable)) {
            MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
            Drawable background = getBackground();
            if (background instanceof ColorDrawable) {
                materialShapeDrawable.setFillColor(ColorStateList.valueOf(((ColorDrawable) background).getColor()));
            }
            materialShapeDrawable.initializeElevationOverlay(context2);
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.setBackground(this, materialShapeDrawable);
        }
        if (obtainTintedStyledAttributes.hasValue(7)) {
            int dimensionPixelSize2 = obtainTintedStyledAttributes.getDimensionPixelSize(7, 0);
            NavigationBarMenuView navigationBarMenuView4 = this.menuView;
            Objects.requireNonNull(navigationBarMenuView4);
            navigationBarMenuView4.itemPaddingTop = dimensionPixelSize2;
            NavigationBarItemView[] navigationBarItemViewArr7 = navigationBarMenuView4.buttons;
            if (navigationBarItemViewArr7 != null) {
                for (NavigationBarItemView navigationBarItemView7 : navigationBarItemViewArr7) {
                    Objects.requireNonNull(navigationBarItemView7);
                    if (navigationBarItemView7.itemPaddingTop != dimensionPixelSize2) {
                        navigationBarItemView7.itemPaddingTop = dimensionPixelSize2;
                        MenuItemImpl menuItemImpl = navigationBarItemView7.itemData;
                        if (menuItemImpl != null) {
                            navigationBarItemView7.setChecked(menuItemImpl.isChecked());
                        }
                    }
                }
            }
        }
        if (obtainTintedStyledAttributes.hasValue(6)) {
            int dimensionPixelSize3 = obtainTintedStyledAttributes.getDimensionPixelSize(6, 0);
            NavigationBarMenuView navigationBarMenuView5 = this.menuView;
            Objects.requireNonNull(navigationBarMenuView5);
            navigationBarMenuView5.itemPaddingBottom = dimensionPixelSize3;
            NavigationBarItemView[] navigationBarItemViewArr8 = navigationBarMenuView5.buttons;
            if (navigationBarItemViewArr8 != null) {
                for (NavigationBarItemView navigationBarItemView8 : navigationBarItemViewArr8) {
                    Objects.requireNonNull(navigationBarItemView8);
                    if (navigationBarItemView8.itemPaddingBottom != dimensionPixelSize3) {
                        navigationBarItemView8.itemPaddingBottom = dimensionPixelSize3;
                        MenuItemImpl menuItemImpl2 = navigationBarItemView8.itemData;
                        if (menuItemImpl2 != null) {
                            navigationBarItemView8.setChecked(menuItemImpl2.isChecked());
                        }
                    }
                }
            }
        }
        if (obtainTintedStyledAttributes.hasValue(1)) {
            setElevation(obtainTintedStyledAttributes.getDimensionPixelSize(1, 0));
        }
        getBackground().mutate().setTintList(MaterialResources.getColorStateList(context2, obtainTintedStyledAttributes, 0));
        int integer = obtainTintedStyledAttributes.mWrapped.getInteger(12, -1);
        NavigationBarMenuView navigationBarMenuView6 = this.menuView;
        Objects.requireNonNull(navigationBarMenuView6);
        if (navigationBarMenuView6.labelVisibilityMode != integer) {
            NavigationBarMenuView navigationBarMenuView7 = this.menuView;
            Objects.requireNonNull(navigationBarMenuView7);
            navigationBarMenuView7.labelVisibilityMode = integer;
            this.presenter.updateMenuView(false);
        }
        int resourceId3 = obtainTintedStyledAttributes.getResourceId(3, 0);
        if (resourceId3 != 0) {
            NavigationBarMenuView navigationBarMenuView8 = this.menuView;
            Objects.requireNonNull(navigationBarMenuView8);
            navigationBarMenuView8.itemBackgroundRes = resourceId3;
            NavigationBarItemView[] navigationBarItemViewArr9 = navigationBarMenuView8.buttons;
            if (navigationBarItemViewArr9 != null) {
                for (NavigationBarItemView navigationBarItemView9 : navigationBarItemViewArr9) {
                    Objects.requireNonNull(navigationBarItemView9);
                    if (resourceId3 == 0) {
                        drawable2 = null;
                    } else {
                        Context context3 = navigationBarItemView9.getContext();
                        Object obj = ContextCompat.sLock;
                        drawable2 = context3.getDrawable(resourceId3);
                    }
                    navigationBarItemView9.setItemBackground(drawable2);
                }
            }
        } else {
            ColorStateList colorStateList5 = MaterialResources.getColorStateList(context2, obtainTintedStyledAttributes, 8);
            if (this.itemRippleColor != colorStateList5) {
                this.itemRippleColor = colorStateList5;
                if (colorStateList5 == null) {
                    this.menuView.setItemBackground(null);
                } else {
                    this.menuView.setItemBackground(new RippleDrawable(RippleUtils.convertToRippleDrawableColor(colorStateList5), null, null));
                }
            } else if (colorStateList5 == null) {
                NavigationBarMenuView navigationBarMenuView9 = this.menuView;
                Objects.requireNonNull(navigationBarMenuView9);
                NavigationBarItemView[] navigationBarItemViewArr10 = navigationBarMenuView9.buttons;
                if (navigationBarItemViewArr10 == null || navigationBarItemViewArr10.length <= 0) {
                    drawable = navigationBarMenuView9.itemBackground;
                } else {
                    drawable = navigationBarItemViewArr10[0].getBackground();
                }
                if (drawable != null) {
                    this.menuView.setItemBackground(null);
                }
            }
        }
        int resourceId4 = obtainTintedStyledAttributes.getResourceId(2, 0);
        if (resourceId4 != 0) {
            NavigationBarMenuView navigationBarMenuView10 = this.menuView;
            Objects.requireNonNull(navigationBarMenuView10);
            navigationBarMenuView10.itemActiveIndicatorEnabled = true;
            NavigationBarItemView[] navigationBarItemViewArr11 = navigationBarMenuView10.buttons;
            if (navigationBarItemViewArr11 != null) {
                for (NavigationBarItemView navigationBarItemView10 : navigationBarItemViewArr11) {
                    Objects.requireNonNull(navigationBarItemView10);
                    navigationBarItemView10.activeIndicatorEnabled = true;
                    View view = navigationBarItemView10.activeIndicatorView;
                    if (view != null) {
                        view.setVisibility(0);
                        navigationBarItemView10.requestLayout();
                    }
                }
            }
            TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(resourceId4, R$styleable.NavigationBarActiveIndicator);
            int dimensionPixelSize4 = obtainStyledAttributes.getDimensionPixelSize(1, 0);
            NavigationBarMenuView navigationBarMenuView11 = this.menuView;
            Objects.requireNonNull(navigationBarMenuView11);
            navigationBarMenuView11.itemActiveIndicatorWidth = dimensionPixelSize4;
            NavigationBarItemView[] navigationBarItemViewArr12 = navigationBarMenuView11.buttons;
            if (navigationBarItemViewArr12 != null) {
                for (NavigationBarItemView navigationBarItemView11 : navigationBarItemViewArr12) {
                    Objects.requireNonNull(navigationBarItemView11);
                    navigationBarItemView11.activeIndicatorDesiredWidth = dimensionPixelSize4;
                    navigationBarItemView11.updateActiveIndicatorLayoutParams(navigationBarItemView11.getWidth());
                }
            }
            int dimensionPixelSize5 = obtainStyledAttributes.getDimensionPixelSize(0, 0);
            NavigationBarMenuView navigationBarMenuView12 = this.menuView;
            Objects.requireNonNull(navigationBarMenuView12);
            navigationBarMenuView12.itemActiveIndicatorHeight = dimensionPixelSize5;
            NavigationBarItemView[] navigationBarItemViewArr13 = navigationBarMenuView12.buttons;
            if (navigationBarItemViewArr13 != null) {
                for (NavigationBarItemView navigationBarItemView12 : navigationBarItemViewArr13) {
                    Objects.requireNonNull(navigationBarItemView12);
                    navigationBarItemView12.activeIndicatorDesiredHeight = dimensionPixelSize5;
                    navigationBarItemView12.updateActiveIndicatorLayoutParams(navigationBarItemView12.getWidth());
                }
            }
            int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(3, 0);
            NavigationBarMenuView navigationBarMenuView13 = this.menuView;
            Objects.requireNonNull(navigationBarMenuView13);
            navigationBarMenuView13.itemActiveIndicatorMarginHorizontal = dimensionPixelOffset;
            NavigationBarItemView[] navigationBarItemViewArr14 = navigationBarMenuView13.buttons;
            if (navigationBarItemViewArr14 != null) {
                for (NavigationBarItemView navigationBarItemView13 : navigationBarItemViewArr14) {
                    Objects.requireNonNull(navigationBarItemView13);
                    navigationBarItemView13.activeIndicatorMarginHorizontal = dimensionPixelOffset;
                    navigationBarItemView13.updateActiveIndicatorLayoutParams(navigationBarItemView13.getWidth());
                }
            }
            ColorStateList colorStateList6 = MaterialResources.getColorStateList(context2, obtainStyledAttributes, 2);
            NavigationBarMenuView navigationBarMenuView14 = this.menuView;
            Objects.requireNonNull(navigationBarMenuView14);
            navigationBarMenuView14.itemActiveIndicatorColor = colorStateList6;
            NavigationBarItemView[] navigationBarItemViewArr15 = navigationBarMenuView14.buttons;
            if (navigationBarItemViewArr15 != null) {
                for (NavigationBarItemView navigationBarItemView14 : navigationBarItemViewArr15) {
                    MaterialShapeDrawable createItemActiveIndicatorDrawable = navigationBarMenuView14.createItemActiveIndicatorDrawable();
                    Objects.requireNonNull(navigationBarItemView14);
                    View view2 = navigationBarItemView14.activeIndicatorView;
                    if (view2 != null) {
                        view2.setBackgroundDrawable(createItemActiveIndicatorDrawable);
                    }
                }
            }
            ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel(ShapeAppearanceModel.builder(context2, obtainStyledAttributes.getResourceId(4, 0), 0, new AbsoluteCornerSize(0)));
            NavigationBarMenuView navigationBarMenuView15 = this.menuView;
            Objects.requireNonNull(navigationBarMenuView15);
            navigationBarMenuView15.itemActiveIndicatorShapeAppearance = shapeAppearanceModel;
            NavigationBarItemView[] navigationBarItemViewArr16 = navigationBarMenuView15.buttons;
            if (navigationBarItemViewArr16 != null) {
                for (NavigationBarItemView navigationBarItemView15 : navigationBarItemViewArr16) {
                    MaterialShapeDrawable createItemActiveIndicatorDrawable2 = navigationBarMenuView15.createItemActiveIndicatorDrawable();
                    Objects.requireNonNull(navigationBarItemView15);
                    View view3 = navigationBarItemView15.activeIndicatorView;
                    if (view3 != null) {
                        view3.setBackgroundDrawable(createItemActiveIndicatorDrawable2);
                    }
                }
            }
            obtainStyledAttributes.recycle();
        }
        if (obtainTintedStyledAttributes.hasValue(13)) {
            int resourceId5 = obtainTintedStyledAttributes.getResourceId(13, 0);
            NavigationBarPresenter navigationBarPresenter2 = this.presenter;
            Objects.requireNonNull(navigationBarPresenter2);
            navigationBarPresenter2.updateSuspended = true;
            if (this.menuInflater == null) {
                this.menuInflater = new SupportMenuInflater(getContext());
            }
            this.menuInflater.inflate(resourceId5, this.menu);
            NavigationBarPresenter navigationBarPresenter3 = this.presenter;
            Objects.requireNonNull(navigationBarPresenter3);
            navigationBarPresenter3.updateSuspended = false;
            this.presenter.updateMenuView(true);
        }
        obtainTintedStyledAttributes.recycle();
        addView(this.menuView);
        NavigationBarMenu navigationBarMenu2 = this.menu;
        MenuBuilder.Callback callback = new MenuBuilder.Callback() { // from class: com.google.android.material.navigation.NavigationBarView.1
            @Override // androidx.appcompat.view.menu.MenuBuilder.Callback
            public final void onMenuModeChange(MenuBuilder menuBuilder) {
            }

            @Override // androidx.appcompat.view.menu.MenuBuilder.Callback
            public final boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                Objects.requireNonNull(NavigationBarView.this);
                Objects.requireNonNull(NavigationBarView.this);
                return false;
            }
        };
        Objects.requireNonNull(navigationBarMenu2);
        navigationBarMenu2.mCallback = callback;
    }

    @Override // android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        Objects.requireNonNull(savedState);
        super.onRestoreInstanceState(savedState.mSuperState);
        this.menu.restorePresenterStates(savedState.menuPresenterState);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        R$bool.setParentAbsoluteElevation(this);
    }

    @Override // android.view.View
    public final Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        Bundle bundle = new Bundle();
        savedState.menuPresenterState = bundle;
        this.menu.savePresenterStates(bundle);
        return savedState;
    }

    @Override // android.view.View
    public final void setElevation(float f) {
        super.setElevation(f);
        R$bool.setElevation(this, f);
    }
}
