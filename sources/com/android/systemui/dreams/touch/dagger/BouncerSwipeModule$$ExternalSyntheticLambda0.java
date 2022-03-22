package com.android.systemui.dreams.touch.dagger;

import android.animation.ValueAnimator;
import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;
import dagger.internal.SetFactory;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Provider;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BouncerSwipeModule$$ExternalSyntheticLambda0 implements BouncerSwipeTouchHandler.ValueAnimatorCreator {
    public static final /* synthetic */ BouncerSwipeModule$$ExternalSyntheticLambda0 INSTANCE = new BouncerSwipeModule$$ExternalSyntheticLambda0();

    @Override // com.android.systemui.dreams.touch.BouncerSwipeTouchHandler.ValueAnimatorCreator
    public ValueAnimator create(float f, float f2) {
        return ValueAnimator.ofFloat(f, f2);
    }

    public static SetFactory m(ArrayList arrayList, Provider provider, ArrayList arrayList2, List list) {
        arrayList.add(provider);
        return new SetFactory(arrayList2, list);
    }
}
