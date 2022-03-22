package com.android.systemui.qs;

import android.content.res.Configuration;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ViewController;
/* loaded from: classes.dex */
public final class QSContainerImplController extends ViewController<QSContainerImpl> {
    public final ConfigurationController mConfigurationController;
    public final AnonymousClass1 mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.qs.QSContainerImplController.1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onConfigChanged(Configuration configuration) {
            QSContainerImplController qSContainerImplController = QSContainerImplController.this;
            ((QSContainerImpl) qSContainerImplController.mView).updateResources(qSContainerImplController.mQsPanelController, qSContainerImplController.mQuickStatusBarHeaderController);
        }
    };
    public final QSPanelController mQsPanelController;
    public final QuickStatusBarHeaderController mQuickStatusBarHeaderController;

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        this.mQuickStatusBarHeaderController.init();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        ((QSContainerImpl) this.mView).updateResources(this.mQsPanelController, this.mQuickStatusBarHeaderController);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.qs.QSContainerImplController$1] */
    public QSContainerImplController(QSContainerImpl qSContainerImpl, QSPanelController qSPanelController, QuickStatusBarHeaderController quickStatusBarHeaderController, ConfigurationController configurationController) {
        super(qSContainerImpl);
        this.mQsPanelController = qSPanelController;
        this.mQuickStatusBarHeaderController = quickStatusBarHeaderController;
        this.mConfigurationController = configurationController;
    }
}
