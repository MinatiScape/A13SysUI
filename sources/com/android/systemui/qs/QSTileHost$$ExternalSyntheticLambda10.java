package com.android.systemui.qs;

import com.android.systemui.Dumpable;
import com.android.systemui.plugins.qs.QSTile;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class QSTileHost$$ExternalSyntheticLambda10 implements Predicate {
    public static final /* synthetic */ QSTileHost$$ExternalSyntheticLambda10 INSTANCE = new QSTileHost$$ExternalSyntheticLambda10();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean z = QSTileHost.DEBUG;
        return ((QSTile) obj) instanceof Dumpable;
    }
}
