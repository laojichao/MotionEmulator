package com.zhufucdev.motion_emulator.data;

import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import com.zhufucdev.me.stub.CellMoment;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: TelephonyRecorder.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002\u001a\u0012\u0010\u0004\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002¨\u0006\u0005"}, d2 = {"isSameTypeOf", "", "Lcom/zhufucdev/me/stub/CellMoment;", "other", "merge", "app_debug"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class TelephonyRecorderKt {
    public static final boolean isSameTypeOf(CellMoment $this$isSameTypeOf, CellMoment other) {
        Intrinsics.checkNotNullParameter($this$isSameTypeOf, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        if ($this$isSameTypeOf.getCell().isEmpty() == other.getCell().isEmpty() && $this$isSameTypeOf.getNeighboring().isEmpty() == other.getNeighboring().isEmpty()) {
            return ($this$isSameTypeOf.getLocation() == null) == (other.getLocation() == null);
        }
        return false;
    }

    public static final CellMoment merge(CellMoment $this$merge, CellMoment other) {
        Intrinsics.checkNotNullParameter($this$merge, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        List<CellInfo> cell = $this$merge.getCell();
        if (!(!cell.isEmpty())) {
            cell = null;
        }
        if (cell == null) {
            cell = other.getCell();
        }
        List<NeighboringCellInfo> neighboring = $this$merge.getNeighboring();
        List<NeighboringCellInfo> neighboring2 = neighboring.isEmpty() ^ true ? neighboring : null;
        if (neighboring2 == null) {
            neighboring2 = other.getNeighboring();
        }
        CellLocation rLocation = $this$merge.getLocation();
        if (rLocation == null) {
            rLocation = other.getLocation();
        }
        return new CellMoment($this$merge.getElapsed(), cell, neighboring2, rLocation);
    }
}
