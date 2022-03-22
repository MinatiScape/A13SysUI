package com.android.systemui.statusbar.notification.stack;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.SparseArray;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.MediaContainerController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationSectionsManager.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsManager implements StackScrollAlgorithm.SectionProvider {
    public final SectionHeaderController alertingHeaderController;
    public final ConfigurationController configurationController;
    public final NotificationSectionsManager$configurationListener$1 configurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$configurationListener$1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onLocaleListChanged() {
            NotificationSectionsManager.this.reinflateViews();
        }
    };
    public final SectionHeaderController incomingHeaderController;
    public boolean initialized;
    public final KeyguardMediaController keyguardMediaController;
    public final NotificationSectionsLogger logger;
    public final MediaContainerController mediaContainerController;
    public final NotifPipelineFlags notifPipelineFlags;
    public NotificationStackScrollLayout parent;
    public final SectionHeaderController peopleHeaderController;
    public final NotificationSectionsFeatureManager sectionsFeatureManager;
    public final SectionHeaderController silentHeaderController;
    public final StatusBarStateController statusBarStateController;

    /* compiled from: NotificationSectionsManager.kt */
    /* loaded from: classes.dex */
    public static abstract class SectionBounds {

        /* compiled from: NotificationSectionsManager.kt */
        /* loaded from: classes.dex */
        public static final class Many extends SectionBounds {
            public final ExpandableView first;
            public final ExpandableView last;

            public Many(ExpandableView expandableView, ExpandableView expandableView2) {
                super(0);
                this.first = expandableView;
                this.last = expandableView2;
            }

            public final boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Many)) {
                    return false;
                }
                Many many = (Many) obj;
                return Intrinsics.areEqual(this.first, many.first) && Intrinsics.areEqual(this.last, many.last);
            }

            public final int hashCode() {
                return this.last.hashCode() + (this.first.hashCode() * 31);
            }

            public final String toString() {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Many(first=");
                m.append(this.first);
                m.append(", last=");
                m.append(this.last);
                m.append(')');
                return m.toString();
            }
        }

        /* compiled from: NotificationSectionsManager.kt */
        /* loaded from: classes.dex */
        public static final class None extends SectionBounds {
            public static final None INSTANCE = new None();

            public None() {
                super(0);
            }
        }

        /* compiled from: NotificationSectionsManager.kt */
        /* loaded from: classes.dex */
        public static final class One extends SectionBounds {
            public final ExpandableView lone;

            public One(ExpandableView expandableView) {
                super(0);
                this.lone = expandableView;
            }

            public final boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return (obj instanceof One) && Intrinsics.areEqual(this.lone, ((One) obj).lone);
            }

            public final int hashCode() {
                return this.lone.hashCode();
            }

            public final String toString() {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("One(lone=");
                m.append(this.lone);
                m.append(')');
                return m.toString();
            }
        }

        public SectionBounds() {
        }

        public SectionBounds(int i) {
        }

        public static boolean setFirstAndLastVisibleChildren(NotificationSection notificationSection, ExpandableView expandableView, ExpandableView expandableView2) {
            boolean z;
            boolean z2;
            if (notificationSection.mFirstVisibleChild != expandableView) {
                z = true;
            } else {
                z = false;
            }
            notificationSection.mFirstVisibleChild = expandableView;
            if (notificationSection.mLastVisibleChild != expandableView2) {
                z2 = true;
            } else {
                z2 = false;
            }
            notificationSection.mLastVisibleChild = expandableView2;
            if (z || z2) {
                return true;
            }
            return false;
        }
    }

    /* compiled from: NotificationSectionsManager.kt */
    /* loaded from: classes.dex */
    public interface SectionUpdateState<T extends ExpandableView> {
        void adjustViewPosition();

        Integer getCurrentPosition();

        Integer getTargetPosition();

        void setCurrentPosition(Integer num);

        void setTargetPosition(Integer num);
    }

    @VisibleForTesting
    public static /* synthetic */ void getAlertingHeaderView$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getIncomingHeaderView$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getMediaControlsView$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getPeopleHeaderView$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getSilentHeaderView$annotations() {
    }

    @VisibleForTesting
    public final Unit updateSectionBoundaries() {
        updateSectionBoundaries("test");
        return Unit.INSTANCE;
    }

    public final NotificationSectionsManager$decorViewHeaderState$1 decorViewHeaderState(SectionHeaderView sectionHeaderView) {
        this.notifPipelineFlags.checkLegacyPipelineEnabled();
        return new NotificationSectionsManager$decorViewHeaderState$1(new NotificationSectionsManager$expandableViewHeaderState$1(sectionHeaderView, this), sectionHeaderView);
    }

    public final SectionHeaderView getAlertingHeaderView() {
        return this.alertingHeaderController.getHeaderView();
    }

    public final MediaContainerView getMediaControlsView() {
        MediaContainerController mediaContainerController = this.mediaContainerController;
        Objects.requireNonNull(mediaContainerController);
        return mediaContainerController.mediaContainerView;
    }

    public final SectionHeaderView getPeopleHeaderView() {
        return this.peopleHeaderController.getHeaderView();
    }

    public final SectionHeaderView getSilentHeaderView() {
        return this.silentHeaderController.getHeaderView();
    }

    public final void logShadeChild(int i, View view) {
        if (view == this.incomingHeaderController.getHeaderView()) {
            NotificationSectionsLogger notificationSectionsLogger = this.logger;
            Objects.requireNonNull(notificationSectionsLogger);
            notificationSectionsLogger.logPosition(i, "INCOMING HEADER");
        } else if (view == getMediaControlsView()) {
            NotificationSectionsLogger notificationSectionsLogger2 = this.logger;
            Objects.requireNonNull(notificationSectionsLogger2);
            notificationSectionsLogger2.logPosition(i, "MEDIA CONTROLS");
        } else if (view == getPeopleHeaderView()) {
            NotificationSectionsLogger notificationSectionsLogger3 = this.logger;
            Objects.requireNonNull(notificationSectionsLogger3);
            notificationSectionsLogger3.logPosition(i, "CONVERSATIONS HEADER");
        } else if (view == getAlertingHeaderView()) {
            NotificationSectionsLogger notificationSectionsLogger4 = this.logger;
            Objects.requireNonNull(notificationSectionsLogger4);
            notificationSectionsLogger4.logPosition(i, "ALERTING HEADER");
        } else if (view == getSilentHeaderView()) {
            NotificationSectionsLogger notificationSectionsLogger5 = this.logger;
            Objects.requireNonNull(notificationSectionsLogger5);
            notificationSectionsLogger5.logPosition(i, "SILENT HEADER");
        } else if (!(view instanceof ExpandableNotificationRow)) {
            NotificationSectionsLogger notificationSectionsLogger6 = this.logger;
            Class<?> cls = view.getClass();
            Objects.requireNonNull(notificationSectionsLogger6);
            LogBuffer logBuffer = notificationSectionsLogger6.logBuffer;
            LogLevel logLevel = LogLevel.DEBUG;
            NotificationSectionsLogger$logOther$2 notificationSectionsLogger$logOther$2 = NotificationSectionsLogger$logOther$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("NotifSections", logLevel, notificationSectionsLogger$logOther$2);
                obtain.int1 = i;
                obtain.str1 = cls.getName();
                logBuffer.push(obtain);
            }
        } else {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
            Objects.requireNonNull(expandableNotificationRow);
            boolean z = expandableNotificationRow.mIsHeadsUp;
            NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
            Objects.requireNonNull(notificationEntry);
            int i2 = notificationEntry.mBucket;
            if (i2 == 2) {
                NotificationSectionsLogger notificationSectionsLogger7 = this.logger;
                Objects.requireNonNull(notificationSectionsLogger7);
                notificationSectionsLogger7.logPosition(i, "Heads Up", z);
            } else if (i2 == 4) {
                NotificationSectionsLogger notificationSectionsLogger8 = this.logger;
                Objects.requireNonNull(notificationSectionsLogger8);
                notificationSectionsLogger8.logPosition(i, "Conversation", z);
            } else if (i2 == 5) {
                NotificationSectionsLogger notificationSectionsLogger9 = this.logger;
                Objects.requireNonNull(notificationSectionsLogger9);
                notificationSectionsLogger9.logPosition(i, "Alerting", z);
            } else if (i2 == 6) {
                NotificationSectionsLogger notificationSectionsLogger10 = this.logger;
                Objects.requireNonNull(notificationSectionsLogger10);
                notificationSectionsLogger10.logPosition(i, "Silent", z);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x002c, code lost:
        kotlin.collections.SetsKt__SetsKt.throwIndexOverflow();
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x002f, code lost:
        throw null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void logShadeContents() {
        /*
            r5 = this;
            java.lang.String r0 = "NotifSectionsManager.logShadeContents"
            android.os.Trace.beginSection(r0)
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r0 = r5.parent     // Catch: all -> 0x0034
            r1 = 0
            if (r0 != 0) goto L_0x000b
            r0 = r1
        L_0x000b:
            kotlin.sequences.SequencesKt__SequenceBuilderKt$sequence$$inlined$Sequence$1 r0 = com.android.systemui.util.ConvenienceExtensionsKt.getChildren(r0)     // Catch: all -> 0x0034
            r2 = 0
            java.util.Iterator r0 = r0.iterator()     // Catch: all -> 0x0034
        L_0x0014:
            r3 = r0
            kotlin.sequences.SequenceBuilderIterator r3 = (kotlin.sequences.SequenceBuilderIterator) r3     // Catch: all -> 0x0034
            boolean r4 = r3.hasNext()     // Catch: all -> 0x0034
            if (r4 == 0) goto L_0x0030
            java.lang.Object r3 = r3.next()     // Catch: all -> 0x0034
            int r4 = r2 + 1
            if (r2 < 0) goto L_0x002c
            android.view.View r3 = (android.view.View) r3     // Catch: all -> 0x0034
            r5.logShadeChild(r2, r3)     // Catch: all -> 0x0034
            r2 = r4
            goto L_0x0014
        L_0x002c:
            kotlin.collections.SetsKt__SetsKt.throwIndexOverflow()     // Catch: all -> 0x0034
            throw r1     // Catch: all -> 0x0034
        L_0x0030:
            android.os.Trace.endSection()
            return
        L_0x0034:
            r5 = move-exception
            android.os.Trace.endSection()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.logShadeContents():void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:25:0x005f  */
    /* JADX WARN: Type inference failed for: r2v0 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void reinflateViews() {
        /*
            r7 = this;
            com.android.systemui.statusbar.notification.collection.render.SectionHeaderController r0 = r7.silentHeaderController
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r1 = r7.parent
            r2 = 0
            if (r1 != 0) goto L_0x0008
            r1 = r2
        L_0x0008:
            r0.reinflateView(r1)
            com.android.systemui.statusbar.notification.collection.render.SectionHeaderController r0 = r7.alertingHeaderController
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r1 = r7.parent
            if (r1 != 0) goto L_0x0012
            r1 = r2
        L_0x0012:
            r0.reinflateView(r1)
            com.android.systemui.statusbar.notification.collection.render.SectionHeaderController r0 = r7.peopleHeaderController
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r1 = r7.parent
            if (r1 != 0) goto L_0x001c
            r1 = r2
        L_0x001c:
            r0.reinflateView(r1)
            com.android.systemui.statusbar.notification.collection.render.SectionHeaderController r0 = r7.incomingHeaderController
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r1 = r7.parent
            if (r1 != 0) goto L_0x0026
            r1 = r2
        L_0x0026:
            r0.reinflateView(r1)
            com.android.systemui.statusbar.notification.collection.render.MediaContainerController r0 = r7.mediaContainerController
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r1 = r7.parent
            if (r1 != 0) goto L_0x0030
            goto L_0x0031
        L_0x0030:
            r2 = r1
        L_0x0031:
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.notification.stack.MediaContainerView r1 = r0.mediaContainerView
            r3 = -1
            if (r1 != 0) goto L_0x003a
            goto L_0x004b
        L_0x003a:
            r1.removeFromTransientContainer()
            android.view.ViewParent r4 = r1.getParent()
            if (r4 != r2) goto L_0x004b
            int r4 = r2.indexOfChild(r1)
            r2.removeView(r1)
            goto L_0x004c
        L_0x004b:
            r4 = r3
        L_0x004c:
            android.view.LayoutInflater r1 = r0.layoutInflater
            r5 = 2131624161(0x7f0e00e1, float:1.8875494E38)
            r6 = 0
            android.view.View r1 = r1.inflate(r5, r2, r6)
            java.lang.String r5 = "null cannot be cast to non-null type com.android.systemui.statusbar.notification.stack.MediaContainerView"
            java.util.Objects.requireNonNull(r1, r5)
            com.android.systemui.statusbar.notification.stack.MediaContainerView r1 = (com.android.systemui.statusbar.notification.stack.MediaContainerView) r1
            if (r4 == r3) goto L_0x0062
            r2.addView(r1, r4)
        L_0x0062:
            r0.mediaContainerView = r1
            com.android.systemui.media.KeyguardMediaController r0 = r7.keyguardMediaController
            com.android.systemui.statusbar.notification.stack.MediaContainerView r7 = r7.getMediaControlsView()
            r0.attachSinglePaneContainer(r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.reinflateViews():void");
    }

    public final boolean updateFirstAndLastViewsForAllSections(NotificationSection[] notificationSectionArr, ArrayList arrayList) {
        SparseArray sparseArray;
        boolean z;
        Object obj;
        SectionBounds.Many many;
        SectionBounds.None none = SectionBounds.None.INSTANCE;
        int length = notificationSectionArr.length;
        if (length < 0) {
            sparseArray = new SparseArray();
        } else {
            sparseArray = new SparseArray(length);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ExpandableView expandableView = (ExpandableView) it.next();
            Integer bucket = getBucket(expandableView);
            if (bucket != null) {
                int intValue = Integer.valueOf(bucket.intValue()).intValue();
                Object obj2 = sparseArray.get(intValue);
                if (obj2 == null) {
                    obj2 = none;
                }
                SectionBounds sectionBounds = (SectionBounds) obj2;
                if (sectionBounds instanceof SectionBounds.None) {
                    obj = new SectionBounds.One(expandableView);
                } else {
                    if (sectionBounds instanceof SectionBounds.One) {
                        many = new SectionBounds.Many(((SectionBounds.One) sectionBounds).lone, expandableView);
                    } else if (sectionBounds instanceof SectionBounds.Many) {
                        many = new SectionBounds.Many(((SectionBounds.Many) sectionBounds).first, expandableView);
                    } else {
                        throw new NoWhenBranchMatchedException();
                    }
                    obj = many;
                }
                sparseArray.put(intValue, obj);
            } else {
                throw new IllegalArgumentException("Cannot find section bucket for view");
            }
        }
        int length2 = notificationSectionArr.length;
        int i = 0;
        boolean z2 = false;
        while (i < length2) {
            NotificationSection notificationSection = notificationSectionArr[i];
            i++;
            Objects.requireNonNull(notificationSection);
            Object obj3 = (SectionBounds) sparseArray.get(notificationSection.mBucket);
            if (obj3 == null) {
                obj3 = SectionBounds.None.INSTANCE;
            }
            if (obj3 instanceof SectionBounds.None) {
                z = SectionBounds.setFirstAndLastVisibleChildren(notificationSection, null, null);
            } else if (obj3 instanceof SectionBounds.One) {
                ExpandableView expandableView2 = ((SectionBounds.One) obj3).lone;
                z = SectionBounds.setFirstAndLastVisibleChildren(notificationSection, expandableView2, expandableView2);
            } else if (obj3 instanceof SectionBounds.Many) {
                SectionBounds.Many many2 = (SectionBounds.Many) obj3;
                z = SectionBounds.setFirstAndLastVisibleChildren(notificationSection, many2.first, many2.last);
            } else {
                throw new NoWhenBranchMatchedException();
            }
            if (z || z2) {
                z2 = true;
            } else {
                z2 = false;
            }
        }
        return z2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:117:0x01a5, code lost:
        if (r16.intValue() != r4.mBucket) goto L_0x01a7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x01c6, code lost:
        r4 = -1;
        r12 = 4;
     */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0188  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0196  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x01ac A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:133:0x01cb A[Catch: all -> 0x02f4, TryCatch #0 {all -> 0x02f4, blocks: (B:3:0x0009, B:9:0x0023, B:11:0x0038, B:12:0x0043, B:16:0x004e, B:19:0x0061, B:20:0x0066, B:23:0x0070, B:24:0x0074, B:27:0x007c, B:28:0x0080, B:31:0x0088, B:32:0x008c, B:35:0x0094, B:36:0x0098, B:39:0x00b5, B:42:0x00c5, B:45:0x00cc, B:48:0x00d4, B:51:0x00df, B:54:0x00e9, B:57:0x00f1, B:60:0x00f9, B:66:0x0105, B:67:0x0119, B:69:0x0122, B:72:0x0130, B:73:0x013c, B:74:0x0141, B:76:0x0145, B:80:0x014d, B:90:0x0160, B:93:0x0167, B:96:0x016d, B:100:0x0174, B:101:0x0178, B:109:0x018c, B:112:0x0191, B:116:0x019a, B:125:0x01b2, B:129:0x01bb, B:133:0x01cb, B:139:0x01dc, B:147:0x01fa, B:149:0x0202, B:150:0x0204, B:153:0x0210, B:156:0x0216, B:157:0x021a, B:160:0x0227, B:163:0x022f, B:164:0x0233, B:167:0x0240, B:170:0x0248, B:171:0x024c, B:174:0x0259, B:177:0x0261, B:178:0x0265, B:181:0x0272, B:184:0x027a, B:185:0x027e, B:187:0x028f, B:189:0x0299, B:190:0x029e, B:191:0x02a5, B:192:0x02a9, B:194:0x02af, B:195:0x02b9, B:198:0x02d1, B:202:0x02d8, B:206:0x02ed), top: B:212:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:137:0x01d9  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x01eb A[LOOP:0: B:42:0x00c5->B:142:0x01eb, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:166:0x023f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:167:0x0240 A[Catch: all -> 0x02f4, TryCatch #0 {all -> 0x02f4, blocks: (B:3:0x0009, B:9:0x0023, B:11:0x0038, B:12:0x0043, B:16:0x004e, B:19:0x0061, B:20:0x0066, B:23:0x0070, B:24:0x0074, B:27:0x007c, B:28:0x0080, B:31:0x0088, B:32:0x008c, B:35:0x0094, B:36:0x0098, B:39:0x00b5, B:42:0x00c5, B:45:0x00cc, B:48:0x00d4, B:51:0x00df, B:54:0x00e9, B:57:0x00f1, B:60:0x00f9, B:66:0x0105, B:67:0x0119, B:69:0x0122, B:72:0x0130, B:73:0x013c, B:74:0x0141, B:76:0x0145, B:80:0x014d, B:90:0x0160, B:93:0x0167, B:96:0x016d, B:100:0x0174, B:101:0x0178, B:109:0x018c, B:112:0x0191, B:116:0x019a, B:125:0x01b2, B:129:0x01bb, B:133:0x01cb, B:139:0x01dc, B:147:0x01fa, B:149:0x0202, B:150:0x0204, B:153:0x0210, B:156:0x0216, B:157:0x021a, B:160:0x0227, B:163:0x022f, B:164:0x0233, B:167:0x0240, B:170:0x0248, B:171:0x024c, B:174:0x0259, B:177:0x0261, B:178:0x0265, B:181:0x0272, B:184:0x027a, B:185:0x027e, B:187:0x028f, B:189:0x0299, B:190:0x029e, B:191:0x02a5, B:192:0x02a9, B:194:0x02af, B:195:0x02b9, B:198:0x02d1, B:202:0x02d8, B:206:0x02ed), top: B:212:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:173:0x0258 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:174:0x0259 A[Catch: all -> 0x02f4, TryCatch #0 {all -> 0x02f4, blocks: (B:3:0x0009, B:9:0x0023, B:11:0x0038, B:12:0x0043, B:16:0x004e, B:19:0x0061, B:20:0x0066, B:23:0x0070, B:24:0x0074, B:27:0x007c, B:28:0x0080, B:31:0x0088, B:32:0x008c, B:35:0x0094, B:36:0x0098, B:39:0x00b5, B:42:0x00c5, B:45:0x00cc, B:48:0x00d4, B:51:0x00df, B:54:0x00e9, B:57:0x00f1, B:60:0x00f9, B:66:0x0105, B:67:0x0119, B:69:0x0122, B:72:0x0130, B:73:0x013c, B:74:0x0141, B:76:0x0145, B:80:0x014d, B:90:0x0160, B:93:0x0167, B:96:0x016d, B:100:0x0174, B:101:0x0178, B:109:0x018c, B:112:0x0191, B:116:0x019a, B:125:0x01b2, B:129:0x01bb, B:133:0x01cb, B:139:0x01dc, B:147:0x01fa, B:149:0x0202, B:150:0x0204, B:153:0x0210, B:156:0x0216, B:157:0x021a, B:160:0x0227, B:163:0x022f, B:164:0x0233, B:167:0x0240, B:170:0x0248, B:171:0x024c, B:174:0x0259, B:177:0x0261, B:178:0x0265, B:181:0x0272, B:184:0x027a, B:185:0x027e, B:187:0x028f, B:189:0x0299, B:190:0x029e, B:191:0x02a5, B:192:0x02a9, B:194:0x02af, B:195:0x02b9, B:198:0x02d1, B:202:0x02d8, B:206:0x02ed), top: B:212:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0271 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0272 A[Catch: all -> 0x02f4, TryCatch #0 {all -> 0x02f4, blocks: (B:3:0x0009, B:9:0x0023, B:11:0x0038, B:12:0x0043, B:16:0x004e, B:19:0x0061, B:20:0x0066, B:23:0x0070, B:24:0x0074, B:27:0x007c, B:28:0x0080, B:31:0x0088, B:32:0x008c, B:35:0x0094, B:36:0x0098, B:39:0x00b5, B:42:0x00c5, B:45:0x00cc, B:48:0x00d4, B:51:0x00df, B:54:0x00e9, B:57:0x00f1, B:60:0x00f9, B:66:0x0105, B:67:0x0119, B:69:0x0122, B:72:0x0130, B:73:0x013c, B:74:0x0141, B:76:0x0145, B:80:0x014d, B:90:0x0160, B:93:0x0167, B:96:0x016d, B:100:0x0174, B:101:0x0178, B:109:0x018c, B:112:0x0191, B:116:0x019a, B:125:0x01b2, B:129:0x01bb, B:133:0x01cb, B:139:0x01dc, B:147:0x01fa, B:149:0x0202, B:150:0x0204, B:153:0x0210, B:156:0x0216, B:157:0x021a, B:160:0x0227, B:163:0x022f, B:164:0x0233, B:167:0x0240, B:170:0x0248, B:171:0x024c, B:174:0x0259, B:177:0x0261, B:178:0x0265, B:181:0x0272, B:184:0x027a, B:185:0x027e, B:187:0x028f, B:189:0x0299, B:190:0x029e, B:191:0x02a5, B:192:0x02a9, B:194:0x02af, B:195:0x02b9, B:198:0x02d1, B:202:0x02d8, B:206:0x02ed), top: B:212:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:187:0x028f A[Catch: all -> 0x02f4, TryCatch #0 {all -> 0x02f4, blocks: (B:3:0x0009, B:9:0x0023, B:11:0x0038, B:12:0x0043, B:16:0x004e, B:19:0x0061, B:20:0x0066, B:23:0x0070, B:24:0x0074, B:27:0x007c, B:28:0x0080, B:31:0x0088, B:32:0x008c, B:35:0x0094, B:36:0x0098, B:39:0x00b5, B:42:0x00c5, B:45:0x00cc, B:48:0x00d4, B:51:0x00df, B:54:0x00e9, B:57:0x00f1, B:60:0x00f9, B:66:0x0105, B:67:0x0119, B:69:0x0122, B:72:0x0130, B:73:0x013c, B:74:0x0141, B:76:0x0145, B:80:0x014d, B:90:0x0160, B:93:0x0167, B:96:0x016d, B:100:0x0174, B:101:0x0178, B:109:0x018c, B:112:0x0191, B:116:0x019a, B:125:0x01b2, B:129:0x01bb, B:133:0x01cb, B:139:0x01dc, B:147:0x01fa, B:149:0x0202, B:150:0x0204, B:153:0x0210, B:156:0x0216, B:157:0x021a, B:160:0x0227, B:163:0x022f, B:164:0x0233, B:167:0x0240, B:170:0x0248, B:171:0x024c, B:174:0x0259, B:177:0x0261, B:178:0x0265, B:181:0x0272, B:184:0x027a, B:185:0x027e, B:187:0x028f, B:189:0x0299, B:190:0x029e, B:191:0x02a5, B:192:0x02a9, B:194:0x02af, B:195:0x02b9, B:198:0x02d1, B:202:0x02d8, B:206:0x02ed), top: B:212:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:194:0x02af A[Catch: all -> 0x02f4, LOOP:2: B:192:0x02a9->B:194:0x02af, LOOP_END, TryCatch #0 {all -> 0x02f4, blocks: (B:3:0x0009, B:9:0x0023, B:11:0x0038, B:12:0x0043, B:16:0x004e, B:19:0x0061, B:20:0x0066, B:23:0x0070, B:24:0x0074, B:27:0x007c, B:28:0x0080, B:31:0x0088, B:32:0x008c, B:35:0x0094, B:36:0x0098, B:39:0x00b5, B:42:0x00c5, B:45:0x00cc, B:48:0x00d4, B:51:0x00df, B:54:0x00e9, B:57:0x00f1, B:60:0x00f9, B:66:0x0105, B:67:0x0119, B:69:0x0122, B:72:0x0130, B:73:0x013c, B:74:0x0141, B:76:0x0145, B:80:0x014d, B:90:0x0160, B:93:0x0167, B:96:0x016d, B:100:0x0174, B:101:0x0178, B:109:0x018c, B:112:0x0191, B:116:0x019a, B:125:0x01b2, B:129:0x01bb, B:133:0x01cb, B:139:0x01dc, B:147:0x01fa, B:149:0x0202, B:150:0x0204, B:153:0x0210, B:156:0x0216, B:157:0x021a, B:160:0x0227, B:163:0x022f, B:164:0x0233, B:167:0x0240, B:170:0x0248, B:171:0x024c, B:174:0x0259, B:177:0x0261, B:178:0x0265, B:181:0x0272, B:184:0x027a, B:185:0x027e, B:187:0x028f, B:189:0x0299, B:190:0x029e, B:191:0x02a5, B:192:0x02a9, B:194:0x02af, B:195:0x02b9, B:198:0x02d1, B:202:0x02d8, B:206:0x02ed), top: B:212:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:197:0x02d0  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x02d1 A[Catch: all -> 0x02f4, TryCatch #0 {all -> 0x02f4, blocks: (B:3:0x0009, B:9:0x0023, B:11:0x0038, B:12:0x0043, B:16:0x004e, B:19:0x0061, B:20:0x0066, B:23:0x0070, B:24:0x0074, B:27:0x007c, B:28:0x0080, B:31:0x0088, B:32:0x008c, B:35:0x0094, B:36:0x0098, B:39:0x00b5, B:42:0x00c5, B:45:0x00cc, B:48:0x00d4, B:51:0x00df, B:54:0x00e9, B:57:0x00f1, B:60:0x00f9, B:66:0x0105, B:67:0x0119, B:69:0x0122, B:72:0x0130, B:73:0x013c, B:74:0x0141, B:76:0x0145, B:80:0x014d, B:90:0x0160, B:93:0x0167, B:96:0x016d, B:100:0x0174, B:101:0x0178, B:109:0x018c, B:112:0x0191, B:116:0x019a, B:125:0x01b2, B:129:0x01bb, B:133:0x01cb, B:139:0x01dc, B:147:0x01fa, B:149:0x0202, B:150:0x0204, B:153:0x0210, B:156:0x0216, B:157:0x021a, B:160:0x0227, B:163:0x022f, B:164:0x0233, B:167:0x0240, B:170:0x0248, B:171:0x024c, B:174:0x0259, B:177:0x0261, B:178:0x0265, B:181:0x0272, B:184:0x027a, B:185:0x027e, B:187:0x028f, B:189:0x0299, B:190:0x029e, B:191:0x02a5, B:192:0x02a9, B:194:0x02af, B:195:0x02b9, B:198:0x02d1, B:202:0x02d8, B:206:0x02ed), top: B:212:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:214:0x01ea A[EDGE_INSN: B:214:0x01ea->B:141:0x01ea ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x015c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateSectionBoundaries(java.lang.String r21) {
        /*
            Method dump skipped, instructions count: 761
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.updateSectionBoundaries(java.lang.String):void");
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$configurationListener$1] */
    public NotificationSectionsManager(StatusBarStateController statusBarStateController, ConfigurationController configurationController, KeyguardMediaController keyguardMediaController, NotificationSectionsFeatureManager notificationSectionsFeatureManager, NotificationSectionsLogger notificationSectionsLogger, NotifPipelineFlags notifPipelineFlags, MediaContainerController mediaContainerController, SectionHeaderController sectionHeaderController, SectionHeaderController sectionHeaderController2, SectionHeaderController sectionHeaderController3, SectionHeaderController sectionHeaderController4) {
        this.statusBarStateController = statusBarStateController;
        this.configurationController = configurationController;
        this.keyguardMediaController = keyguardMediaController;
        this.sectionsFeatureManager = notificationSectionsFeatureManager;
        this.logger = notificationSectionsLogger;
        this.notifPipelineFlags = notifPipelineFlags;
        this.mediaContainerController = mediaContainerController;
        this.incomingHeaderController = sectionHeaderController;
        this.peopleHeaderController = sectionHeaderController2;
        this.alertingHeaderController = sectionHeaderController3;
        this.silentHeaderController = sectionHeaderController4;
    }

    @Override // com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm.SectionProvider
    public final boolean beginsSection(View view, View view2) {
        if (view == getSilentHeaderView() || view == getMediaControlsView() || view == getPeopleHeaderView() || view == getAlertingHeaderView() || view == this.incomingHeaderController.getHeaderView() || !Intrinsics.areEqual(getBucket(view), getBucket(view2))) {
            return true;
        }
        return false;
    }

    public final Integer getBucket(View view) {
        if (view == getSilentHeaderView()) {
            return 6;
        }
        if (view == this.incomingHeaderController.getHeaderView()) {
            return 2;
        }
        if (view == getMediaControlsView()) {
            return 1;
        }
        if (view == getPeopleHeaderView()) {
            return 4;
        }
        if (view == getAlertingHeaderView()) {
            return 5;
        }
        if (!(view instanceof ExpandableNotificationRow)) {
            return null;
        }
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
        Objects.requireNonNull(expandableNotificationRow);
        NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
        Objects.requireNonNull(notificationEntry);
        return Integer.valueOf(notificationEntry.mBucket);
    }
}
