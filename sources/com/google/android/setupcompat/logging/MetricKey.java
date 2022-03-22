package com.google.android.setupcompat.logging;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.preference.R$drawable;
import com.android.systemui.R$array;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public final class MetricKey implements Parcelable {
    public static final Parcelable.Creator<MetricKey> CREATOR = new Parcelable.Creator<MetricKey>() { // from class: com.google.android.setupcompat.logging.MetricKey.1
        @Override // android.os.Parcelable.Creator
        public final MetricKey createFromParcel(Parcel parcel) {
            return new MetricKey(parcel.readString(), parcel.readString());
        }

        @Override // android.os.Parcelable.Creator
        public final MetricKey[] newArray(int i) {
            return new MetricKey[i];
        }
    };
    public static final Pattern METRIC_KEY_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]+");
    public final String name;
    public final String screenName;

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        boolean z;
        boolean z2;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MetricKey)) {
            return false;
        }
        MetricKey metricKey = (MetricKey) obj;
        String str = this.name;
        String str2 = metricKey.name;
        if (str == str2 || (str != null && str.equals(str2))) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            String str3 = this.screenName;
            String str4 = metricKey.screenName;
            if (str3 == str4 || (str3 != null && str3.equals(str4))) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.name, this.screenName});
    }

    static {
        Pattern.compile("^([a-z]+[.])+[A-Z][a-zA-Z0-9]+");
        Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]+");
    }

    public static Bundle fromMetricKey(MetricKey metricKey) {
        Objects.requireNonNull(metricKey, "MetricKey cannot be null.");
        Bundle bundle = new Bundle();
        bundle.putInt("MetricKey_version", 1);
        bundle.putString("MetricKey_name", metricKey.name);
        bundle.putString("MetricKey_screenName", metricKey.screenName);
        return bundle;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.screenName);
    }

    public MetricKey(String str, String str2) {
        this.name = str;
        this.screenName = str2;
    }

    public static MetricKey get(String str, Activity activity) {
        String className = activity.getComponentName().getClassName();
        R$drawable.assertLengthInRange(str, "MetricKey.name", 5, 30);
        R$array.checkArgument(METRIC_KEY_PATTERN.matcher(str).matches(), "Invalid MetricKey, only alpha numeric characters are allowed.");
        return new MetricKey(str, className);
    }
}
