package com.google.android.material.navigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.badge.BadgeDrawable;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public abstract class NavigationBarItemView extends FrameLayout implements MenuView.ItemView {
    public ValueAnimator activeIndicatorAnimator;
    public BadgeDrawable badgeDrawable;
    public final ImageView icon;
    public ColorStateList iconTint;
    public boolean isShifting;
    public MenuItemImpl itemData;
    public int itemPaddingBottom;
    public final ViewGroup labelGroup;
    public int labelVisibilityMode;
    public final TextView largeLabel;
    public Drawable originalIconDrawable;
    public float scaleDownFactor;
    public float scaleUpFactor;
    public float shiftAmount;
    public final TextView smallLabel;
    public Drawable wrappedIconDrawable;
    public static final int[] CHECKED_STATE_SET = {16842912};
    public static final ActiveIndicatorTransform ACTIVE_INDICATOR_LABELED_TRANSFORM = new ActiveIndicatorTransform();
    public static final ActiveIndicatorUnlabeledTransform ACTIVE_INDICATOR_UNLABELED_TRANSFORM = new ActiveIndicatorUnlabeledTransform();
    public boolean initialized = false;
    public ActiveIndicatorTransform activeIndicatorTransform = ACTIVE_INDICATOR_LABELED_TRANSFORM;
    public float activeIndicatorProgress = 0.0f;
    public boolean activeIndicatorEnabled = false;
    public int activeIndicatorDesiredWidth = 0;
    public int activeIndicatorDesiredHeight = 0;
    public boolean activeIndicatorResizeable = false;
    public int activeIndicatorMarginHorizontal = 0;
    public final FrameLayout iconContainer = (FrameLayout) findViewById(2131428481);
    public final View activeIndicatorView = findViewById(2131428480);
    public int itemPaddingTop = getResources().getDimensionPixelSize(getItemDefaultMarginResId());

    /* loaded from: classes.dex */
    public static class ActiveIndicatorTransform {
        public float calculateScaleY(float f, float f2) {
            return 1.0f;
        }
    }

    /* loaded from: classes.dex */
    public static class ActiveIndicatorUnlabeledTransform extends ActiveIndicatorTransform {
        @Override // com.google.android.material.navigation.NavigationBarItemView.ActiveIndicatorTransform
        public final float calculateScaleY(float f, float f2) {
            LinearInterpolator linearInterpolator = AnimationUtils.LINEAR_INTERPOLATOR;
            return (f * 0.6f) + 0.4f;
        }
    }

    public int getItemDefaultMarginResId() {
        return 2131166514;
    }

    public abstract int getItemLayoutResId();

    public final View getIconOrContainer() {
        FrameLayout frameLayout = this.iconContainer;
        if (frameLayout != null) {
            return frameLayout;
        }
        return this.icon;
    }

    @Override // android.view.View
    public final int getSuggestedMinimumHeight() {
        int i;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.labelGroup.getLayoutParams();
        BadgeDrawable badgeDrawable = this.badgeDrawable;
        if (badgeDrawable != null) {
            i = badgeDrawable.getMinimumHeight() / 2;
        } else {
            i = 0;
        }
        int max = Math.max(i, ((FrameLayout.LayoutParams) getIconOrContainer().getLayoutParams()).topMargin);
        return this.labelGroup.getMeasuredHeight() + this.icon.getMeasuredWidth() + max + i + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    @Override // android.view.View
    public final int getSuggestedMinimumWidth() {
        int i;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.labelGroup.getLayoutParams();
        int measuredWidth = this.labelGroup.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
        BadgeDrawable badgeDrawable = this.badgeDrawable;
        if (badgeDrawable == null) {
            i = 0;
        } else {
            int minimumWidth = badgeDrawable.getMinimumWidth();
            BadgeDrawable badgeDrawable2 = this.badgeDrawable;
            Objects.requireNonNull(badgeDrawable2);
            i = minimumWidth - badgeDrawable2.savedState.horizontalOffsetWithoutText;
        }
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) getIconOrContainer().getLayoutParams();
        int max = Math.max(i, layoutParams2.leftMargin);
        return Math.max(Math.max(i, layoutParams2.rightMargin) + this.icon.getMeasuredWidth() + max, measuredWidth);
    }

    @Override // androidx.appcompat.view.menu.MenuView.ItemView
    public final void initialize(MenuItemImpl menuItemImpl) {
        CharSequence charSequence;
        int i;
        this.itemData = menuItemImpl;
        Objects.requireNonNull(menuItemImpl);
        refreshDrawableState();
        setChecked(menuItemImpl.isChecked());
        setEnabled(menuItemImpl.isEnabled());
        Drawable icon = menuItemImpl.getIcon();
        if (icon != this.originalIconDrawable) {
            this.originalIconDrawable = icon;
            if (icon != null) {
                Drawable.ConstantState constantState = icon.getConstantState();
                if (constantState != null) {
                    icon = constantState.newDrawable();
                }
                icon = icon.mutate();
                this.wrappedIconDrawable = icon;
                ColorStateList colorStateList = this.iconTint;
                if (colorStateList != null) {
                    icon.setTintList(colorStateList);
                }
            }
            this.icon.setImageDrawable(icon);
        }
        CharSequence charSequence2 = menuItemImpl.mTitle;
        this.smallLabel.setText(charSequence2);
        this.largeLabel.setText(charSequence2);
        MenuItemImpl menuItemImpl2 = this.itemData;
        if (menuItemImpl2 == null || TextUtils.isEmpty(menuItemImpl2.mContentDescription)) {
            setContentDescription(charSequence2);
        }
        MenuItemImpl menuItemImpl3 = this.itemData;
        if (menuItemImpl3 != null && !TextUtils.isEmpty(menuItemImpl3.mTooltipText)) {
            MenuItemImpl menuItemImpl4 = this.itemData;
            Objects.requireNonNull(menuItemImpl4);
            charSequence2 = menuItemImpl4.mTooltipText;
        }
        setTooltipText(charSequence2);
        setId(menuItemImpl.mId);
        if (!TextUtils.isEmpty(menuItemImpl.mContentDescription)) {
            setContentDescription(menuItemImpl.mContentDescription);
        }
        if (!TextUtils.isEmpty(menuItemImpl.mTooltipText)) {
            charSequence = menuItemImpl.mTooltipText;
        } else {
            charSequence = menuItemImpl.mTitle;
        }
        setTooltipText(charSequence);
        if (menuItemImpl.isVisible()) {
            i = 0;
        } else {
            i = 8;
        }
        setVisibility(i);
        this.initialized = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i + 1);
        MenuItemImpl menuItemImpl = this.itemData;
        if (menuItemImpl != null && menuItemImpl.isCheckable() && this.itemData.isChecked()) {
            View.mergeDrawableStates(onCreateDrawableState, CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }

    public final void setActiveIndicatorProgress(float f, float f2) {
        float f3;
        float f4;
        View view = this.activeIndicatorView;
        if (view != null) {
            ActiveIndicatorTransform activeIndicatorTransform = this.activeIndicatorTransform;
            Objects.requireNonNull(activeIndicatorTransform);
            LinearInterpolator linearInterpolator = AnimationUtils.LINEAR_INTERPOLATOR;
            view.setScaleX((0.6f * f) + 0.4f);
            view.setScaleY(activeIndicatorTransform.calculateScaleY(f, f2));
            int i = (f2 > 0.0f ? 1 : (f2 == 0.0f ? 0 : -1));
            if (i == 0) {
                f3 = 0.8f;
            } else {
                f3 = 0.0f;
            }
            if (i == 0) {
                f4 = 1.0f;
            } else {
                f4 = 0.2f;
            }
            view.setAlpha(AnimationUtils.lerp(0.0f, 1.0f, f3, f4, f));
        }
        this.activeIndicatorProgress = f;
    }

    public final void setBadge(BadgeDrawable badgeDrawable) {
        boolean z;
        this.badgeDrawable = badgeDrawable;
        ImageView imageView = this.icon;
        if (imageView != null) {
            if (badgeDrawable != null) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                setClipChildren(false);
                setClipToPadding(false);
                BadgeDrawable badgeDrawable2 = this.badgeDrawable;
                Rect rect = new Rect();
                imageView.getDrawingRect(rect);
                badgeDrawable2.setBounds(rect);
                badgeDrawable2.updateBadgeCoordinates(imageView, null);
                if (badgeDrawable2.getCustomBadgeParent() != null) {
                    badgeDrawable2.getCustomBadgeParent().setForeground(badgeDrawable2);
                } else {
                    imageView.getOverlay().add(badgeDrawable2);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x00ad  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0139  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setChecked(boolean r10) {
        /*
            Method dump skipped, instructions count: 430
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.navigation.NavigationBarItemView.setChecked(boolean):void");
    }

    public final void setItemBackground(Drawable drawable) {
        if (!(drawable == null || drawable.getConstantState() == null)) {
            drawable = drawable.getConstantState().newDrawable().mutate();
        }
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api16Impl.setBackground(this, drawable);
    }

    public final void setLabelVisibilityMode(int i) {
        boolean z;
        if (this.labelVisibilityMode != i) {
            this.labelVisibilityMode = i;
            if (!this.activeIndicatorResizeable || i != 2) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                this.activeIndicatorTransform = ACTIVE_INDICATOR_UNLABELED_TRANSFORM;
            } else {
                this.activeIndicatorTransform = ACTIVE_INDICATOR_LABELED_TRANSFORM;
            }
            updateActiveIndicatorLayoutParams(getWidth());
            MenuItemImpl menuItemImpl = this.itemData;
            if (menuItemImpl != null) {
                setChecked(menuItemImpl.isChecked());
            }
        }
    }

    public final void setTextColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.smallLabel.setTextColor(colorStateList);
            this.largeLabel.setTextColor(colorStateList);
        }
    }

    public final void updateActiveIndicatorLayoutParams(int i) {
        boolean z;
        int i2;
        if (this.activeIndicatorView != null) {
            int min = Math.min(this.activeIndicatorDesiredWidth, i - (this.activeIndicatorMarginHorizontal * 2));
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.activeIndicatorView.getLayoutParams();
            if (!this.activeIndicatorResizeable || this.labelVisibilityMode != 2) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                i2 = min;
            } else {
                i2 = this.activeIndicatorDesiredHeight;
            }
            layoutParams.height = i2;
            layoutParams.width = min;
            this.activeIndicatorView.setLayoutParams(layoutParams);
        }
    }

    public NavigationBarItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(getItemLayoutResId(), (ViewGroup) this, true);
        ImageView imageView = (ImageView) findViewById(2131428482);
        this.icon = imageView;
        ViewGroup viewGroup = (ViewGroup) findViewById(2131428483);
        this.labelGroup = viewGroup;
        TextView textView = (TextView) findViewById(2131428485);
        this.smallLabel = textView;
        TextView textView2 = (TextView) findViewById(2131428484);
        this.largeLabel = textView2;
        setBackgroundResource(2131232506);
        this.itemPaddingBottom = viewGroup.getPaddingBottom();
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api16Impl.setImportantForAccessibility(textView, 2);
        ViewCompat.Api16Impl.setImportantForAccessibility(textView2, 2);
        setFocusable(true);
        float textSize = textView.getTextSize();
        float textSize2 = textView2.getTextSize();
        this.shiftAmount = textSize - textSize2;
        this.scaleUpFactor = (textSize2 * 1.0f) / textSize;
        this.scaleDownFactor = (textSize * 1.0f) / textSize2;
        if (imageView != null) {
            imageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.google.android.material.navigation.NavigationBarItemView.1
                @Override // android.view.View.OnLayoutChangeListener
                public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    boolean z;
                    if (NavigationBarItemView.this.icon.getVisibility() == 0) {
                        NavigationBarItemView navigationBarItemView = NavigationBarItemView.this;
                        ImageView imageView2 = navigationBarItemView.icon;
                        BadgeDrawable badgeDrawable = navigationBarItemView.badgeDrawable;
                        if (badgeDrawable != null) {
                            z = true;
                        } else {
                            z = false;
                        }
                        if (z) {
                            Rect rect = new Rect();
                            imageView2.getDrawingRect(rect);
                            badgeDrawable.setBounds(rect);
                            badgeDrawable.updateBadgeCoordinates(imageView2, null);
                        }
                    }
                }
            });
        }
    }

    public static void setViewScaleValues(TextView textView, float f, float f2, int i) {
        textView.setScaleX(f);
        textView.setScaleY(f2);
        textView.setVisibility(i);
    }

    public static void setViewTopMarginAndGravity(View view, int i, int i2) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.topMargin = i;
        layoutParams.bottomMargin = i;
        layoutParams.gravity = i2;
        view.setLayoutParams(layoutParams);
    }

    public static void updateViewPaddingBottom(ViewGroup viewGroup, int i) {
        viewGroup.setPadding(viewGroup.getPaddingLeft(), viewGroup.getPaddingTop(), viewGroup.getPaddingRight(), i);
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        Context context;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        BadgeDrawable badgeDrawable = this.badgeDrawable;
        if (badgeDrawable != null && badgeDrawable.isVisible()) {
            MenuItemImpl menuItemImpl = this.itemData;
            Objects.requireNonNull(menuItemImpl);
            CharSequence charSequence = menuItemImpl.mTitle;
            MenuItemImpl menuItemImpl2 = this.itemData;
            Objects.requireNonNull(menuItemImpl2);
            if (!TextUtils.isEmpty(menuItemImpl2.mContentDescription)) {
                MenuItemImpl menuItemImpl3 = this.itemData;
                Objects.requireNonNull(menuItemImpl3);
                charSequence = menuItemImpl3.mContentDescription;
            }
            StringBuilder sb = new StringBuilder();
            sb.append((Object) charSequence);
            sb.append(", ");
            BadgeDrawable badgeDrawable2 = this.badgeDrawable;
            Objects.requireNonNull(badgeDrawable2);
            String str = null;
            if (badgeDrawable2.isVisible()) {
                if (!badgeDrawable2.hasNumber()) {
                    str = badgeDrawable2.savedState.contentDescriptionNumberless;
                } else if (badgeDrawable2.savedState.contentDescriptionQuantityStrings > 0 && (context = badgeDrawable2.contextRef.get()) != null) {
                    int number = badgeDrawable2.getNumber();
                    int i = badgeDrawable2.maxBadgeNumber;
                    str = number <= i ? context.getResources().getQuantityString(badgeDrawable2.savedState.contentDescriptionQuantityStrings, badgeDrawable2.getNumber(), Integer.valueOf(badgeDrawable2.getNumber())) : context.getString(badgeDrawable2.savedState.contentDescriptionExceedsMaxBadgeNumberRes, Integer.valueOf(i));
                }
            }
            sb.append((Object) str);
            accessibilityNodeInfo.setContentDescription(sb.toString());
        }
        ViewGroup viewGroup = (ViewGroup) getParent();
        int indexOfChild = viewGroup.indexOfChild(this);
        int i2 = 0;
        for (int i3 = 0; i3 < indexOfChild; i3++) {
            View childAt = viewGroup.getChildAt(i3);
            if ((childAt instanceof NavigationBarItemView) && childAt.getVisibility() == 0) {
                i2++;
            }
        }
        accessibilityNodeInfo.setCollectionItemInfo((AccessibilityNodeInfo.CollectionItemInfo) AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, i2, 1, isSelected()).mInfo);
        if (isSelected()) {
            accessibilityNodeInfo.setClickable(false);
            accessibilityNodeInfo.removeAction((AccessibilityNodeInfo.AccessibilityAction) AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK.mAction);
        }
        accessibilityNodeInfo.getExtras().putCharSequence("AccessibilityNodeInfo.roleDescription", getResources().getString(2131952480));
    }

    @Override // android.view.View
    public final void onSizeChanged(final int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        post(new Runnable() { // from class: com.google.android.material.navigation.NavigationBarItemView.2
            @Override // java.lang.Runnable
            public final void run() {
                NavigationBarItemView.this.updateActiveIndicatorLayoutParams(i);
            }
        });
    }

    @Override // android.view.View
    public final void setEnabled(boolean z) {
        super.setEnabled(z);
        this.smallLabel.setEnabled(z);
        this.largeLabel.setEnabled(z);
        this.icon.setEnabled(z);
        if (z) {
            PointerIcon systemIcon = PointerIcon.getSystemIcon(getContext(), 1002);
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api24Impl.setPointerIcon(this, systemIcon);
            return;
        }
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api24Impl.setPointerIcon(this, null);
    }

    @Override // androidx.appcompat.view.menu.MenuView.ItemView
    public final MenuItemImpl getItemData() {
        return this.itemData;
    }
}
