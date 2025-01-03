package com.dephoegon.delchoco.common.commands;

import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.init.ModAttributes;
import com.dephoegon.delchoco.common.teamColors;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

import static com.dephoegon.delchoco.DelChoco.DELCHOCO_ID;

public class chocoboTeams {
    public static void commands (@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        final LiteralArgumentBuilder<CommandSourceStack> delChocobo = Commands.literal("DelChoco");
        delChocobo.then(Commands.literal("TeamInitialize").requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.literal("refresh").executes(chocoboTeams::refreshTeams))
                .then(Commands.literal("create").executes(chocoboTeams::createTeams)));
        delChocobo.then(Commands.literal("TeamSettings").then(Commands.literal("TeamFriendlyFire")
                .then(Commands.literal("True").executes((command) -> setFriendlyFire(command, true)))
                .then(Commands.literal("False").executes((command)-> setFriendlyFire(command, false)))));
        Arrays.stream(teamColors.values()).map(delCo -> Commands.literal("Player").then(Commands.literal("JoinTeam")
                .then(Commands.literal(delCo.getColorName()).executes((command) -> join(command, delCo.getTeamName()))))).forEach(delChocobo::then);
        delChocobo.then(Commands.literal("Player")
                .then(Commands.literal("LeaveTeam").executes(chocoboTeams::leave)));
        delChocobo.then(Commands.literal("Chocobo")
                .then(Commands.literal("RiddenChocoboStats").executes(chocoboTeams::sendList)));
        dispatcher.register(delChocobo);
    }
    private static int setFriendlyFire(@NotNull CommandContext<CommandSourceStack> commandSourceStack, boolean fire) throws CommandSyntaxException {
        ServerPlayer player = commandSourceStack.getSource().getPlayerOrException();
        PlayerTeam playerTeam = commandSourceStack.getSource().getScoreboard().getPlayersTeam(player.getName().getString());
        if (playerTeam != null) {
            playerTeam.setAllowFriendlyFire(fire);
            commandSourceStack.getSource().sendSuccess(() -> Component.nullToEmpty("Player "+ player.getName().getString()+ " set friendly fire to " + fire + " for "+ playerTeam.getName()), true);
        } else {
            commandSourceStack.getSource().sendSuccess(() -> Component.nullToEmpty("Player "+ player.getName().getString()+ " Must be on a team to set Friendly fire for their team"), true);
        }
        return 1;
    }
    private static int join(@NotNull CommandContext<CommandSourceStack> commandSourceStack, String teamName) throws CommandSyntaxException {
        Scoreboard scoreboard = commandSourceStack.getSource().getScoreboard();
        addTeams(scoreboard, teamName, commandSourceStack);
        ServerPlayer player = commandSourceStack.getSource().getPlayerOrException();
        PlayerTeam playerTeam = scoreboard.getPlayerTeam(teamName);
        assert playerTeam != null;
        scoreboard.addPlayerToTeam(player.getName().getString(), playerTeam);
        commandSourceStack.getSource().sendSuccess(() -> Component.nullToEmpty("Player " + player.getName().getString() + " added to " + teamName + " team."), true);
        return 1;
    }
    private static int leave(@NotNull CommandContext<CommandSourceStack> commandSourceStack) throws CommandSyntaxException {
        Scoreboard scoreboard = commandSourceStack.getSource().getScoreboard();
        ServerPlayer player = commandSourceStack.getSource().getPlayerOrException();
        scoreboard.removePlayerFromTeam(player.getName().getString());
        commandSourceStack.getSource().sendSuccess(() -> Component.nullToEmpty("Player " + player.getName().getString() + " left their team."), true);
        return 1;
    }
    private static int refreshTeams(@NotNull CommandContext<CommandSourceStack> commandSourceStack) {
        Scoreboard scoreboard = commandSourceStack.getSource().getScoreboard();
        for (teamColors colors: teamColors.values()) {
            removeTeams(scoreboard, colors.getTeamName(), commandSourceStack);
            addTeams(scoreboard, colors.getTeamName(), commandSourceStack);
        }
        return 1;
    }
    private static void removeTeams(@NotNull Scoreboard scoreboard, String tName, CommandContext<CommandSourceStack> commandSourceStack) {
        PlayerTeam chocoboTeam = scoreboard.getPlayerTeam(tName);
        if (chocoboTeam != null) {
            scoreboard.removePlayerTeam(chocoboTeam);
            commandSourceStack.getSource().sendSuccess(() -> Component.nullToEmpty("Removed " + tName + "team"), true);
        }
    }
    private static void addTeams(@NotNull Scoreboard scoreboard, String tName, CommandContext<CommandSourceStack> commandSourceStack) {
        PlayerTeam chocoboTeam = scoreboard.getPlayerTeam(tName);
        if (chocoboTeam == null) {
            scoreboard.addPlayerTeam(tName);
            commandSourceStack.getSource().sendSuccess(() -> Component.nullToEmpty("Added " + tName + "team"), true);
        }
    }
    private static int createTeams(@NotNull CommandContext<CommandSourceStack> commandSourceStack) {
        Scoreboard scoreboard = commandSourceStack.getSource().getScoreboard();
        Arrays.stream(teamColors.values()).forEach(colors -> addTeams(scoreboard, colors.getTeamName(), commandSourceStack));
        return 1;
    }
    @SuppressWarnings("SameReturnValue")
    private static int sendList(@NotNull CommandContext<CommandSourceStack> commandContext) {
        CommandSourceStack source = commandContext.getSource();
        Entity commandEntity = source.getEntity();
        if(commandEntity instanceof Player player) {
            Entity mount = player.getVehicle();
            if (!(mount instanceof Chocobo chocobo)) {
                source.sendSuccess(() -> Component.translatable("command." + DELCHOCO_ID + ".chocobo.not_riding_chocobo"), false);
                return 0;
            } else {
                source.sendSuccess(() -> getText("get_health", chocobo, Attributes.MAX_HEALTH), false);
                source.sendSuccess(() -> getText("get_resistance", chocobo, Attributes.ARMOR), false);
                source.sendSuccess(() -> getText("get_speed", chocobo, Attributes.MOVEMENT_SPEED), false);
                source.sendSuccess(() -> getText("get_stamina", chocobo, ModAttributes.MAX_STAMINA.get()), false);
                source.sendSuccess(() -> getText("get_attack", chocobo, Attributes.ATTACK_DAMAGE), false);
                source.sendSuccess(() -> getText(chocobo.getGenerationString()), false);
            }
        }
        return 0;
    }
    @Contract("_, _, _ -> new")
    private static @NotNull net.minecraft.network.chat.Component getText(String key, @NotNull Chocobo chocobo, Attribute attribute) { return Component.translatable("command." + DELCHOCO_ID + ".chocobo." + key, Objects.requireNonNull(chocobo.getAttribute(attribute)).getValue()); }
    @Contract(value = "_ -> new", pure = true)
    private static @NotNull net.minecraft.network.chat.Component getText(String value) { return Component.translatable("command." + DELCHOCO_ID + ".chocobo." + "get_generation", value); }
}