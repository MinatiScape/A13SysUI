package com.android.systemui.dreams;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.systemui.CoreStartable;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import com.android.systemui.dreams.complication.ComplicationViewModel;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
/* loaded from: classes.dex */
public final class SmartSpaceComplication implements Complication {
    public final Context mContext;
    public final LockscreenSmartspaceController mSmartSpaceController;

    /* loaded from: classes.dex */
    public static class Registrant extends CoreStartable {
        public final SmartSpaceComplication mComplication;
        public final DreamOverlayStateController mDreamOverlayStateController;
        public final LockscreenSmartspaceController mSmartSpaceController;

        @Override // com.android.systemui.CoreStartable
        public final void start() {
            if (this.mSmartSpaceController.isEnabled()) {
                this.mDreamOverlayStateController.addComplication(this.mComplication);
            }
        }

        public Registrant(Context context, DreamOverlayStateController dreamOverlayStateController, SmartSpaceComplication smartSpaceComplication, LockscreenSmartspaceController lockscreenSmartspaceController) {
            super(context);
            this.mDreamOverlayStateController = dreamOverlayStateController;
            this.mComplication = smartSpaceComplication;
            this.mSmartSpaceController = lockscreenSmartspaceController;
        }
    }

    /* loaded from: classes.dex */
    public static class SmartSpaceComplicationViewHolder implements Complication.ViewHolder {
        public final Context mContext;
        public final LockscreenSmartspaceController mSmartSpaceController;

        @Override // com.android.systemui.dreams.complication.Complication.ViewHolder
        public final ComplicationLayoutParams getLayoutParams() {
            return new ComplicationLayoutParams(0, 5, 2, 10, true);
        }

        @Override // com.android.systemui.dreams.complication.Complication.ViewHolder
        public final View getView() {
            FrameLayout frameLayout = new FrameLayout(this.mContext);
            frameLayout.addView(this.mSmartSpaceController.buildAndConnectView(frameLayout), new ViewGroup.LayoutParams(-1, -2));
            return frameLayout;
        }

        public SmartSpaceComplicationViewHolder(Context context, LockscreenSmartspaceController lockscreenSmartspaceController) {
            this.mSmartSpaceController = lockscreenSmartspaceController;
            this.mContext = context;
        }
    }

    @Override // com.android.systemui.dreams.complication.Complication
    public final Complication.ViewHolder createView(ComplicationViewModel complicationViewModel) {
        return new SmartSpaceComplicationViewHolder(this.mContext, this.mSmartSpaceController);
    }

    public SmartSpaceComplication(Context context, LockscreenSmartspaceController lockscreenSmartspaceController) {
        this.mContext = context;
        this.mSmartSpaceController = lockscreenSmartspaceController;
    }
}
