package com.android.systemui.statusbar.notification.collection.render;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Trace;
import android.view.View;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import java.util.LinkedHashMap;
import java.util.Objects;
import kotlin.Pair;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ShadeViewDiffer.kt */
/* loaded from: classes.dex */
public final class ShadeViewDiffer {
    public final ShadeViewDifferLogger logger;
    public final LinkedHashMap nodes;
    public final ShadeNode rootNode;
    public final LinkedHashMap views;

    public final void applySpec(NodeSpecImpl nodeSpecImpl) {
        Trace.beginSection("ShadeViewDiffer.applySpec");
        try {
            LinkedHashMap treeToMap = treeToMap(nodeSpecImpl);
            NodeController nodeController = nodeSpecImpl.controller;
            ShadeNode shadeNode = this.rootNode;
            Objects.requireNonNull(shadeNode);
            if (Intrinsics.areEqual(nodeController, shadeNode.controller)) {
                detachChildren(this.rootNode, treeToMap);
                attachChildren(this.rootNode, treeToMap);
                return;
            }
            throw new IllegalArgumentException("Tree root " + nodeSpecImpl.controller.getNodeLabel() + " does not match own root at " + this.rootNode.getLabel());
        } finally {
            Trace.endSection();
        }
    }

    public final void attachChildren(ShadeNode shadeNode, LinkedHashMap linkedHashMap) {
        String str;
        LogLevel logLevel = LogLevel.DEBUG;
        Objects.requireNonNull(shadeNode);
        Object obj = linkedHashMap.get(shadeNode.controller);
        if (obj != null) {
            int i = 0;
            for (NodeSpec nodeSpec : ((NodeSpec) obj).getChildren()) {
                int i2 = i + 1;
                View childAt = shadeNode.controller.getChildAt(i);
                ShadeNode node = getNode(nodeSpec);
                if (!Intrinsics.areEqual(node.view, childAt)) {
                    ShadeNode shadeNode2 = node.parent;
                    if (shadeNode2 == null) {
                        ShadeViewDifferLogger shadeViewDifferLogger = this.logger;
                        String label = node.getLabel();
                        String label2 = shadeNode.getLabel();
                        Objects.requireNonNull(shadeViewDifferLogger);
                        LogBuffer logBuffer = shadeViewDifferLogger.buffer;
                        ShadeViewDifferLogger$logAttachingChild$2 shadeViewDifferLogger$logAttachingChild$2 = ShadeViewDifferLogger$logAttachingChild$2.INSTANCE;
                        Objects.requireNonNull(logBuffer);
                        if (!logBuffer.frozen) {
                            LogMessageImpl obtain = logBuffer.obtain("NotifViewManager", logLevel, shadeViewDifferLogger$logAttachingChild$2);
                            obtain.str1 = label;
                            obtain.str2 = label2;
                            logBuffer.push(obtain);
                        }
                        shadeNode.controller.addChildAt(node.controller, i);
                        node.controller.onViewAdded();
                        node.parent = shadeNode;
                    } else if (Intrinsics.areEqual(shadeNode2, shadeNode)) {
                        ShadeViewDifferLogger shadeViewDifferLogger2 = this.logger;
                        String label3 = node.getLabel();
                        String label4 = shadeNode.getLabel();
                        Objects.requireNonNull(shadeViewDifferLogger2);
                        LogBuffer logBuffer2 = shadeViewDifferLogger2.buffer;
                        ShadeViewDifferLogger$logMovingChild$2 shadeViewDifferLogger$logMovingChild$2 = ShadeViewDifferLogger$logMovingChild$2.INSTANCE;
                        Objects.requireNonNull(logBuffer2);
                        if (!logBuffer2.frozen) {
                            LogMessageImpl obtain2 = logBuffer2.obtain("NotifViewManager", logLevel, shadeViewDifferLogger$logMovingChild$2);
                            obtain2.str1 = label3;
                            obtain2.str2 = label4;
                            obtain2.int1 = i;
                            logBuffer2.push(obtain2);
                        }
                        shadeNode.controller.moveChildTo(node.controller, i);
                        node.controller.onViewMoved();
                    } else {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Child ");
                        m.append(node.getLabel());
                        m.append(" should have parent ");
                        m.append(shadeNode.getLabel());
                        m.append(" but is actually ");
                        ShadeNode shadeNode3 = node.parent;
                        if (shadeNode3 == null) {
                            str = null;
                        } else {
                            str = shadeNode3.getLabel();
                        }
                        m.append((Object) str);
                        throw new IllegalStateException(m.toString());
                    }
                }
                if (!nodeSpec.getChildren().isEmpty()) {
                    attachChildren(node, linkedHashMap);
                }
                i = i2;
            }
            return;
        }
        throw new IllegalStateException("Required value was null.".toString());
    }

    public final void detachChildren(ShadeNode shadeNode, LinkedHashMap linkedHashMap) {
        NodeSpec nodeSpec;
        ShadeNode shadeNode2;
        boolean z;
        String str;
        Objects.requireNonNull(shadeNode);
        NodeSpec nodeSpec2 = (NodeSpec) linkedHashMap.get(shadeNode.controller);
        boolean z2 = true;
        int childCount = shadeNode.controller.getChildCount() - 1;
        if (childCount >= 0) {
            while (true) {
                int i = childCount - 1;
                ShadeNode shadeNode3 = (ShadeNode) this.views.get(shadeNode.controller.getChildAt(childCount));
                if (shadeNode3 != null) {
                    NodeSpec nodeSpec3 = (NodeSpec) linkedHashMap.get(shadeNode3.controller);
                    if (nodeSpec3 == null) {
                        nodeSpec = null;
                    } else {
                        nodeSpec = nodeSpec3.getParent();
                    }
                    if (nodeSpec == null) {
                        shadeNode2 = null;
                    } else {
                        shadeNode2 = getNode(nodeSpec);
                    }
                    if (!Intrinsics.areEqual(shadeNode2, shadeNode)) {
                        boolean z3 = false;
                        if (shadeNode2 == null) {
                            z = z2;
                        } else {
                            z = false;
                        }
                        if (z) {
                            this.nodes.remove(shadeNode3.controller);
                            this.views.remove(shadeNode3.controller.getView());
                        }
                        ShadeViewDifferLogger shadeViewDifferLogger = this.logger;
                        String label = shadeNode3.getLabel();
                        boolean z4 = !z;
                        if (nodeSpec2 == null) {
                            z3 = z2;
                        }
                        String label2 = shadeNode.getLabel();
                        if (shadeNode2 == null) {
                            str = null;
                        } else {
                            str = shadeNode2.getLabel();
                        }
                        Objects.requireNonNull(shadeViewDifferLogger);
                        LogBuffer logBuffer = shadeViewDifferLogger.buffer;
                        LogLevel logLevel = LogLevel.DEBUG;
                        ShadeViewDifferLogger$logDetachingChild$2 shadeViewDifferLogger$logDetachingChild$2 = ShadeViewDifferLogger$logDetachingChild$2.INSTANCE;
                        Objects.requireNonNull(logBuffer);
                        if (!logBuffer.frozen) {
                            LogMessageImpl obtain = logBuffer.obtain("NotifViewManager", logLevel, shadeViewDifferLogger$logDetachingChild$2);
                            obtain.str1 = label;
                            obtain.bool1 = z4;
                            obtain.bool2 = z3;
                            obtain.str2 = label2;
                            obtain.str3 = str;
                            logBuffer.push(obtain);
                        }
                        shadeNode.controller.removeChild(shadeNode3.controller, z4);
                        shadeNode3.controller.onViewRemoved();
                        shadeNode3.parent = null;
                    }
                    if (shadeNode3.controller.getChildCount() > 0) {
                        detachChildren(shadeNode3, linkedHashMap);
                    }
                }
                if (i >= 0) {
                    childCount = i;
                    z2 = true;
                } else {
                    return;
                }
            }
        }
    }

    public final ShadeNode getNode(NodeSpec nodeSpec) {
        ShadeNode shadeNode = (ShadeNode) this.nodes.get(nodeSpec.getController());
        if (shadeNode != null) {
            return shadeNode;
        }
        NodeController controller = nodeSpec.getController();
        ShadeNode shadeNode2 = new ShadeNode(controller);
        this.nodes.put(controller, shadeNode2);
        this.views.put(shadeNode2.view, shadeNode2);
        return shadeNode2;
    }

    public final LinkedHashMap treeToMap(NodeSpecImpl nodeSpecImpl) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        try {
            registerNodes(nodeSpecImpl, linkedHashMap);
            return linkedHashMap;
        } catch (DuplicateNodeException e) {
            ShadeViewDifferLogger shadeViewDifferLogger = this.logger;
            Objects.requireNonNull(shadeViewDifferLogger);
            LogBuffer logBuffer = shadeViewDifferLogger.buffer;
            LogLevel logLevel = LogLevel.ERROR;
            ShadeViewDifferLogger$logDuplicateNodeInTree$2 shadeViewDifferLogger$logDuplicateNodeInTree$2 = ShadeViewDifferLogger$logDuplicateNodeInTree$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("NotifViewManager", logLevel, shadeViewDifferLogger$logDuplicateNodeInTree$2);
                obtain.str1 = e.toString();
                StringBuilder sb = new StringBuilder();
                NodeControllerKt.treeSpecToStrHelper(nodeSpecImpl, sb, "");
                obtain.str2 = sb.toString();
                logBuffer.push(obtain);
            }
            throw e;
        }
    }

    public ShadeViewDiffer(RootNodeController rootNodeController, ShadeViewDifferLogger shadeViewDifferLogger) {
        this.logger = shadeViewDifferLogger;
        ShadeNode shadeNode = new ShadeNode(rootNodeController);
        this.rootNode = shadeNode;
        Pair pair = new Pair(rootNodeController, shadeNode);
        int i = 0;
        Pair[] pairArr = {pair};
        LinkedHashMap linkedHashMap = new LinkedHashMap(MapsKt__MapsJVMKt.mapCapacity(1));
        while (i < 1) {
            Pair pair2 = pairArr[i];
            i++;
            linkedHashMap.put(pair2.component1(), pair2.component2());
        }
        this.nodes = linkedHashMap;
        this.views = new LinkedHashMap();
    }

    public static void registerNodes(NodeSpec nodeSpec, LinkedHashMap linkedHashMap) {
        if (!linkedHashMap.containsKey(nodeSpec.getController())) {
            linkedHashMap.put(nodeSpec.getController(), nodeSpec);
            if (!nodeSpec.getChildren().isEmpty()) {
                for (NodeSpec nodeSpec2 : nodeSpec.getChildren()) {
                    registerNodes(nodeSpec2, linkedHashMap);
                }
                return;
            }
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Node ");
        m.append(nodeSpec.getController().getNodeLabel());
        m.append(" appears more than once");
        throw new DuplicateNodeException(m.toString());
    }
}
