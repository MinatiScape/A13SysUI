package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public final class StatusBarTouchableRegionManager implements Dumpable {
    public final Context mContext;
    public int mDisplayCutoutTouchableRegionSize;
    public final HeadsUpManagerPhone mHeadsUpManager;
    public View mNotificationPanelView;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public View mNotificationShadeWindowView;
    public StatusBar mStatusBar;
    public int mStatusBarHeight;
    public boolean mIsStatusBarExpanded = false;
    public boolean mShouldAdjustInsets = false;
    public boolean mForceCollapsedUntilLayout = false;
    public Region mTouchableRegion = new Region();
    public final AnonymousClass5 mOnComputeInternalInsetsListener = new ViewTreeObserver.OnComputeInternalInsetsListener() { // from class: com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager.5
        public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
            StatusBarTouchableRegionManager statusBarTouchableRegionManager = StatusBarTouchableRegionManager.this;
            if (!statusBarTouchableRegionManager.mIsStatusBarExpanded) {
                StatusBar statusBar = statusBarTouchableRegionManager.mStatusBar;
                Objects.requireNonNull(statusBar);
                if (!statusBar.mBouncerShowing) {
                    internalInsetsInfo.setTouchableInsets(3);
                    internalInsetsInfo.touchableRegion.set(StatusBarTouchableRegionManager.this.calculateTouchableRegion());
                }
            }
        }
    };

    public final Region calculateTouchableRegion() {
        Region region;
        boolean z;
        NotificationEntry groupSummary;
        HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
        Objects.requireNonNull(headsUpManagerPhone);
        NotificationEntry topEntry = headsUpManagerPhone.getTopEntry();
        if (!headsUpManagerPhone.mHasPinnedNotification || topEntry == null) {
            region = null;
        } else {
            ExpandableNotificationRow expandableNotificationRow = topEntry.row;
            if (expandableNotificationRow == null || !expandableNotificationRow.isChildInGroup()) {
                z = false;
            } else {
                z = true;
            }
            if (z && (groupSummary = headsUpManagerPhone.mGroupMembershipManager.getGroupSummary(topEntry)) != null) {
                topEntry = groupSummary;
            }
            ExpandableNotificationRow expandableNotificationRow2 = topEntry.row;
            int[] iArr = new int[2];
            expandableNotificationRow2.getLocationOnScreen(iArr);
            headsUpManagerPhone.mTouchableRegion.set(iArr[0], 0, expandableNotificationRow2.getWidth() + iArr[0], headsUpManagerPhone.mHeadsUpInset + expandableNotificationRow2.getIntrinsicHeight());
            region = headsUpManagerPhone.mTouchableRegion;
        }
        if (region != null) {
            this.mTouchableRegion.set(region);
        } else {
            this.mTouchableRegion.set(0, 0, this.mNotificationShadeWindowView.getWidth(), this.mStatusBarHeight);
            updateRegionForNotch(this.mTouchableRegion);
        }
        return this.mTouchableRegion;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("StatusBarTouchableRegionManager state:");
        printWriter.print("  mTouchableRegion=");
        printWriter.println(this.mTouchableRegion);
    }

    public final void initResources() {
        this.mDisplayCutoutTouchableRegionSize = this.mContext.getResources().getDimensionPixelSize(17105201);
        this.mStatusBarHeight = SystemBarUtils.getStatusBarHeight(this.mContext);
    }

    public final void updateRegionForNotch(Region region) {
        WindowInsets rootWindowInsets = this.mNotificationShadeWindowView.getRootWindowInsets();
        if (rootWindowInsets == null) {
            Log.w("TouchableRegionManager", "StatusBarWindowView is not attached.");
            return;
        }
        DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
        if (displayCutout != null) {
            Rect rect = new Rect();
            ScreenDecorations.DisplayCutoutView.boundsFromDirection(displayCutout, 48, rect);
            rect.offset(0, this.mDisplayCutoutTouchableRegionSize);
            region.union(rect);
        }
    }

    public final void updateTouchableRegion() {
        boolean z;
        View view = this.mNotificationShadeWindowView;
        boolean z2 = true;
        if (view == null || view.getRootWindowInsets() == null || this.mNotificationShadeWindowView.getRootWindowInsets().getDisplayCutout() == null) {
            z = false;
        } else {
            z = true;
        }
        HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
        Objects.requireNonNull(headsUpManagerPhone);
        if (!headsUpManagerPhone.mHasPinnedNotification) {
            HeadsUpManagerPhone headsUpManagerPhone2 = this.mHeadsUpManager;
            Objects.requireNonNull(headsUpManagerPhone2);
            if (!headsUpManagerPhone2.mHeadsUpGoingAway && !this.mForceCollapsedUntilLayout && !z && !this.mNotificationShadeWindowController.getForcePluginOpen()) {
                z2 = false;
            }
        }
        if (z2 != this.mShouldAdjustInsets) {
            if (z2) {
                this.mNotificationShadeWindowView.getViewTreeObserver().addOnComputeInternalInsetsListener(this.mOnComputeInternalInsetsListener);
                this.mNotificationShadeWindowView.requestLayout();
            } else {
                this.mNotificationShadeWindowView.getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mOnComputeInternalInsetsListener);
            }
            this.mShouldAdjustInsets = z2;
        }
    }

    public StatusBarTouchableRegionManager(Context context, NotificationShadeWindowController notificationShadeWindowController, ConfigurationController configurationController, HeadsUpManagerPhone headsUpManagerPhone) {
        this.mContext = context;
        initResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager.1
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public final void onDensityOrFontScaleChanged() {
                StatusBarTouchableRegionManager.this.initResources();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public final void onThemeChanged() {
                StatusBarTouchableRegionManager.this.initResources();
            }
        });
        this.mHeadsUpManager = headsUpManagerPhone;
        headsUpManagerPhone.addListener(new OnHeadsUpChangedListener() { // from class: com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager.2
            @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
            public final void onHeadsUpPinnedModeChanged(boolean z) {
                if (Log.isLoggable("TouchableRegionManager", 5)) {
                    Log.w("TouchableRegionManager", "onHeadsUpPinnedModeChanged");
                }
                StatusBarTouchableRegionManager.this.updateTouchableRegion();
            }
        });
        headsUpManagerPhone.mHeadsUpPhoneListeners.add(new HeadsUpManagerPhone.OnHeadsUpPhoneListenerChange() { // from class: com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager.3
            @Override // com.android.systemui.statusbar.phone.HeadsUpManagerPhone.OnHeadsUpPhoneListenerChange
            public final void onHeadsUpGoingAwayStateChanged(boolean z) {
                if (!z) {
                    final StatusBarTouchableRegionManager statusBarTouchableRegionManager = StatusBarTouchableRegionManager.this;
                    Objects.requireNonNull(statusBarTouchableRegionManager);
                    View view = statusBarTouchableRegionManager.mNotificationPanelView;
                    if (view != null) {
                        statusBarTouchableRegionManager.mForceCollapsedUntilLayout = true;
                        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager.4
                            @Override // android.view.View.OnLayoutChangeListener
                            public final void onLayoutChange(View view2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                                if (!StatusBarTouchableRegionManager.this.mNotificationPanelView.isVisibleToUser()) {
                                    StatusBarTouchableRegionManager.this.mNotificationPanelView.removeOnLayoutChangeListener(this);
                                    StatusBarTouchableRegionManager statusBarTouchableRegionManager2 = StatusBarTouchableRegionManager.this;
                                    statusBarTouchableRegionManager2.mForceCollapsedUntilLayout = false;
                                    statusBarTouchableRegionManager2.updateTouchableRegion();
                                }
                            }
                        });
                        return;
                    }
                    return;
                }
                StatusBarTouchableRegionManager.this.updateTouchableRegion();
            }
        });
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        notificationShadeWindowController.setForcePluginOpenListener(new StatusBarTouchableRegionManager$$ExternalSyntheticLambda0(this));
    }
}
