package com.android.wm.shell.bubbles;

import android.widget.TextView;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: StackEducationView.kt */
/* loaded from: classes.dex */
public final class StackEducationView$descTextView$2 extends Lambda implements Function0<TextView> {
    public final /* synthetic */ StackEducationView this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StackEducationView$descTextView$2(StackEducationView stackEducationView) {
        super(0);
        this.this$0 = stackEducationView;
    }

    @Override // kotlin.jvm.functions.Function0
    public final TextView invoke() {
        return (TextView) this.this$0.findViewById(2131428909);
    }
}
