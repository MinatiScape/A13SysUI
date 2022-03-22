package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: _Sequences.kt */
/* renamed from: com.android.systemui.statusbar.notification.collection.render.RenderStageManager$dispatchOnAfterRenderGroups$lambda-5$$inlined$filterIsInstance$1  reason: invalid class name */
/* loaded from: classes.dex */
public final class RenderStageManager$dispatchOnAfterRenderGroups$lambda5$$inlined$filterIsInstance$1 extends Lambda implements Function1<Object, Boolean> {
    public static final RenderStageManager$dispatchOnAfterRenderGroups$lambda5$$inlined$filterIsInstance$1 INSTANCE = new RenderStageManager$dispatchOnAfterRenderGroups$lambda5$$inlined$filterIsInstance$1();

    public RenderStageManager$dispatchOnAfterRenderGroups$lambda5$$inlined$filterIsInstance$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(Object obj) {
        return Boolean.valueOf(obj instanceof GroupEntry);
    }
}
