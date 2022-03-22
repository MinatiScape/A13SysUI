package androidx.appcompat.view.menu;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import androidx.appcompat.app.AlertController;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ListMenuPresenter implements MenuPresenter, AdapterView.OnItemClickListener {
    public MenuAdapter mAdapter;
    public MenuPresenter.Callback mCallback;
    public Context mContext;
    public LayoutInflater mInflater;
    public MenuBuilder mMenu;
    public ExpandedMenuView mMenuView;

    /* loaded from: classes.dex */
    public class MenuAdapter extends BaseAdapter {
        public int mExpandedIndex = -1;

        @Override // android.widget.Adapter
        public final long getItemId(int i) {
            return i;
        }

        public MenuAdapter() {
            findExpandedIndex();
        }

        public final void findExpandedIndex() {
            MenuBuilder menuBuilder = ListMenuPresenter.this.mMenu;
            Objects.requireNonNull(menuBuilder);
            MenuItemImpl menuItemImpl = menuBuilder.mExpandedItem;
            if (menuItemImpl != null) {
                MenuBuilder menuBuilder2 = ListMenuPresenter.this.mMenu;
                Objects.requireNonNull(menuBuilder2);
                menuBuilder2.flagActionItems();
                ArrayList<MenuItemImpl> arrayList = menuBuilder2.mNonActionItems;
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    if (arrayList.get(i) == menuItemImpl) {
                        this.mExpandedIndex = i;
                        return;
                    }
                }
            }
            this.mExpandedIndex = -1;
        }

        @Override // android.widget.Adapter
        public final int getCount() {
            MenuBuilder menuBuilder = ListMenuPresenter.this.mMenu;
            Objects.requireNonNull(menuBuilder);
            menuBuilder.flagActionItems();
            int size = menuBuilder.mNonActionItems.size();
            Objects.requireNonNull(ListMenuPresenter.this);
            int i = size + 0;
            if (this.mExpandedIndex < 0) {
                return i;
            }
            return i - 1;
        }

        @Override // android.widget.Adapter
        public final MenuItemImpl getItem(int i) {
            MenuBuilder menuBuilder = ListMenuPresenter.this.mMenu;
            Objects.requireNonNull(menuBuilder);
            menuBuilder.flagActionItems();
            ArrayList<MenuItemImpl> arrayList = menuBuilder.mNonActionItems;
            Objects.requireNonNull(ListMenuPresenter.this);
            int i2 = i + 0;
            int i3 = this.mExpandedIndex;
            if (i3 >= 0 && i2 >= i3) {
                i2++;
            }
            return arrayList.get(i2);
        }

        @Override // android.widget.Adapter
        public final View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = ListMenuPresenter.this.mInflater.inflate(2131623952, viewGroup, false);
            }
            ((MenuView.ItemView) view).initialize(getItem(i));
            return view;
        }

        @Override // android.widget.BaseAdapter
        public final void notifyDataSetChanged() {
            findExpandedIndex();
            super.notifyDataSetChanged();
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
    public final int getId() {
        return 0;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void initForMenu(Context context, MenuBuilder menuBuilder) {
        if (this.mContext != null) {
            this.mContext = context;
            if (this.mInflater == null) {
                this.mInflater = LayoutInflater.from(context);
            }
        }
        this.mMenu = menuBuilder;
        MenuAdapter menuAdapter = this.mAdapter;
        if (menuAdapter != null) {
            menuAdapter.notifyDataSetChanged();
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        MenuPresenter.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCloseMenu(menuBuilder, z);
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public final void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        this.mMenu.performItemAction(this.mAdapter.getItem(i), this, 0);
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void onRestoreInstanceState(Parcelable parcelable) {
        SparseArray<Parcelable> sparseParcelableArray = ((Bundle) parcelable).getSparseParcelableArray("android:menu:list");
        if (sparseParcelableArray != null) {
            this.mMenuView.restoreHierarchyState(sparseParcelableArray);
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final Parcelable onSaveInstanceState() {
        if (this.mMenuView == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        SparseArray<Parcelable> sparseArray = new SparseArray<>();
        ExpandedMenuView expandedMenuView = this.mMenuView;
        if (expandedMenuView != null) {
            expandedMenuView.saveHierarchyState(sparseArray);
        }
        bundle.putSparseParcelableArray("android:menu:list", sparseArray);
        return bundle;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void updateMenuView(boolean z) {
        MenuAdapter menuAdapter = this.mAdapter;
        if (menuAdapter != null) {
            menuAdapter.notifyDataSetChanged();
        }
    }

    public ListMenuPresenter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        MenuDialogHelper menuDialogHelper = new MenuDialogHelper(subMenuBuilder);
        AlertDialog.Builder builder = new AlertDialog.Builder(subMenuBuilder.mContext);
        ListMenuPresenter listMenuPresenter = new ListMenuPresenter(builder.P.mContext);
        menuDialogHelper.mPresenter = listMenuPresenter;
        listMenuPresenter.mCallback = menuDialogHelper;
        MenuBuilder menuBuilder = menuDialogHelper.mMenu;
        Objects.requireNonNull(menuBuilder);
        menuBuilder.addMenuPresenter(listMenuPresenter, menuBuilder.mContext);
        ListMenuPresenter listMenuPresenter2 = menuDialogHelper.mPresenter;
        Objects.requireNonNull(listMenuPresenter2);
        if (listMenuPresenter2.mAdapter == null) {
            listMenuPresenter2.mAdapter = new MenuAdapter();
        }
        MenuAdapter menuAdapter = listMenuPresenter2.mAdapter;
        AlertController.AlertParams alertParams = builder.P;
        alertParams.mAdapter = menuAdapter;
        alertParams.mOnClickListener = menuDialogHelper;
        View view = subMenuBuilder.mHeaderView;
        if (view != null) {
            alertParams.mCustomTitleView = view;
        } else {
            alertParams.mIcon = subMenuBuilder.mHeaderIcon;
            alertParams.mTitle = subMenuBuilder.mHeaderTitle;
        }
        alertParams.mOnKeyListener = menuDialogHelper;
        AlertDialog create = builder.create();
        menuDialogHelper.mDialog = create;
        create.setOnDismissListener(menuDialogHelper);
        WindowManager.LayoutParams attributes = menuDialogHelper.mDialog.getWindow().getAttributes();
        attributes.type = 1003;
        attributes.flags |= 131072;
        menuDialogHelper.mDialog.show();
        MenuPresenter.Callback callback = this.mCallback;
        if (callback == null) {
            return true;
        }
        callback.onOpenSubMenu(subMenuBuilder);
        return true;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void setCallback(MenuPresenter.Callback callback) {
        this.mCallback = callback;
    }
}
