package com.zhufucdev.motion_emulator.data;

import com.zhufucdev.me.stub.Motion;
import com.zhufucdev.me.stub.MotionMoment;
import io.ktor.http.LinkHeader;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/* JADX INFO: compiled from: MotionRecorder.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00030\u0005H&J$\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00030\u0005H&J\b\u0010\t\u001a\u00020\nH&¨\u0006\u000b"}, d2 = {"Lcom/zhufucdev/motion_emulator/data/MotionCallback;", "", "onUpdate", "", "l", "Lkotlin/Function1;", "Lcom/zhufucdev/me/stub/MotionMoment;", LinkHeader.Parameters.Type, "", "summarize", "Lcom/zhufucdev/me/stub/Motion;", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface MotionCallback {
    void onUpdate(int type, Function1<? super MotionMoment, Unit> l);

    void onUpdate(Function1<? super MotionMoment, Unit> l);

    Motion summarize();
}
