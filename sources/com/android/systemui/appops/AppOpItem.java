package com.android.systemui.appops;
/* loaded from: classes.dex */
public final class AppOpItem {
    public int mCode;
    public boolean mIsDisabled;
    public String mPackageName;
    public StringBuilder mState;
    public long mTimeStartedElapsed;
    public int mUid;

    public final String toString() {
        StringBuilder sb = this.mState;
        sb.append(this.mIsDisabled);
        sb.append(")");
        return sb.toString();
    }

    public AppOpItem(int i, int i2, String str, long j) {
        this.mCode = i;
        this.mUid = i2;
        this.mPackageName = str;
        this.mTimeStartedElapsed = j;
        StringBuilder sb = new StringBuilder();
        sb.append("AppOpItem(");
        sb.append("Op code=");
        sb.append(i);
        sb.append(", ");
        sb.append("UID=");
        sb.append(i2);
        sb.append(", ");
        sb.append("Package name=");
        sb.append(str);
        sb.append(", ");
        sb.append("Paused=");
        this.mState = sb;
    }
}
