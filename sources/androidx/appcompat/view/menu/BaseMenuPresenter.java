package androidx.appcompat.view.menu;

import android.content.Context;
import android.view.LayoutInflater;
import androidx.appcompat.view.menu.MenuPresenter;
/* loaded from: classes.dex */
public abstract class BaseMenuPresenter implements MenuPresenter {
    public MenuPresenter.Callback mCallback;
    public Context mContext;
    public int mId;
    public MenuBuilder mMenu;
    public MenuView mMenuView;
    public Context mSystemContext;
    public LayoutInflater mSystemInflater;
    public int mMenuLayoutRes = 2131623939;
    public int mItemLayoutRes = 2131623938;

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final boolean collapseItemActionView(MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final boolean expandItemActionView(MenuItemImpl menuItemImpl) {
        return false;
    }

    public BaseMenuPresenter(Context context) {
        this.mSystemContext = context;
        this.mSystemInflater = LayoutInflater.from(context);
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void setCallback(MenuPresenter.Callback callback) {
        this.mCallback = callback;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final int getId() {
        return this.mId;
    }
}
