package com.android.systemui.statusbar.notification.stack;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.systemui.statusbar.notification.stack.PeopleHubView;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: PeopleHubView.kt */
/* loaded from: classes.dex */
public final class PeopleHubView$onFinishInflate$1 extends Lambda implements Function1<Integer, PeopleHubView.PersonDataListenerImpl> {
    public final /* synthetic */ PeopleHubView this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PeopleHubView$onFinishInflate$1(PeopleHubView peopleHubView) {
        super(1);
        this.this$0 = peopleHubView;
    }

    @Override // kotlin.jvm.functions.Function1
    public final PeopleHubView.PersonDataListenerImpl invoke(Integer num) {
        ImageView imageView;
        int intValue = num.intValue();
        ViewGroup viewGroup = this.this$0.contents;
        if (viewGroup == null) {
            viewGroup = null;
        }
        View childAt = viewGroup.getChildAt(intValue);
        if (childAt instanceof ImageView) {
            imageView = (ImageView) childAt;
        } else {
            imageView = null;
        }
        if (imageView == null) {
            return null;
        }
        return new PeopleHubView.PersonDataListenerImpl();
    }
}
