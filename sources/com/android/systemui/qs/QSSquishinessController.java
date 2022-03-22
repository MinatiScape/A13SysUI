package com.android.systemui.qs;
/* compiled from: QSSquishinessController.kt */
/* loaded from: classes.dex */
public final class QSSquishinessController {
    public final QSAnimator qsAnimator;
    public final QSPanelController qsPanelController;
    public final QuickQSPanelController quickQSPanelController;
    public float squishiness = 1.0f;

    public QSSquishinessController(QSAnimator qSAnimator, QSPanelController qSPanelController, QuickQSPanelController quickQSPanelController) {
        this.qsAnimator = qSAnimator;
        this.qsPanelController = qSPanelController;
        this.quickQSPanelController = quickQSPanelController;
    }
}
