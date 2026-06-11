package com.zhufucdev.motion_emulator.provider;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.serialization.KSerializer;
import kotlinx.serialization.Serializable;
import kotlinx.serialization.descriptors.SerialDescriptor;
import kotlinx.serialization.encoding.CompositeEncoder;
import kotlinx.serialization.internal.GeneratedSerializer;
import kotlinx.serialization.internal.PluginGeneratedSerialDescriptor;
import kotlinx.serialization.internal.SerializationConstructorMarker;

/* JADX INFO: compiled from: EmulationRef.kt */
/* JADX INFO: loaded from: classes12.dex */
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0087\b\u0018\u0000 .2\u00020\u0001:\u0002-.BO\b\u0011\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u0012\b\u0010\f\u001a\u0004\u0018\u00010\r¢\u0006\u0002\u0010\u000eBA\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003¢\u0006\u0002\u0010\u000fJ\t\u0010\u0019\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001c\u001a\u00020\tHÆ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003JE\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u0003HÆ\u0001J\u0013\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010#\u001a\u00020\u0003HÖ\u0001J\t\u0010$\u001a\u00020\u0005HÖ\u0001J&\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u00002\u0006\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+HÁ\u0001¢\u0006\u0002\b,R\u0011\u0010\u0007\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0011R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018¨\u0006/"}, d2 = {"Lcom/zhufucdev/motion_emulator/provider/EmulationRef;", "", "seen1", "", "trace", "", "motion", "cells", "velocity", "", "repeat", "satelliteCount", "serializationConstructorMarker", "Lkotlinx/serialization/internal/SerializationConstructorMarker;", "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;DIILkotlinx/serialization/internal/SerializationConstructorMarker;)V", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;DII)V", "getCells", "()Ljava/lang/String;", "getMotion", "getRepeat", "()I", "getSatelliteCount", "getTrace", "getVelocity", "()D", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "toString", "write$Self", "", "self", "output", "Lkotlinx/serialization/encoding/CompositeEncoder;", "serialDesc", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "write$Self$app_debug", "$serializer", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
@Serializable
public final /* data */ class EmulationRef {
    public static final int $stable = 0;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final String cells;
    private final String motion;
    private final int repeat;
    private final int satelliteCount;
    private final String trace;
    private final double velocity;

    public EmulationRef() {
        this((String) null, (String) null, (String) null, 0.0d, 0, 0, 63, (DefaultConstructorMarker) null);
    }

    public static /* synthetic */ EmulationRef copy$default(EmulationRef emulationRef, String str, String str2, String str3, double d, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            str = emulationRef.trace;
        }
        if ((i3 & 2) != 0) {
            str2 = emulationRef.motion;
        }
        String str4 = str2;
        if ((i3 & 4) != 0) {
            str3 = emulationRef.cells;
        }
        String str5 = str3;
        if ((i3 & 8) != 0) {
            d = emulationRef.velocity;
        }
        double d2 = d;
        if ((i3 & 16) != 0) {
            i = emulationRef.repeat;
        }
        int i4 = i;
        if ((i3 & 32) != 0) {
            i2 = emulationRef.satelliteCount;
        }
        return emulationRef.copy(str, str4, str5, d2, i4, i2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getTrace() {
        return this.trace;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getMotion() {
        return this.motion;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getCells() {
        return this.cells;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final double getVelocity() {
        return this.velocity;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final int getRepeat() {
        return this.repeat;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final int getSatelliteCount() {
        return this.satelliteCount;
    }

    public final EmulationRef copy(String trace, String motion, String cells, double velocity, int repeat, int satelliteCount) {
        Intrinsics.checkNotNullParameter(trace, "trace");
        Intrinsics.checkNotNullParameter(motion, "motion");
        Intrinsics.checkNotNullParameter(cells, "cells");
        return new EmulationRef(trace, motion, cells, velocity, repeat, satelliteCount);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EmulationRef)) {
            return false;
        }
        EmulationRef emulationRef = (EmulationRef) other;
        return Intrinsics.areEqual(this.trace, emulationRef.trace) && Intrinsics.areEqual(this.motion, emulationRef.motion) && Intrinsics.areEqual(this.cells, emulationRef.cells) && Double.compare(this.velocity, emulationRef.velocity) == 0 && this.repeat == emulationRef.repeat && this.satelliteCount == emulationRef.satelliteCount;
    }

    public int hashCode() {
        return (((((((((this.trace.hashCode() * 31) + this.motion.hashCode()) * 31) + this.cells.hashCode()) * 31) + Double.hashCode(this.velocity)) * 31) + Integer.hashCode(this.repeat)) * 31) + Integer.hashCode(this.satelliteCount);
    }

    public String toString() {
        return "EmulationRef(trace=" + this.trace + ", motion=" + this.motion + ", cells=" + this.cells + ", velocity=" + this.velocity + ", repeat=" + this.repeat + ", satelliteCount=" + this.satelliteCount + ")";
    }

    /* JADX INFO: compiled from: EmulationRef.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004HÆ\u0001¨\u0006\u0006"}, d2 = {"Lcom/zhufucdev/motion_emulator/provider/EmulationRef$Companion;", "", "()V", "serializer", "Lkotlinx/serialization/KSerializer;", "Lcom/zhufucdev/motion_emulator/provider/EmulationRef;", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final KSerializer<EmulationRef> serializer() {
            return new GeneratedSerializer<EmulationRef>() { // from class: com.zhufucdev.motion_emulator.provider.EmulationRef$$serializer
                public static final int $stable = 0;
                private static final /* synthetic */ PluginGeneratedSerialDescriptor descriptor;

                static {
                    PluginGeneratedSerialDescriptor pluginGeneratedSerialDescriptor = new PluginGeneratedSerialDescriptor("com.zhufucdev.motion_emulator.provider.EmulationRef", 
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0004: RETURN 
                          (wrap:com.zhufucdev.motion_emulator.provider.EmulationRef$$serializer:0x0002: SGET  A[WRAPPED] (LINE:18) com.zhufucdev.motion_emulator.provider.EmulationRef$$serializer.INSTANCE com.zhufucdev.motion_emulator.provider.EmulationRef$$serializer)
                         (LINE:18) in method: com.zhufucdev.motion_emulator.provider.EmulationRef.Companion.serializer():kotlinx.serialization.KSerializer<com.zhufucdev.motion_emulator.provider.EmulationRef>, file: classes12.dex
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:310)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:273)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:94)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
                        	at jadx.core.dex.regions.Region.generate(Region.java:35)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
                        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:305)
                        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:284)
                        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:412)
                        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:337)
                        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:303)
                        	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
                        	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                        	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Method generation error
                        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:340)
                        	... 5 more
                        Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0010: CONSTRUCTOR (r0v1 'pluginGeneratedSerialDescriptor' kotlinx.serialization.internal.PluginGeneratedSerialDescriptor) = 
                          ("com.zhufucdev.motion_emulator.provider.EmulationRef")
                          (wrap:com.zhufucdev.motion_emulator.provider.EmulationRef$$serializer:0x0009: SGET  A[WRAPPED] com.zhufucdev.motion_emulator.provider.EmulationRef$$serializer.INSTANCE com.zhufucdev.motion_emulator.provider.EmulationRef$$serializer)
                          (6 int)
                         A[DECLARE_VAR, MD:(java.lang.String, kotlinx.serialization.internal.GeneratedSerializer<?>, int):void (m)] (LINE:18) call: kotlinx.serialization.internal.PluginGeneratedSerialDescriptor.<init>(java.lang.String, kotlinx.serialization.internal.GeneratedSerializer, int):void type: CONSTRUCTOR in method: com.zhufucdev.motion_emulator.provider.EmulationRef$$serializer.<clinit>():void, file: classes12.dex
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:310)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:273)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:94)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
                        	at jadx.core.dex.regions.Region.generate(Region.java:35)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
                        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:305)
                        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:284)
                        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:412)
                        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:337)
                        	... 5 more
                        Caused by: jadx.core.utils.exceptions.CodegenException: Anonymous inner class unlimited recursion detected. Convert class to inner: com.zhufucdev.motion_emulator.provider.EmulationRef$$serializer
                        	at jadx.core.codegen.InsnGen.inlineAnonymousConstructor(InsnGen.java:813)
                        	at jadx.core.codegen.InsnGen.staticField(InsnGen.java:225)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:492)
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:145)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:121)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:108)
                        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1143)
                        	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:782)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:418)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:303)
                        	... 15 more
                        */
                    /*
                        this = this;
                        com.zhufucdev.motion_emulator.provider.EmulationRef$$serializer r0 = com.zhufucdev.motion_emulator.provider.EmulationRef$$serializer.INSTANCE
                        kotlinx.serialization.KSerializer r0 = (kotlinx.serialization.KSerializer) r0
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.zhufucdev.motion_emulator.provider.EmulationRef.Companion.serializer():kotlinx.serialization.KSerializer");
                }
            }

            @Deprecated(level = DeprecationLevel.HIDDEN, message = "This synthesized declaration should not be used directly", replaceWith = @ReplaceWith(expression = "", imports = {}))
            public /* synthetic */ EmulationRef(int seen1, String trace, String motion, String cells, double velocity, int repeat, int satelliteCount, SerializationConstructorMarker serializationConstructorMarker) {
                if ((seen1 & 1) == 0) {
                    this.trace = "";
                } else {
                    this.trace = trace;
                }
                if ((seen1 & 2) == 0) {
                    this.motion = "";
                } else {
                    this.motion = motion;
                }
                if ((seen1 & 4) == 0) {
                    this.cells = "";
                } else {
                    this.cells = cells;
                }
                if ((seen1 & 8) == 0) {
                    this.velocity = 0.0d;
                } else {
                    this.velocity = velocity;
                }
                if ((seen1 & 16) == 0) {
                    this.repeat = 0;
                } else {
                    this.repeat = repeat;
                }
                if ((seen1 & 32) == 0) {
                    this.satelliteCount = 0;
                } else {
                    this.satelliteCount = satelliteCount;
                }
            }

            public EmulationRef(String trace, String motion, String cells, double velocity, int repeat, int satelliteCount) {
                Intrinsics.checkNotNullParameter(trace, "trace");
                Intrinsics.checkNotNullParameter(motion, "motion");
                Intrinsics.checkNotNullParameter(cells, "cells");
                this.trace = trace;
                this.motion = motion;
                this.cells = cells;
                this.velocity = velocity;
                this.repeat = repeat;
                this.satelliteCount = satelliteCount;
            }

            @JvmStatic
            public static final /* synthetic */ void write$Self$app_debug(EmulationRef self, CompositeEncoder output, SerialDescriptor serialDesc) {
                boolean z = output.shouldEncodeElementDefault(serialDesc, 0) || !Intrinsics.areEqual(self.trace, "");
                if (z) {
                    output.encodeStringElement(serialDesc, 0, self.trace);
                }
                boolean z2 = output.shouldEncodeElementDefault(serialDesc, 1) || !Intrinsics.areEqual(self.motion, "");
                if (z2) {
                    output.encodeStringElement(serialDesc, 1, self.motion);
                }
                boolean z3 = output.shouldEncodeElementDefault(serialDesc, 2) || !Intrinsics.areEqual(self.cells, "");
                if (z3) {
                    output.encodeStringElement(serialDesc, 2, self.cells);
                }
                boolean z4 = output.shouldEncodeElementDefault(serialDesc, 3) || Double.compare(self.velocity, 0.0d) != 0;
                if (z4) {
                    output.encodeDoubleElement(serialDesc, 3, self.velocity);
                }
                if (output.shouldEncodeElementDefault(serialDesc, 4) || self.repeat != 0) {
                    output.encodeIntElement(serialDesc, 4, self.repeat);
                }
                if (output.shouldEncodeElementDefault(serialDesc, 5) || self.satelliteCount != 0) {
                    output.encodeIntElement(serialDesc, 5, self.satelliteCount);
                }
            }

            public /* synthetic */ EmulationRef(String str, String str2, String str3, double d, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
                this((i3 & 1) != 0 ? "" : str, (i3 & 2) != 0 ? "" : str2, (i3 & 4) == 0 ? str3 : "", (i3 & 8) != 0 ? 0.0d : d, (i3 & 16) != 0 ? 0 : i, (i3 & 32) != 0 ? 0 : i2);
            }

            public final String getTrace() {
                return this.trace;
            }

            public final String getMotion() {
                return this.motion;
            }

            public final String getCells() {
                return this.cells;
            }

            public final double getVelocity() {
                return this.velocity;
            }

            public final int getRepeat() {
                return this.repeat;
            }

            public final int getSatelliteCount() {
                return this.satelliteCount;
            }
        }
