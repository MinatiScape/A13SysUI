package androidx.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.IndentingPrintWriter;
import com.google.android.setupcompat.internal.PersistableBundles;
import com.google.android.setupcompat.internal.SetupCompatServiceInvoker;
import com.google.android.setupcompat.logging.CustomEvent;
import com.google.android.setupcompat.logging.MetricKey;
import com.google.android.setupcompat.util.Logger;
import java.io.PrintWriter;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public final class R$id {
    public static final IndentingPrintWriter asIndenting(PrintWriter printWriter) {
        IndentingPrintWriter indentingPrintWriter;
        if (printWriter instanceof IndentingPrintWriter) {
            indentingPrintWriter = (IndentingPrintWriter) printWriter;
        } else {
            indentingPrintWriter = null;
        }
        if (indentingPrintWriter == null) {
            return new IndentingPrintWriter(printWriter);
        }
        return indentingPrintWriter;
    }

    public static void logCustomEvent(Context context, CustomEvent customEvent) {
        Objects.requireNonNull(context, "Context cannot be null.");
        SetupCompatServiceInvoker setupCompatServiceInvoker = SetupCompatServiceInvoker.get(context);
        Bundle bundle = new Bundle();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("CustomEvent_version", 1);
        bundle2.putLong("CustomEvent_timestamp", customEvent.timestampMillis);
        bundle2.putBundle("CustomEvent_metricKey", MetricKey.fromMetricKey(customEvent.metricKey));
        PersistableBundle persistableBundle = new PersistableBundle(customEvent.persistableBundle);
        Logger logger = PersistableBundles.LOG;
        Bundle bundle3 = new Bundle();
        bundle3.putAll(persistableBundle);
        bundle2.putBundle("CustomEvent_bundleValues", bundle3);
        PersistableBundle persistableBundle2 = customEvent.piiValues;
        Bundle bundle4 = new Bundle();
        bundle4.putAll(persistableBundle2);
        bundle2.putBundle("CustomEvent_pii_bundleValues", bundle4);
        bundle.putParcelable("CustomEvent_bundle", bundle2);
        setupCompatServiceInvoker.logMetricEvent(1, bundle);
    }

    public static final String visibilityString(int i) {
        if (i == 0) {
            return "visible";
        }
        if (i == 4) {
            return "invisible";
        }
        if (i != 8) {
            return Intrinsics.stringPlus("unknown:", Integer.valueOf(i));
        }
        return "gone";
    }

    public static final void withIncreasedIndent(IndentingPrintWriter indentingPrintWriter, Runnable runnable) {
        indentingPrintWriter.increaseIndent();
        try {
            runnable.run();
        } finally {
            indentingPrintWriter.decreaseIndent();
        }
    }
}
