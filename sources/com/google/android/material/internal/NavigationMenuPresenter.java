package com.google.android.material.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class NavigationMenuPresenter implements MenuPresenter {
    public NavigationMenuAdapter adapter;
    public int dividerInsetEnd;
    public int dividerInsetStart;
    public boolean hasCustomItemIconSize;
    public LinearLayout headerLayout;
    public ColorStateList iconTintList;
    public int id;
    public Drawable itemBackground;
    public int itemHorizontalPadding;
    public int itemIconPadding;
    public int itemIconSize;
    public int itemMaxLines;
    public int itemVerticalPadding;
    public LayoutInflater layoutInflater;
    public MenuBuilder menu;
    public NavigationMenuView menuView;
    public int paddingSeparator;
    public int paddingTopDefault;
    public ColorStateList subheaderColor;
    public int subheaderInsetStart;
    public ColorStateList textColor;
    public int subheaderTextAppearance = 0;
    public int textAppearance = 0;
    public boolean isBehindStatusBar = true;
    public int overScrollMode = -1;
    public final AnonymousClass1 onClickListener = new View.OnClickListener() { // from class: com.google.android.material.internal.NavigationMenuPresenter.1
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView) view;
            NavigationMenuPresenter navigationMenuPresenter = NavigationMenuPresenter.this;
            Objects.requireNonNull(navigationMenuPresenter);
            NavigationMenuAdapter navigationMenuAdapter = navigationMenuPresenter.adapter;
            boolean z = true;
            if (navigationMenuAdapter != null) {
                navigationMenuAdapter.updateSuspended = true;
            }
            Objects.requireNonNull(navigationMenuItemView);
            MenuItemImpl menuItemImpl = navigationMenuItemView.itemData;
            NavigationMenuPresenter navigationMenuPresenter2 = NavigationMenuPresenter.this;
            boolean performItemAction = navigationMenuPresenter2.menu.performItemAction(menuItemImpl, navigationMenuPresenter2, 0);
            if (menuItemImpl == null || !menuItemImpl.isCheckable() || !performItemAction) {
                z = false;
            } else {
                NavigationMenuPresenter.this.adapter.setCheckedItem(menuItemImpl);
            }
            NavigationMenuPresenter navigationMenuPresenter3 = NavigationMenuPresenter.this;
            Objects.requireNonNull(navigationMenuPresenter3);
            NavigationMenuAdapter navigationMenuAdapter2 = navigationMenuPresenter3.adapter;
            if (navigationMenuAdapter2 != null) {
                navigationMenuAdapter2.updateSuspended = false;
            }
            if (z) {
                NavigationMenuPresenter.this.updateMenuView(false);
            }
        }
    };

    /* loaded from: classes.dex */
    public class NavigationMenuAdapter extends RecyclerView.Adapter<ViewHolder> {
        public MenuItemImpl checkedItem;
        public final ArrayList<NavigationMenuItem> items = new ArrayList<>();
        public boolean updateSuspended;

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final long getItemId(int i) {
            return i;
        }

        public NavigationMenuAdapter() {
            prepareMenuItems();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final int getItemCount() {
            return this.items.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final int getItemViewType(int i) {
            NavigationMenuItem navigationMenuItem = this.items.get(i);
            if (navigationMenuItem instanceof NavigationMenuSeparatorItem) {
                return 2;
            }
            if (navigationMenuItem instanceof NavigationMenuHeaderItem) {
                return 3;
            }
            if (navigationMenuItem instanceof NavigationMenuTextItem) {
                NavigationMenuTextItem navigationMenuTextItem = (NavigationMenuTextItem) navigationMenuItem;
                Objects.requireNonNull(navigationMenuTextItem);
                if (navigationMenuTextItem.menuItem.hasSubMenu()) {
                    return 1;
                }
                return 0;
            }
            throw new RuntimeException("Unknown item type.");
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final void onBindViewHolder(ViewHolder viewHolder, int i) {
            Drawable drawable;
            ViewHolder viewHolder2 = viewHolder;
            int itemViewType = getItemViewType(i);
            boolean z = true;
            if (itemViewType == 0) {
                NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView) viewHolder2.itemView;
                ColorStateList colorStateList = NavigationMenuPresenter.this.iconTintList;
                Objects.requireNonNull(navigationMenuItemView);
                navigationMenuItemView.iconTintList = colorStateList;
                if (colorStateList == null) {
                    z = false;
                }
                navigationMenuItemView.hasIconTintList = z;
                MenuItemImpl menuItemImpl = navigationMenuItemView.itemData;
                if (menuItemImpl != null) {
                    navigationMenuItemView.setIcon(menuItemImpl.getIcon());
                }
                int i2 = NavigationMenuPresenter.this.textAppearance;
                if (i2 != 0) {
                    navigationMenuItemView.textView.setTextAppearance(i2);
                }
                ColorStateList colorStateList2 = NavigationMenuPresenter.this.textColor;
                if (colorStateList2 != null) {
                    navigationMenuItemView.textView.setTextColor(colorStateList2);
                }
                Drawable drawable2 = NavigationMenuPresenter.this.itemBackground;
                if (drawable2 != null) {
                    drawable = drawable2.getConstantState().newDrawable();
                } else {
                    drawable = null;
                }
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                ViewCompat.Api16Impl.setBackground(navigationMenuItemView, drawable);
                NavigationMenuTextItem navigationMenuTextItem = (NavigationMenuTextItem) this.items.get(i);
                navigationMenuItemView.needsEmptyIcon = navigationMenuTextItem.needsEmptyIcon;
                NavigationMenuPresenter navigationMenuPresenter = NavigationMenuPresenter.this;
                int i3 = navigationMenuPresenter.itemHorizontalPadding;
                int i4 = navigationMenuPresenter.itemVerticalPadding;
                navigationMenuItemView.setPadding(i3, i4, i3, i4);
                navigationMenuItemView.textView.setCompoundDrawablePadding(NavigationMenuPresenter.this.itemIconPadding);
                NavigationMenuPresenter navigationMenuPresenter2 = NavigationMenuPresenter.this;
                if (navigationMenuPresenter2.hasCustomItemIconSize) {
                    navigationMenuItemView.iconSize = navigationMenuPresenter2.itemIconSize;
                }
                navigationMenuItemView.textView.setMaxLines(navigationMenuPresenter2.itemMaxLines);
                navigationMenuItemView.initialize(navigationMenuTextItem.menuItem);
            } else if (itemViewType == 1) {
                TextView textView = (TextView) viewHolder2.itemView;
                NavigationMenuTextItem navigationMenuTextItem2 = (NavigationMenuTextItem) this.items.get(i);
                Objects.requireNonNull(navigationMenuTextItem2);
                MenuItemImpl menuItemImpl2 = navigationMenuTextItem2.menuItem;
                Objects.requireNonNull(menuItemImpl2);
                textView.setText(menuItemImpl2.mTitle);
                int i5 = NavigationMenuPresenter.this.subheaderTextAppearance;
                if (i5 != 0) {
                    textView.setTextAppearance(i5);
                }
                int i6 = NavigationMenuPresenter.this.subheaderInsetStart;
                int paddingTop = textView.getPaddingTop();
                Objects.requireNonNull(NavigationMenuPresenter.this);
                textView.setPadding(i6, paddingTop, 0, textView.getPaddingBottom());
                ColorStateList colorStateList3 = NavigationMenuPresenter.this.subheaderColor;
                if (colorStateList3 != null) {
                    textView.setTextColor(colorStateList3);
                }
            } else if (itemViewType == 2) {
                NavigationMenuSeparatorItem navigationMenuSeparatorItem = (NavigationMenuSeparatorItem) this.items.get(i);
                View view = viewHolder2.itemView;
                int i7 = NavigationMenuPresenter.this.dividerInsetStart;
                Objects.requireNonNull(navigationMenuSeparatorItem);
                view.setPadding(i7, navigationMenuSeparatorItem.paddingTop, NavigationMenuPresenter.this.dividerInsetEnd, navigationMenuSeparatorItem.paddingBottom);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
            RecyclerView.ViewHolder viewHolder;
            if (i == 0) {
                NavigationMenuPresenter navigationMenuPresenter = NavigationMenuPresenter.this;
                viewHolder = new NormalViewHolder(navigationMenuPresenter.layoutInflater, recyclerView, navigationMenuPresenter.onClickListener);
            } else if (i == 1) {
                viewHolder = new SubheaderViewHolder(NavigationMenuPresenter.this.layoutInflater, recyclerView);
            } else if (i == 2) {
                viewHolder = new SeparatorViewHolder(NavigationMenuPresenter.this.layoutInflater, recyclerView);
            } else if (i != 3) {
                return null;
            } else {
                return new HeaderViewHolder(NavigationMenuPresenter.this.headerLayout);
            }
            return viewHolder;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final void onViewRecycled(ViewHolder viewHolder) {
            ViewHolder viewHolder2 = viewHolder;
            if (viewHolder2 instanceof NormalViewHolder) {
                NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView) viewHolder2.itemView;
                Objects.requireNonNull(navigationMenuItemView);
                FrameLayout frameLayout = navigationMenuItemView.actionArea;
                if (frameLayout != null) {
                    frameLayout.removeAllViews();
                }
                navigationMenuItemView.textView.setCompoundDrawables(null, null, null, null);
            }
        }

        public final void prepareMenuItems() {
            if (!this.updateSuspended) {
                this.updateSuspended = true;
                this.items.clear();
                this.items.add(new NavigationMenuHeaderItem());
                int i = -1;
                int size = NavigationMenuPresenter.this.menu.getVisibleItems().size();
                boolean z = false;
                int i2 = 0;
                boolean z2 = false;
                int i3 = 0;
                while (i2 < size) {
                    MenuItemImpl menuItemImpl = NavigationMenuPresenter.this.menu.getVisibleItems().get(i2);
                    if (menuItemImpl.isChecked()) {
                        setCheckedItem(menuItemImpl);
                    }
                    if (menuItemImpl.isCheckable()) {
                        menuItemImpl.setExclusiveCheckable(z);
                    }
                    if (menuItemImpl.hasSubMenu()) {
                        SubMenuBuilder subMenuBuilder = menuItemImpl.mSubMenu;
                        if (subMenuBuilder.hasVisibleItems()) {
                            if (i2 != 0) {
                                ArrayList<NavigationMenuItem> arrayList = this.items;
                                int i4 = NavigationMenuPresenter.this.paddingSeparator;
                                int i5 = z ? 1 : 0;
                                int i6 = z ? 1 : 0;
                                int i7 = z ? 1 : 0;
                                int i8 = z ? 1 : 0;
                                arrayList.add(new NavigationMenuSeparatorItem(i4, i5));
                            }
                            this.items.add(new NavigationMenuTextItem(menuItemImpl));
                            int size2 = subMenuBuilder.size();
                            int i9 = z ? 1 : 0;
                            Object[] objArr = z ? 1 : 0;
                            Object[] objArr2 = z ? 1 : 0;
                            Object[] objArr3 = z ? 1 : 0;
                            int i10 = i9;
                            int i11 = i10;
                            while (i10 < size2) {
                                MenuItemImpl menuItemImpl2 = (MenuItemImpl) subMenuBuilder.getItem(i10);
                                if (menuItemImpl2.isVisible()) {
                                    if (i11 == 0 && menuItemImpl2.getIcon() != null) {
                                        i11 = 1;
                                    }
                                    if (menuItemImpl2.isCheckable()) {
                                        menuItemImpl2.setExclusiveCheckable(z);
                                    }
                                    if (menuItemImpl.isChecked()) {
                                        setCheckedItem(menuItemImpl);
                                    }
                                    this.items.add(new NavigationMenuTextItem(menuItemImpl2));
                                }
                                i10++;
                                z = false;
                            }
                            if (i11 != 0) {
                                int size3 = this.items.size();
                                for (int size4 = this.items.size(); size4 < size3; size4++) {
                                    ((NavigationMenuTextItem) this.items.get(size4)).needsEmptyIcon = true;
                                }
                            }
                        }
                    } else {
                        int i12 = menuItemImpl.mGroup;
                        if (i12 != i) {
                            i3 = this.items.size();
                            if (menuItemImpl.getIcon() != null) {
                                z2 = true;
                            } else {
                                z2 = false;
                            }
                            if (i2 != 0) {
                                i3++;
                                ArrayList<NavigationMenuItem> arrayList2 = this.items;
                                int i13 = NavigationMenuPresenter.this.paddingSeparator;
                                arrayList2.add(new NavigationMenuSeparatorItem(i13, i13));
                            }
                        } else if (!z2 && menuItemImpl.getIcon() != null) {
                            int size5 = this.items.size();
                            for (int i14 = i3; i14 < size5; i14++) {
                                ((NavigationMenuTextItem) this.items.get(i14)).needsEmptyIcon = true;
                            }
                            z2 = true;
                        }
                        NavigationMenuTextItem navigationMenuTextItem = new NavigationMenuTextItem(menuItemImpl);
                        navigationMenuTextItem.needsEmptyIcon = z2;
                        this.items.add(navigationMenuTextItem);
                        i = i12;
                    }
                    i2++;
                    z = false;
                }
                this.updateSuspended = z;
            }
        }

        public final void setCheckedItem(MenuItemImpl menuItemImpl) {
            if (this.checkedItem != menuItemImpl && menuItemImpl.isCheckable()) {
                MenuItemImpl menuItemImpl2 = this.checkedItem;
                if (menuItemImpl2 != null) {
                    menuItemImpl2.setChecked(false);
                }
                this.checkedItem = menuItemImpl;
                menuItemImpl.setChecked(true);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class NavigationMenuHeaderItem implements NavigationMenuItem {
    }

    /* loaded from: classes.dex */
    public interface NavigationMenuItem {
    }

    /* loaded from: classes.dex */
    public class NavigationMenuViewAccessibilityDelegate extends RecyclerViewAccessibilityDelegate {
        public NavigationMenuViewAccessibilityDelegate(NavigationMenuView navigationMenuView) {
            super(navigationMenuView);
        }

        @Override // androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate, androidx.core.view.AccessibilityDelegateCompat
        public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            int i;
            int i2;
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            NavigationMenuAdapter navigationMenuAdapter = NavigationMenuPresenter.this.adapter;
            Objects.requireNonNull(navigationMenuAdapter);
            if (NavigationMenuPresenter.this.headerLayout.getChildCount() == 0) {
                i = 0;
                i2 = 0;
            } else {
                i = 1;
                i2 = 0;
            }
            while (i2 < NavigationMenuPresenter.this.adapter.getItemCount()) {
                if (NavigationMenuPresenter.this.adapter.getItemViewType(i2) == 0) {
                    i++;
                }
                i2++;
            }
            accessibilityNodeInfoCompat.mInfo.setCollectionInfo(AccessibilityNodeInfo.CollectionInfo.obtain(i, 0, false));
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

    /* loaded from: classes.dex */
    public static class NavigationMenuSeparatorItem implements NavigationMenuItem {
        public final int paddingBottom;
        public final int paddingTop;

        public NavigationMenuSeparatorItem(int i, int i2) {
            this.paddingTop = i;
            this.paddingBottom = i2;
        }
    }

    /* loaded from: classes.dex */
    public static class NavigationMenuTextItem implements NavigationMenuItem {
        public final MenuItemImpl menuItem;
        public boolean needsEmptyIcon;

        public NavigationMenuTextItem(MenuItemImpl menuItemImpl) {
            this.menuItem = menuItemImpl;
        }
    }

    /* loaded from: classes.dex */
    public static class NormalViewHolder extends ViewHolder {
        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public NormalViewHolder(android.view.LayoutInflater r3, androidx.recyclerview.widget.RecyclerView r4, com.google.android.material.internal.NavigationMenuPresenter.AnonymousClass1 r5) {
            /*
                r2 = this;
                r0 = 2131624074(0x7f0e008a, float:1.8875317E38)
                r1 = 0
                android.view.View r3 = r3.inflate(r0, r4, r1)
                r2.<init>(r3)
                r3.setOnClickListener(r5)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.internal.NavigationMenuPresenter.NormalViewHolder.<init>(android.view.LayoutInflater, androidx.recyclerview.widget.RecyclerView, com.google.android.material.internal.NavigationMenuPresenter$1):void");
        }
    }

    /* loaded from: classes.dex */
    public static class SeparatorViewHolder extends ViewHolder {
        public SeparatorViewHolder(LayoutInflater layoutInflater, RecyclerView recyclerView) {
            super(layoutInflater.inflate(2131624076, (ViewGroup) recyclerView, false));
        }
    }

    /* loaded from: classes.dex */
    public static class SubheaderViewHolder extends ViewHolder {
        public SubheaderViewHolder(LayoutInflater layoutInflater, RecyclerView recyclerView) {
            super(layoutInflater.inflate(2131624077, (ViewGroup) recyclerView, false));
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void onRestoreInstanceState(Parcelable parcelable) {
        View actionView;
        ParcelableSparseArray parcelableSparseArray;
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            SparseArray<Parcelable> sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:list");
            if (sparseParcelableArray != null) {
                this.menuView.restoreHierarchyState(sparseParcelableArray);
            }
            Bundle bundle2 = bundle.getBundle("android:menu:adapter");
            if (bundle2 != null) {
                NavigationMenuAdapter navigationMenuAdapter = this.adapter;
                Objects.requireNonNull(navigationMenuAdapter);
                int i = bundle2.getInt("android:menu:checked", 0);
                if (i != 0) {
                    navigationMenuAdapter.updateSuspended = true;
                    int size = navigationMenuAdapter.items.size();
                    int i2 = 0;
                    while (true) {
                        if (i2 >= size) {
                            break;
                        }
                        NavigationMenuItem navigationMenuItem = navigationMenuAdapter.items.get(i2);
                        if (navigationMenuItem instanceof NavigationMenuTextItem) {
                            NavigationMenuTextItem navigationMenuTextItem = (NavigationMenuTextItem) navigationMenuItem;
                            Objects.requireNonNull(navigationMenuTextItem);
                            MenuItemImpl menuItemImpl = navigationMenuTextItem.menuItem;
                            if (menuItemImpl != null && menuItemImpl.mId == i) {
                                navigationMenuAdapter.setCheckedItem(menuItemImpl);
                                break;
                            }
                        }
                        i2++;
                    }
                    navigationMenuAdapter.updateSuspended = false;
                    navigationMenuAdapter.prepareMenuItems();
                }
                SparseArray sparseParcelableArray2 = bundle2.getSparseParcelableArray("android:menu:action_views");
                if (sparseParcelableArray2 != null) {
                    int size2 = navigationMenuAdapter.items.size();
                    for (int i3 = 0; i3 < size2; i3++) {
                        NavigationMenuItem navigationMenuItem2 = navigationMenuAdapter.items.get(i3);
                        if (navigationMenuItem2 instanceof NavigationMenuTextItem) {
                            NavigationMenuTextItem navigationMenuTextItem2 = (NavigationMenuTextItem) navigationMenuItem2;
                            Objects.requireNonNull(navigationMenuTextItem2);
                            MenuItemImpl menuItemImpl2 = navigationMenuTextItem2.menuItem;
                            if (!(menuItemImpl2 == null || (actionView = menuItemImpl2.getActionView()) == null || (parcelableSparseArray = (ParcelableSparseArray) sparseParcelableArray2.get(menuItemImpl2.mId)) == null)) {
                                actionView.restoreHierarchyState(parcelableSparseArray);
                            }
                        }
                    }
                }
            }
            SparseArray<Parcelable> sparseParcelableArray3 = bundle.getSparseParcelableArray("android:menu:header");
            if (sparseParcelableArray3 != null) {
                this.headerLayout.restoreHierarchyState(sparseParcelableArray3);
            }
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final Parcelable onSaveInstanceState() {
        View view;
        Bundle bundle = new Bundle();
        if (this.menuView != null) {
            SparseArray<Parcelable> sparseArray = new SparseArray<>();
            this.menuView.saveHierarchyState(sparseArray);
            bundle.putSparseParcelableArray("android:menu:list", sparseArray);
        }
        NavigationMenuAdapter navigationMenuAdapter = this.adapter;
        if (navigationMenuAdapter != null) {
            Objects.requireNonNull(navigationMenuAdapter);
            Bundle bundle2 = new Bundle();
            MenuItemImpl menuItemImpl = navigationMenuAdapter.checkedItem;
            if (menuItemImpl != null) {
                bundle2.putInt("android:menu:checked", menuItemImpl.mId);
            }
            SparseArray<? extends Parcelable> sparseArray2 = new SparseArray<>();
            int size = navigationMenuAdapter.items.size();
            for (int i = 0; i < size; i++) {
                NavigationMenuItem navigationMenuItem = navigationMenuAdapter.items.get(i);
                if (navigationMenuItem instanceof NavigationMenuTextItem) {
                    NavigationMenuTextItem navigationMenuTextItem = (NavigationMenuTextItem) navigationMenuItem;
                    Objects.requireNonNull(navigationMenuTextItem);
                    MenuItemImpl menuItemImpl2 = navigationMenuTextItem.menuItem;
                    if (menuItemImpl2 != null) {
                        view = menuItemImpl2.getActionView();
                    } else {
                        view = null;
                    }
                    if (view != null) {
                        ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
                        view.saveHierarchyState(parcelableSparseArray);
                        Objects.requireNonNull(menuItemImpl2);
                        sparseArray2.put(menuItemImpl2.mId, parcelableSparseArray);
                    }
                }
            }
            bundle2.putSparseParcelableArray("android:menu:action_views", sparseArray2);
            bundle.putBundle("android:menu:adapter", bundle2);
        }
        if (this.headerLayout != null) {
            SparseArray<Parcelable> sparseArray3 = new SparseArray<>();
            this.headerLayout.saveHierarchyState(sparseArray3);
            bundle.putSparseParcelableArray("android:menu:header", sparseArray3);
        }
        return bundle;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void updateMenuView(boolean z) {
        NavigationMenuAdapter navigationMenuAdapter = this.adapter;
        if (navigationMenuAdapter != null) {
            Objects.requireNonNull(navigationMenuAdapter);
            navigationMenuAdapter.prepareMenuItems();
            navigationMenuAdapter.notifyDataSetChanged();
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final void initForMenu(Context context, MenuBuilder menuBuilder) {
        this.layoutInflater = LayoutInflater.from(context);
        this.menu = menuBuilder;
        this.paddingSeparator = context.getResources().getDimensionPixelOffset(2131165638);
    }

    /* loaded from: classes.dex */
    public static class HeaderViewHolder extends ViewHolder {
        public HeaderViewHolder(LinearLayout linearLayout) {
            super(linearLayout);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public final int getId() {
        return this.id;
    }
}
