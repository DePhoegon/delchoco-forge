package com.dephoegon.delchoco.common.items;

import com.dephoegon.delbase.aid.util.kb;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ChocoboLeashPointer extends Item {
    private String tip1;
    private String tip2;
    private BlockPos centerPoint;
    private BlockPos leashPoint;
    private int leashDistance;
    public ChocoboLeashPointer(Properties pProperties) {
        super(pProperties);
    }
    public BlockPos getCenterPoint() { return this.centerPoint; }
    public BlockPos getLeashPoint() { return this.leashPoint; }
    public int getLeashDistance() { return this.leashDistance; }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> toolTip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, toolTip, flag);
        if (centerPoint != null) {
            this.tip1 = new TranslatableComponent("delchoco.leash.center").getString() + " : X - "+this.centerPoint.getX()+" || Z - "+this.centerPoint.getZ();
            if (leashPoint != null) {
                int leash = this.leashDistance;
                tip2 = new TranslatableComponent("delchoco.leash.spot").getString() + " - "+leash;
            }
        } else if (this.leashPoint != null) { this.tip2 = new TranslatableComponent("delchoco.leash.dist").getString() + " : X - "+ this.leashPoint.getX()+" || Z - "+ this.leashPoint.getZ()+" || Y~ - "+ this.leashPoint.getY(); }
        String hybridTip0 = new TranslatableComponent("delchoco.leash.none").getString();
        if (this.tip1 != null) {
            if (this.tip2 != null) { hybridTip0 = new TranslatableComponent("delchoco.leash.center").getString() + " & " + new TranslatableComponent("delchoco.leash.dist").getString(); }
            else { hybridTip0 = new TranslatableComponent("delchoco.leash.dist").getString(); }
        } else if (this.tip2 != null) { hybridTip0 = new TranslatableComponent("delchoco.leash.dist").getString(); }
        if(!kb.HShift() && !kb.HCtrl()) toolTip.add(new TextComponent(hybridTip0)); //if neither pressed, show tip0 (if not empty)
        if(kb.HCtrl() && this.tip2 != null) toolTip.add(new TranslatableComponent(this.tip2)); //if ctrl, show tip2 (if not empty), do first
        if(kb.HShift() && this.tip1 != null) toolTip.add(new TranslatableComponent(this.tip1)); //if shifted, show tip1 (if not empty)
    }
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        boolean shift = context.getPlayer() != null && context.getPlayer().isShiftKeyDown();
        BlockPos selection = null;
        String line = " ";
        boolean invalid = false;
        if (shift) {
            this.leashPoint = context.getClickedPos();
            selection = this.leashPoint;
            line = new TranslatableComponent("delchoco.leash.dist").getString();
        } else {
            this.centerPoint = context.getClickedPos();
            selection = this.centerPoint;
            line = new TranslatableComponent("delchoco.leash.center").getString();
        }
        if (this.centerPoint != null && this.leashPoint != null) {
            this.leashDistance = Math.max(positiveDifference(this.centerPoint.getX(), this.leashPoint.getX()), positiveDifference(this.centerPoint.getZ(), this.leashPoint.getZ()));
            if (this.leashDistance < 6 || this.leashDistance > 40) {
                line = String.valueOf(this.leashDistance);
                invalid = true;
                this.leashDistance = 40;
                this.leashPoint = null;
                this.centerPoint = null;
            }
        }
        if (invalid) {
            String out = new TranslatableComponent("delchoco.leash.invalid").getString()+" "+ line;
            context.getPlayer().displayClientMessage(new TextComponent(out), true);
        } else {
            context.getPlayer().displayClientMessage(new TextComponent(line+" @ X:"+ selection.getX()+" Z:"+selection.getZ()+" Y:"+selection.getY() ), true);
        }
        return InteractionResult.PASS;
    }
    protected static int positiveDifference(int center, int leash) {
        return center < leash ? leash - center : center - leash;
    }
}
