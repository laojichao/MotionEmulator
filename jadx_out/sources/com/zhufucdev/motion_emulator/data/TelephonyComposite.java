package com.zhufucdev.motion_emulator.data;

import com.zhufucdev.me.stub.CellTimeline;
import com.zhufucdev.me.stub.Data;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.serialization.KSerializer;
import kotlinx.serialization.Serializable;
import kotlinx.serialization.descriptors.SerialDescriptor;
import kotlinx.serialization.encoding.CompositeEncoder;
import kotlinx.serialization.internal.ArrayListSerializer;
import kotlinx.serialization.internal.PluginExceptionsKt;
import kotlinx.serialization.internal.SerializationConstructorMarker;
import kotlinx.serialization.internal.StringSerializer;

/* JADX INFO: compiled from: Telephony.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0087\b\u0018\u0000 /2\u00020\u0001:\u0002./B=\b\u0011\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\u000e\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010\u000bB#\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b¢\u0006\u0002\u0010\fJ\t\u0010\u0016\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0005HÆ\u0003J\u000f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\bHÂ\u0003J-\u0010\u0019\u001a\u00020\u00002\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bHÆ\u0001J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dHÖ\u0003J\u0010\u0010\u001e\u001a\u00020\u00052\u0006\u0010\u001f\u001a\u00020 H\u0016J\t\u0010!\u001a\u00020\u0003HÖ\u0001J\t\u0010\"\u001a\u00020\u0005HÖ\u0001J&\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u00002\u0006\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020)HÁ\u0001¢\u0006\u0002\b*J\u0010\u0010+\u001a\u00020$2\u0006\u0010,\u001a\u00020-H\u0016R\u0014\u0010\u0004\u001a\u00020\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bX\u0082\u0004¢\u0006\u0002\n\u0000R#\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00110\b8FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0012\u0010\u0013¨\u00060"}, d2 = {"Lcom/zhufucdev/motion_emulator/data/TelephonyComposite;", "Lcom/zhufucdev/me/stub/Data;", "seen1", "", "id", "", "name", "ref", "", "serializationConstructorMarker", "Lkotlinx/serialization/internal/SerializationConstructorMarker;", "(ILjava/lang/String;Ljava/lang/String;Ljava/util/List;Lkotlinx/serialization/internal/SerializationConstructorMarker;)V", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)V", "getId", "()Ljava/lang/String;", "getName", "timelines", "Lcom/zhufucdev/me/stub/CellTimeline;", "getTimelines", "()Ljava/util/List;", "timelines$delegate", "Lkotlin/Lazy;", "component1", "component2", "component3", "copy", "equals", "", "other", "", "getDisplayName", "format", "Ljava/text/DateFormat;", "hashCode", "toString", "write$Self", "", "self", "output", "Lkotlinx/serialization/encoding/CompositeEncoder;", "serialDesc", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "write$Self$app_debug", "writeTo", "stream", "Ljava/io/OutputStream;", "$serializer", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
@Serializable
public final /* data */ class TelephonyComposite implements Data {
    private final String id;
    private final String name;
    private final List<String> ref;

    /* JADX INFO: renamed from: timelines$delegate, reason: from kotlin metadata */
    private final Lazy timelines;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int $stable = 8;
    private static final KSerializer<Object>[] $childSerializers = {null, null, new ArrayListSerializer(StringSerializer.INSTANCE)};

    private final List<String> component3() {
        return this.ref;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ TelephonyComposite copy$default(TelephonyComposite telephonyComposite, String str, String str2, List list, int i, Object obj) {
        if ((i & 1) != 0) {
            str = telephonyComposite.id;
        }
        if ((i & 2) != 0) {
            str2 = telephonyComposite.name;
        }
        if ((i & 4) != 0) {
            list = telephonyComposite.ref;
        }
        return telephonyComposite.copy(str, str2, list);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getId() {
        return this.id;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getName() {
        return this.name;
    }

    public final TelephonyComposite copy(String id, String name, List<String> ref) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(ref, "ref");
        return new TelephonyComposite(id, name, ref);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TelephonyComposite)) {
            return false;
        }
        TelephonyComposite telephonyComposite = (TelephonyComposite) other;
        return Intrinsics.areEqual(this.id, telephonyComposite.id) && Intrinsics.areEqual(this.name, telephonyComposite.name) && Intrinsics.areEqual(this.ref, telephonyComposite.ref);
    }

    public int hashCode() {
        return (((this.id.hashCode() * 31) + this.name.hashCode()) * 31) + this.ref.hashCode();
    }

    public String toString() {
        return "TelephonyComposite(id=" + this.id + ", name=" + this.name + ", ref=" + this.ref + ")";
    }

    /* JADX INFO: compiled from: Telephony.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004HÆ\u0001¨\u0006\u0006"}, d2 = {"Lcom/zhufucdev/motion_emulator/data/TelephonyComposite$Companion;", "", "()V", "serializer", "Lkotlinx/serialization/KSerializer;", "Lcom/zhufucdev/motion_emulator/data/TelephonyComposite;", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final KSerializer<TelephonyComposite> serializer() {
            return TelephonyComposite$$serializer.INSTANCE;
        }
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "This synthesized declaration should not be used directly", replaceWith = @ReplaceWith(expression = "", imports = {}))
    public /* synthetic */ TelephonyComposite(int seen1, String id, String name, List ref, SerializationConstructorMarker serializationConstructorMarker) {
        if (7 != (seen1 & 7)) {
            PluginExceptionsKt.throwMissingFieldException(seen1, 7, TelephonyComposite$$serializer.INSTANCE.getDescriptor());
        }
        this.id = id;
        this.name = name;
        this.ref = ref;
        this.timelines = LazyKt.lazy(new Function0<List<? extends CellTimeline>>() { // from class: com.zhufucdev.motion_emulator.data.TelephonyComposite.1
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public final List<? extends CellTimeline> invoke() {
                Iterable $this$map$iv = TelephonyComposite.this.ref;
                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                for (Object item$iv$iv : $this$map$iv) {
                    String it = (String) item$iv$iv;
                    destination$iv$iv.add(Telephonies.INSTANCE.get(it));
                }
                return (List) destination$iv$iv;
            }
        });
    }

    public TelephonyComposite(String id, String name, List<String> ref) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(ref, "ref");
        this.id = id;
        this.name = name;
        this.ref = ref;
        this.timelines = LazyKt.lazy(new Function0<List<? extends CellTimeline>>() { // from class: com.zhufucdev.motion_emulator.data.TelephonyComposite.1
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public final List<? extends CellTimeline> invoke() {
                Iterable $this$map$iv = TelephonyComposite.this.ref;
                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                for (Object item$iv$iv : $this$map$iv) {
                    String it = (String) item$iv$iv;
                    destination$iv$iv.add(Telephonies.INSTANCE.get(it));
                }
                return (List) destination$iv$iv;
            }
        });
    }

    @JvmStatic
    public static final /* synthetic */ void write$Self$app_debug(TelephonyComposite self, CompositeEncoder output, SerialDescriptor serialDesc) {
        KSerializer<Object>[] kSerializerArr = $childSerializers;
        output.encodeStringElement(serialDesc, 0, self.getId());
        output.encodeStringElement(serialDesc, 1, self.name);
        output.encodeSerializableElement(serialDesc, 2, kSerializerArr[2], self.ref);
    }

    @Override // com.zhufucdev.me.stub.Data
    public String getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

    public final List<CellTimeline> getTimelines() {
        return (List) this.timelines.getValue();
    }

    @Override // com.zhufucdev.me.stub.Data
    public String getDisplayName(DateFormat format) {
        Intrinsics.checkNotNullParameter(format, "format");
        return this.name;
    }

    @Override // com.zhufucdev.me.stub.Data
    public void writeTo(OutputStream stream) {
        Intrinsics.checkNotNullParameter(stream, "stream");
    }
}
