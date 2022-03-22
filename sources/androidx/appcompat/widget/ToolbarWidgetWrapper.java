package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.appcompat.R$styleable;
import androidx.appcompat.app.AppCompatDelegateImpl;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.ActionMenuPresenter;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import com.airbnb.lottie.L;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ToolbarWidgetWrapper implements DecorToolbar {
    public ActionMenuPresenter mActionMenuPresenter;
    public View mCustomView;
    public int mDefaultNavigationContentDescription;
    public Drawable mDefaultNavigationIcon;
    public int mDisplayOpts;
    public CharSequence mHomeDescription;
    public Drawable mIcon;
    public Drawable mLogo;
    public boolean mMenuPrepared;
    public Drawable mNavIcon;
    public CharSequence mSubtitle;
    public CharSequence mTitle;
    public boolean mTitleSet;
    public Toolbar mToolbar;
    public Window.Callback mWindowCallback;

    @Override // androidx.appcompat.widget.DecorToolbar
    public final int getNavigationMode() {
        return 0;
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final void setEmbeddedTabView() {
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final void setHomeButtonEnabled() {
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final void setMenuPrepared() {
        this.mMenuPrepared = true;
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final boolean canShowOverflowMenu() {
        ActionMenuView actionMenuView;
        Toolbar toolbar = this.mToolbar;
        Objects.requireNonNull(toolbar);
        if (toolbar.getVisibility() != 0 || (actionMenuView = toolbar.mMenuView) == null || !actionMenuView.mReserveOverflow) {
            return false;
        }
        return true;
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final void collapseActionView() {
        MenuItemImpl menuItemImpl;
        Toolbar toolbar = this.mToolbar;
        Objects.requireNonNull(toolbar);
        Toolbar.ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = toolbar.mExpandedMenuPresenter;
        if (expandedActionViewMenuPresenter == null) {
            menuItemImpl = null;
        } else {
            menuItemImpl = expandedActionViewMenuPresenter.mCurrentExpandedItem;
        }
        if (menuItemImpl != null) {
            menuItemImpl.collapseActionView();
        }
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final void dismissPopupMenus() {
        ActionMenuPresenter actionMenuPresenter;
        Toolbar toolbar = this.mToolbar;
        Objects.requireNonNull(toolbar);
        ActionMenuView actionMenuView = toolbar.mMenuView;
        if (actionMenuView != null && (actionMenuPresenter = actionMenuView.mPresenter) != null) {
            actionMenuPresenter.hideOverflowMenu();
            ActionMenuPresenter.ActionButtonSubmenu actionButtonSubmenu = actionMenuPresenter.mActionButtonPopup;
            if (actionButtonSubmenu != null && actionButtonSubmenu.isShowing()) {
                actionButtonSubmenu.mPopup.dismiss();
            }
        }
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final Context getContext() {
        return this.mToolbar.getContext();
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final boolean hasExpandedActionView() {
        Toolbar toolbar = this.mToolbar;
        Objects.requireNonNull(toolbar);
        Toolbar.ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = toolbar.mExpandedMenuPresenter;
        if (expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null) {
            return false;
        }
        return true;
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final boolean hideOverflowMenu() {
        boolean z;
        Toolbar toolbar = this.mToolbar;
        Objects.requireNonNull(toolbar);
        ActionMenuView actionMenuView = toolbar.mMenuView;
        if (actionMenuView != null) {
            ActionMenuPresenter actionMenuPresenter = actionMenuView.mPresenter;
            if (actionMenuPresenter == null || !actionMenuPresenter.hideOverflowMenu()) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final void initIndeterminateProgress() {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final void initProgress() {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }

    /* JADX WARN: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    @Override // androidx.appcompat.widget.DecorToolbar
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean isOverflowMenuShowPending() {
        /*
            r3 = this;
            androidx.appcompat.widget.Toolbar r3 = r3.mToolbar
            java.util.Objects.requireNonNull(r3)
            androidx.appcompat.widget.ActionMenuView r3 = r3.mMenuView
            r0 = 1
            r1 = 0
            if (r3 == 0) goto L_0x0025
            androidx.appcompat.widget.ActionMenuPresenter r3 = r3.mPresenter
            if (r3 == 0) goto L_0x0021
            androidx.appcompat.widget.ActionMenuPresenter$OpenOverflowRunnable r2 = r3.mPostedOpenRunnable
            if (r2 != 0) goto L_0x001c
            boolean r3 = r3.isOverflowMenuShowing()
            if (r3 == 0) goto L_0x001a
            goto L_0x001c
        L_0x001a:
            r3 = r1
            goto L_0x001d
        L_0x001c:
            r3 = r0
        L_0x001d:
            if (r3 == 0) goto L_0x0021
            r3 = r0
            goto L_0x0022
        L_0x0021:
            r3 = r1
        L_0x0022:
            if (r3 == 0) goto L_0x0025
            goto L_0x0026
        L_0x0025:
            r0 = r1
        L_0x0026:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ToolbarWidgetWrapper.isOverflowMenuShowPending():boolean");
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final boolean isOverflowMenuShowing() {
        boolean z;
        Toolbar toolbar = this.mToolbar;
        Objects.requireNonNull(toolbar);
        ActionMenuView actionMenuView = toolbar.mMenuView;
        if (actionMenuView != null) {
            ActionMenuPresenter actionMenuPresenter = actionMenuView.mPresenter;
            if (actionMenuPresenter == null || !actionMenuPresenter.isOverflowMenuShowing()) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final void setCollapsible(boolean z) {
        Toolbar toolbar = this.mToolbar;
        Objects.requireNonNull(toolbar);
        toolbar.mCollapsible = z;
        toolbar.requestLayout();
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final void setDisplayOptions(int i) {
        View view;
        int i2 = this.mDisplayOpts ^ i;
        this.mDisplayOpts = i;
        if (i2 != 0) {
            if ((i2 & 4) != 0) {
                if ((i & 4) != 0) {
                    updateHomeAccessibility();
                }
                if ((this.mDisplayOpts & 4) != 0) {
                    Toolbar toolbar = this.mToolbar;
                    Drawable drawable = this.mNavIcon;
                    if (drawable == null) {
                        drawable = this.mDefaultNavigationIcon;
                    }
                    toolbar.setNavigationIcon(drawable);
                } else {
                    this.mToolbar.setNavigationIcon(null);
                }
            }
            if ((i2 & 3) != 0) {
                updateToolbarLogo();
            }
            if ((i2 & 8) != 0) {
                if ((i & 8) != 0) {
                    this.mToolbar.setTitle(this.mTitle);
                    this.mToolbar.setSubtitle(this.mSubtitle);
                } else {
                    this.mToolbar.setTitle(null);
                    this.mToolbar.setSubtitle(null);
                }
            }
            if ((i2 & 16) != 0 && (view = this.mCustomView) != null) {
                if ((i & 16) != 0) {
                    this.mToolbar.addView(view);
                } else {
                    this.mToolbar.removeView(view);
                }
            }
        }
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final void setMenu(MenuBuilder menuBuilder, AppCompatDelegateImpl.ActionMenuPresenterCallback actionMenuPresenterCallback) {
        if (this.mActionMenuPresenter == null) {
            ActionMenuPresenter actionMenuPresenter = new ActionMenuPresenter(this.mToolbar.getContext());
            this.mActionMenuPresenter = actionMenuPresenter;
            actionMenuPresenter.mId = 2131427427;
        }
        ActionMenuPresenter actionMenuPresenter2 = this.mActionMenuPresenter;
        Objects.requireNonNull(actionMenuPresenter2);
        actionMenuPresenter2.mCallback = actionMenuPresenterCallback;
        Toolbar toolbar = this.mToolbar;
        ActionMenuPresenter actionMenuPresenter3 = this.mActionMenuPresenter;
        Objects.requireNonNull(toolbar);
        if (menuBuilder != null || toolbar.mMenuView != null) {
            toolbar.ensureMenuView();
            ActionMenuView actionMenuView = toolbar.mMenuView;
            Objects.requireNonNull(actionMenuView);
            MenuBuilder menuBuilder2 = actionMenuView.mMenu;
            if (menuBuilder2 != menuBuilder) {
                if (menuBuilder2 != null) {
                    menuBuilder2.removeMenuPresenter(toolbar.mOuterActionMenuPresenter);
                    menuBuilder2.removeMenuPresenter(toolbar.mExpandedMenuPresenter);
                }
                if (toolbar.mExpandedMenuPresenter == null) {
                    toolbar.mExpandedMenuPresenter = new Toolbar.ExpandedActionViewMenuPresenter();
                }
                Objects.requireNonNull(actionMenuPresenter3);
                actionMenuPresenter3.mExpandedActionViewsExclusive = true;
                if (menuBuilder != null) {
                    menuBuilder.addMenuPresenter(actionMenuPresenter3, toolbar.mPopupContext);
                    menuBuilder.addMenuPresenter(toolbar.mExpandedMenuPresenter, toolbar.mPopupContext);
                } else {
                    actionMenuPresenter3.initForMenu(toolbar.mPopupContext, null);
                    toolbar.mExpandedMenuPresenter.initForMenu(toolbar.mPopupContext, null);
                    actionMenuPresenter3.updateMenuView(true);
                    toolbar.mExpandedMenuPresenter.updateMenuView(true);
                }
                ActionMenuView actionMenuView2 = toolbar.mMenuView;
                int i = toolbar.mPopupTheme;
                Objects.requireNonNull(actionMenuView2);
                if (actionMenuView2.mPopupTheme != i) {
                    actionMenuView2.mPopupTheme = i;
                    if (i == 0) {
                        actionMenuView2.mPopupContext = actionMenuView2.getContext();
                    } else {
                        actionMenuView2.mPopupContext = new ContextThemeWrapper(actionMenuView2.getContext(), i);
                    }
                }
                ActionMenuView actionMenuView3 = toolbar.mMenuView;
                Objects.requireNonNull(actionMenuView3);
                actionMenuView3.mPresenter = actionMenuPresenter3;
                actionMenuPresenter3.mMenuView = actionMenuView3;
                actionMenuView3.mMenu = actionMenuPresenter3.mMenu;
                toolbar.mOuterActionMenuPresenter = actionMenuPresenter3;
            }
        }
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final void setVisibility(int i) {
        this.mToolbar.setVisibility(i);
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final void setWindowTitle(CharSequence charSequence) {
        if (!this.mTitleSet) {
            this.mTitle = charSequence;
            if ((this.mDisplayOpts & 8) != 0) {
                this.mToolbar.setTitle(charSequence);
                if (this.mTitleSet) {
                    ViewCompat.setAccessibilityPaneTitle(this.mToolbar.getRootView(), charSequence);
                }
            }
        }
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final ViewPropertyAnimatorCompat setupAnimatorToVisibility(final int i, long j) {
        float f;
        ViewPropertyAnimatorCompat animate = ViewCompat.animate(this.mToolbar);
        if (i == 0) {
            f = 1.0f;
        } else {
            f = 0.0f;
        }
        animate.alpha(f);
        animate.setDuration(j);
        animate.setListener(new L() { // from class: androidx.appcompat.widget.ToolbarWidgetWrapper.2
            public boolean mCanceled = false;

            @Override // com.airbnb.lottie.L, androidx.core.view.ViewPropertyAnimatorListener
            public final void onAnimationCancel(View view) {
                this.mCanceled = true;
            }

            @Override // androidx.core.view.ViewPropertyAnimatorListener
            public final void onAnimationEnd() {
                if (!this.mCanceled) {
                    ToolbarWidgetWrapper.this.mToolbar.setVisibility(i);
                }
            }

            @Override // com.airbnb.lottie.L, androidx.core.view.ViewPropertyAnimatorListener
            public final void onAnimationStart() {
                ToolbarWidgetWrapper.this.mToolbar.setVisibility(0);
            }
        });
        return animate;
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final boolean showOverflowMenu() {
        boolean z;
        Toolbar toolbar = this.mToolbar;
        Objects.requireNonNull(toolbar);
        ActionMenuView actionMenuView = toolbar.mMenuView;
        if (actionMenuView != null) {
            ActionMenuPresenter actionMenuPresenter = actionMenuView.mPresenter;
            if (actionMenuPresenter == null || !actionMenuPresenter.showOverflowMenu()) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    public final void updateHomeAccessibility() {
        CharSequence charSequence;
        if ((this.mDisplayOpts & 4) == 0) {
            return;
        }
        if (TextUtils.isEmpty(this.mHomeDescription)) {
            Toolbar toolbar = this.mToolbar;
            int i = this.mDefaultNavigationContentDescription;
            Objects.requireNonNull(toolbar);
            if (i != 0) {
                charSequence = toolbar.getContext().getText(i);
            } else {
                charSequence = null;
            }
            toolbar.setNavigationContentDescription(charSequence);
            return;
        }
        this.mToolbar.setNavigationContentDescription(this.mHomeDescription);
    }

    public final void updateToolbarLogo() {
        Drawable drawable;
        int i = this.mDisplayOpts;
        if ((i & 2) == 0) {
            drawable = null;
        } else if ((i & 1) != 0) {
            drawable = this.mLogo;
            if (drawable == null) {
                drawable = this.mIcon;
            }
        } else {
            drawable = this.mIcon;
        }
        this.mToolbar.setLogo(drawable);
    }

    public ToolbarWidgetWrapper(Toolbar toolbar) {
        boolean z;
        CharSequence charSequence;
        String str;
        Drawable drawable;
        this.mDefaultNavigationContentDescription = 0;
        this.mToolbar = toolbar;
        Objects.requireNonNull(toolbar);
        CharSequence charSequence2 = toolbar.mTitleText;
        this.mTitle = charSequence2;
        this.mSubtitle = toolbar.mSubtitleText;
        if (charSequence2 != null) {
            z = true;
        } else {
            z = false;
        }
        this.mTitleSet = z;
        this.mNavIcon = toolbar.getNavigationIcon();
        CharSequence charSequence3 = null;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(toolbar.getContext(), null, R$styleable.ActionBar, 2130968581);
        this.mDefaultNavigationIcon = obtainStyledAttributes.getDrawable(15);
        CharSequence text = obtainStyledAttributes.getText(27);
        if (!TextUtils.isEmpty(text)) {
            this.mTitleSet = true;
            this.mTitle = text;
            if ((this.mDisplayOpts & 8) != 0) {
                this.mToolbar.setTitle(text);
                if (this.mTitleSet) {
                    ViewCompat.setAccessibilityPaneTitle(this.mToolbar.getRootView(), text);
                }
            }
        }
        CharSequence text2 = obtainStyledAttributes.getText(25);
        if (!TextUtils.isEmpty(text2)) {
            this.mSubtitle = text2;
            if ((this.mDisplayOpts & 8) != 0) {
                this.mToolbar.setSubtitle(text2);
            }
        }
        Drawable drawable2 = obtainStyledAttributes.getDrawable(20);
        if (drawable2 != null) {
            this.mLogo = drawable2;
            updateToolbarLogo();
        }
        Drawable drawable3 = obtainStyledAttributes.getDrawable(17);
        if (drawable3 != null) {
            this.mIcon = drawable3;
            updateToolbarLogo();
        }
        if (this.mNavIcon == null && (drawable = this.mDefaultNavigationIcon) != null) {
            this.mNavIcon = drawable;
            if ((this.mDisplayOpts & 4) != 0) {
                this.mToolbar.setNavigationIcon(drawable);
            } else {
                this.mToolbar.setNavigationIcon(null);
            }
        }
        setDisplayOptions(obtainStyledAttributes.getInt(10, 0));
        int resourceId = obtainStyledAttributes.getResourceId(9, 0);
        if (resourceId != 0) {
            View inflate = LayoutInflater.from(this.mToolbar.getContext()).inflate(resourceId, (ViewGroup) this.mToolbar, false);
            View view = this.mCustomView;
            if (!(view == null || (this.mDisplayOpts & 16) == 0)) {
                this.mToolbar.removeView(view);
            }
            this.mCustomView = inflate;
            if (!(inflate == null || (this.mDisplayOpts & 16) == 0)) {
                this.mToolbar.addView(inflate);
            }
            setDisplayOptions(this.mDisplayOpts | 16);
        }
        int layoutDimension = obtainStyledAttributes.mWrapped.getLayoutDimension(13, 0);
        if (layoutDimension > 0) {
            ViewGroup.LayoutParams layoutParams = this.mToolbar.getLayoutParams();
            layoutParams.height = layoutDimension;
            this.mToolbar.setLayoutParams(layoutParams);
        }
        int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(7, -1);
        int dimensionPixelOffset2 = obtainStyledAttributes.getDimensionPixelOffset(3, -1);
        if (dimensionPixelOffset >= 0 || dimensionPixelOffset2 >= 0) {
            Toolbar toolbar2 = this.mToolbar;
            int max = Math.max(dimensionPixelOffset, 0);
            int max2 = Math.max(dimensionPixelOffset2, 0);
            Objects.requireNonNull(toolbar2);
            if (toolbar2.mContentInsets == null) {
                toolbar2.mContentInsets = new RtlSpacingHelper();
            }
            toolbar2.mContentInsets.setRelative(max, max2);
        }
        int resourceId2 = obtainStyledAttributes.getResourceId(28, 0);
        if (resourceId2 != 0) {
            Toolbar toolbar3 = this.mToolbar;
            Context context = toolbar3.getContext();
            toolbar3.mTitleTextAppearance = resourceId2;
            AppCompatTextView appCompatTextView = toolbar3.mTitleTextView;
            if (appCompatTextView != null) {
                appCompatTextView.setTextAppearance(context, resourceId2);
            }
        }
        int resourceId3 = obtainStyledAttributes.getResourceId(26, 0);
        if (resourceId3 != 0) {
            Toolbar toolbar4 = this.mToolbar;
            Context context2 = toolbar4.getContext();
            toolbar4.mSubtitleTextAppearance = resourceId3;
            AppCompatTextView appCompatTextView2 = toolbar4.mSubtitleTextView;
            if (appCompatTextView2 != null) {
                appCompatTextView2.setTextAppearance(context2, resourceId3);
            }
        }
        int resourceId4 = obtainStyledAttributes.getResourceId(22, 0);
        if (resourceId4 != 0) {
            Toolbar toolbar5 = this.mToolbar;
            Objects.requireNonNull(toolbar5);
            if (toolbar5.mPopupTheme != resourceId4) {
                toolbar5.mPopupTheme = resourceId4;
                if (resourceId4 == 0) {
                    toolbar5.mPopupContext = toolbar5.getContext();
                } else {
                    toolbar5.mPopupContext = new ContextThemeWrapper(toolbar5.getContext(), resourceId4);
                }
            }
        }
        obtainStyledAttributes.recycle();
        if (2131951619 != this.mDefaultNavigationContentDescription) {
            this.mDefaultNavigationContentDescription = 2131951619;
            Toolbar toolbar6 = this.mToolbar;
            Objects.requireNonNull(toolbar6);
            AppCompatImageButton appCompatImageButton = toolbar6.mNavButtonView;
            if (appCompatImageButton != null) {
                charSequence = appCompatImageButton.getContentDescription();
            } else {
                charSequence = null;
            }
            if (TextUtils.isEmpty(charSequence)) {
                int i = this.mDefaultNavigationContentDescription;
                if (i == 0) {
                    str = null;
                } else {
                    str = getContext().getString(i);
                }
                this.mHomeDescription = str;
                updateHomeAccessibility();
            }
        }
        Toolbar toolbar7 = this.mToolbar;
        Objects.requireNonNull(toolbar7);
        AppCompatImageButton appCompatImageButton2 = toolbar7.mNavButtonView;
        this.mHomeDescription = appCompatImageButton2 != null ? appCompatImageButton2.getContentDescription() : charSequence3;
        Toolbar toolbar8 = this.mToolbar;
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: androidx.appcompat.widget.ToolbarWidgetWrapper.1
            public final ActionMenuItem mNavItem;

            {
                this.mNavItem = new ActionMenuItem(ToolbarWidgetWrapper.this.mToolbar.getContext(), ToolbarWidgetWrapper.this.mTitle);
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ToolbarWidgetWrapper toolbarWidgetWrapper = ToolbarWidgetWrapper.this;
                Window.Callback callback = toolbarWidgetWrapper.mWindowCallback;
                if (callback != null && toolbarWidgetWrapper.mMenuPrepared) {
                    callback.onMenuItemSelected(0, this.mNavItem);
                }
            }
        };
        Objects.requireNonNull(toolbar8);
        toolbar8.ensureNavButtonView();
        toolbar8.mNavButtonView.setOnClickListener(onClickListener);
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final void setWindowCallback(Window.Callback callback) {
        this.mWindowCallback = callback;
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public final int getDisplayOptions() {
        return this.mDisplayOpts;
    }
}
