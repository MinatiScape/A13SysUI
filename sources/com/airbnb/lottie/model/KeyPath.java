package com.airbnb.lottie.model;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public final class KeyPath {
    public final List<String> keys;
    public KeyPathElement resolvedElement;

    public KeyPath(String... strArr) {
        this.keys = Arrays.asList(strArr);
    }

    public final boolean fullyResolvesTo(String str, int i) {
        boolean z;
        boolean z2;
        List<String> list;
        boolean z3;
        List<String> list2;
        if (i >= this.keys.size()) {
            return false;
        }
        if (i == this.keys.size() - 1) {
            z = true;
        } else {
            z = false;
        }
        String str2 = this.keys.get(i);
        if (!str2.equals("**")) {
            if (str2.equals(str) || str2.equals("*")) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (!z) {
                if (i != this.keys.size() - 2) {
                    return false;
                }
                if (!this.keys.get(list2.size() - 1).equals("**")) {
                    return false;
                }
            }
            if (z3) {
                return true;
            }
            return false;
        }
        if (z || !this.keys.get(i + 1).equals(str)) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z2) {
            if (i != this.keys.size() - 2) {
                if (i != this.keys.size() - 3) {
                    return false;
                }
                if (!this.keys.get(list.size() - 1).equals("**")) {
                    return false;
                }
            }
            return true;
        } else if (z) {
            return true;
        } else {
            int i2 = i + 1;
            if (i2 < this.keys.size() - 1) {
                return false;
            }
            return this.keys.get(i2).equals(str);
        }
    }

    public final int incrementDepthBy(String str, int i) {
        if ("__container".equals(str)) {
            return 0;
        }
        if (!this.keys.get(i).equals("**")) {
            return 1;
        }
        if (i != this.keys.size() - 1 && this.keys.get(i + 1).equals(str)) {
            return 2;
        }
        return 0;
    }

    public final boolean matches(String str, int i) {
        if ("__container".equals(str)) {
            return true;
        }
        if (i >= this.keys.size()) {
            return false;
        }
        if (this.keys.get(i).equals(str) || this.keys.get(i).equals("**") || this.keys.get(i).equals("*")) {
            return true;
        }
        return false;
    }

    public final boolean propagateToChildren(String str, int i) {
        if (!"__container".equals(str) && i >= this.keys.size() - 1 && !this.keys.get(i).equals("**")) {
            return false;
        }
        return true;
    }

    public final String toString() {
        boolean z;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("KeyPath{keys=");
        m.append(this.keys);
        m.append(",resolved=");
        if (this.resolvedElement != null) {
            z = true;
        } else {
            z = false;
        }
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, z, '}');
    }

    public KeyPath(KeyPath keyPath) {
        this.keys = new ArrayList(keyPath.keys);
        this.resolvedElement = keyPath.resolvedElement;
    }
}
