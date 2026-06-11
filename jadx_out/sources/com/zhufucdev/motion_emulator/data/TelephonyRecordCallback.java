package com.zhufucdev.motion_emulator.data;

import com.zhufucdev.me.stub.CellMoment;
import com.zhufucdev.me.stub.CellTimeline;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/* JADX INFO: compiled from: TelephonyRecorder.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00030\u0005H&J\b\u0010\u0007\u001a\u00020\bH&¨\u0006\t"}, d2 = {"Lcom/zhufucdev/motion_emulator/data/TelephonyRecordCallback;", "", "onUpdate", "", "l", "Lkotlin/Function1;", "Lcom/zhufucdev/me/stub/CellMoment;", "summarize", "Lcom/zhufucdev/me/stub/CellTimeline;", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface TelephonyRecordCallback {
    void onUpdate(Function1<? super CellMoment, Unit> l);

    CellTimeline summarize();
}
