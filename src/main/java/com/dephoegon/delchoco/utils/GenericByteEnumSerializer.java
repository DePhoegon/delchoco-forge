package com.dephoegon.delchoco.utils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import org.jetbrains.annotations.NotNull;

public class GenericByteEnumSerializer<E extends Enum<E>> implements EntityDataSerializer<E> {
    private final E[] values;

    public GenericByteEnumSerializer(E[] values) { this.values = values; }
    public void write(@NotNull FriendlyByteBuf buf, @NotNull E value) { buf.writeByte(value.ordinal()); }
    public @NotNull E read(@NotNull FriendlyByteBuf buf) { return values[buf.readByte()]; }
    public @NotNull EntityDataAccessor<E> createAccessor(int id) { return new EntityDataAccessor<>(id, this); }
    public @NotNull E copy(@NotNull E value) { return value; }
}