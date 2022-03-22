package androidx.appcompat.widget;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import androidx.appcompat.view.menu.ListMenuItemView;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MenuPopupWindow extends ListPopupWindow implements MenuItemHoverListener {
    public MenuItemHoverListener mHoverListener;

    /* loaded from: classes.dex */
    public static class MenuDropDownListView extends DropDownListView {
        public final int mAdvanceKey;
        public MenuItemHoverListener mHoverListener;
        public MenuItemImpl mHoveredMenuItem;
        public final int mRetreatKey;

        @Override // androidx.appcompat.widget.DropDownListView, android.view.View
        public final boolean onHoverEvent(MotionEvent motionEvent) {
            int i;
            MenuAdapter menuAdapter;
            int pointToPosition;
            int i2;
            if (this.mHoverListener != null) {
                ListAdapter adapter = getAdapter();
                if (adapter instanceof HeaderViewListAdapter) {
                    HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
                    i = headerViewListAdapter.getHeadersCount();
                    menuAdapter = (MenuAdapter) headerViewListAdapter.getWrappedAdapter();
                } else {
                    i = 0;
                    menuAdapter = (MenuAdapter) adapter;
                }
                MenuItemImpl menuItemImpl = null;
                if (motionEvent.getAction() != 10 && (pointToPosition = pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY())) != -1 && (i2 = pointToPosition - i) >= 0 && i2 < menuAdapter.getCount()) {
                    menuItemImpl = menuAdapter.getItem(i2);
                }
                MenuItemImpl menuItemImpl2 = this.mHoveredMenuItem;
                if (menuItemImpl2 != menuItemImpl) {
                    Objects.requireNonNull(menuAdapter);
                    MenuBuilder menuBuilder = menuAdapter.mAdapterMenu;
                    if (menuItemImpl2 != null) {
                        this.mHoverListener.onItemHoverExit(menuBuilder, menuItemImpl2);
                    }
                    this.mHoveredMenuItem = menuItemImpl;
                    if (menuItemImpl != null) {
                        this.mHoverListener.onItemHoverEnter(menuBuilder, menuItemImpl);
                    }
                }
            }
            return super.onHoverEvent(motionEvent);
        }

        public MenuDropDownListView(Context context, boolean z) {
            super(context, z);
            if (1 == context.getResources().getConfiguration().getLayoutDirection()) {
                this.mAdvanceKey = 21;
                this.mRetreatKey = 22;
                return;
            }
            this.mAdvanceKey = 22;
            this.mRetreatKey = 21;
        }

        @Override // android.widget.ListView, android.widget.AbsListView, android.view.View, android.view.KeyEvent.Callback
        public final boolean onKeyDown(int i, KeyEvent keyEvent) {
            MenuAdapter menuAdapter;
            ListMenuItemView listMenuItemView = (ListMenuItemView) getSelectedView();
            if (listMenuItemView != null && i == this.mAdvanceKey) {
                if (listMenuItemView.isEnabled() && listMenuItemView.mItemData.hasSubMenu()) {
                    performItemClick(listMenuItemView, getSelectedItemPosition(), getSelectedItemId());
                }
                return true;
            } else if (listMenuItemView == null || i != this.mRetreatKey) {
                return super.onKeyDown(i, keyEvent);
            } else {
                setSelection(-1);
                ListAdapter adapter = getAdapter();
                if (adapter instanceof HeaderViewListAdapter) {
                    menuAdapter = (MenuAdapter) ((HeaderViewListAdapter) adapter).getWrappedAdapter();
                } else {
                    menuAdapter = (MenuAdapter) adapter;
                }
                Objects.requireNonNull(menuAdapter);
                menuAdapter.mAdapterMenu.close(false);
                return true;
            }
        }
    }

    public MenuPopupWindow(Context context, int i, int i2) {
        super(context, null, i, i2);
    }

    @Override // androidx.appcompat.widget.ListPopupWindow
    public final DropDownListView createDropDownListView(Context context, boolean z) {
        MenuDropDownListView menuDropDownListView = new MenuDropDownListView(context, z);
        menuDropDownListView.mHoverListener = this;
        return menuDropDownListView;
    }

    @Override // androidx.appcompat.widget.MenuItemHoverListener
    public final void onItemHoverEnter(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        MenuItemHoverListener menuItemHoverListener = this.mHoverListener;
        if (menuItemHoverListener != null) {
            menuItemHoverListener.onItemHoverEnter(menuBuilder, menuItemImpl);
        }
    }

    @Override // androidx.appcompat.widget.MenuItemHoverListener
    public final void onItemHoverExit(MenuBuilder menuBuilder, MenuItem menuItem) {
        MenuItemHoverListener menuItemHoverListener = this.mHoverListener;
        if (menuItemHoverListener != null) {
            menuItemHoverListener.onItemHoverExit(menuBuilder, menuItem);
        }
    }
}
