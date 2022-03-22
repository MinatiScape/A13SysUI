package com.google.android.systemui.elmyra;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Binder;
import com.android.systemui.Dumpable;
import com.google.android.systemui.elmyra.proto.nano.ChassisProtos$SensorEvent;
import com.google.android.systemui.elmyra.proto.nano.SnapshotProtos$Event;
import com.google.android.systemui.elmyra.proto.nano.SnapshotProtos$Snapshot;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SnapshotLogger implements Dumpable {
    public final int mSnapshotCapacity;
    public ArrayList mSnapshots;

    /* loaded from: classes.dex */
    public class Snapshot {
        public final SnapshotProtos$Snapshot mSnapshot;
        public final long mTimestamp;

        public Snapshot(SnapshotProtos$Snapshot snapshotProtos$Snapshot, long j) {
            this.mSnapshot = snapshotProtos$Snapshot;
            this.mTimestamp = j;
        }
    }

    public final void addSnapshot(SnapshotProtos$Snapshot snapshotProtos$Snapshot, long j) {
        if (this.mSnapshots.size() == this.mSnapshotCapacity) {
            this.mSnapshots.remove(0);
        }
        this.mSnapshots.add(new Snapshot(snapshotProtos$Snapshot, j));
    }

    public final void dumpInternal(PrintWriter printWriter) {
        boolean z;
        boolean z2;
        ChassisProtos$SensorEvent chassisProtos$SensorEvent;
        int i;
        printWriter.println("Dumping Elmyra Snapshots");
        for (int i2 = 0; i2 < this.mSnapshots.size(); i2++) {
            Snapshot snapshot = (Snapshot) this.mSnapshots.get(i2);
            Objects.requireNonNull(snapshot);
            SnapshotProtos$Snapshot snapshotProtos$Snapshot = snapshot.mSnapshot;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("SystemTime: ");
            Snapshot snapshot2 = (Snapshot) this.mSnapshots.get(i2);
            Objects.requireNonNull(snapshot2);
            m.append(snapshot2.mTimestamp);
            printWriter.println(m.toString());
            printWriter.println("Snapshot: " + i2);
            printWriter.print("header {");
            printWriter.print("  identifier: " + snapshotProtos$Snapshot.header.identifier);
            printWriter.print("  gesture_type: " + snapshotProtos$Snapshot.header.gestureType);
            printWriter.print("  feedback: " + snapshotProtos$Snapshot.header.feedback);
            printWriter.print("}");
            for (int i3 = 0; i3 < snapshotProtos$Snapshot.events.length; i3++) {
                printWriter.print("events {");
                SnapshotProtos$Event snapshotProtos$Event = snapshotProtos$Snapshot.events[i3];
                Objects.requireNonNull(snapshotProtos$Event);
                if (snapshotProtos$Event.typesCase_ == 2) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("  gesture_stage: ");
                    SnapshotProtos$Event snapshotProtos$Event2 = snapshotProtos$Snapshot.events[i3];
                    Objects.requireNonNull(snapshotProtos$Event2);
                    if (snapshotProtos$Event2.typesCase_ == 2) {
                        i = ((Integer) snapshotProtos$Event2.types_).intValue();
                    } else {
                        i = 0;
                    }
                    m2.append(i);
                    printWriter.print(m2.toString());
                } else {
                    SnapshotProtos$Event snapshotProtos$Event3 = snapshotProtos$Snapshot.events[i3];
                    Objects.requireNonNull(snapshotProtos$Event3);
                    if (snapshotProtos$Event3.typesCase_ == 1) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (z2) {
                        SnapshotProtos$Event snapshotProtos$Event4 = snapshotProtos$Snapshot.events[i3];
                        Objects.requireNonNull(snapshotProtos$Event4);
                        if (snapshotProtos$Event4.typesCase_ == 1) {
                            chassisProtos$SensorEvent = (ChassisProtos$SensorEvent) snapshotProtos$Event4.types_;
                        } else {
                            chassisProtos$SensorEvent = null;
                        }
                        printWriter.print("  sensor_event {");
                        printWriter.print("    timestamp: " + chassisProtos$SensorEvent.timestamp);
                        for (int i4 = 0; i4 < chassisProtos$SensorEvent.values.length; i4++) {
                            StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("    values: ");
                            m3.append(chassisProtos$SensorEvent.values[i4]);
                            printWriter.print(m3.toString());
                        }
                        printWriter.print("  }");
                    }
                }
                printWriter.print("}");
            }
            StringBuilder m4 = VendorAtomValue$$ExternalSyntheticOutline1.m("sensitivity_setting: ");
            m4.append(snapshotProtos$Snapshot.sensitivitySetting);
            printWriter.println(m4.toString());
            printWriter.println();
        }
        this.mSnapshots.clear();
        printWriter.println("Finished Dumping Elmyra Snapshots");
    }

    public SnapshotLogger(int i) {
        this.mSnapshotCapacity = i;
        this.mSnapshots = new ArrayList(i);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            dumpInternal(printWriter);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }
}
