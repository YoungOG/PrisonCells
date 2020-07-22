package com.young.prisoncells.cells.commands;

import com.young.prisoncells.cells.PrisonCell;
import com.young.prisoncells.utilities.StringUtils;
import com.young.prisoncells.utilities.command.BaseCommand;
import com.young.prisoncells.utilities.command.Command;
import com.young.prisoncells.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PrisonCellInformationCommand extends BaseCommand {

    @Command(name = "prisoncell.information",
            aliases = {"prisoncells.information", "pc.information", "cell.information", "cells.information", "prisoncell.info", "prisoncells.info", "pc.info", "cell.info", "cells.info"})
    public void onCommand(CommandArgs commandArgs) {
        String[] args = commandArgs.getArgs();
        Player player = commandArgs.getPlayer();

        if (args.length == 0) {
            if (player.hasPermission("prisoncells.command.administrator")) {
                if (getPrisonCellManager().getCurrentlyEditingPrisonCells().containsKey(player.getUniqueId())) {
                    if (getPrisonCellManager().getCurrentlyEditingPrisonCells().get(player.getUniqueId()) != null) {
                        PrisonCell prisonCell = getPrisonCellManager().getCurrentlyEditingPrisonCells().get(player.getUniqueId());

                        for (String line : prisonCell.getPrisonCellInformationLines())
                            player.sendMessage(StringUtils.color(line));

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
        }

        if (!player.hasPermission("prisoncells.commands.administrator")) {
            if (getPrisonCellManager().getPrisonCell(player.getUniqueId()) == null) {
                player.sendMessage("&cYou do not own or are apart of any Prison Cells.");
                return;
            }

            PrisonCell prisonCell = getPrisonCellManager().getPrisonCell(player);

            for (String line : prisonCell.getPrisonCellInformationLines())
                player.sendMessage(StringUtils.color(line));

            return;
        }

        String cellId = args[0];

        if (getPrisonCellManager().getPrisonCell(cellId) == null) {
            player.sendMessage(ChatColor.RED + "There is not a Prison Cell with that cell ID.");
            return;
        }

        PrisonCell prisonCell = getPrisonCellManager().getPrisonCell(cellId);

        for (String line : prisonCell.getPrisonCellInformationLines())
            player.sendMessage(StringUtils.color(line));
    }
}
