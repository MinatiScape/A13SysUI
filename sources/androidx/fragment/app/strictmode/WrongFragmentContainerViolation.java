package androidx.fragment.app.strictmode;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
/* loaded from: classes.dex */
public final class WrongFragmentContainerViolation extends Violation {
    private final ViewGroup mContainer;

    @Override // java.lang.Throwable
    public final String getMessage() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Attempting to add fragment ");
        m.append(this.mFragment);
        m.append(" to container ");
        m.append(this.mContainer);
        m.append(" which is not a FragmentContainerView");
        return m.toString();
    }

    public WrongFragmentContainerViolation(Fragment fragment, ViewGroup viewGroup) {
        super(fragment);
        this.mContainer = viewGroup;
    }
}
