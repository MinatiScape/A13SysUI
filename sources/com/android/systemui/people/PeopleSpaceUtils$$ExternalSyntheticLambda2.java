package com.android.systemui.people;

import android.app.people.IPeopleManager;
import android.app.people.PeopleSpaceTile;
import android.graphics.Rect;
import android.util.Log;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceUtils$$ExternalSyntheticLambda2 implements Function {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        int i;
        Long l;
        switch (this.$r8$classId) {
            case 0:
                IPeopleManager iPeopleManager = (IPeopleManager) this.f$0;
                PeopleSpaceTile peopleSpaceTile = (PeopleSpaceTile) obj;
                PeopleSpaceTile.Builder builder = peopleSpaceTile.toBuilder();
                try {
                    l = Long.valueOf(iPeopleManager.getLastInteraction(peopleSpaceTile.getPackageName(), peopleSpaceTile.getUserHandle().getIdentifier(), peopleSpaceTile.getId()));
                } catch (Exception e) {
                    Log.e("PeopleSpaceUtils", "Couldn't retrieve last interaction time", e);
                    l = 0L;
                }
                return builder.setLastInteractionTimestamp(l.longValue()).build();
            default:
                PipTouchHandler pipTouchHandler = (PipTouchHandler) this.f$0;
                Rect rect = (Rect) obj;
                Objects.requireNonNull(pipTouchHandler);
                Rect rect2 = new Rect();
                PipBoundsAlgorithm pipBoundsAlgorithm = pipTouchHandler.mPipBoundsAlgorithm;
                Rect rect3 = pipTouchHandler.mInsetBounds;
                if (pipTouchHandler.mIsImeShowing) {
                    i = pipTouchHandler.mImeHeight;
                } else {
                    i = 0;
                }
                Objects.requireNonNull(pipBoundsAlgorithm);
                PipBoundsAlgorithm.getMovementBounds(rect, rect3, rect2, i);
                return rect2;
        }
    }
}
