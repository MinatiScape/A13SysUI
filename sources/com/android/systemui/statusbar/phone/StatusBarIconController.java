package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArraySet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.Dependency;
import com.android.systemui.demomode.DemoModeCommandReceiver;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.StatusBarMobileView;
import com.android.systemui.statusbar.StatusBarWifiView;
import com.android.systemui.statusbar.StatusIconDisplayable;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public interface StatusBarIconController {

    /* loaded from: classes.dex */
    public static class DarkIconManager extends IconManager {
        public int mIconHPadding = this.mContext.getResources().getDimensionPixelSize(2131167065);
        public final DarkIconDispatcher mDarkIconDispatcher = (DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class);

        @Override // com.android.systemui.statusbar.phone.StatusBarIconController.IconManager
        public final void destroy() {
            for (int i = 0; i < this.mGroup.getChildCount(); i++) {
                this.mDarkIconDispatcher.removeDarkReceiver((DarkIconDispatcher.DarkReceiver) this.mGroup.getChildAt(i));
            }
            this.mGroup.removeAllViews();
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarIconController.IconManager
        public final void exitDemoMode() {
            this.mDarkIconDispatcher.removeDarkReceiver(this.mDemoStatusIcons);
            super.exitDemoMode();
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarIconController.IconManager
        public final LinearLayout.LayoutParams onCreateLayoutParams() {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, this.mIconSize);
            int i = this.mIconHPadding;
            layoutParams.setMargins(i, 0, i, 0);
            return layoutParams;
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarIconController.IconManager
        public final void onRemoveIcon(int i) {
            this.mDarkIconDispatcher.removeDarkReceiver((DarkIconDispatcher.DarkReceiver) this.mGroup.getChildAt(i));
            super.onRemoveIcon(i);
        }

        public DarkIconManager(LinearLayout linearLayout, FeatureFlags featureFlags) {
            super(linearLayout, featureFlags);
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarIconController.IconManager
        public final DemoStatusIcons createDemoStatusIcons() {
            DemoStatusIcons createDemoStatusIcons = super.createDemoStatusIcons();
            this.mDarkIconDispatcher.addDarkReceiver(createDemoStatusIcons);
            return createDemoStatusIcons;
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarIconController.IconManager
        public final void onIconAdded(int i, String str, boolean z, StatusBarIconHolder statusBarIconHolder) {
            this.mDarkIconDispatcher.addDarkReceiver(addHolder(i, str, z, statusBarIconHolder));
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarIconController.IconManager
        public final void onSetIcon(int i, StatusBarIcon statusBarIcon) {
            super.onSetIcon(i, statusBarIcon);
            this.mDarkIconDispatcher.applyDark((DarkIconDispatcher.DarkReceiver) this.mGroup.getChildAt(i));
        }
    }

    /* loaded from: classes.dex */
    public static class IconManager implements DemoModeCommandReceiver {
        public final Context mContext;
        public DemoStatusIcons mDemoStatusIcons;
        public final FeatureFlags mFeatureFlags;
        public final ViewGroup mGroup;
        public final int mIconSize;
        public boolean mIsInDemoMode;
        public boolean mShouldLog = false;
        public ArrayList<String> mBlockList = new ArrayList<>();

        @Override // com.android.systemui.demomode.DemoModeCommandReceiver
        public final void onDemoModeStarted() {
            this.mIsInDemoMode = true;
            if (this.mDemoStatusIcons == null) {
                this.mDemoStatusIcons = createDemoStatusIcons();
            }
            this.mDemoStatusIcons.onDemoModeStarted();
        }

        public final StatusIconDisplayable addHolder(int i, String str, boolean z, StatusBarIconHolder statusBarIconHolder) {
            if (this.mBlockList.contains(str)) {
                z = true;
            }
            Objects.requireNonNull(statusBarIconHolder);
            int i2 = statusBarIconHolder.mType;
            if (i2 == 0) {
                return addIcon(i, str, z, statusBarIconHolder.mIcon);
            }
            if (i2 == 1) {
                return addSignalIcon(i, str, statusBarIconHolder.mWifiState);
            }
            if (i2 != 2) {
                return null;
            }
            return addMobileIcon(i, str, statusBarIconHolder.mMobileState);
        }

        public StatusBarIconView addIcon(int i, String str, boolean z, StatusBarIcon statusBarIcon) {
            StatusBarIconView statusBarIconView = new StatusBarIconView(this.mContext, str, null, z);
            statusBarIconView.set(statusBarIcon);
            this.mGroup.addView(statusBarIconView, i, onCreateLayoutParams());
            return statusBarIconView;
        }

        public StatusBarMobileView addMobileIcon(int i, String str, StatusBarSignalPolicy.MobileIconState mobileIconState) {
            StatusBarMobileView fromContext = StatusBarMobileView.fromContext(this.mContext, str, this.mFeatureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS));
            fromContext.applyMobileState(mobileIconState);
            this.mGroup.addView(fromContext, i, onCreateLayoutParams());
            if (this.mIsInDemoMode) {
                this.mDemoStatusIcons.addMobileView(mobileIconState);
            }
            return fromContext;
        }

        public StatusBarWifiView addSignalIcon(int i, String str, StatusBarSignalPolicy.WifiIconState wifiIconState) {
            StatusBarWifiView fromContext = StatusBarWifiView.fromContext(this.mContext, str);
            fromContext.applyWifiState(wifiIconState);
            this.mGroup.addView(fromContext, i, onCreateLayoutParams());
            if (this.mIsInDemoMode) {
                this.mDemoStatusIcons.addDemoWifiView(wifiIconState);
            }
            return fromContext;
        }

        public DemoStatusIcons createDemoStatusIcons() {
            return new DemoStatusIcons((LinearLayout) this.mGroup, this.mIconSize, this.mFeatureFlags);
        }

        public void destroy() {
            this.mGroup.removeAllViews();
        }

        @Override // com.android.systemui.demomode.DemoModeCommandReceiver
        public final void dispatchDemoCommand(String str, Bundle bundle) {
            this.mDemoStatusIcons.dispatchDemoCommand(str, bundle);
        }

        public void exitDemoMode() {
            DemoStatusIcons demoStatusIcons = this.mDemoStatusIcons;
            Objects.requireNonNull(demoStatusIcons);
            demoStatusIcons.mMobileViews.clear();
            ((ViewGroup) demoStatusIcons.getParent()).removeView(demoStatusIcons);
            this.mDemoStatusIcons = null;
        }

        public LinearLayout.LayoutParams onCreateLayoutParams() {
            return new LinearLayout.LayoutParams(-2, this.mIconSize);
        }

        @Override // com.android.systemui.demomode.DemoModeCommandReceiver
        public final void onDemoModeFinished() {
            DemoStatusIcons demoStatusIcons = this.mDemoStatusIcons;
            if (demoStatusIcons != null) {
                demoStatusIcons.onDemoModeFinished();
                exitDemoMode();
                this.mIsInDemoMode = false;
            }
        }

        public void onRemoveIcon(int i) {
            if (this.mIsInDemoMode) {
                DemoStatusIcons demoStatusIcons = this.mDemoStatusIcons;
                StatusIconDisplayable statusIconDisplayable = (StatusIconDisplayable) this.mGroup.getChildAt(i);
                Objects.requireNonNull(demoStatusIcons);
                StatusBarMobileView statusBarMobileView = null;
                if (statusIconDisplayable.getSlot().equals("wifi")) {
                    demoStatusIcons.removeView(demoStatusIcons.mWifiView);
                    demoStatusIcons.mWifiView = null;
                } else {
                    if (statusIconDisplayable instanceof StatusBarMobileView) {
                        StatusBarMobileView statusBarMobileView2 = (StatusBarMobileView) statusIconDisplayable;
                        Iterator<StatusBarMobileView> it = demoStatusIcons.mMobileViews.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            StatusBarMobileView next = it.next();
                            if (next.getState().subId == statusBarMobileView2.getState().subId) {
                                statusBarMobileView = next;
                                break;
                            }
                        }
                    }
                    if (statusBarMobileView != null) {
                        demoStatusIcons.removeView(statusBarMobileView);
                        demoStatusIcons.mMobileViews.remove(statusBarMobileView);
                    }
                }
            }
            this.mGroup.removeViewAt(i);
        }

        public void onSetIcon(int i, StatusBarIcon statusBarIcon) {
            ((StatusBarIconView) this.mGroup.getChildAt(i)).set(statusBarIcon);
        }

        public IconManager(ViewGroup viewGroup, FeatureFlags featureFlags) {
            this.mFeatureFlags = featureFlags;
            this.mGroup = viewGroup;
            Context context = viewGroup.getContext();
            this.mContext = context;
            this.mIconSize = context.getResources().getDimensionPixelSize(17105554);
        }

        public void onIconAdded(int i, String str, boolean z, StatusBarIconHolder statusBarIconHolder) {
            addHolder(i, str, z, statusBarIconHolder);
        }
    }

    /* loaded from: classes.dex */
    public static class TintedIconManager extends IconManager {
        public int mColor;

        /* loaded from: classes.dex */
        public static class Factory {
            public final FeatureFlags mFeatureFlags;

            public Factory(FeatureFlags featureFlags) {
                this.mFeatureFlags = featureFlags;
            }
        }

        public final void setTint(int i) {
            this.mColor = i;
            for (int i2 = 0; i2 < this.mGroup.getChildCount(); i2++) {
                View childAt = this.mGroup.getChildAt(i2);
                if (childAt instanceof StatusIconDisplayable) {
                    StatusIconDisplayable statusIconDisplayable = (StatusIconDisplayable) childAt;
                    statusIconDisplayable.setStaticDrawableColor(this.mColor);
                    statusIconDisplayable.setDecorColor(this.mColor);
                }
            }
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarIconController.IconManager
        public final DemoStatusIcons createDemoStatusIcons() {
            DemoStatusIcons createDemoStatusIcons = super.createDemoStatusIcons();
            createDemoStatusIcons.setColor(this.mColor);
            return createDemoStatusIcons;
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarIconController.IconManager
        public final void onIconAdded(int i, String str, boolean z, StatusBarIconHolder statusBarIconHolder) {
            StatusIconDisplayable addHolder = addHolder(i, str, z, statusBarIconHolder);
            addHolder.setStaticDrawableColor(this.mColor);
            addHolder.setDecorColor(this.mColor);
        }

        public TintedIconManager(ViewGroup viewGroup, FeatureFlags featureFlags) {
            super(viewGroup, featureFlags);
        }
    }

    void addIconGroup(IconManager iconManager);

    void removeAllIconsForSlot(String str);

    void removeIcon(String str, int i);

    void removeIconGroup(IconManager iconManager);

    void setCallStrengthIcons(String str, ArrayList arrayList);

    void setExternalIcon(String str);

    void setIcon(String str, int i, String str2);

    void setIcon(String str, StatusBarIcon statusBarIcon);

    void setIconAccessibilityLiveRegion(String str, int i);

    void setIconVisibility(String str, boolean z);

    void setMobileIcons(String str, ArrayList arrayList);

    void setNoCallingIcons(String str, ArrayList arrayList);

    void setSignalIcon(String str, StatusBarSignalPolicy.WifiIconState wifiIconState);

    static ArraySet<String> getIconHideList(Context context, String str) {
        String[] strArr;
        ArraySet<String> arraySet = new ArraySet<>();
        if (str == null) {
            strArr = context.getResources().getStringArray(2130903103);
        } else {
            strArr = str.split(",");
        }
        for (String str2 : strArr) {
            if (!TextUtils.isEmpty(str2)) {
                arraySet.add(str2);
            }
        }
        return arraySet;
    }
}
