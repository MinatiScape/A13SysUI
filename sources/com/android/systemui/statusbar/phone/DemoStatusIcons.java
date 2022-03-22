package com.android.systemui.statusbar.phone;

import android.graphics.Rect;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.demomode.DemoMode;
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
import java.util.List;
/* loaded from: classes.dex */
public final class DemoStatusIcons extends StatusIconContainer implements DemoMode, DarkIconDispatcher.DarkReceiver {
    public boolean mDemoMode;
    public final FeatureFlags mFeatureFlags;
    public final int mIconSize;
    public final LinearLayout mStatusIcons;
    public StatusBarWifiView mWifiView;
    public final ArrayList<StatusBarMobileView> mMobileViews = new ArrayList<>();
    public int mColor = -1;

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public final void onDemoModeFinished() {
        this.mDemoMode = false;
        this.mStatusIcons.setVisibility(0);
        setVisibility(8);
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public final void onDemoModeStarted() {
        this.mDemoMode = true;
        this.mStatusIcons.setVisibility(8);
        setVisibility(0);
    }

    public final void addDemoWifiView(StatusBarSignalPolicy.WifiIconState wifiIconState) {
        Log.d("DemoStatusIcons", "addDemoWifiView: ");
        StatusBarWifiView fromContext = StatusBarWifiView.fromContext(((LinearLayout) this).mContext, wifiIconState.slot);
        int childCount = getChildCount();
        int i = 0;
        while (true) {
            if (i >= getChildCount()) {
                break;
            } else if (getChildAt(i) instanceof StatusBarMobileView) {
                childCount = i;
                break;
            } else {
                i++;
            }
        }
        this.mWifiView = fromContext;
        fromContext.applyWifiState(wifiIconState);
        this.mWifiView.setStaticDrawableColor(this.mColor);
        addView(fromContext, childCount, new LinearLayout.LayoutParams(-2, this.mIconSize));
    }

    public final void addMobileView(StatusBarSignalPolicy.MobileIconState mobileIconState) {
        Log.d("DemoStatusIcons", "addMobileView: ");
        StatusBarMobileView fromContext = StatusBarMobileView.fromContext(((LinearLayout) this).mContext, mobileIconState.slot, this.mFeatureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS));
        fromContext.applyMobileState(mobileIconState);
        fromContext.setStaticDrawableColor(this.mColor);
        this.mMobileViews.add(fromContext);
        addView(fromContext, getChildCount(), new LinearLayout.LayoutParams(-2, this.mIconSize));
    }

    @Override // com.android.systemui.demomode.DemoMode
    public final List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("status");
        return arrayList;
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
    public final void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        setColor(DarkIconDispatcher.getTint(arrayList, this.mStatusIcons, i));
        StatusBarWifiView statusBarWifiView = this.mWifiView;
        if (statusBarWifiView != null) {
            statusBarWifiView.onDarkChanged(arrayList, f, i);
        }
        Iterator<StatusBarMobileView> it = this.mMobileViews.iterator();
        while (it.hasNext()) {
            it.next().onDarkChanged(arrayList, f, i);
        }
    }

    public final void setColor(int i) {
        this.mColor = i;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            StatusIconDisplayable statusIconDisplayable = (StatusIconDisplayable) getChildAt(i2);
            statusIconDisplayable.setStaticDrawableColor(this.mColor);
            statusIconDisplayable.setDecorColor(this.mColor);
        }
    }

    public final void updateSlot(String str, int i) {
        if (this.mDemoMode) {
            String packageName = ((LinearLayout) this).mContext.getPackageName();
            int i2 = 0;
            while (true) {
                if (i2 >= getChildCount()) {
                    i2 = -1;
                    break;
                }
                View childAt = getChildAt(i2);
                if (childAt instanceof StatusBarIconView) {
                    StatusBarIconView statusBarIconView = (StatusBarIconView) childAt;
                    if (str.equals(statusBarIconView.getTag())) {
                        if (i != 0) {
                            StatusBarIcon statusBarIcon = statusBarIconView.mIcon;
                            statusBarIcon.visible = true;
                            statusBarIcon.icon = Icon.createWithResource(statusBarIcon.icon.getResPackage(), i);
                            statusBarIconView.set(statusBarIcon);
                            statusBarIconView.updateDrawable(true);
                            return;
                        }
                    }
                }
                i2++;
            }
            if (i != 0) {
                StatusBarIcon statusBarIcon2 = new StatusBarIcon(packageName, UserHandle.SYSTEM, i, 0, 0, "Demo");
                statusBarIcon2.visible = true;
                StatusBarIconView statusBarIconView2 = new StatusBarIconView(getContext(), str, null, false);
                statusBarIconView2.setTag(str);
                statusBarIconView2.set(statusBarIcon2);
                statusBarIconView2.setStaticDrawableColor(this.mColor);
                statusBarIconView2.setDecorColor(this.mColor);
                addView(statusBarIconView2, 0, new LinearLayout.LayoutParams(-2, this.mIconSize));
            } else if (i2 != -1) {
                removeViewAt(i2);
            }
        }
    }

    public DemoStatusIcons(LinearLayout linearLayout, int i, FeatureFlags featureFlags) {
        super(linearLayout.getContext());
        this.mStatusIcons = linearLayout;
        this.mIconSize = i;
        this.mFeatureFlags = featureFlags;
        if (linearLayout instanceof StatusIconContainer) {
            this.mShouldRestrictIcons = ((StatusIconContainer) linearLayout).mShouldRestrictIcons;
        } else {
            this.mShouldRestrictIcons = false;
        }
        setLayoutParams(linearLayout.getLayoutParams());
        setPadding(linearLayout.getPaddingLeft(), linearLayout.getPaddingTop(), linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());
        setOrientation(linearLayout.getOrientation());
        setGravity(16);
        ViewGroup viewGroup = (ViewGroup) linearLayout.getParent();
        viewGroup.addView(this, viewGroup.indexOfChild(linearLayout));
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public final void dispatchDemoCommand(String str, Bundle bundle) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        String string = bundle.getString("volume");
        int i10 = 0;
        if (string != null) {
            if (string.equals("vibrate")) {
                i9 = 2131232686;
            } else {
                i9 = 0;
            }
            updateSlot("volume", i9);
        }
        String string2 = bundle.getString("zen");
        if (string2 != null) {
            if (string2.equals("dnd")) {
                i8 = 2131232678;
            } else {
                i8 = 0;
            }
            updateSlot("zen", i8);
        }
        String string3 = bundle.getString("bluetooth");
        if (string3 != null) {
            if (string3.equals("connected")) {
                i7 = 2131232676;
            } else {
                i7 = 0;
            }
            updateSlot("bluetooth", i7);
        }
        String string4 = bundle.getString("location");
        if (string4 != null) {
            if (string4.equals("show")) {
                i6 = PhoneStatusBarPolicy.LOCATION_STATUS_ICON_ID;
            } else {
                i6 = 0;
            }
            updateSlot("location", i6);
        }
        String string5 = bundle.getString("alarm");
        if (string5 != null) {
            if (string5.equals("show")) {
                i5 = 2131232672;
            } else {
                i5 = 0;
            }
            updateSlot("alarm_clock", i5);
        }
        String string6 = bundle.getString("tty");
        if (string6 != null) {
            if (string6.equals("show")) {
                i4 = 2131232696;
            } else {
                i4 = 0;
            }
            updateSlot("tty", i4);
        }
        String string7 = bundle.getString("mute");
        if (string7 != null) {
            if (string7.equals("show")) {
                i3 = 17301622;
            } else {
                i3 = 0;
            }
            updateSlot("mute", i3);
        }
        String string8 = bundle.getString("speakerphone");
        if (string8 != null) {
            if (string8.equals("show")) {
                i2 = 17301639;
            } else {
                i2 = 0;
            }
            updateSlot("speakerphone", i2);
        }
        String string9 = bundle.getString("cast");
        if (string9 != null) {
            if (string9.equals("show")) {
                i = 2131232675;
            } else {
                i = 0;
            }
            updateSlot("cast", i);
        }
        String string10 = bundle.getString("hotspot");
        if (string10 != null) {
            if (string10.equals("show")) {
                i10 = 2131232683;
            }
            updateSlot("hotspot", i10);
        }
    }
}
