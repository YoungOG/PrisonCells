package com.young.prisoncells.cells.commands;

import com.young.prisoncells.cells.PrisonCell;
import com.young.prisoncells.utilities.StringUtils;
import com.young.prisoncells.utilities.command.BaseCommand;
import com.young.prisoncells.utilities.command.Command;
import com.young.prisoncells.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PrisonCellCreateCommand extends BaseCommand {

    @Command(name = "prisoncell.create",
            aliases = {"prisoncells.create", "pc.create", "cell.create", "cells.create"},
            permission = "prisoncell.command.administrator",
            inGameOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        String[] args = commandArgs.getArgs();
        Player player = commandArgs.getPlayer();

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /prisoncell create [cellId]");
            return;
        }

        String cellId = args[0];

        if (getPrisonCellManager().getPrisonCell(cellId) != null) {
            player.sendMessage(ChatColor.RED + "There is already a Prison Cell with that cell ID.");
            return;
        }

        PrisonCell prisonCell = new PrisonCell(cellId);

        getPrisonCellManager().getPrisonCells().add(prisonCell);

        getPrisonCellManager().getCurrentlyEditingPrisonCells().remove(player.getUniqueId());
        getPrisonCellManager().getCurrentlyEditingPrisonCells().put(player.getUniqueId(), prisonCell);

        player.sendMessage(StringUtils.color("&7You have created and are editing a new Prison Cell named \"&c" + prisonCell.getCellId() + "&7\"."));
    }
}
