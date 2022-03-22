package com.android.systemui.statusbar.policy;

import android.content.ClipData;
import com.android.systemui.statusbar.policy.RemoteInputView;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class RemoteInputView$RemoteEditText$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ RemoteInputView$RemoteEditText$$ExternalSyntheticLambda1 INSTANCE = new RemoteInputView$RemoteEditText$$ExternalSyntheticLambda1();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        int i = RemoteInputView.RemoteEditText.$r8$clinit;
        if (((ClipData.Item) obj).getUri() != null) {
            return true;
        }
        return false;
    }
}
