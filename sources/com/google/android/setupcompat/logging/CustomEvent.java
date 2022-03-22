package com.google.android.setupcompat.logging;

import android.annotation.TargetApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import androidx.preference.R$drawable;
import com.android.systemui.R$array;
import com.google.android.setupcompat.internal.ClockProvider;
import com.google.android.setupcompat.internal.PersistableBundles;
import com.google.android.setupcompat.internal.Ticker;
import com.google.android.setupcompat.util.Logger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
@TargetApi(29)
/* loaded from: classes.dex */
public final class CustomEvent implements Parcelable {
    public static final Parcelable.Creator<CustomEvent> CREATOR = new Parcelable.Creator<CustomEvent>() { // from class: com.google.android.setupcompat.logging.CustomEvent.1
        @Override // android.os.Parcelable.Creator
        public final CustomEvent createFromParcel(Parcel parcel) {
            return new CustomEvent(parcel.readLong(), (MetricKey) parcel.readParcelable(MetricKey.class.getClassLoader()), parcel.readPersistableBundle(MetricKey.class.getClassLoader()), parcel.readPersistableBundle(MetricKey.class.getClassLoader()));
        }

        @Override // android.os.Parcelable.Creator
        public final CustomEvent[] newArray(int i) {
            return new CustomEvent[i];
        }
    };
    public static final int MAX_STR_LENGTH = 50;
    public static final int MIN_BUNDLE_KEY_LENGTH = 3;
    public final MetricKey metricKey;
    public final PersistableBundle persistableBundle;
    public final PersistableBundle piiValues;
    public final long timestampMillis;

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        boolean z;
        boolean z2;
        boolean z3;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CustomEvent)) {
            return false;
        }
        CustomEvent customEvent = (CustomEvent) obj;
        if (this.timestampMillis == customEvent.timestampMillis) {
            MetricKey metricKey = this.metricKey;
            MetricKey metricKey2 = customEvent.metricKey;
            if (metricKey == metricKey2 || (metricKey != null && metricKey.equals(metricKey2))) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                PersistableBundle persistableBundle = this.persistableBundle;
                PersistableBundle persistableBundle2 = customEvent.persistableBundle;
                Logger logger = PersistableBundles.LOG;
                if (persistableBundle == persistableBundle2 || PersistableBundles.toMap(persistableBundle).equals(PersistableBundles.toMap(persistableBundle2))) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2) {
                    PersistableBundle persistableBundle3 = this.piiValues;
                    PersistableBundle persistableBundle4 = customEvent.piiValues;
                    if (persistableBundle3 == persistableBundle4 || PersistableBundles.toMap(persistableBundle3).equals(PersistableBundles.toMap(persistableBundle4))) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    if (z3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.timestampMillis), this.metricKey, this.persistableBundle, this.piiValues});
    }

    public static CustomEvent create(MetricKey metricKey, PersistableBundle persistableBundle) {
        PersistableBundle persistableBundle2 = PersistableBundle.EMPTY;
        Ticker ticker = ClockProvider.ticker;
        long millis = TimeUnit.NANOSECONDS.toMillis(ClockProvider.ticker.read());
        PersistableBundles.assertIsValid(persistableBundle);
        PersistableBundles.assertIsValid(persistableBundle2);
        return new CustomEvent(millis, metricKey, persistableBundle, persistableBundle2);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.timestampMillis);
        parcel.writeParcelable(this.metricKey, i);
        parcel.writePersistableBundle(this.persistableBundle);
        parcel.writePersistableBundle(this.piiValues);
    }

    public CustomEvent(long j, MetricKey metricKey, PersistableBundle persistableBundle, PersistableBundle persistableBundle2) {
        boolean z;
        boolean z2;
        if (j >= 0) {
            z = true;
        } else {
            z = false;
        }
        R$array.checkArgument(z, "Timestamp cannot be negative.");
        Objects.requireNonNull(metricKey, "MetricKey cannot be null.");
        Objects.requireNonNull(persistableBundle, "Bundle cannot be null.");
        R$array.checkArgument(!persistableBundle.isEmpty(), "Bundle cannot be empty.");
        Objects.requireNonNull(persistableBundle2, "piiValues cannot be null.");
        for (String str : persistableBundle.keySet()) {
            R$drawable.assertLengthInRange(str, "bundle key", 3, 50);
            Object obj = persistableBundle.get(str);
            if (obj instanceof String) {
                if (((String) obj).length() <= 50) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                R$array.checkArgument(z2, String.format("Maximum length of string value for key='%s' cannot exceed %s.", str, 50));
            }
        }
        this.timestampMillis = j;
        this.metricKey = metricKey;
        this.persistableBundle = new PersistableBundle(persistableBundle);
        this.piiValues = new PersistableBundle(persistableBundle2);
    }
}
