package com.google.android.setupcompat.internal;

import android.annotation.TargetApi;
import android.os.BaseBundle;
import android.os.PersistableBundle;
import android.util.ArrayMap;
import com.android.systemui.R$array;
import com.google.android.setupcompat.util.Logger;
import java.util.Objects;
@TargetApi(22)
/* loaded from: classes.dex */
public final class PersistableBundles {
    public static final Logger LOG = new Logger("PersistableBundles");

    public static ArrayMap<String, Object> toMap(BaseBundle baseBundle) {
        if (baseBundle == null || baseBundle.isEmpty()) {
            return new ArrayMap<>(0);
        }
        ArrayMap<String, Object> arrayMap = new ArrayMap<>(baseBundle.size());
        for (String str : baseBundle.keySet()) {
            Object obj = baseBundle.get(str);
            if (!isSupportedDataType(obj)) {
                LOG.w(String.format("Unknown/unsupported data type [%s] for key %s", obj, str));
            } else {
                arrayMap.put(str, baseBundle.get(str));
            }
        }
        return arrayMap;
    }

    public static PersistableBundle assertIsValid(PersistableBundle persistableBundle) {
        Objects.requireNonNull(persistableBundle, "PersistableBundle cannot be null!");
        for (String str : persistableBundle.keySet()) {
            Object obj = persistableBundle.get(str);
            R$array.checkArgument(isSupportedDataType(obj), String.format("Unknown/unsupported data type [%s] for key %s", obj, str));
        }
        return persistableBundle;
    }

    public static boolean isSupportedDataType(Object obj) {
        if ((obj instanceof Integer) || (obj instanceof Long) || (obj instanceof Double) || (obj instanceof Float) || (obj instanceof String) || (obj instanceof Boolean)) {
            return true;
        }
        return false;
    }
}
