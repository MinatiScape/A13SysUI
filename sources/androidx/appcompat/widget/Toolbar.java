package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.appcompat.R$styleable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.appcompat.widget.ActionMenuView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.customview.view.AbsSavedState;
import java.util.ArrayList;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class Toolbar extends ViewGroup {
    public int mButtonGravity;
    public AppCompatImageButton mCollapseButtonView;
    public CharSequence mCollapseDescription;
    public Drawable mCollapseIcon;
    public boolean mCollapsible;
    public int mContentInsetEndWithActions;
    public int mContentInsetStartWithNavigation;
    public RtlSpacingHelper mContentInsets;
    public boolean mEatingHover;
    public boolean mEatingTouch;
    public View mExpandedActionView;
    public ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    public int mGravity;
    public final ArrayList<View> mHiddenViews;
    public AppCompatImageView mLogoView;
    public int mMaxButtonHeight;
    public ActionMenuView mMenuView;
    public final AnonymousClass1 mMenuViewItemClickListener;
    public AppCompatImageButton mNavButtonView;
    public ActionMenuPresenter mOuterActionMenuPresenter;
    public Context mPopupContext;
    public int mPopupTheme;
    public final AnonymousClass2 mShowOverflowMenuRunnable;
    public CharSequence mSubtitleText;
    public int mSubtitleTextAppearance;
    public ColorStateList mSubtitleTextColor;
    public AppCompatTextView mSubtitleTextView;
    public final int[] mTempMargins;
    public final ArrayList<View> mTempViews;
    public int mTitleMarginBottom;
    public int mTitleMarginEnd;
    public int mTitleMarginStart;
    public int mTitleMarginTop;
    public CharSequence mTitleText;
    public int mTitleTextAppearance;
    public ColorStateList mTitleTextColor;
    public AppCompatTextView mTitleTextView;
    public ToolbarWidgetWrapper mWrapper;

    /* renamed from: androidx.appcompat.widget.Toolbar$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements ActionMenuView.OnMenuItemClickListener {
        public AnonymousClass1() {
        }
    }

    /* loaded from: classes.dex */
    public class ExpandedActionViewMenuPresenter implements MenuPresenter {
        public MenuItemImpl mCurrentExpandedItem;
        public MenuBuilder mMenu;

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public final boolean flagActionItems() {
            return false;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public final int getId() {
            return 0;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public final void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public final void onRestoreInstanceState(Parcelable parcelable) {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public final Parcelable onSaveInstanceState() {
            return null;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public final boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
            return false;
        }

        public ExpandedActionViewMenuPresenter() {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public final boolean collapseItemActionView(MenuItemImpl menuItemImpl) {
            View view = Toolbar.this.mExpandedActionView;
            if (view instanceof CollapsibleActionView) {
                ((CollapsibleActionView) view).onActionViewCollapsed();
            }
            Toolbar toolbar = Toolbar.this;
            toolbar.removeView(toolbar.mExpandedActionView);
            Toolbar toolbar2 = Toolbar.this;
            toolbar2.removeView(toolbar2.mCollapseButtonView);
            Toolbar toolbar3 = Toolbar.this;
            toolbar3.mExpandedActionView = null;
            int size = toolbar3.mHiddenViews.size();
            while (true) {
                size--;
                if (size >= 0) {
                    toolbar3.addView(toolbar3.mHiddenViews.get(size));
                } else {
                    toolbar3.mHiddenViews.clear();
                    this.mCurrentExpandedItem = null;
                    Toolbar.this.requestLayout();
                    Objects.requireNonNull(menuItemImpl);
                    menuItemImpl.mIsActionViewExpanded = false;
                    menuItemImpl.mMenu.onItemsChanged(false);
                    return true;
                }
            }
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public final boolean expandItemActionView(MenuItemImpl menuItemImpl) {
            final Toolbar toolbar = Toolbar.this;
            Objects.requireNonNull(toolbar);
            if (toolbar.mCollapseButtonView == null) {
                AppCompatImageButton appCompatImageButton = new AppCompatImageButton(toolbar.getContext(), null, 2130970057);
                toolbar.mCollapseButtonView = appCompatImageButton;
                appCompatImageButton.setImageDrawable(toolbar.mCollapseIcon);
                toolbar.mCollapseButtonView.setContentDescription(toolbar.mCollapseDescription);
                LayoutParams layoutParams = new LayoutParams();
                layoutParams.gravity = (toolbar.mButtonGravity & 112) | 8388611;
                layoutParams.mViewType = 2;
                toolbar.mCollapseButtonView.setLayoutParams(layoutParams);
                toolbar.mCollapseButtonView.setOnClickListener(new View.OnClickListener() { // from class: androidx.appcompat.widget.Toolbar.3
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        MenuItemImpl menuItemImpl2;
                        Toolbar toolbar2 = Toolbar.this;
                        Objects.requireNonNull(toolbar2);
                        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = toolbar2.mExpandedMenuPresenter;
                        if (expandedActionViewMenuPresenter == null) {
                            menuItemImpl2 = null;
                        } else {
                            menuItemImpl2 = expandedActionViewMenuPresenter.mCurrentExpandedItem;
                        }
                        if (menuItemImpl2 != null) {
                            menuItemImpl2.collapseActionView();
                        }
                    }
                });
            }
            ViewParent parent = Toolbar.this.mCollapseButtonView.getParent();
            Toolbar toolbar2 = Toolbar.this;
            if (parent != toolbar2) {
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(toolbar2.mCollapseButtonView);
                }
                Toolbar toolbar3 = Toolbar.this;
                toolbar3.addView(toolbar3.mCollapseButtonView);
            }
            Toolbar.this.mExpandedActionView = menuItemImpl.getActionView();
            this.mCurrentExpandedItem = menuItemImpl;
            ViewParent parent2 = Toolbar.this.mExpandedActionView.getParent();
            Toolbar toolbar4 = Toolbar.this;
            if (parent2 != toolbar4) {
                if (parent2 instanceof ViewGroup) {
                    ((ViewGroup) parent2).removeView(toolbar4.mExpandedActionView);
                }
                Objects.requireNonNull(Toolbar.this);
                LayoutParams layoutParams2 = new LayoutParams();
                Toolbar toolbar5 = Toolbar.this;
                layoutParams2.gravity = 8388611 | (toolbar5.mButtonGravity & 112);
                layoutParams2.mViewType = 2;
                toolbar5.mExpandedActionView.setLayoutParams(layoutParams2);
                Toolbar toolbar6 = Toolbar.this;
                toolbar6.addView(toolbar6.mExpandedActionView);
            }
            Toolbar toolbar7 = Toolbar.this;
            Objects.requireNonNull(toolbar7);
            int childCount = toolbar7.getChildCount();
            while (true) {
                childCount--;
                if (childCount < 0) {
                    break;
                }
                View childAt = toolbar7.getChildAt(childCount);
                if (!(((LayoutParams) childAt.getLayoutParams()).mViewType == 2 || childAt == toolbar7.mMenuView)) {
                    toolbar7.removeViewAt(childCount);
                    toolbar7.mHiddenViews.add(childAt);
                }
            }
            Toolbar.this.requestLayout();
            menuItemImpl.mIsActionViewExpanded = true;
            menuItemImpl.mMenu.onItemsChanged(false);
            View view = Toolbar.this.mExpandedActionView;
            if (view instanceof CollapsibleActionView) {
                ((CollapsibleActionView) view).onActionViewExpanded();
            }
            return true;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public final void initForMenu(Context context, MenuBuilder menuBuilder) {
            MenuItemImpl menuItemImpl;
            MenuBuilder menuBuilder2 = this.mMenu;
            if (!(menuBuilder2 == null || (menuItemImpl = this.mCurrentExpandedItem) == null)) {
                menuBuilder2.collapseItemActionView(menuItemImpl);
            }
            this.mMenu = menuBuilder;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public final void updateMenuView(boolean z) {
            if (this.mCurrentExpandedItem != null) {
                MenuBuilder menuBuilder = this.mMenu;
                boolean z2 = false;
                if (menuBuilder != null) {
                    int size = menuBuilder.size();
                    int i = 0;
                    while (true) {
                        if (i >= size) {
                            break;
                        } else if (this.mMenu.getItem(i) == this.mCurrentExpandedItem) {
                            z2 = true;
                            break;
                        } else {
                            i++;
                        }
                    }
                }
                if (!z2) {
                    collapseItemActionView(this.mCurrentExpandedItem);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: androidx.appcompat.widget.Toolbar.SavedState.1
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
        public int expandedMenuItemId;
        public boolean isOverflowOpen;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.expandedMenuItemId = parcel.readInt();
            this.isOverflowOpen = parcel.readInt() != 0;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mSuperState, i);
            parcel.writeInt(this.expandedMenuItemId);
            parcel.writeInt(this.isOverflowOpen ? 1 : 0);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    public Toolbar(Context context) {
        this(context, null);
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ActionBar.LayoutParams {
        public int mViewType;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.mViewType = 0;
        }

        public LayoutParams() {
            this.mViewType = 0;
            this.gravity = 8388627;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ActionBar.LayoutParams) layoutParams);
            this.mViewType = 0;
            this.mViewType = layoutParams.mViewType;
        }

        public LayoutParams(ActionBar.LayoutParams layoutParams) {
            super(layoutParams);
            this.mViewType = 0;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.mViewType = 0;
            ((ViewGroup.MarginLayoutParams) this).leftMargin = marginLayoutParams.leftMargin;
            ((ViewGroup.MarginLayoutParams) this).topMargin = marginLayoutParams.topMargin;
            ((ViewGroup.MarginLayoutParams) this).rightMargin = marginLayoutParams.rightMargin;
            ((ViewGroup.MarginLayoutParams) this).bottomMargin = marginLayoutParams.bottomMargin;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.mViewType = 0;
        }
    }

    public Toolbar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130970058);
    }

    public final void addCustomViewsWithGravity(ArrayList arrayList, int i) {
        boolean z;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        if (ViewCompat.Api17Impl.getLayoutDirection(this) == 1) {
            z = true;
        } else {
            z = false;
        }
        int childCount = getChildCount();
        int absoluteGravity = Gravity.getAbsoluteGravity(i, ViewCompat.Api17Impl.getLayoutDirection(this));
        arrayList.clear();
        if (z) {
            for (int i2 = childCount - 1; i2 >= 0; i2--) {
                View childAt = getChildAt(i2);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.mViewType == 0 && shouldLayout(childAt)) {
                    int i3 = layoutParams.gravity;
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                    int layoutDirection = ViewCompat.Api17Impl.getLayoutDirection(this);
                    int absoluteGravity2 = Gravity.getAbsoluteGravity(i3, layoutDirection) & 7;
                    if (!(absoluteGravity2 == 1 || absoluteGravity2 == 3 || absoluteGravity2 == 5)) {
                        absoluteGravity2 = layoutDirection == 1 ? 5 : 3;
                    }
                    if (absoluteGravity2 == absoluteGravity) {
                        arrayList.add(childAt);
                    }
                }
            }
            return;
        }
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt2 = getChildAt(i4);
            LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
            if (layoutParams2.mViewType == 0 && shouldLayout(childAt2)) {
                int i5 = layoutParams2.gravity;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap3 = ViewCompat.sViewPropertyAnimatorMap;
                int layoutDirection2 = ViewCompat.Api17Impl.getLayoutDirection(this);
                int absoluteGravity3 = Gravity.getAbsoluteGravity(i5, layoutDirection2) & 7;
                if (!(absoluteGravity3 == 1 || absoluteGravity3 == 3 || absoluteGravity3 == 5)) {
                    absoluteGravity3 = layoutDirection2 == 1 ? 5 : 3;
                }
                if (absoluteGravity3 == absoluteGravity) {
                    arrayList.add(childAt2);
                }
            }
        }
    }

    public final void ensureMenuView() {
        if (this.mMenuView == null) {
            ActionMenuView actionMenuView = new ActionMenuView(getContext());
            this.mMenuView = actionMenuView;
            int i = this.mPopupTheme;
            if (actionMenuView.mPopupTheme != i) {
                actionMenuView.mPopupTheme = i;
                if (i == 0) {
                    actionMenuView.mPopupContext = actionMenuView.getContext();
                } else {
                    actionMenuView.mPopupContext = new ContextThemeWrapper(actionMenuView.getContext(), i);
                }
            }
            ActionMenuView actionMenuView2 = this.mMenuView;
            AnonymousClass1 r1 = this.mMenuViewItemClickListener;
            Objects.requireNonNull(actionMenuView2);
            actionMenuView2.mOnMenuItemClickListener = r1;
            ActionMenuView actionMenuView3 = this.mMenuView;
            Objects.requireNonNull(actionMenuView3);
            actionMenuView3.mActionMenuPresenterCallback = null;
            actionMenuView3.mMenuBuilderCallback = null;
            LayoutParams layoutParams = new LayoutParams();
            layoutParams.gravity = 8388613 | (this.mButtonGravity & 112);
            this.mMenuView.setLayoutParams(layoutParams);
            addSystemView(this.mMenuView, false);
        }
    }

    public final void ensureNavButtonView() {
        if (this.mNavButtonView == null) {
            this.mNavButtonView = new AppCompatImageButton(getContext(), null, 2130970057);
            LayoutParams layoutParams = new LayoutParams();
            layoutParams.gravity = 8388611 | (this.mButtonGravity & 112);
            this.mNavButtonView.setLayoutParams(layoutParams);
        }
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public final int getCurrentContentInsetEnd() {
        boolean z;
        int i;
        MenuBuilder menuBuilder;
        ActionMenuView actionMenuView = this.mMenuView;
        int i2 = 0;
        if (actionMenuView == null || (menuBuilder = actionMenuView.mMenu) == null || !menuBuilder.hasVisibleItems()) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
            if (rtlSpacingHelper == null) {
                i = 0;
            } else if (rtlSpacingHelper.mIsRtl) {
                i = rtlSpacingHelper.mLeft;
            } else {
                i = rtlSpacingHelper.mRight;
            }
            return Math.max(i, Math.max(this.mContentInsetEndWithActions, 0));
        }
        RtlSpacingHelper rtlSpacingHelper2 = this.mContentInsets;
        if (rtlSpacingHelper2 != null) {
            if (rtlSpacingHelper2.mIsRtl) {
                i2 = rtlSpacingHelper2.mLeft;
            } else {
                i2 = rtlSpacingHelper2.mRight;
            }
        }
        return i2;
    }

    public final Drawable getNavigationIcon() {
        AppCompatImageButton appCompatImageButton = this.mNavButtonView;
        if (appCompatImageButton != null) {
            return appCompatImageButton.getDrawable();
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:112:0x02af A[LOOP:0: B:111:0x02ad->B:112:0x02af, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:115:0x02d1 A[LOOP:1: B:114:0x02cf->B:115:0x02d1, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:118:0x02f5 A[LOOP:2: B:117:0x02f3->B:118:0x02f5, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0336  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0346 A[LOOP:3: B:125:0x0344->B:126:0x0346, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0061  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0092  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00e3  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0118  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0145  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x014c  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x017f  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01b7  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01c6  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0235  */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onLayout(boolean r19, int r20, int r21, int r22, int r23) {
        /*
            Method dump skipped, instructions count: 859
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.Toolbar.onLayout(boolean, int, int, int, int):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x0284  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onMeasure(int r17, int r18) {
        /*
            Method dump skipped, instructions count: 649
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.Toolbar.onMeasure(int, int):void");
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        MenuBuilder menuBuilder;
        MenuItem findItem;
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        Objects.requireNonNull(savedState);
        super.onRestoreInstanceState(savedState.mSuperState);
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            menuBuilder = actionMenuView.mMenu;
        } else {
            menuBuilder = null;
        }
        int i = savedState.expandedMenuItemId;
        if (!(i == 0 || this.mExpandedMenuPresenter == null || menuBuilder == null || (findItem = menuBuilder.findItem(i)) == null)) {
            findItem.expandActionView();
        }
        if (savedState.isOverflowOpen) {
            removeCallbacks(this.mShowOverflowMenuRunnable);
            post(this.mShowOverflowMenuRunnable);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0028, code lost:
        if (r3 != false) goto L_0x002c;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.os.Parcelable onSaveInstanceState() {
        /*
            r3 = this;
            androidx.appcompat.widget.Toolbar$SavedState r0 = new androidx.appcompat.widget.Toolbar$SavedState
            android.os.Parcelable r1 = super.onSaveInstanceState()
            r0.<init>(r1)
            androidx.appcompat.widget.Toolbar$ExpandedActionViewMenuPresenter r1 = r3.mExpandedMenuPresenter
            if (r1 == 0) goto L_0x0015
            androidx.appcompat.view.menu.MenuItemImpl r1 = r1.mCurrentExpandedItem
            if (r1 == 0) goto L_0x0015
            int r1 = r1.mId
            r0.expandedMenuItemId = r1
        L_0x0015:
            androidx.appcompat.widget.ActionMenuView r3 = r3.mMenuView
            r1 = 1
            r2 = 0
            if (r3 == 0) goto L_0x002b
            androidx.appcompat.widget.ActionMenuPresenter r3 = r3.mPresenter
            if (r3 == 0) goto L_0x0027
            boolean r3 = r3.isOverflowMenuShowing()
            if (r3 == 0) goto L_0x0027
            r3 = r1
            goto L_0x0028
        L_0x0027:
            r3 = r2
        L_0x0028:
            if (r3 == 0) goto L_0x002b
            goto L_0x002c
        L_0x002b:
            r1 = r2
        L_0x002c:
            r0.isOverflowOpen = r1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.Toolbar.onSaveInstanceState():android.os.Parcelable");
    }

    public final void setLogo(Drawable drawable) {
        if (drawable != null) {
            if (this.mLogoView == null) {
                this.mLogoView = new AppCompatImageView(getContext());
            }
            if (!isChildOrHidden(this.mLogoView)) {
                addSystemView(this.mLogoView, true);
            }
        } else {
            AppCompatImageView appCompatImageView = this.mLogoView;
            if (appCompatImageView != null && isChildOrHidden(appCompatImageView)) {
                removeView(this.mLogoView);
                this.mHiddenViews.remove(this.mLogoView);
            }
        }
        AppCompatImageView appCompatImageView2 = this.mLogoView;
        if (appCompatImageView2 != null) {
            appCompatImageView2.setImageDrawable(drawable);
        }
    }

    public void setNavigationIcon(Drawable drawable) {
        if (drawable != null) {
            ensureNavButtonView();
            if (!isChildOrHidden(this.mNavButtonView)) {
                addSystemView(this.mNavButtonView, true);
            }
        } else {
            AppCompatImageButton appCompatImageButton = this.mNavButtonView;
            if (appCompatImageButton != null && isChildOrHidden(appCompatImageButton)) {
                removeView(this.mNavButtonView);
                this.mHiddenViews.remove(this.mNavButtonView);
            }
        }
        AppCompatImageButton appCompatImageButton2 = this.mNavButtonView;
        if (appCompatImageButton2 != null) {
            appCompatImageButton2.setImageDrawable(drawable);
        }
    }

    public final boolean shouldLayout(View view) {
        if (view == null || view.getParent() != this || view.getVisibility() == 8) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [androidx.appcompat.widget.Toolbar$2] */
    public Toolbar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mGravity = 8388627;
        this.mTempViews = new ArrayList<>();
        this.mHiddenViews = new ArrayList<>();
        this.mTempMargins = new int[2];
        this.mMenuViewItemClickListener = new AnonymousClass1();
        this.mShowOverflowMenuRunnable = new Runnable() { // from class: androidx.appcompat.widget.Toolbar.2
            @Override // java.lang.Runnable
            public final void run() {
                ActionMenuPresenter actionMenuPresenter;
                Toolbar toolbar = Toolbar.this;
                Objects.requireNonNull(toolbar);
                ActionMenuView actionMenuView = toolbar.mMenuView;
                if (actionMenuView != null && (actionMenuPresenter = actionMenuView.mPresenter) != null) {
                    actionMenuPresenter.showOverflowMenu();
                }
            }
        };
        Context context2 = getContext();
        int[] iArr = R$styleable.Toolbar;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context2, attributeSet, iArr, i);
        TypedArray typedArray = obtainStyledAttributes.mWrapped;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context, iArr, attributeSet, typedArray, i, 0);
        this.mTitleTextAppearance = obtainStyledAttributes.getResourceId(28, 0);
        this.mSubtitleTextAppearance = obtainStyledAttributes.getResourceId(19, 0);
        this.mGravity = obtainStyledAttributes.mWrapped.getInteger(0, this.mGravity);
        this.mButtonGravity = obtainStyledAttributes.mWrapped.getInteger(2, 48);
        int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(22, 0);
        dimensionPixelOffset = obtainStyledAttributes.hasValue(27) ? obtainStyledAttributes.getDimensionPixelOffset(27, dimensionPixelOffset) : dimensionPixelOffset;
        this.mTitleMarginBottom = dimensionPixelOffset;
        this.mTitleMarginTop = dimensionPixelOffset;
        this.mTitleMarginEnd = dimensionPixelOffset;
        this.mTitleMarginStart = dimensionPixelOffset;
        int dimensionPixelOffset2 = obtainStyledAttributes.getDimensionPixelOffset(25, -1);
        if (dimensionPixelOffset2 >= 0) {
            this.mTitleMarginStart = dimensionPixelOffset2;
        }
        int dimensionPixelOffset3 = obtainStyledAttributes.getDimensionPixelOffset(24, -1);
        if (dimensionPixelOffset3 >= 0) {
            this.mTitleMarginEnd = dimensionPixelOffset3;
        }
        int dimensionPixelOffset4 = obtainStyledAttributes.getDimensionPixelOffset(26, -1);
        if (dimensionPixelOffset4 >= 0) {
            this.mTitleMarginTop = dimensionPixelOffset4;
        }
        int dimensionPixelOffset5 = obtainStyledAttributes.getDimensionPixelOffset(23, -1);
        if (dimensionPixelOffset5 >= 0) {
            this.mTitleMarginBottom = dimensionPixelOffset5;
        }
        this.mMaxButtonHeight = obtainStyledAttributes.getDimensionPixelSize(13, -1);
        int dimensionPixelOffset6 = obtainStyledAttributes.getDimensionPixelOffset(9, Integer.MIN_VALUE);
        int dimensionPixelOffset7 = obtainStyledAttributes.getDimensionPixelOffset(5, Integer.MIN_VALUE);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(7, 0);
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(8, 0);
        if (this.mContentInsets == null) {
            this.mContentInsets = new RtlSpacingHelper();
        }
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        Objects.requireNonNull(rtlSpacingHelper);
        rtlSpacingHelper.mIsRelative = false;
        if (dimensionPixelSize != Integer.MIN_VALUE) {
            rtlSpacingHelper.mExplicitLeft = dimensionPixelSize;
            rtlSpacingHelper.mLeft = dimensionPixelSize;
        }
        if (dimensionPixelSize2 != Integer.MIN_VALUE) {
            rtlSpacingHelper.mExplicitRight = dimensionPixelSize2;
            rtlSpacingHelper.mRight = dimensionPixelSize2;
        }
        if (!(dimensionPixelOffset6 == Integer.MIN_VALUE && dimensionPixelOffset7 == Integer.MIN_VALUE)) {
            this.mContentInsets.setRelative(dimensionPixelOffset6, dimensionPixelOffset7);
        }
        this.mContentInsetStartWithNavigation = obtainStyledAttributes.getDimensionPixelOffset(10, Integer.MIN_VALUE);
        this.mContentInsetEndWithActions = obtainStyledAttributes.getDimensionPixelOffset(6, Integer.MIN_VALUE);
        this.mCollapseIcon = obtainStyledAttributes.getDrawable(4);
        this.mCollapseDescription = obtainStyledAttributes.getText(3);
        CharSequence text = obtainStyledAttributes.getText(21);
        if (!TextUtils.isEmpty(text)) {
            setTitle(text);
        }
        CharSequence text2 = obtainStyledAttributes.getText(18);
        if (!TextUtils.isEmpty(text2)) {
            setSubtitle(text2);
        }
        this.mPopupContext = getContext();
        int resourceId = obtainStyledAttributes.getResourceId(17, 0);
        if (this.mPopupTheme != resourceId) {
            this.mPopupTheme = resourceId;
            if (resourceId == 0) {
                this.mPopupContext = getContext();
            } else {
                this.mPopupContext = new ContextThemeWrapper(getContext(), resourceId);
            }
        }
        Drawable drawable = obtainStyledAttributes.getDrawable(16);
        if (drawable != null) {
            setNavigationIcon(drawable);
        }
        CharSequence text3 = obtainStyledAttributes.getText(15);
        if (!TextUtils.isEmpty(text3)) {
            setNavigationContentDescription(text3);
        }
        Drawable drawable2 = obtainStyledAttributes.getDrawable(11);
        if (drawable2 != null) {
            setLogo(drawable2);
        }
        CharSequence text4 = obtainStyledAttributes.getText(12);
        if (!TextUtils.isEmpty(text4)) {
            if (!TextUtils.isEmpty(text4) && this.mLogoView == null) {
                this.mLogoView = new AppCompatImageView(getContext());
            }
            AppCompatImageView appCompatImageView = this.mLogoView;
            if (appCompatImageView != null) {
                appCompatImageView.setContentDescription(text4);
            }
        }
        if (obtainStyledAttributes.hasValue(29)) {
            ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(29);
            this.mTitleTextColor = colorStateList;
            AppCompatTextView appCompatTextView = this.mTitleTextView;
            if (appCompatTextView != null) {
                appCompatTextView.setTextColor(colorStateList);
            }
        }
        if (obtainStyledAttributes.hasValue(20)) {
            ColorStateList colorStateList2 = obtainStyledAttributes.getColorStateList(20);
            this.mSubtitleTextColor = colorStateList2;
            AppCompatTextView appCompatTextView2 = this.mSubtitleTextView;
            if (appCompatTextView2 != null) {
                appCompatTextView2.setTextColor(colorStateList2);
            }
        }
        if (obtainStyledAttributes.hasValue(14)) {
            new SupportMenuInflater(getContext()).inflate(obtainStyledAttributes.getResourceId(14, 0), getMenu());
        }
        obtainStyledAttributes.recycle();
    }

    public static LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) layoutParams);
        }
        if (layoutParams instanceof ActionBar.LayoutParams) {
            return new LayoutParams((ActionBar.LayoutParams) layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    public static int getHorizontalMargins(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return marginLayoutParams.getMarginEnd() + marginLayoutParams.getMarginStart();
    }

    public static int getVerticalMargins(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    }

    public final void addSystemView(View view, boolean z) {
        LayoutParams layoutParams;
        ViewGroup.LayoutParams layoutParams2 = view.getLayoutParams();
        if (layoutParams2 == null) {
            layoutParams = new LayoutParams();
        } else if (!checkLayoutParams(layoutParams2)) {
            layoutParams = generateLayoutParams(layoutParams2);
        } else {
            layoutParams = (LayoutParams) layoutParams2;
        }
        layoutParams.mViewType = 1;
        if (!z || this.mExpandedActionView == null) {
            addView(view, layoutParams);
            return;
        }
        view.setLayoutParams(layoutParams);
        this.mHiddenViews.add(view);
    }

    @Override // android.view.ViewGroup
    public final boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (!super.checkLayoutParams(layoutParams) || !(layoutParams instanceof LayoutParams)) {
            return false;
        }
        return true;
    }

    public final int getChildTop(View view, int i) {
        int i2;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int measuredHeight = view.getMeasuredHeight();
        if (i > 0) {
            i2 = (measuredHeight - i) / 2;
        } else {
            i2 = 0;
        }
        int i3 = layoutParams.gravity & 112;
        if (!(i3 == 16 || i3 == 48 || i3 == 80)) {
            i3 = this.mGravity & 112;
        }
        if (i3 == 48) {
            return getPaddingTop() - i2;
        }
        if (i3 == 80) {
            return (((getHeight() - getPaddingBottom()) - measuredHeight) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin) - i2;
        }
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int height = getHeight();
        int i4 = (((height - paddingTop) - paddingBottom) - measuredHeight) / 2;
        int i5 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
        if (i4 < i5) {
            i4 = i5;
        } else {
            int i6 = (((height - paddingBottom) - measuredHeight) - i4) - paddingTop;
            int i7 = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
            if (i6 < i7) {
                i4 = Math.max(0, i4 - (i7 - i6));
            }
        }
        return paddingTop + i4;
    }

    public final int getCurrentContentInsetStart() {
        int i;
        int i2 = 0;
        if (getNavigationIcon() != null) {
            RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
            if (rtlSpacingHelper == null) {
                i = 0;
            } else if (rtlSpacingHelper.mIsRtl) {
                i = rtlSpacingHelper.mRight;
            } else {
                i = rtlSpacingHelper.mLeft;
            }
            return Math.max(i, Math.max(this.mContentInsetStartWithNavigation, 0));
        }
        RtlSpacingHelper rtlSpacingHelper2 = this.mContentInsets;
        if (rtlSpacingHelper2 != null) {
            if (rtlSpacingHelper2.mIsRtl) {
                i2 = rtlSpacingHelper2.mRight;
            } else {
                i2 = rtlSpacingHelper2.mLeft;
            }
        }
        return i2;
    }

    public final MenuBuilder getMenu() {
        ensureMenuView();
        ActionMenuView actionMenuView = this.mMenuView;
        Objects.requireNonNull(actionMenuView);
        if (actionMenuView.mMenu == null) {
            MenuBuilder menu = this.mMenuView.getMenu();
            if (this.mExpandedMenuPresenter == null) {
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
            }
            ActionMenuView actionMenuView2 = this.mMenuView;
            Objects.requireNonNull(actionMenuView2);
            ActionMenuPresenter actionMenuPresenter = actionMenuView2.mPresenter;
            Objects.requireNonNull(actionMenuPresenter);
            actionMenuPresenter.mExpandedActionViewsExclusive = true;
            menu.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
        return this.mMenuView.getMenu();
    }

    public final boolean isChildOrHidden(View view) {
        if (view.getParent() == this || this.mHiddenViews.contains(view)) {
            return true;
        }
        return false;
    }

    public final int layoutChildLeft(View view, int i, int[] iArr, int i2) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i3 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin - iArr[0];
        int max = Math.max(0, i3) + i;
        iArr[0] = Math.max(0, -i3);
        int childTop = getChildTop(view, i2);
        int measuredWidth = view.getMeasuredWidth();
        view.layout(max, childTop, max + measuredWidth, view.getMeasuredHeight() + childTop);
        return measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + max;
    }

    public final int layoutChildRight(View view, int i, int[] iArr, int i2) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i3 = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin - iArr[1];
        int max = i - Math.max(0, i3);
        iArr[1] = Math.max(0, -i3);
        int childTop = getChildTop(view, i2);
        int measuredWidth = view.getMeasuredWidth();
        view.layout(max - measuredWidth, childTop, max, view.getMeasuredHeight() + childTop);
        return max - (measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin);
    }

    public final int measureChildCollapseMargins(View view, int i, int i2, int i3, int i4, int[] iArr) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int i5 = marginLayoutParams.leftMargin - iArr[0];
        int i6 = marginLayoutParams.rightMargin - iArr[1];
        int max = Math.max(0, i6) + Math.max(0, i5);
        iArr[0] = Math.max(0, -i5);
        iArr[1] = Math.max(0, -i6);
        view.measure(ViewGroup.getChildMeasureSpec(i, getPaddingRight() + getPaddingLeft() + max + i2, marginLayoutParams.width), ViewGroup.getChildMeasureSpec(i3, getPaddingBottom() + getPaddingTop() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + i4, marginLayoutParams.height));
        return view.getMeasuredWidth() + max;
    }

    public final void measureChildConstrained(View view, int i, int i2, int i3, int i4) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i, getPaddingRight() + getPaddingLeft() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i2, marginLayoutParams.width);
        int childMeasureSpec2 = ViewGroup.getChildMeasureSpec(i3, getPaddingBottom() + getPaddingTop() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + 0, marginLayoutParams.height);
        int mode = View.MeasureSpec.getMode(childMeasureSpec2);
        if (mode != 1073741824 && i4 >= 0) {
            if (mode != 0) {
                i4 = Math.min(View.MeasureSpec.getSize(childMeasureSpec2), i4);
            }
            childMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i4, 1073741824);
        }
        view.measure(childMeasureSpec, childMeasureSpec2);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mShowOverflowMenuRunnable);
    }

    @Override // android.view.View
    public final boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 9) {
            this.mEatingHover = false;
        }
        if (!this.mEatingHover) {
            boolean onHoverEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && !onHoverEvent) {
                this.mEatingHover = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.mEatingHover = false;
        }
        return true;
    }

    @Override // android.view.View
    public final void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        if (this.mContentInsets == null) {
            this.mContentInsets = new RtlSpacingHelper();
        }
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        boolean z = true;
        if (i != 1) {
            z = false;
        }
        Objects.requireNonNull(rtlSpacingHelper);
        if (z != rtlSpacingHelper.mIsRtl) {
            rtlSpacingHelper.mIsRtl = z;
            if (!rtlSpacingHelper.mIsRelative) {
                rtlSpacingHelper.mLeft = rtlSpacingHelper.mExplicitLeft;
                rtlSpacingHelper.mRight = rtlSpacingHelper.mExplicitRight;
            } else if (z) {
                int i2 = rtlSpacingHelper.mEnd;
                if (i2 == Integer.MIN_VALUE) {
                    i2 = rtlSpacingHelper.mExplicitLeft;
                }
                rtlSpacingHelper.mLeft = i2;
                int i3 = rtlSpacingHelper.mStart;
                if (i3 == Integer.MIN_VALUE) {
                    i3 = rtlSpacingHelper.mExplicitRight;
                }
                rtlSpacingHelper.mRight = i3;
            } else {
                int i4 = rtlSpacingHelper.mStart;
                if (i4 == Integer.MIN_VALUE) {
                    i4 = rtlSpacingHelper.mExplicitLeft;
                }
                rtlSpacingHelper.mLeft = i4;
                int i5 = rtlSpacingHelper.mEnd;
                if (i5 == Integer.MIN_VALUE) {
                    i5 = rtlSpacingHelper.mExplicitRight;
                }
                rtlSpacingHelper.mRight = i5;
            }
        }
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            boolean onTouchEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && !onTouchEvent) {
                this.mEatingTouch = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.mEatingTouch = false;
        }
        return true;
    }

    public final void setNavigationContentDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            ensureNavButtonView();
        }
        AppCompatImageButton appCompatImageButton = this.mNavButtonView;
        if (appCompatImageButton != null) {
            appCompatImageButton.setContentDescription(charSequence);
        }
    }

    public void setSubtitle(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (this.mSubtitleTextView == null) {
                Context context = getContext();
                AppCompatTextView appCompatTextView = new AppCompatTextView(context);
                this.mSubtitleTextView = appCompatTextView;
                appCompatTextView.setSingleLine();
                this.mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                int i = this.mSubtitleTextAppearance;
                if (i != 0) {
                    this.mSubtitleTextView.setTextAppearance(context, i);
                }
                ColorStateList colorStateList = this.mSubtitleTextColor;
                if (colorStateList != null) {
                    this.mSubtitleTextView.setTextColor(colorStateList);
                }
            }
            if (!isChildOrHidden(this.mSubtitleTextView)) {
                addSystemView(this.mSubtitleTextView, true);
            }
        } else {
            AppCompatTextView appCompatTextView2 = this.mSubtitleTextView;
            if (appCompatTextView2 != null && isChildOrHidden(appCompatTextView2)) {
                removeView(this.mSubtitleTextView);
                this.mHiddenViews.remove(this.mSubtitleTextView);
            }
        }
        AppCompatTextView appCompatTextView3 = this.mSubtitleTextView;
        if (appCompatTextView3 != null) {
            appCompatTextView3.setText(charSequence);
        }
        this.mSubtitleText = charSequence;
    }

    public void setTitle(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (this.mTitleTextView == null) {
                Context context = getContext();
                AppCompatTextView appCompatTextView = new AppCompatTextView(context);
                this.mTitleTextView = appCompatTextView;
                appCompatTextView.setSingleLine();
                this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                int i = this.mTitleTextAppearance;
                if (i != 0) {
                    this.mTitleTextView.setTextAppearance(context, i);
                }
                ColorStateList colorStateList = this.mTitleTextColor;
                if (colorStateList != null) {
                    this.mTitleTextView.setTextColor(colorStateList);
                }
            }
            if (!isChildOrHidden(this.mTitleTextView)) {
                addSystemView(this.mTitleTextView, true);
            }
        } else {
            AppCompatTextView appCompatTextView2 = this.mTitleTextView;
            if (appCompatTextView2 != null && isChildOrHidden(appCompatTextView2)) {
                removeView(this.mTitleTextView);
                this.mHiddenViews.remove(this.mTitleTextView);
            }
        }
        AppCompatTextView appCompatTextView3 = this.mTitleTextView;
        if (appCompatTextView3 != null) {
            appCompatTextView3.setText(charSequence);
        }
        this.mTitleText = charSequence;
    }
}
