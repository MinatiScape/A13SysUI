package androidx.appcompat.view.menu;

import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;
import androidx.collection.SimpleArrayMap;
import androidx.core.internal.view.SupportMenuItem;
import androidx.core.internal.view.SupportSubMenu;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class BaseMenuWrapper {
    public final Context mContext;
    public SimpleArrayMap<SupportMenuItem, MenuItem> mMenuItems;
    public SimpleArrayMap<SupportSubMenu, SubMenu> mSubMenus;

    public final MenuItem getMenuItemWrapper(MenuItem menuItem) {
        if (!(menuItem instanceof SupportMenuItem)) {
            return menuItem;
        }
        SupportMenuItem supportMenuItem = (SupportMenuItem) menuItem;
        if (this.mMenuItems == null) {
            this.mMenuItems = new SimpleArrayMap<>();
        }
        SimpleArrayMap<SupportMenuItem, MenuItem> simpleArrayMap = this.mMenuItems;
        Objects.requireNonNull(simpleArrayMap);
        MenuItem orDefault = simpleArrayMap.getOrDefault(menuItem, null);
        if (orDefault != null) {
            return orDefault;
        }
        MenuItemWrapperICS menuItemWrapperICS = new MenuItemWrapperICS(this.mContext, supportMenuItem);
        this.mMenuItems.put(supportMenuItem, menuItemWrapperICS);
        return menuItemWrapperICS;
    }

    public final SubMenu getSubMenuWrapper(SubMenu subMenu) {
        if (!(subMenu instanceof SupportSubMenu)) {
            return subMenu;
        }
        SupportSubMenu supportSubMenu = (SupportSubMenu) subMenu;
        if (this.mSubMenus == null) {
            this.mSubMenus = new SimpleArrayMap<>();
        }
        SimpleArrayMap<SupportSubMenu, SubMenu> simpleArrayMap = this.mSubMenus;
        Objects.requireNonNull(simpleArrayMap);
        SubMenu orDefault = simpleArrayMap.getOrDefault(supportSubMenu, null);
        if (orDefault != null) {
            return orDefault;
        }
        SubMenuWrapperICS subMenuWrapperICS = new SubMenuWrapperICS(this.mContext, supportSubMenu);
        this.mSubMenus.put(supportSubMenu, subMenuWrapperICS);
        return subMenuWrapperICS;
    }

    public BaseMenuWrapper(Context context) {
        this.mContext = context;
    }
}
