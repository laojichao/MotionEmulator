package com.zhufucdev.motion_emulator.data;

import kotlin.Metadata;
import kotlinx.serialization.KSerializer;

/* JADX INFO: compiled from: Motions.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\bÇ\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003R\u001a\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u00058TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\u00020\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b¨\u0006\f"}, d2 = {"Lcom/zhufucdev/motion_emulator/data/MotionComposites;", "Lcom/zhufucdev/motion_emulator/data/DataStore;", "Lcom/zhufucdev/motion_emulator/data/MotionComposite;", "()V", "dataSerializer", "Lkotlinx/serialization/KSerializer;", "getDataSerializer", "()Lkotlinx/serialization/KSerializer;", "typeName", "", "getTypeName", "()Ljava/lang/String;", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MotionComposites extends DataStore<MotionComposite> {
    public static final int $stable = 0;
    public static final MotionComposites INSTANCE = new MotionComposites();

    private MotionComposites() {
    }

    @Override // com.zhufucdev.motion_emulator.data.DataStore
    public String getTypeName() {
        return "motion_composite";
    }

    @Override // com.zhufucdev.motion_emulator.data.DataStore
    protected KSerializer<MotionComposite> getDataSerializer() {
        return MotionComposite.INSTANCE.serializer();
    }
}
