package com.android.systemui.media;

import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MediaHierarchyManager.kt */
/* loaded from: classes.dex */
public final class MediaHierarchyManager$register$1 extends Lambda implements Function1<Boolean, Unit> {
    public final /* synthetic */ MediaHost $mediaObject;
    public final /* synthetic */ MediaHierarchyManager this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MediaHierarchyManager$register$1(MediaHost mediaHost, MediaHierarchyManager mediaHierarchyManager) {
        super(1);
        this.$mediaObject = mediaHost;
        this.this$0 = mediaHierarchyManager;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(Boolean bool) {
        boolean z;
        bool.booleanValue();
        MediaHost mediaHost = this.$mediaObject;
        Objects.requireNonNull(mediaHost);
        if (mediaHost.location == 1) {
            z = true;
        } else {
            z = false;
        }
        this.this$0.updateDesiredLocation(true, z);
        return Unit.INSTANCE;
    }
}
