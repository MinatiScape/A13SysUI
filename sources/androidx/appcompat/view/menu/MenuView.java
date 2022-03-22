package androidx.appcompat.view.menu;
/* loaded from: classes.dex */
public interface MenuView {

    /* loaded from: classes.dex */
    public interface ItemView {
        MenuItemImpl getItemData();

        void initialize(MenuItemImpl menuItemImpl);
    }

    void initialize(MenuBuilder menuBuilder);
}
