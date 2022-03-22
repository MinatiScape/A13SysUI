package com.android.wm.shell.bubbles;

import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.Complication;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleData$$ExternalSyntheticLambda5 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BubbleData$$ExternalSyntheticLambda5(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                Bubble bubble = (Bubble) obj;
                Objects.requireNonNull(bubble);
                return bubble.mKey.equals((String) this.f$0);
            case 1:
                return ((Set) this.f$0).contains((Integer) obj);
            default:
                DreamOverlayStateController dreamOverlayStateController = (DreamOverlayStateController) this.f$0;
                int i = DreamOverlayStateController.$r8$clinit;
                Objects.requireNonNull(dreamOverlayStateController);
                int requiredTypeAvailability = ((Complication) obj).getRequiredTypeAvailability();
                if (dreamOverlayStateController.mShouldShowComplications) {
                    if ((dreamOverlayStateController.mAvailableComplicationTypes & requiredTypeAvailability) == requiredTypeAvailability) {
                        return true;
                    }
                } else if (requiredTypeAvailability == 0) {
                    return true;
                }
                return false;
        }
    }
}
