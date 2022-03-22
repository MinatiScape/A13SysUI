package com.google.android.material.internal;

import android.content.Context;
import android.view.SubMenu;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
/* loaded from: classes.dex */
public final class NavigationMenu extends MenuBuilder {
    @Override // androidx.appcompat.view.menu.MenuBuilder, android.view.Menu
    public final SubMenu addSubMenu(int i, int i2, int i3, CharSequence charSequence) {
        MenuItemImpl addInternal = addInternal(i, i2, i3, charSequence);
        NavigationSubMenu navigationSubMenu = new NavigationSubMenu(this.mContext, this, addInternal);
        addInternal.mSubMenu = navigationSubMenu;
        navigationSubMenu.setHeaderTitle(addInternal.mTitle);
        return navigationSubMenu;
    }

    public NavigationMenu(Context context) {
        super(context);
    }
}
