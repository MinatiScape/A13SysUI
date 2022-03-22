package com.android.wm.shell.util;

import android.app.ActivityManager;
import android.app.WindowConfiguration;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class GroupedRecentTaskInfo implements Parcelable {
    public static final Parcelable.Creator<GroupedRecentTaskInfo> CREATOR = new Parcelable.Creator<GroupedRecentTaskInfo>() { // from class: com.android.wm.shell.util.GroupedRecentTaskInfo.1
        @Override // android.os.Parcelable.Creator
        public final GroupedRecentTaskInfo createFromParcel(Parcel parcel) {
            return new GroupedRecentTaskInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final GroupedRecentTaskInfo[] newArray(int i) {
            return new GroupedRecentTaskInfo[i];
        }
    };
    public StagedSplitBounds mStagedSplitBounds;
    public ActivityManager.RecentTaskInfo mTaskInfo1;
    public ActivityManager.RecentTaskInfo mTaskInfo2;

    public GroupedRecentTaskInfo(ActivityManager.RecentTaskInfo recentTaskInfo, ActivityManager.RecentTaskInfo recentTaskInfo2, StagedSplitBounds stagedSplitBounds) {
        this.mTaskInfo1 = recentTaskInfo;
        this.mTaskInfo2 = recentTaskInfo2;
        this.mStagedSplitBounds = stagedSplitBounds;
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public static String getTaskInfo(ActivityManager.RecentTaskInfo recentTaskInfo) {
        Object obj;
        if (recentTaskInfo == null) {
            return null;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("id=");
        m.append(recentTaskInfo.taskId);
        m.append(" baseIntent=");
        Intent intent = recentTaskInfo.baseIntent;
        if (intent != null) {
            obj = intent.getComponent();
        } else {
            obj = "null";
        }
        m.append(obj);
        m.append(" winMode=");
        m.append(WindowConfiguration.windowingModeToString(recentTaskInfo.getWindowingMode()));
        return m.toString();
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Task1: ");
        m.append(getTaskInfo(this.mTaskInfo1));
        m.append(", Task2: ");
        m.append(getTaskInfo(this.mTaskInfo2));
        String sb = m.toString();
        if (this.mStagedSplitBounds == null) {
            return sb;
        }
        StringBuilder m2 = DebugInfo$$ExternalSyntheticOutline0.m(sb, ", SplitBounds: ");
        m2.append(this.mStagedSplitBounds.toString());
        return m2.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedObject(this.mTaskInfo1, i);
        parcel.writeTypedObject(this.mTaskInfo2, i);
        parcel.writeTypedObject(this.mStagedSplitBounds, i);
    }

    public GroupedRecentTaskInfo(Parcel parcel) {
        this.mTaskInfo1 = (ActivityManager.RecentTaskInfo) parcel.readTypedObject(ActivityManager.RecentTaskInfo.CREATOR);
        this.mTaskInfo2 = (ActivityManager.RecentTaskInfo) parcel.readTypedObject(ActivityManager.RecentTaskInfo.CREATOR);
        this.mStagedSplitBounds = (StagedSplitBounds) parcel.readTypedObject(StagedSplitBounds.CREATOR);
    }
}
