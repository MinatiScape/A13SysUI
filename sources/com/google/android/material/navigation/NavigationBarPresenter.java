package com.google.android.material.navigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.transition.TransitionManager;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.internal.ParcelableSparseArray;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.shape.MaterialShapeDrawable;
import java.lang.ref.WeakReference;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NavigationBarPresenter implements MenuPresenter {
    public int id;
    public NavigationBarMenuView menuView;
    public boolean updateSuspended = false;

    /* loaded from: classes.dex */
    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.google.android.material.navigation.NavigationBarPresenter.SavedState.1
            @Override // android.os.Parcelable.Creator
            public final SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public ParcelableSparseArray badgeSavedStates;
        public int selectedItemId;

        public SavedState() {
        }

        @Override // android.os.Parcelable
        public final int describeContents() {
            return 0;
        }

        public SavedState(Parcel parcel) {
            this.selectedItemId = parcel.readInt();
            this.badgeSavedStates = (ParcelableSparseArray) parcel.readParcelable(getClass().getClassLoader());
        }

        @Override // android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.selectedItemId);
            parcel.writeParcelable(this.badgeSavedStates, 0);
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final boolean collapseItemActionView(MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final boolean expandItemActionView(MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final boolean flagActionItems() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void initForMenu(Context context, MenuBuilder menuBuilder) {
        NavigationBarMenuView navigationBarMenuView = this.menuView;
        Objects.requireNonNull(navigationBarMenuView);
        navigationBarMenuView.menu = menuBuilder;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void onRestoreInstanceState(Parcelable parcelable) {
        FrameLayout frameLayout;
        if (parcelable instanceof SavedState) {
            NavigationBarMenuView navigationBarMenuView = this.menuView;
            SavedState savedState = (SavedState) parcelable;
            int i = savedState.selectedItemId;
            Objects.requireNonNull(navigationBarMenuView);
            int size = navigationBarMenuView.menu.size();
            int i2 = 0;
            while (true) {
                if (i2 >= size) {
                    break;
                }
                MenuItem item = navigationBarMenuView.menu.getItem(i2);
                if (i == item.getItemId()) {
                    navigationBarMenuView.selectedItemId = i;
                    navigationBarMenuView.selectedItemPosition = i2;
                    item.setChecked(true);
                    break;
                }
                i2++;
            }
            Context context = this.menuView.getContext();
            ParcelableSparseArray parcelableSparseArray = savedState.badgeSavedStates;
            SparseArray<BadgeDrawable> sparseArray = new SparseArray<>(parcelableSparseArray.size());
            for (int i3 = 0; i3 < parcelableSparseArray.size(); i3++) {
                int keyAt = parcelableSparseArray.keyAt(i3);
                BadgeDrawable.SavedState savedState2 = (BadgeDrawable.SavedState) parcelableSparseArray.valueAt(i3);
                if (savedState2 != null) {
                    BadgeDrawable badgeDrawable = new BadgeDrawable(context);
                    int i4 = savedState2.maxCharacterCount;
                    BadgeDrawable.SavedState savedState3 = badgeDrawable.savedState;
                    if (savedState3.maxCharacterCount != i4) {
                        savedState3.maxCharacterCount = i4;
                        badgeDrawable.maxBadgeNumber = ((int) Math.pow(10.0d, i4 - 1.0d)) - 1;
                        TextDrawableHelper textDrawableHelper = badgeDrawable.textDrawableHelper;
                        Objects.requireNonNull(textDrawableHelper);
                        textDrawableHelper.textWidthDirty = true;
                        badgeDrawable.updateCenterAndBounds();
                        badgeDrawable.invalidateSelf();
                    }
                    int i5 = savedState2.number;
                    if (i5 != -1) {
                        int max = Math.max(0, i5);
                        BadgeDrawable.SavedState savedState4 = badgeDrawable.savedState;
                        if (savedState4.number != max) {
                            savedState4.number = max;
                            TextDrawableHelper textDrawableHelper2 = badgeDrawable.textDrawableHelper;
                            Objects.requireNonNull(textDrawableHelper2);
                            textDrawableHelper2.textWidthDirty = true;
                            badgeDrawable.updateCenterAndBounds();
                            badgeDrawable.invalidateSelf();
                        }
                    }
                    int i6 = savedState2.backgroundColor;
                    badgeDrawable.savedState.backgroundColor = i6;
                    ColorStateList valueOf = ColorStateList.valueOf(i6);
                    MaterialShapeDrawable materialShapeDrawable = badgeDrawable.shapeDrawable;
                    Objects.requireNonNull(materialShapeDrawable);
                    if (materialShapeDrawable.drawableState.fillColor != valueOf) {
                        badgeDrawable.shapeDrawable.setFillColor(valueOf);
                        badgeDrawable.invalidateSelf();
                    }
                    int i7 = savedState2.badgeTextColor;
                    badgeDrawable.savedState.badgeTextColor = i7;
                    TextDrawableHelper textDrawableHelper3 = badgeDrawable.textDrawableHelper;
                    Objects.requireNonNull(textDrawableHelper3);
                    if (textDrawableHelper3.textPaint.getColor() != i7) {
                        TextDrawableHelper textDrawableHelper4 = badgeDrawable.textDrawableHelper;
                        Objects.requireNonNull(textDrawableHelper4);
                        textDrawableHelper4.textPaint.setColor(i7);
                        badgeDrawable.invalidateSelf();
                    }
                    int i8 = savedState2.badgeGravity;
                    BadgeDrawable.SavedState savedState5 = badgeDrawable.savedState;
                    if (savedState5.badgeGravity != i8) {
                        savedState5.badgeGravity = i8;
                        WeakReference<View> weakReference = badgeDrawable.anchorViewRef;
                        if (!(weakReference == null || weakReference.get() == null)) {
                            View view = badgeDrawable.anchorViewRef.get();
                            WeakReference<FrameLayout> weakReference2 = badgeDrawable.customBadgeParentRef;
                            if (weakReference2 != null) {
                                frameLayout = weakReference2.get();
                            } else {
                                frameLayout = null;
                            }
                            badgeDrawable.updateBadgeCoordinates(view, frameLayout);
                        }
                    }
                    badgeDrawable.savedState.horizontalOffsetWithoutText = savedState2.horizontalOffsetWithoutText;
                    badgeDrawable.updateCenterAndBounds();
                    badgeDrawable.savedState.verticalOffsetWithoutText = savedState2.verticalOffsetWithoutText;
                    badgeDrawable.updateCenterAndBounds();
                    badgeDrawable.savedState.horizontalOffsetWithText = savedState2.horizontalOffsetWithText;
                    badgeDrawable.updateCenterAndBounds();
                    badgeDrawable.savedState.verticalOffsetWithText = savedState2.verticalOffsetWithText;
                    badgeDrawable.updateCenterAndBounds();
                    badgeDrawable.savedState.additionalHorizontalOffset = savedState2.additionalHorizontalOffset;
                    badgeDrawable.updateCenterAndBounds();
                    badgeDrawable.savedState.additionalVerticalOffset = savedState2.additionalVerticalOffset;
                    badgeDrawable.updateCenterAndBounds();
                    boolean z = savedState2.isVisible;
                    badgeDrawable.setVisible(z, false);
                    badgeDrawable.savedState.isVisible = z;
                    sparseArray.put(keyAt, badgeDrawable);
                } else {
                    throw new IllegalArgumentException("BadgeDrawable's savedState cannot be null");
                }
            }
            NavigationBarMenuView navigationBarMenuView2 = this.menuView;
            Objects.requireNonNull(navigationBarMenuView2);
            navigationBarMenuView2.badgeDrawables = sparseArray;
            NavigationBarItemView[] navigationBarItemViewArr = navigationBarMenuView2.buttons;
            if (navigationBarItemViewArr != null) {
                for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                    navigationBarItemView.setBadge(sparseArray.get(navigationBarItemView.getId()));
                }
            }
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState();
        NavigationBarMenuView navigationBarMenuView = this.menuView;
        Objects.requireNonNull(navigationBarMenuView);
        savedState.selectedItemId = navigationBarMenuView.selectedItemId;
        NavigationBarMenuView navigationBarMenuView2 = this.menuView;
        Objects.requireNonNull(navigationBarMenuView2);
        SparseArray<BadgeDrawable> sparseArray = navigationBarMenuView2.badgeDrawables;
        ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
        for (int i = 0; i < sparseArray.size(); i++) {
            int keyAt = sparseArray.keyAt(i);
            BadgeDrawable valueAt = sparseArray.valueAt(i);
            if (valueAt != null) {
                parcelableSparseArray.put(keyAt, valueAt.savedState);
            } else {
                throw new IllegalArgumentException("badgeDrawable cannot be null");
            }
        }
        savedState.badgeSavedStates = parcelableSparseArray;
        return savedState;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void updateMenuView(boolean z) {
        if (!this.updateSuspended) {
            if (z) {
                this.menuView.buildMenuView();
                return;
            }
            NavigationBarMenuView navigationBarMenuView = this.menuView;
            Objects.requireNonNull(navigationBarMenuView);
            MenuBuilder menuBuilder = navigationBarMenuView.menu;
            if (!(menuBuilder == null || navigationBarMenuView.buttons == null)) {
                int size = menuBuilder.size();
                if (size != navigationBarMenuView.buttons.length) {
                    navigationBarMenuView.buildMenuView();
                    return;
                }
                int i = navigationBarMenuView.selectedItemId;
                for (int i2 = 0; i2 < size; i2++) {
                    MenuItem item = navigationBarMenuView.menu.getItem(i2);
                    if (item.isChecked()) {
                        navigationBarMenuView.selectedItemId = item.getItemId();
                        navigationBarMenuView.selectedItemPosition = i2;
                    }
                }
                if (i != navigationBarMenuView.selectedItemId) {
                    TransitionManager.beginDelayedTransition(navigationBarMenuView, navigationBarMenuView.set);
                }
                boolean isShifting = NavigationBarMenuView.isShifting(navigationBarMenuView.labelVisibilityMode, navigationBarMenuView.menu.getVisibleItems().size());
                for (int i3 = 0; i3 < size; i3++) {
                    NavigationBarPresenter navigationBarPresenter = navigationBarMenuView.presenter;
                    Objects.requireNonNull(navigationBarPresenter);
                    navigationBarPresenter.updateSuspended = true;
                    navigationBarMenuView.buttons[i3].setLabelVisibilityMode(navigationBarMenuView.labelVisibilityMode);
                    NavigationBarItemView navigationBarItemView = navigationBarMenuView.buttons[i3];
                    Objects.requireNonNull(navigationBarItemView);
                    if (navigationBarItemView.isShifting != isShifting) {
                        navigationBarItemView.isShifting = isShifting;
                        MenuItemImpl menuItemImpl = navigationBarItemView.itemData;
                        if (menuItemImpl != null) {
                            navigationBarItemView.setChecked(menuItemImpl.isChecked());
                        }
                    }
                    navigationBarMenuView.buttons[i3].initialize((MenuItemImpl) navigationBarMenuView.menu.getItem(i3));
                    NavigationBarPresenter navigationBarPresenter2 = navigationBarMenuView.presenter;
                    Objects.requireNonNull(navigationBarPresenter2);
                    navigationBarPresenter2.updateSuspended = false;
                }
            }
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final int getId() {
        return this.id;
    }
}
