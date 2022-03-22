package com.google.android.material.internal;

import android.content.Context;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.SubMenuBuilder;
/* loaded from: classes.dex */
public final class NavigationSubMenu extends SubMenuBuilder {
    @Override // androidx.appcompat.view.menu.MenuBuilder
    public final void onItemsChanged(boolean z) {
        super.onItemsChanged(z);
        this.mParentMenu.onItemsChanged(z);
    }

    public NavigationSubMenu(Context context, NavigationMenu navigationMenu, MenuItemImpl menuItemImpl) {
        super(context, navigationMenu, menuItemImpl);
    }
}
