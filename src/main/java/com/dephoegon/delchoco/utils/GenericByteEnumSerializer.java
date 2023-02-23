package com.dephoegon.delchoco.utils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import org.jetbrains.annotations.NotNull;

public class GenericByteEnumSerializer<E extends Enum<E>> implements EntityDataSerializer<E> {
    private final E[] values;

    public GenericByteEnumSerializer(E[] values) { this.values = values; }
    @Override
    public void write(@NotNull FriendlyByteBuf buf, @NotNull E value) { buf.writeByte(value.ordinal()); }
    @Override
    public @NotNull E read(@NotNull FriendlyByteBuf buf) { return values[buf.readByte()]; }
    @Override
    public @NotNull EntityDataAccessor<E> createAccessor(int id) { return new EntityDataAccessor<>(id, this); }
    @Override
    public @NotNull E copy(@NotNull E value) { return value; }
}
