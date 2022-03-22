package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.widget.DropDownListView;
import androidx.appcompat.widget.MenuItemHoverListener;
import androidx.appcompat.widget.MenuPopupWindow;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class CascadingMenuPopup extends MenuPopup implements View.OnKeyListener, PopupWindow.OnDismissListener {
    public View mAnchorView;
    public final Context mContext;
    public boolean mHasXOffset;
    public boolean mHasYOffset;
    public int mLastPosition;
    public final int mMenuMaxWidth;
    public PopupWindow.OnDismissListener mOnDismissListener;
    public final boolean mOverflowOnly;
    public final int mPopupStyleAttr;
    public final int mPopupStyleRes;
    public MenuPresenter.Callback mPresenterCallback;
    public boolean mShouldCloseImmediately;
    public boolean mShowTitle;
    public View mShownAnchorView;
    public final Handler mSubMenuHoverHandler;
    public ViewTreeObserver mTreeObserver;
    public int mXOffset;
    public int mYOffset;
    public final ArrayList mPendingMenus = new ArrayList();
    public final ArrayList mShowingMenus = new ArrayList();
    public final AnonymousClass1 mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: androidx.appcompat.view.menu.CascadingMenuPopup.1
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public final void onGlobalLayout() {
            if (CascadingMenuPopup.this.isShowing() && CascadingMenuPopup.this.mShowingMenus.size() > 0) {
                MenuPopupWindow menuPopupWindow = ((CascadingMenuInfo) CascadingMenuPopup.this.mShowingMenus.get(0)).window;
                Objects.requireNonNull(menuPopupWindow);
                if (!menuPopupWindow.mModal) {
                    View view = CascadingMenuPopup.this.mShownAnchorView;
                    if (view == null || !view.isShown()) {
                        CascadingMenuPopup.this.dismiss();
                        return;
                    }
                    Iterator it = CascadingMenuPopup.this.mShowingMenus.iterator();
                    while (it.hasNext()) {
                        ((CascadingMenuInfo) it.next()).window.show();
                    }
                }
            }
        }
    };
    public final AnonymousClass2 mAttachStateChangeListener = new View.OnAttachStateChangeListener() { // from class: androidx.appcompat.view.menu.CascadingMenuPopup.2
        @Override // android.view.View.OnAttachStateChangeListener
        public final void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public final void onViewDetachedFromWindow(View view) {
            ViewTreeObserver viewTreeObserver = CascadingMenuPopup.this.mTreeObserver;
            if (viewTreeObserver != null) {
                if (!viewTreeObserver.isAlive()) {
                    CascadingMenuPopup.this.mTreeObserver = view.getViewTreeObserver();
                }
                CascadingMenuPopup cascadingMenuPopup = CascadingMenuPopup.this;
                cascadingMenuPopup.mTreeObserver.removeGlobalOnLayoutListener(cascadingMenuPopup.mGlobalLayoutListener);
            }
            view.removeOnAttachStateChangeListener(this);
        }
    };
    public final AnonymousClass3 mMenuItemHoverListener = new MenuItemHoverListener() { // from class: androidx.appcompat.view.menu.CascadingMenuPopup.3
        @Override // androidx.appcompat.widget.MenuItemHoverListener
        public final void onItemHoverEnter(final MenuBuilder menuBuilder, final MenuItemImpl menuItemImpl) {
            final CascadingMenuInfo cascadingMenuInfo = null;
            CascadingMenuPopup.this.mSubMenuHoverHandler.removeCallbacksAndMessages(null);
            int size = CascadingMenuPopup.this.mShowingMenus.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    i = -1;
                    break;
                } else if (menuBuilder == ((CascadingMenuInfo) CascadingMenuPopup.this.mShowingMenus.get(i)).menu) {
                    break;
                } else {
                    i++;
                }
            }
            if (i != -1) {
                int i2 = i + 1;
                if (i2 < CascadingMenuPopup.this.mShowingMenus.size()) {
                    cascadingMenuInfo = (CascadingMenuInfo) CascadingMenuPopup.this.mShowingMenus.get(i2);
                }
                CascadingMenuPopup.this.mSubMenuHoverHandler.postAtTime(new Runnable() { // from class: androidx.appcompat.view.menu.CascadingMenuPopup.3.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        CascadingMenuInfo cascadingMenuInfo2 = cascadingMenuInfo;
                        if (cascadingMenuInfo2 != null) {
                            CascadingMenuPopup.this.mShouldCloseImmediately = true;
                            cascadingMenuInfo2.menu.close(false);
                            CascadingMenuPopup.this.mShouldCloseImmediately = false;
                        }
                        if (menuItemImpl.isEnabled() && menuItemImpl.hasSubMenu()) {
                            MenuBuilder menuBuilder2 = menuBuilder;
                            MenuItem menuItem = menuItemImpl;
                            Objects.requireNonNull(menuBuilder2);
                            menuBuilder2.performItemAction(menuItem, null, 4);
                        }
                    }
                }, menuBuilder, SystemClock.uptimeMillis() + 200);
            }
        }

        @Override // androidx.appcompat.widget.MenuItemHoverListener
        public final void onItemHoverExit(MenuBuilder menuBuilder, MenuItem menuItem) {
            CascadingMenuPopup.this.mSubMenuHoverHandler.removeCallbacksAndMessages(menuBuilder);
        }
    };
    public int mRawDropDownGravity = 0;
    public int mDropDownGravity = 0;
    public boolean mForceShowIcon = false;

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final boolean flagActionItems() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void onRestoreInstanceState(Parcelable parcelable) {
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final Parcelable onSaveInstanceState() {
        return null;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public final void setHorizontalOffset(int i) {
        this.mHasXOffset = true;
        this.mXOffset = i;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public final void setVerticalOffset(int i) {
        this.mHasYOffset = true;
        this.mYOffset = i;
    }

    /* loaded from: classes.dex */
    public static class CascadingMenuInfo {
        public final MenuBuilder menu;
        public final int position;
        public final MenuPopupWindow window;

        public CascadingMenuInfo(MenuPopupWindow menuPopupWindow, MenuBuilder menuBuilder, int i) {
            this.window = menuPopupWindow;
            this.menu = menuBuilder;
            this.position = i;
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public final void addMenu(MenuBuilder menuBuilder) {
        menuBuilder.addMenuPresenter(this, this.mContext);
        if (isShowing()) {
            showMenu(menuBuilder);
        } else {
            this.mPendingMenus.add(menuBuilder);
        }
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public final void dismiss() {
        int size = this.mShowingMenus.size();
        if (size > 0) {
            CascadingMenuInfo[] cascadingMenuInfoArr = (CascadingMenuInfo[]) this.mShowingMenus.toArray(new CascadingMenuInfo[size]);
            while (true) {
                size--;
                if (size >= 0) {
                    CascadingMenuInfo cascadingMenuInfo = cascadingMenuInfoArr[size];
                    if (cascadingMenuInfo.window.isShowing()) {
                        cascadingMenuInfo.window.dismiss();
                    }
                } else {
                    return;
                }
            }
        }
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public final DropDownListView getListView() {
        if (this.mShowingMenus.isEmpty()) {
            return null;
        }
        ArrayList arrayList = this.mShowingMenus;
        CascadingMenuInfo cascadingMenuInfo = (CascadingMenuInfo) arrayList.get(arrayList.size() - 1);
        Objects.requireNonNull(cascadingMenuInfo);
        MenuPopupWindow menuPopupWindow = cascadingMenuInfo.window;
        Objects.requireNonNull(menuPopupWindow);
        return menuPopupWindow.mDropDownList;
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public final boolean isShowing() {
        if (this.mShowingMenus.size() <= 0 || !((CascadingMenuInfo) this.mShowingMenus.get(0)).window.isShowing()) {
            return false;
        }
        return true;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        int i;
        int size = this.mShowingMenus.size();
        int i2 = 0;
        while (true) {
            if (i2 >= size) {
                i2 = -1;
                break;
            } else if (menuBuilder == ((CascadingMenuInfo) this.mShowingMenus.get(i2)).menu) {
                break;
            } else {
                i2++;
            }
        }
        if (i2 >= 0) {
            int i3 = i2 + 1;
            if (i3 < this.mShowingMenus.size()) {
                ((CascadingMenuInfo) this.mShowingMenus.get(i3)).menu.close(false);
            }
            CascadingMenuInfo cascadingMenuInfo = (CascadingMenuInfo) this.mShowingMenus.remove(i2);
            cascadingMenuInfo.menu.removeMenuPresenter(this);
            if (this.mShouldCloseImmediately) {
                MenuPopupWindow menuPopupWindow = cascadingMenuInfo.window;
                Objects.requireNonNull(menuPopupWindow);
                menuPopupWindow.mPopup.setExitTransition(null);
                MenuPopupWindow menuPopupWindow2 = cascadingMenuInfo.window;
                Objects.requireNonNull(menuPopupWindow2);
                menuPopupWindow2.mPopup.setAnimationStyle(0);
            }
            cascadingMenuInfo.window.dismiss();
            int size2 = this.mShowingMenus.size();
            if (size2 > 0) {
                this.mLastPosition = ((CascadingMenuInfo) this.mShowingMenus.get(size2 - 1)).position;
            } else {
                View view = this.mAnchorView;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                if (ViewCompat.Api17Impl.getLayoutDirection(view) == 1) {
                    i = 0;
                } else {
                    i = 1;
                }
                this.mLastPosition = i;
            }
            if (size2 == 0) {
                dismiss();
                MenuPresenter.Callback callback = this.mPresenterCallback;
                if (callback != null) {
                    callback.onCloseMenu(menuBuilder, true);
                }
                ViewTreeObserver viewTreeObserver = this.mTreeObserver;
                if (viewTreeObserver != null) {
                    if (viewTreeObserver.isAlive()) {
                        this.mTreeObserver.removeGlobalOnLayoutListener(this.mGlobalLayoutListener);
                    }
                    this.mTreeObserver = null;
                }
                this.mShownAnchorView.removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
                this.mOnDismissListener.onDismiss();
            } else if (z) {
                ((CascadingMenuInfo) this.mShowingMenus.get(0)).menu.close(false);
            }
        }
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public final void onDismiss() {
        CascadingMenuInfo cascadingMenuInfo;
        int size = this.mShowingMenus.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                cascadingMenuInfo = null;
                break;
            }
            cascadingMenuInfo = (CascadingMenuInfo) this.mShowingMenus.get(i);
            if (!cascadingMenuInfo.window.isShowing()) {
                break;
            }
            i++;
        }
        if (cascadingMenuInfo != null) {
            cascadingMenuInfo.menu.close(false);
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        Iterator it = this.mShowingMenus.iterator();
        while (it.hasNext()) {
            CascadingMenuInfo cascadingMenuInfo = (CascadingMenuInfo) it.next();
            if (subMenuBuilder == cascadingMenuInfo.menu) {
                MenuPopupWindow menuPopupWindow = cascadingMenuInfo.window;
                Objects.requireNonNull(menuPopupWindow);
                menuPopupWindow.mDropDownList.requestFocus();
                return true;
            }
        }
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        addMenu(subMenuBuilder);
        MenuPresenter.Callback callback = this.mPresenterCallback;
        if (callback != null) {
            callback.onOpenSubMenu(subMenuBuilder);
        }
        return true;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public final void setAnchorView(View view) {
        if (this.mAnchorView != view) {
            this.mAnchorView = view;
            int i = this.mRawDropDownGravity;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            this.mDropDownGravity = Gravity.getAbsoluteGravity(i, ViewCompat.Api17Impl.getLayoutDirection(view));
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public final void setGravity(int i) {
        if (this.mRawDropDownGravity != i) {
            this.mRawDropDownGravity = i;
            View view = this.mAnchorView;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            this.mDropDownGravity = Gravity.getAbsoluteGravity(i, ViewCompat.Api17Impl.getLayoutDirection(view));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x0134, code lost:
        if (((r8.getWidth() + r10[0]) + r4) > r11.right) goto L_0x013f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x013a, code lost:
        if ((r10[0] - r4) < 0) goto L_0x013c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x013c, code lost:
        r8 = 1;
        r11 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x013f, code lost:
        r11 = 0;
        r8 = 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void showMenu(androidx.appcompat.view.menu.MenuBuilder r17) {
        /*
            Method dump skipped, instructions count: 461
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.view.menu.CascadingMenuPopup.showMenu(androidx.appcompat.view.menu.MenuBuilder):void");
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void updateMenuView(boolean z) {
        Iterator it = this.mShowingMenus.iterator();
        while (it.hasNext()) {
            CascadingMenuInfo cascadingMenuInfo = (CascadingMenuInfo) it.next();
            Objects.requireNonNull(cascadingMenuInfo);
            MenuPopupWindow menuPopupWindow = cascadingMenuInfo.window;
            Objects.requireNonNull(menuPopupWindow);
            ListAdapter adapter = menuPopupWindow.mDropDownList.getAdapter();
            if (adapter instanceof HeaderViewListAdapter) {
                adapter = ((HeaderViewListAdapter) adapter).getWrappedAdapter();
            }
            ((MenuAdapter) adapter).notifyDataSetChanged();
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [androidx.appcompat.view.menu.CascadingMenuPopup$1] */
    /* JADX WARN: Type inference failed for: r0v3, types: [androidx.appcompat.view.menu.CascadingMenuPopup$2] */
    /* JADX WARN: Type inference failed for: r0v4, types: [androidx.appcompat.view.menu.CascadingMenuPopup$3] */
    public CascadingMenuPopup(Context context, View view, int i, int i2, boolean z) {
        int i3 = 0;
        this.mContext = context;
        this.mAnchorView = view;
        this.mPopupStyleAttr = i;
        this.mPopupStyleRes = i2;
        this.mOverflowOnly = z;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        this.mLastPosition = ViewCompat.Api17Impl.getLayoutDirection(view) != 1 ? 1 : i3;
        Resources resources = context.getResources();
        this.mMenuMaxWidth = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(2131165209));
        this.mSubMenuHoverHandler = new Handler();
    }

    @Override // android.view.View.OnKeyListener
    public final boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 1 || i != 82) {
            return false;
        }
        dismiss();
        return true;
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public final void show() {
        boolean z;
        if (!isShowing()) {
            Iterator it = this.mPendingMenus.iterator();
            while (it.hasNext()) {
                showMenu((MenuBuilder) it.next());
            }
            this.mPendingMenus.clear();
            View view = this.mAnchorView;
            this.mShownAnchorView = view;
            if (view != null) {
                if (this.mTreeObserver == null) {
                    z = true;
                } else {
                    z = false;
                }
                ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
                this.mTreeObserver = viewTreeObserver;
                if (z) {
                    viewTreeObserver.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
                }
                this.mShownAnchorView.addOnAttachStateChangeListener(this.mAttachStateChangeListener);
            }
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void setCallback(MenuPresenter.Callback callback) {
        this.mPresenterCallback = callback;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public final void setForceShowIcon(boolean z) {
        this.mForceShowIcon = z;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public final void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public final void setShowTitle(boolean z) {
        this.mShowTitle = z;
    }
}
