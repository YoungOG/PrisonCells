package com.young.prisoncells.cells.commands;

import com.young.prisoncells.cells.PrisonCell;
import com.young.prisoncells.utilities.StringUtils;
import com.young.prisoncells.utilities.command.BaseCommand;
import com.young.prisoncells.utilities.command.Command;
import com.young.prisoncells.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PrisonCellRemoveCommand extends BaseCommand {

    @Command(name = "prisoncell.remove",
            aliases = {"prisoncells.remove", "pc.remove", "cell.remove", "cells.remove"},
            permission = "prisoncell.command.administrator",
            inGameOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        String[] args = commandArgs.getArgs();
        Player player = commandArgs.getPlayer();

        if (args.length == 0) {
            if (getPrisonCellManager().getCurrentlyEditingPrisonCells().containsKey(player.getUniqueId())) {
                if (getPrisonCellManager().getCurrentlyEditingPrisonCells().get(player.getUniqueId()) != null) {
                    PrisonCell prisonCell = getPrisonCellManager().getCurrentlyEditingPrisonCells().get(player.getUniqueId());
                    prisonCell.remove();


                    getPrisonCellManager().getCurrentlyEditingPrisonCells().remove(player.getUniqueId());
                    getPrisonCellManager().getCurrentlyEditingPrisonCells().put(player.getUniqueId(), prisonCell);

                    player.sendMessage(StringUtils.color("&7You have removed the \"&c" + prisonCell.getCellId() + "&7\" Prison Cell."));
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
        prisonCell.remove();

        if (getPrisonCellManager().getCurrentlyEditingPrisonCells().containsKey(player.getUniqueId()))
            if (getPrisonCellManager().getCurrentlyEditingPrisonCells().get(player.getUniqueId()).equals(prisonCell))
                getPrisonCellManager().getCurrentlyEditingPrisonCells().remove(player.getUniqueId());

        player.sendMessage(StringUtils.color("&7You have removed the \"&c" + prisonCell.getCellId() + "&7\" Prison Cell."));
    }
}
