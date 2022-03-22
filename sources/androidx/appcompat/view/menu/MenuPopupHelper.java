package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class MenuPopupHelper {
    public View mAnchorView;
    public final Context mContext;
    public int mDropDownGravity;
    public boolean mForceShowIcon;
    public final AnonymousClass1 mInternalOnDismissListener;
    public final MenuBuilder mMenu;
    public PopupWindow.OnDismissListener mOnDismissListener;
    public final boolean mOverflowOnly;
    public MenuPopup mPopup;
    public final int mPopupStyleAttr;
    public final int mPopupStyleRes;
    public MenuPresenter.Callback mPresenterCallback;

    public MenuPopupHelper(Context context, MenuBuilder menuBuilder, View view, boolean z) {
        this(context, menuBuilder, view, z, 2130968609, 0);
    }

    public void onDismiss() {
        this.mPopup = null;
        PopupWindow.OnDismissListener onDismissListener = this.mOnDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [androidx.appcompat.view.menu.MenuPopupHelper$1] */
    public MenuPopupHelper(Context context, MenuBuilder menuBuilder, View view, boolean z, int i, int i2) {
        this.mDropDownGravity = 8388611;
        this.mInternalOnDismissListener = new PopupWindow.OnDismissListener() { // from class: androidx.appcompat.view.menu.MenuPopupHelper.1
            @Override // android.widget.PopupWindow.OnDismissListener
            public final void onDismiss() {
                MenuPopupHelper.this.onDismiss();
            }
        };
        this.mContext = context;
        this.mMenu = menuBuilder;
        this.mAnchorView = view;
        this.mOverflowOnly = z;
        this.mPopupStyleAttr = i;
        this.mPopupStyleRes = i2;
    }

    public final MenuPopup getPopup() {
        boolean z;
        MenuPopup menuPopup;
        if (this.mPopup == null) {
            Display defaultDisplay = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getRealSize(point);
            if (Math.min(point.x, point.y) >= this.mContext.getResources().getDimensionPixelSize(2131165208)) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                menuPopup = new CascadingMenuPopup(this.mContext, this.mAnchorView, this.mPopupStyleAttr, this.mPopupStyleRes, this.mOverflowOnly);
            } else {
                menuPopup = new StandardMenuPopup(this.mContext, this.mMenu, this.mAnchorView, this.mPopupStyleAttr, this.mPopupStyleRes, this.mOverflowOnly);
            }
            menuPopup.addMenu(this.mMenu);
            menuPopup.setOnDismissListener(this.mInternalOnDismissListener);
            menuPopup.setAnchorView(this.mAnchorView);
            menuPopup.setCallback(this.mPresenterCallback);
            menuPopup.setForceShowIcon(this.mForceShowIcon);
            menuPopup.setGravity(this.mDropDownGravity);
            this.mPopup = menuPopup;
        }
        return this.mPopup;
    }

    public final boolean isShowing() {
        MenuPopup menuPopup = this.mPopup;
        if (menuPopup == null || !menuPopup.isShowing()) {
            return false;
        }
        return true;
    }

    public final void showPopup(int i, int i2, boolean z, boolean z2) {
        MenuPopup popup = getPopup();
        popup.setShowTitle(z2);
        if (z) {
            int i3 = this.mDropDownGravity;
            View view = this.mAnchorView;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            if ((Gravity.getAbsoluteGravity(i3, ViewCompat.Api17Impl.getLayoutDirection(view)) & 7) == 5) {
                i -= this.mAnchorView.getWidth();
            }
            popup.setHorizontalOffset(i);
            popup.setVerticalOffset(i2);
            int i4 = (int) ((this.mContext.getResources().getDisplayMetrics().density * 48.0f) / 2.0f);
            popup.mEpicenterBounds = new Rect(i - i4, i2 - i4, i + i4, i2 + i4);
        }
        popup.show();
    }
}
