package com.android.systemui.accessibility.floatingmenu;

import android.content.Context;
import android.graphics.Rect;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.StatsEvent;
import android.util.StatsLog;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import com.android.internal.accessibility.dialog.AccessibilityTargetHelper;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Prefs;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class AccessibilityFloatingMenuController implements AccessibilityButtonModeObserver.ModeChangedListener, AccessibilityButtonTargetsObserver.TargetsChangedListener {
    public final AccessibilityButtonModeObserver mAccessibilityButtonModeObserver;
    public final AccessibilityButtonTargetsObserver mAccessibilityButtonTargetsObserver;
    public int mBtnMode;
    public String mBtnTargets;
    public Context mContext;
    @VisibleForTesting
    public IAccessibilityFloatingMenu mFloatingMenu;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    @VisibleForTesting
    public final KeyguardUpdateMonitorCallback mKeyguardCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardVisibilityChanged(boolean z) {
            AccessibilityFloatingMenuController accessibilityFloatingMenuController = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController.mIsKeyguardVisible = z;
            if (accessibilityFloatingMenuController.mIsAccessibilityManagerServiceReady) {
                accessibilityFloatingMenuController.handleFloatingMenuVisibility(z, accessibilityFloatingMenuController.mBtnMode, accessibilityFloatingMenuController.mBtnTargets);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onUserSwitchComplete(int i) {
            AccessibilityFloatingMenuController accessibilityFloatingMenuController = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController.mContext = accessibilityFloatingMenuController.mContext.createContextAsUser(UserHandle.of(i), 0);
            AccessibilityFloatingMenuController accessibilityFloatingMenuController2 = AccessibilityFloatingMenuController.this;
            AccessibilityButtonModeObserver accessibilityButtonModeObserver = accessibilityFloatingMenuController2.mAccessibilityButtonModeObserver;
            Objects.requireNonNull(accessibilityButtonModeObserver);
            accessibilityFloatingMenuController2.mBtnMode = AccessibilityButtonModeObserver.parseAccessibilityButtonMode(Settings.Secure.getStringForUser(accessibilityButtonModeObserver.mContentResolver, accessibilityButtonModeObserver.mKey, -2));
            AccessibilityFloatingMenuController accessibilityFloatingMenuController3 = AccessibilityFloatingMenuController.this;
            AccessibilityButtonTargetsObserver accessibilityButtonTargetsObserver = accessibilityFloatingMenuController3.mAccessibilityButtonTargetsObserver;
            Objects.requireNonNull(accessibilityButtonTargetsObserver);
            accessibilityFloatingMenuController3.mBtnTargets = Settings.Secure.getStringForUser(accessibilityButtonTargetsObserver.mContentResolver, accessibilityButtonTargetsObserver.mKey, -2);
            AccessibilityFloatingMenuController accessibilityFloatingMenuController4 = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController4.handleFloatingMenuVisibility(accessibilityFloatingMenuController4.mIsKeyguardVisible, accessibilityFloatingMenuController4.mBtnMode, accessibilityFloatingMenuController4.mBtnTargets);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onUserSwitching(int i) {
            AccessibilityFloatingMenuController.this.destroyFloatingMenu();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onUserUnlocked() {
            AccessibilityFloatingMenuController accessibilityFloatingMenuController = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController.mIsAccessibilityManagerServiceReady = true;
            accessibilityFloatingMenuController.handleFloatingMenuVisibility(accessibilityFloatingMenuController.mIsKeyguardVisible, accessibilityFloatingMenuController.mBtnMode, accessibilityFloatingMenuController.mBtnTargets);
        }
    };
    public boolean mIsKeyguardVisible = false;
    public boolean mIsAccessibilityManagerServiceReady = false;

    public final void destroyFloatingMenu() {
        IAccessibilityFloatingMenu iAccessibilityFloatingMenu = this.mFloatingMenu;
        if (iAccessibilityFloatingMenu != null) {
            AccessibilityFloatingMenu accessibilityFloatingMenu = (AccessibilityFloatingMenu) iAccessibilityFloatingMenu;
            AccessibilityFloatingMenuView accessibilityFloatingMenuView = accessibilityFloatingMenu.mMenuView;
            Objects.requireNonNull(accessibilityFloatingMenuView);
            if (accessibilityFloatingMenuView.mIsShowing) {
                AccessibilityFloatingMenuView accessibilityFloatingMenuView2 = accessibilityFloatingMenu.mMenuView;
                Objects.requireNonNull(accessibilityFloatingMenuView2);
                if (accessibilityFloatingMenuView2.mIsShowing) {
                    accessibilityFloatingMenuView2.mIsShowing = false;
                    accessibilityFloatingMenuView2.mWindowManager.removeView(accessibilityFloatingMenuView2);
                    accessibilityFloatingMenuView2.setOnApplyWindowInsetsListener(null);
                    accessibilityFloatingMenuView2.setSystemGestureExclusion();
                }
                AccessibilityFloatingMenuView accessibilityFloatingMenuView3 = accessibilityFloatingMenu.mMenuView;
                Objects.requireNonNull(accessibilityFloatingMenuView3);
                accessibilityFloatingMenuView3.mOnDragEndListener = Optional.ofNullable(null);
                accessibilityFloatingMenu.mMigrationTooltipView.hide();
                accessibilityFloatingMenu.mDockTooltipView.hide();
                accessibilityFloatingMenu.mContext.getContentResolver().unregisterContentObserver(accessibilityFloatingMenu.mContentObserver);
                accessibilityFloatingMenu.mContext.getContentResolver().unregisterContentObserver(accessibilityFloatingMenu.mSizeContentObserver);
                accessibilityFloatingMenu.mContext.getContentResolver().unregisterContentObserver(accessibilityFloatingMenu.mFadeOutContentObserver);
                accessibilityFloatingMenu.mContext.getContentResolver().unregisterContentObserver(accessibilityFloatingMenu.mEnabledA11yServicesContentObserver);
            }
            this.mFloatingMenu = null;
        }
    }

    public final void handleFloatingMenuVisibility(boolean z, int i, String str) {
        boolean z2;
        boolean z3;
        boolean z4;
        if (z) {
            destroyFloatingMenu();
            return;
        }
        if (i != 1 || TextUtils.isEmpty(str)) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z2) {
            if (this.mFloatingMenu == null) {
                this.mFloatingMenu = new AccessibilityFloatingMenu(this.mContext);
            }
            final AccessibilityFloatingMenu accessibilityFloatingMenu = (AccessibilityFloatingMenu) this.mFloatingMenu;
            Objects.requireNonNull(accessibilityFloatingMenu);
            AccessibilityFloatingMenuView accessibilityFloatingMenuView = accessibilityFloatingMenu.mMenuView;
            Objects.requireNonNull(accessibilityFloatingMenuView);
            if (!accessibilityFloatingMenuView.mIsShowing) {
                final AccessibilityFloatingMenuView accessibilityFloatingMenuView2 = accessibilityFloatingMenu.mMenuView;
                Objects.requireNonNull(accessibilityFloatingMenuView2);
                if (!accessibilityFloatingMenuView2.mIsShowing) {
                    accessibilityFloatingMenuView2.mIsShowing = true;
                    accessibilityFloatingMenuView2.mWindowManager.addView(accessibilityFloatingMenuView2, accessibilityFloatingMenuView2.mCurrentLayoutParams);
                    accessibilityFloatingMenuView2.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda2
                        @Override // android.view.View.OnApplyWindowInsetsListener
                        public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                            boolean z5;
                            AccessibilityFloatingMenuView accessibilityFloatingMenuView3 = AccessibilityFloatingMenuView.this;
                            Objects.requireNonNull(accessibilityFloatingMenuView3);
                            WindowMetrics currentWindowMetrics = accessibilityFloatingMenuView3.mWindowManager.getCurrentWindowMetrics();
                            if (!currentWindowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout()).toRect().equals(accessibilityFloatingMenuView3.mDisplayInsetsRect)) {
                                accessibilityFloatingMenuView3.updateDisplaySizeWith(currentWindowMetrics);
                                accessibilityFloatingMenuView3.updateLocationWith(accessibilityFloatingMenuView3.mPosition);
                            }
                            Rect rect = currentWindowMetrics.getWindowInsets().getInsets(WindowInsets.Type.ime()).toRect();
                            if (!rect.equals(accessibilityFloatingMenuView3.mImeInsetsRect)) {
                                if (rect.left == 0 && rect.top == 0 && rect.right == 0 && rect.bottom == 0) {
                                    z5 = false;
                                } else {
                                    z5 = true;
                                }
                                if (z5) {
                                    accessibilityFloatingMenuView3.mImeInsetsRect.set(rect);
                                } else {
                                    accessibilityFloatingMenuView3.mImeInsetsRect.setEmpty();
                                }
                                accessibilityFloatingMenuView3.updateLocationWith(accessibilityFloatingMenuView3.mPosition);
                            }
                            return windowInsets;
                        }
                    });
                    accessibilityFloatingMenuView2.setSystemGestureExclusion();
                }
                accessibilityFloatingMenu.mMenuView.onTargetsChanged(AccessibilityTargetHelper.getTargets(accessibilityFloatingMenu.mContext, 0));
                AccessibilityFloatingMenuView accessibilityFloatingMenuView3 = accessibilityFloatingMenu.mMenuView;
                if (Settings.Secure.getInt(accessibilityFloatingMenu.mContext.getContentResolver(), "accessibility_floating_menu_fade_enabled", 1) == 1) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                accessibilityFloatingMenuView3.updateOpacityWith(z3, Settings.Secure.getFloat(accessibilityFloatingMenu.mContext.getContentResolver(), "accessibility_floating_menu_opacity", 0.55f));
                accessibilityFloatingMenu.mMenuView.setSizeType(Settings.Secure.getInt(accessibilityFloatingMenu.mContext.getContentResolver(), "accessibility_floating_menu_size", 0));
                accessibilityFloatingMenu.mMenuView.setShapeType(Settings.Secure.getInt(accessibilityFloatingMenu.mContext.getContentResolver(), "accessibility_floating_menu_icon_type", 0));
                AccessibilityFloatingMenuView accessibilityFloatingMenuView4 = accessibilityFloatingMenu.mMenuView;
                AccessibilityFloatingMenuView.OnDragEndListener accessibilityFloatingMenu$$ExternalSyntheticLambda0 = new AccessibilityFloatingMenuView.OnDragEndListener() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu$$ExternalSyntheticLambda0
                    @Override // com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView.OnDragEndListener
                    public final void onDragEnd(Position position) {
                        AccessibilityFloatingMenu accessibilityFloatingMenu2 = AccessibilityFloatingMenu.this;
                        Objects.requireNonNull(accessibilityFloatingMenu2);
                        Objects.requireNonNull(position);
                        float f = position.mPercentageX;
                        float f2 = position.mPercentageY;
                        int i2 = accessibilityFloatingMenu2.mContext.getResources().getConfiguration().orientation;
                        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
                        newBuilder.setAtomId(393);
                        newBuilder.writeFloat(f);
                        newBuilder.writeFloat(f2);
                        newBuilder.writeInt(i2);
                        newBuilder.usePooledBuffer();
                        StatsLog.write(newBuilder.build());
                        Context context = accessibilityFloatingMenu2.mContext;
                        boolean z5 = false;
                        context.getSharedPreferences(context.getPackageName(), 0).edit().putString("AccessibilityFloatingMenuPosition", position.toString()).apply();
                        Context context2 = accessibilityFloatingMenu2.mContext;
                        if (!context2.getSharedPreferences(context2.getPackageName(), 0).getBoolean("HasSeenAccessibilityFloatingMenuDockTooltip", false)) {
                            AccessibilityFloatingMenuView accessibilityFloatingMenuView5 = accessibilityFloatingMenu2.mMenuView;
                            Objects.requireNonNull(accessibilityFloatingMenuView5);
                            if (accessibilityFloatingMenuView5.mShapeType == 0) {
                                z5 = true;
                            }
                            if (z5) {
                                accessibilityFloatingMenu2.mDockTooltipView.show();
                            }
                            Prefs.putBoolean(context2, "HasSeenAccessibilityFloatingMenuDockTooltip", true);
                        }
                    }
                };
                Objects.requireNonNull(accessibilityFloatingMenuView4);
                accessibilityFloatingMenuView4.mOnDragEndListener = Optional.ofNullable(accessibilityFloatingMenu$$ExternalSyntheticLambda0);
                if (Settings.Secure.getInt(accessibilityFloatingMenu.mContext.getContentResolver(), "accessibility_floating_menu_migration_tooltip_prompt", 0) == 1) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                if (z4) {
                    MigrationTooltipView migrationTooltipView = accessibilityFloatingMenu.mMigrationTooltipView;
                    Objects.requireNonNull(migrationTooltipView);
                    if (!migrationTooltipView.mIsShowing) {
                        migrationTooltipView.mIsShowing = true;
                        migrationTooltipView.updateTooltipView();
                        migrationTooltipView.mWindowManager.addView(migrationTooltipView, migrationTooltipView.mCurrentLayoutParams);
                    }
                    Settings.Secure.putInt(accessibilityFloatingMenu.mContext.getContentResolver(), "accessibility_floating_menu_migration_tooltip_prompt", 0);
                }
                accessibilityFloatingMenu.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_button_targets"), false, accessibilityFloatingMenu.mContentObserver, -2);
                accessibilityFloatingMenu.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_floating_menu_size"), false, accessibilityFloatingMenu.mSizeContentObserver, -2);
                accessibilityFloatingMenu.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_floating_menu_fade_enabled"), false, accessibilityFloatingMenu.mFadeOutContentObserver, -2);
                accessibilityFloatingMenu.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_floating_menu_opacity"), false, accessibilityFloatingMenu.mFadeOutContentObserver, -2);
                accessibilityFloatingMenu.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("enabled_accessibility_services"), false, accessibilityFloatingMenu.mEnabledA11yServicesContentObserver, -2);
                return;
            }
            return;
        }
        destroyFloatingMenu();
    }

    @Override // com.android.systemui.accessibility.AccessibilityButtonModeObserver.ModeChangedListener
    public final void onAccessibilityButtonModeChanged(int i) {
        this.mBtnMode = i;
        handleFloatingMenuVisibility(this.mIsKeyguardVisible, i, this.mBtnTargets);
    }

    @Override // com.android.systemui.accessibility.AccessibilityButtonTargetsObserver.TargetsChangedListener
    public final void onAccessibilityButtonTargetsChanged(String str) {
        this.mBtnTargets = str;
        handleFloatingMenuVisibility(this.mIsKeyguardVisible, this.mBtnMode, str);
    }

    public AccessibilityFloatingMenuController(Context context, AccessibilityButtonTargetsObserver accessibilityButtonTargetsObserver, AccessibilityButtonModeObserver accessibilityButtonModeObserver, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mContext = context;
        this.mAccessibilityButtonTargetsObserver = accessibilityButtonTargetsObserver;
        this.mAccessibilityButtonModeObserver = accessibilityButtonModeObserver;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
    }
}
