package androidx.transition;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.view.View;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: classes.dex */
public final class TransitionValues {
    public View view;
    public final HashMap values = new HashMap();
    public final ArrayList<Transition> mTargetedTransitions = new ArrayList<>();

    @Deprecated
    public TransitionValues() {
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof TransitionValues)) {
            return false;
        }
        TransitionValues transitionValues = (TransitionValues) obj;
        if (this.view != transitionValues.view || !this.values.equals(transitionValues.values)) {
            return false;
        }
        return true;
    }

    public final int hashCode() {
        return this.values.hashCode() + (this.view.hashCode() * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("TransitionValues@");
        m.append(Integer.toHexString(hashCode()));
        m.append(":\n");
        StringBuilder m2 = DebugInfo$$ExternalSyntheticOutline0.m(m.toString(), "    view = ");
        m2.append(this.view);
        m2.append("\n");
        String m3 = SupportMenuInflater$$ExternalSyntheticOutline0.m(m2.toString(), "    values:");
        for (String str : this.values.keySet()) {
            m3 = m3 + "    " + str + ": " + this.values.get(str) + "\n";
        }
        return m3;
    }

    public TransitionValues(View view) {
        this.view = view;
    }
}
