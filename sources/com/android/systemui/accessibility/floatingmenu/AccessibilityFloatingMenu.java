package com.android.systemui.accessibility.floatingmenu;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.accessibility.dialog.AccessibilityTargetHelper;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AccessibilityFloatingMenu implements IAccessibilityFloatingMenu {
    public final AnonymousClass1 mContentObserver;
    public final Context mContext;
    public final DockTooltipView mDockTooltipView;
    public final AnonymousClass4 mEnabledA11yServicesContentObserver;
    public final AnonymousClass3 mFadeOutContentObserver;
    public final AccessibilityFloatingMenuView mMenuView;
    public final MigrationTooltipView mMigrationTooltipView;
    public final AnonymousClass2 mSizeContentObserver;

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu$2] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu$3] */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu$4] */
    public AccessibilityFloatingMenu(Context context) {
        Position position;
        Handler handler = new Handler(Looper.getMainLooper());
        this.mContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.1
            @Override // android.database.ContentObserver
            public final void onChange(boolean z) {
                AccessibilityFloatingMenu accessibilityFloatingMenu = AccessibilityFloatingMenu.this;
                accessibilityFloatingMenu.mMenuView.onTargetsChanged(AccessibilityTargetHelper.getTargets(accessibilityFloatingMenu.mContext, 0));
            }
        };
        this.mSizeContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.2
            @Override // android.database.ContentObserver
            public final void onChange(boolean z) {
                AccessibilityFloatingMenu accessibilityFloatingMenu = AccessibilityFloatingMenu.this;
                accessibilityFloatingMenu.mMenuView.setSizeType(Settings.Secure.getInt(accessibilityFloatingMenu.mContext.getContentResolver(), "accessibility_floating_menu_size", 0));
            }
        };
        this.mFadeOutContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.3
            @Override // android.database.ContentObserver
            public final void onChange(boolean z) {
                AccessibilityFloatingMenu accessibilityFloatingMenu = AccessibilityFloatingMenu.this;
                AccessibilityFloatingMenuView accessibilityFloatingMenuView = accessibilityFloatingMenu.mMenuView;
                boolean z2 = true;
                if (Settings.Secure.getInt(accessibilityFloatingMenu.mContext.getContentResolver(), "accessibility_floating_menu_fade_enabled", 1) != 1) {
                    z2 = false;
                }
                accessibilityFloatingMenuView.updateOpacityWith(z2, Settings.Secure.getFloat(AccessibilityFloatingMenu.this.mContext.getContentResolver(), "accessibility_floating_menu_opacity", 0.55f));
            }
        };
        this.mEnabledA11yServicesContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.4
            @Override // android.database.ContentObserver
            public final void onChange(boolean z) {
                AccessibilityFloatingMenuView accessibilityFloatingMenuView = AccessibilityFloatingMenu.this.mMenuView;
                Objects.requireNonNull(accessibilityFloatingMenuView);
                accessibilityFloatingMenuView.mAdapter.notifyDataSetChanged();
            }
        };
        this.mContext = context;
        String string = context.getSharedPreferences(context.getPackageName(), 0).getString("AccessibilityFloatingMenuPosition", null);
        if (TextUtils.isEmpty(string)) {
            position = new Position(1.0f, 0.9f);
        } else {
            TextUtils.SimpleStringSplitter simpleStringSplitter = Position.sStringCommaSplitter;
            simpleStringSplitter.setString(string);
            if (simpleStringSplitter.hasNext()) {
                position = new Position(Float.parseFloat(simpleStringSplitter.next()), Float.parseFloat(simpleStringSplitter.next()));
            } else {
                throw new IllegalArgumentException(SupportMenuInflater$$ExternalSyntheticOutline0.m("Invalid Position string: ", string));
            }
        }
        AccessibilityFloatingMenuView accessibilityFloatingMenuView = new AccessibilityFloatingMenuView(context, position, new RecyclerView(context));
        this.mMenuView = accessibilityFloatingMenuView;
        this.mMigrationTooltipView = new MigrationTooltipView(context, accessibilityFloatingMenuView);
        this.mDockTooltipView = new DockTooltipView(context, accessibilityFloatingMenuView);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu$2] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu$3] */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu$4] */
    @VisibleForTesting
    public AccessibilityFloatingMenu(Context context, AccessibilityFloatingMenuView accessibilityFloatingMenuView) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.1
            @Override // android.database.ContentObserver
            public final void onChange(boolean z) {
                AccessibilityFloatingMenu accessibilityFloatingMenu = AccessibilityFloatingMenu.this;
                accessibilityFloatingMenu.mMenuView.onTargetsChanged(AccessibilityTargetHelper.getTargets(accessibilityFloatingMenu.mContext, 0));
            }
        };
        this.mSizeContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.2
            @Override // android.database.ContentObserver
            public final void onChange(boolean z) {
                AccessibilityFloatingMenu accessibilityFloatingMenu = AccessibilityFloatingMenu.this;
                accessibilityFloatingMenu.mMenuView.setSizeType(Settings.Secure.getInt(accessibilityFloatingMenu.mContext.getContentResolver(), "accessibility_floating_menu_size", 0));
            }
        };
        this.mFadeOutContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.3
            @Override // android.database.ContentObserver
            public final void onChange(boolean z) {
                AccessibilityFloatingMenu accessibilityFloatingMenu = AccessibilityFloatingMenu.this;
                AccessibilityFloatingMenuView accessibilityFloatingMenuView2 = accessibilityFloatingMenu.mMenuView;
                boolean z2 = true;
                if (Settings.Secure.getInt(accessibilityFloatingMenu.mContext.getContentResolver(), "accessibility_floating_menu_fade_enabled", 1) != 1) {
                    z2 = false;
                }
                accessibilityFloatingMenuView2.updateOpacityWith(z2, Settings.Secure.getFloat(AccessibilityFloatingMenu.this.mContext.getContentResolver(), "accessibility_floating_menu_opacity", 0.55f));
            }
        };
        this.mEnabledA11yServicesContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.4
            @Override // android.database.ContentObserver
            public final void onChange(boolean z) {
                AccessibilityFloatingMenuView accessibilityFloatingMenuView2 = AccessibilityFloatingMenu.this.mMenuView;
                Objects.requireNonNull(accessibilityFloatingMenuView2);
                accessibilityFloatingMenuView2.mAdapter.notifyDataSetChanged();
            }
        };
        this.mContext = context;
        this.mMenuView = accessibilityFloatingMenuView;
        this.mMigrationTooltipView = new MigrationTooltipView(context, accessibilityFloatingMenuView);
        this.mDockTooltipView = new DockTooltipView(context, accessibilityFloatingMenuView);
    }
}
