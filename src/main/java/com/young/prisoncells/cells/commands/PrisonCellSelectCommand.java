package com.young.prisoncells.cells.commands;

import com.young.prisoncells.cells.PrisonCell;
import com.young.prisoncells.utilities.StringUtils;
import com.young.prisoncells.utilities.command.BaseCommand;
import com.young.prisoncells.utilities.command.Command;
import com.young.prisoncells.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PrisonCellSelectCommand extends BaseCommand {

    @Command(name = "prisoncell.select",
            aliases = {"prisoncells.select", "pc.select", "cell.select", "cells.select"},
            permission = "prisoncell.command.administrator",
            inGameOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        String[] args = commandArgs.getArgs();
        Player player = commandArgs.getPlayer();

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /prisoncell select [name/clear]");
            return;
        }

        String cellId = args[0];

        if (getPrisonCellManager().getPrisonCell(cellId) == null) {
            player.sendMessage(ChatColor.RED + "There is not a Prison Cell with that cell ID.");
            return;
        }

        PrisonCell prisonCell = getPrisonCellManager().getPrisonCell(cellId);

        getPrisonCellManager().getCurrentlyEditingPrisonCells().put(player.getUniqueId(), prisonCell);

        player.sendMessage(StringUtils.color("&7You have selected and are editing the Prison Cell \"&c" + prisonCell.getCellId() + "&7\"."));
    }
}
