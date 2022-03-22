package com.android.systemui.media.dream;

import android.content.Context;
import com.android.systemui.CoreStartable;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.DreamOverlayStateController$$ExternalSyntheticLambda0;
import com.android.systemui.media.MediaData;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.SmartspaceMediaData;
import java.util.Objects;
/* loaded from: classes.dex */
public class MediaDreamSentinel extends CoreStartable {
    public final MediaDreamComplication mComplication;
    public final DreamOverlayStateController mDreamOverlayStateController;
    public AnonymousClass1 mListener = new MediaDataManager.Listener() { // from class: com.android.systemui.media.dream.MediaDreamSentinel.1
        public boolean mAdded;

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public final void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z, boolean z2) {
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public final void onSmartspaceMediaDataRemoved(String str, boolean z) {
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public final void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i) {
            if (!this.mAdded && MediaDreamSentinel.this.mMediaDataManager.hasActiveMedia()) {
                this.mAdded = true;
                MediaDreamSentinel mediaDreamSentinel = MediaDreamSentinel.this;
                mediaDreamSentinel.mDreamOverlayStateController.addComplication(mediaDreamSentinel.mComplication);
            }
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public final void onMediaDataRemoved(String str) {
            if (this.mAdded && !MediaDreamSentinel.this.mMediaDataManager.hasActiveMedia()) {
                this.mAdded = false;
                MediaDreamSentinel mediaDreamSentinel = MediaDreamSentinel.this;
                DreamOverlayStateController dreamOverlayStateController = mediaDreamSentinel.mDreamOverlayStateController;
                MediaDreamComplication mediaDreamComplication = mediaDreamSentinel.mComplication;
                Objects.requireNonNull(dreamOverlayStateController);
                dreamOverlayStateController.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda0(dreamOverlayStateController, mediaDreamComplication, 0));
            }
        }
    };
    public final MediaDataManager mMediaDataManager;

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        this.mMediaDataManager.addListener(this.mListener);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.media.dream.MediaDreamSentinel$1] */
    public MediaDreamSentinel(Context context, MediaDataManager mediaDataManager, DreamOverlayStateController dreamOverlayStateController, MediaDreamComplication mediaDreamComplication) {
        super(context);
        this.mMediaDataManager = mediaDataManager;
        this.mDreamOverlayStateController = dreamOverlayStateController;
        this.mComplication = mediaDreamComplication;
    }
}
