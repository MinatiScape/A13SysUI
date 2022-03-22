package com.google.android.material.navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pools$SynchronizedPool;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.transition.AutoTransition;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.internal.TextScale;
import com.google.android.material.motion.MotionUtils;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.util.HashSet;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public abstract class NavigationBarMenuView extends ViewGroup implements MenuView {
    public static final int[] CHECKED_STATE_SET = {16842912};
    public static final int[] DISABLED_STATE_SET = {-16842910};
    public NavigationBarItemView[] buttons;
    public ColorStateList itemActiveIndicatorColor;
    public boolean itemActiveIndicatorEnabled;
    public int itemActiveIndicatorHeight;
    public int itemActiveIndicatorMarginHorizontal;
    public ShapeAppearanceModel itemActiveIndicatorShapeAppearance;
    public int itemActiveIndicatorWidth;
    public Drawable itemBackground;
    public int itemBackgroundRes;
    public int itemIconSize;
    public ColorStateList itemIconTint;
    public int itemTextAppearanceActive;
    public int itemTextAppearanceInactive;
    public ColorStateList itemTextColorFromUser;
    public int labelVisibilityMode;
    public MenuBuilder menu;
    public final AnonymousClass1 onClickListener;
    public NavigationBarPresenter presenter;
    public final AutoTransition set;
    public final Pools$SynchronizedPool itemPool = new Pools$SynchronizedPool(5);
    public final SparseArray<View.OnTouchListener> onTouchListeners = new SparseArray<>(5);
    public int selectedItemId = 0;
    public int selectedItemPosition = 0;
    public SparseArray<BadgeDrawable> badgeDrawables = new SparseArray<>(5);
    public int itemPaddingTop = -1;
    public int itemPaddingBottom = -1;
    public boolean itemActiveIndicatorResizeable = false;
    public final ColorStateList itemTextColorDefault = createDefaultColorStateList();

    public static boolean isShifting(int i, int i2) {
        return i != -1 ? i == 0 : i2 > 3;
    }

    public abstract NavigationBarItemView createNavigationBarItemView(Context context);

    public final ColorStateList createDefaultColorStateList() {
        TypedValue typedValue = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(16842808, typedValue, true)) {
            return null;
        }
        ColorStateList colorStateList = AppCompatResources.getColorStateList(getContext(), typedValue.resourceId);
        if (!getContext().getTheme().resolveAttribute(2130968838, typedValue, true)) {
            return null;
        }
        int i = typedValue.data;
        int defaultColor = colorStateList.getDefaultColor();
        int[] iArr = DISABLED_STATE_SET;
        return new ColorStateList(new int[][]{iArr, CHECKED_STATE_SET, ViewGroup.EMPTY_STATE_SET}, new int[]{colorStateList.getColorForState(iArr, defaultColor), i, defaultColor});
    }

    public final MaterialShapeDrawable createItemActiveIndicatorDrawable() {
        if (this.itemActiveIndicatorShapeAppearance == null || this.itemActiveIndicatorColor == null) {
            return null;
        }
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(this.itemActiveIndicatorShapeAppearance);
        materialShapeDrawable.setFillColor(this.itemActiveIndicatorColor);
        return materialShapeDrawable;
    }

    public final void setItemBackground(Drawable drawable) {
        this.itemBackground = drawable;
        NavigationBarItemView[] navigationBarItemViewArr = this.buttons;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setItemBackground(drawable);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r5v9, types: [com.google.android.material.navigation.NavigationBarMenuView$1] */
    public NavigationBarMenuView(Context context) {
        super(context);
        AutoTransition autoTransition = new AutoTransition();
        this.set = autoTransition;
        autoTransition.setOrdering(0);
        Context context2 = getContext();
        int integer = getResources().getInteger(2131492994);
        TypedValue resolve = MaterialAttributes.resolve(context2, 2130969488);
        if (resolve != null && resolve.type == 16) {
            integer = resolve.data;
        }
        autoTransition.setDuration(integer);
        autoTransition.setInterpolator(MotionUtils.resolveThemeInterpolator(getContext(), AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR));
        autoTransition.addTransition(new TextScale());
        this.onClickListener = new View.OnClickListener() { // from class: com.google.android.material.navigation.NavigationBarMenuView.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NavigationBarItemView navigationBarItemView = (NavigationBarItemView) view;
                Objects.requireNonNull(navigationBarItemView);
                MenuItemImpl menuItemImpl = navigationBarItemView.itemData;
                NavigationBarMenuView navigationBarMenuView = NavigationBarMenuView.this;
                if (!navigationBarMenuView.menu.performItemAction(menuItemImpl, navigationBarMenuView.presenter, 0)) {
                    menuItemImpl.setChecked(true);
                }
            }
        };
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api16Impl.setImportantForAccessibility(this, 1);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public final void buildMenuView() {
        boolean z;
        BadgeDrawable badgeDrawable;
        Drawable drawable;
        int i;
        Drawable drawable2;
        boolean z2;
        removeAllViews();
        NavigationBarItemView[] navigationBarItemViewArr = this.buttons;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                if (navigationBarItemView != null) {
                    this.itemPool.release(navigationBarItemView);
                    ImageView imageView = navigationBarItemView.icon;
                    if (navigationBarItemView.badgeDrawable != null) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (z2) {
                        if (imageView != null) {
                            navigationBarItemView.setClipChildren(true);
                            navigationBarItemView.setClipToPadding(true);
                            BadgeDrawable badgeDrawable2 = navigationBarItemView.badgeDrawable;
                            if (badgeDrawable2 != null) {
                                if (badgeDrawable2.getCustomBadgeParent() != null) {
                                    badgeDrawable2.getCustomBadgeParent().setForeground(null);
                                } else {
                                    imageView.getOverlay().remove(badgeDrawable2);
                                }
                            }
                        }
                        navigationBarItemView.badgeDrawable = null;
                    }
                    navigationBarItemView.itemData = null;
                    navigationBarItemView.activeIndicatorProgress = 0.0f;
                    navigationBarItemView.initialized = false;
                }
            }
        }
        if (this.menu.size() == 0) {
            this.selectedItemId = 0;
            this.selectedItemPosition = 0;
            this.buttons = null;
            return;
        }
        HashSet hashSet = new HashSet();
        for (int i2 = 0; i2 < this.menu.size(); i2++) {
            hashSet.add(Integer.valueOf(this.menu.getItem(i2).getItemId()));
        }
        for (int i3 = 0; i3 < this.badgeDrawables.size(); i3++) {
            int keyAt = this.badgeDrawables.keyAt(i3);
            if (!hashSet.contains(Integer.valueOf(keyAt))) {
                this.badgeDrawables.delete(keyAt);
            }
        }
        this.buttons = new NavigationBarItemView[this.menu.size()];
        boolean isShifting = isShifting(this.labelVisibilityMode, this.menu.getVisibleItems().size());
        for (int i4 = 0; i4 < this.menu.size(); i4++) {
            NavigationBarPresenter navigationBarPresenter = this.presenter;
            Objects.requireNonNull(navigationBarPresenter);
            navigationBarPresenter.updateSuspended = true;
            this.menu.getItem(i4).setCheckable(true);
            NavigationBarPresenter navigationBarPresenter2 = this.presenter;
            Objects.requireNonNull(navigationBarPresenter2);
            navigationBarPresenter2.updateSuspended = false;
            NavigationBarItemView navigationBarItemView2 = (NavigationBarItemView) this.itemPool.acquire();
            if (navigationBarItemView2 == null) {
                navigationBarItemView2 = createNavigationBarItemView(getContext());
            }
            this.buttons[i4] = navigationBarItemView2;
            ColorStateList colorStateList = this.itemIconTint;
            Objects.requireNonNull(navigationBarItemView2);
            navigationBarItemView2.iconTint = colorStateList;
            if (!(navigationBarItemView2.itemData == null || (drawable2 = navigationBarItemView2.wrappedIconDrawable) == null)) {
                drawable2.setTintList(colorStateList);
                navigationBarItemView2.wrappedIconDrawable.invalidateSelf();
            }
            int i5 = this.itemIconSize;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) navigationBarItemView2.icon.getLayoutParams();
            layoutParams.width = i5;
            layoutParams.height = i5;
            navigationBarItemView2.icon.setLayoutParams(layoutParams);
            navigationBarItemView2.setTextColor(this.itemTextColorDefault);
            navigationBarItemView2.smallLabel.setTextAppearance(this.itemTextAppearanceInactive);
            float textSize = navigationBarItemView2.smallLabel.getTextSize();
            float textSize2 = navigationBarItemView2.largeLabel.getTextSize();
            navigationBarItemView2.shiftAmount = textSize - textSize2;
            navigationBarItemView2.scaleUpFactor = (textSize2 * 1.0f) / textSize;
            navigationBarItemView2.scaleDownFactor = (textSize * 1.0f) / textSize2;
            navigationBarItemView2.largeLabel.setTextAppearance(this.itemTextAppearanceActive);
            float textSize3 = navigationBarItemView2.smallLabel.getTextSize();
            float textSize4 = navigationBarItemView2.largeLabel.getTextSize();
            navigationBarItemView2.shiftAmount = textSize3 - textSize4;
            navigationBarItemView2.scaleUpFactor = (textSize4 * 1.0f) / textSize3;
            navigationBarItemView2.scaleDownFactor = (textSize3 * 1.0f) / textSize4;
            navigationBarItemView2.setTextColor(this.itemTextColorFromUser);
            int i6 = this.itemPaddingTop;
            if (!(i6 == -1 || navigationBarItemView2.itemPaddingTop == i6)) {
                navigationBarItemView2.itemPaddingTop = i6;
                MenuItemImpl menuItemImpl = navigationBarItemView2.itemData;
                if (menuItemImpl != null) {
                    navigationBarItemView2.setChecked(menuItemImpl.isChecked());
                }
            }
            int i7 = this.itemPaddingBottom;
            if (!(i7 == -1 || navigationBarItemView2.itemPaddingBottom == i7)) {
                navigationBarItemView2.itemPaddingBottom = i7;
                MenuItemImpl menuItemImpl2 = navigationBarItemView2.itemData;
                if (menuItemImpl2 != null) {
                    navigationBarItemView2.setChecked(menuItemImpl2.isChecked());
                }
            }
            navigationBarItemView2.activeIndicatorDesiredWidth = this.itemActiveIndicatorWidth;
            navigationBarItemView2.updateActiveIndicatorLayoutParams(navigationBarItemView2.getWidth());
            navigationBarItemView2.activeIndicatorDesiredHeight = this.itemActiveIndicatorHeight;
            navigationBarItemView2.updateActiveIndicatorLayoutParams(navigationBarItemView2.getWidth());
            navigationBarItemView2.activeIndicatorMarginHorizontal = this.itemActiveIndicatorMarginHorizontal;
            navigationBarItemView2.updateActiveIndicatorLayoutParams(navigationBarItemView2.getWidth());
            MaterialShapeDrawable createItemActiveIndicatorDrawable = createItemActiveIndicatorDrawable();
            View view = navigationBarItemView2.activeIndicatorView;
            if (view != null) {
                view.setBackgroundDrawable(createItemActiveIndicatorDrawable);
            }
            navigationBarItemView2.activeIndicatorResizeable = this.itemActiveIndicatorResizeable;
            boolean z3 = this.itemActiveIndicatorEnabled;
            navigationBarItemView2.activeIndicatorEnabled = z3;
            View view2 = navigationBarItemView2.activeIndicatorView;
            if (view2 != null) {
                if (z3) {
                    i = 0;
                } else {
                    i = 8;
                }
                view2.setVisibility(i);
                navigationBarItemView2.requestLayout();
            }
            Drawable drawable3 = this.itemBackground;
            if (drawable3 != null) {
                navigationBarItemView2.setItemBackground(drawable3);
            } else {
                int i8 = this.itemBackgroundRes;
                if (i8 == 0) {
                    drawable = null;
                } else {
                    Context context = navigationBarItemView2.getContext();
                    Object obj = ContextCompat.sLock;
                    drawable = context.getDrawable(i8);
                }
                navigationBarItemView2.setItemBackground(drawable);
            }
            if (navigationBarItemView2.isShifting != isShifting) {
                navigationBarItemView2.isShifting = isShifting;
                MenuItemImpl menuItemImpl3 = navigationBarItemView2.itemData;
                if (menuItemImpl3 != null) {
                    navigationBarItemView2.setChecked(menuItemImpl3.isChecked());
                }
            }
            navigationBarItemView2.setLabelVisibilityMode(this.labelVisibilityMode);
            MenuItemImpl menuItemImpl4 = (MenuItemImpl) this.menu.getItem(i4);
            navigationBarItemView2.initialize(menuItemImpl4);
            int i9 = menuItemImpl4.mId;
            navigationBarItemView2.setOnTouchListener(this.onTouchListeners.get(i9));
            navigationBarItemView2.setOnClickListener(this.onClickListener);
            int i10 = this.selectedItemId;
            if (i10 != 0 && i9 == i10) {
                this.selectedItemPosition = i4;
            }
            int id = navigationBarItemView2.getId();
            if (id != -1) {
                z = true;
            } else {
                z = false;
            }
            if (z && (badgeDrawable = this.badgeDrawables.get(id)) != null) {
                navigationBarItemView2.setBadge(badgeDrawable);
            }
            addView(navigationBarItemView2);
        }
        int min = Math.min(this.menu.size() - 1, this.selectedItemPosition);
        this.selectedItemPosition = min;
        this.menu.getItem(min).setChecked(true);
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setCollectionInfo((AccessibilityNodeInfo.CollectionInfo) AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, this.menu.getVisibleItems().size(), 1).mInfo);
    }

    @Override // androidx.appcompat.view.menu.MenuView
    public final void initialize(MenuBuilder menuBuilder) {
        this.menu = menuBuilder;
    }
}
