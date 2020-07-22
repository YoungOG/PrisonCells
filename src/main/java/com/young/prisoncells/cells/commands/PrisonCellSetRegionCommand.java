package com.young.prisoncells.cells.commands;

import com.young.prisoncells.cells.PrisonCell;
import com.young.prisoncells.cells.objects.PrisonRegion;
import com.young.prisoncells.utilities.StringUtils;
import com.young.prisoncells.utilities.command.BaseCommand;
import com.young.prisoncells.utilities.command.Command;
import com.young.prisoncells.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PrisonCellSetRegionCommand extends BaseCommand {

    @Command(name = "prisoncell.setregion",
            aliases = {"prisoncells.setregion", "pc.setregion", "cell.setregion", "cells.setregion"},
            permission = "prisoncell.command.administrator",
            inGameOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        String[] args = commandArgs.getArgs();
        Player player = commandArgs.getPlayer();

        if (args.length == 0) {
            if (getPrisonCellManager().getCurrentlyEditingPrisonCells().containsKey(player.getUniqueId())) {
                if (getPrisonCellManager().getCurrentlyEditingPrisonCells().get(player.getUniqueId()) != null) {
                    PrisonCell prisonCell = getPrisonCellManager().getCurrentlyEditingPrisonCells().get(player.getUniqueId());

                    if (PrisonRegion.getPrisonRegionFromUser(player) == null || (PrisonRegion.getPrisonRegionFromUser(player).getCorner1() == null || PrisonRegion.getPrisonRegionFromUser(player).getCorner2() == null)) {
                        player.sendMessage(ChatColor.RED + "You do not have a region selected.");
                        return;
                    }

                    prisonCell.setPrisonRegion(PrisonRegion.getPrisonRegionFromUser(player));

                    player.sendMessage(StringUtils.color("&7You have set the region for the Prison Cell \"&c" + prisonCell.getCellId() + "&7\"."));
                    return;
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have a Prison Cell currently selected.\nPlease either select a cell or specify the cell ID.");
                    return;
                }
            } else {
                player.sendMessage(ChatColor.RED + "You do not have a Prison Cell currently selected.\nPlease either select a cell or specify the cell ID.");
                return;
            }
        }

        String cellId = args[0];

        if (getPrisonCellManager().getPrisonCell(cellId) == null) {
            player.sendMessage(ChatColor.RED + "There is not a Prison Cell with that cell ID.");
            return;
        }

        PrisonCell prisonCell = getPrisonCellManager().getPrisonCell(cellId);

        if (PrisonRegion.getPrisonRegionFromUser(player) == null) {
            player.sendMessage(ChatColor.RED + "You do not have a region selected.");
            return;
        }

        prisonCell.setPrisonRegion(PrisonRegion.getPrisonRegionFromUser(player));

        player.sendMessage(StringUtils.color("&7You have set the region for the Prison Cell \"&c" + prisonCell.getCellId() + "&7\"."));
    }
}
