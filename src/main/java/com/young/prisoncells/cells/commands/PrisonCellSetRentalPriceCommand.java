package com.young.prisoncells.cells.commands;

import com.young.prisoncells.cells.PrisonCell;
import com.young.prisoncells.cells.objects.PrisonRegion;
import com.young.prisoncells.utilities.StringUtils;
import com.young.prisoncells.utilities.command.BaseCommand;
import com.young.prisoncells.utilities.command.Command;
import com.young.prisoncells.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PrisonCellSetRentalPriceCommand extends BaseCommand {

    @Command(name = "prisoncell.setrental",
            aliases = {"prisoncells.setrental", "pc.setrental", "cell.setrental", "cells.setrental", "prisoncell.setrent", "prisoncells.setrent", "pc.setrent", "cell.setrent", "cells.setrent"},
            permission = "prisoncell.command.administrator",
            inGameOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        String[] args = commandArgs.getArgs();
        Player player = commandArgs.getPlayer();

        if (args.length == 1) {
            if (getPrisonCellManager().getCurrentlyEditingPrisonCells().containsKey(player.getUniqueId())) {
                if (getPrisonCellManager().getCurrentlyEditingPrisonCells().get(player.getUniqueId()) != null) {
                    PrisonCell prisonCell = getPrisonCellManager().getCurrentlyEditingPrisonCells().get(player.getUniqueId());

                    try {
                        double amount = Double.valueOf(args[0]);

                        prisonCell.setRentalPrice(amount);

                        player.sendMessage(StringUtils.color("&7You have set the rental price to &c$" + amount + " &7for the Prison Cell \"&c" + prisonCell.getCellId() + "&7\"."));
                    } catch (NumberFormatException ignored) {
                        player.sendMessage(ChatColor.RED + "Please enter a valid double amount.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have a Prison Cell currently selected.\nPlease either select a cell or specify the cell ID.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You do not have a Prison Cell currently selected.\nPlease either select a cell or specify the cell ID.");
            }
        } else if (args.length == 2) {
            PrisonCell prisonCell = getPrisonCellManager().getPrisonCell(args[0]);

            if (prisonCell == null) {
                player.sendMessage(ChatColor.RED + "A Prison Cell could not be found by that cell ID.");
                return;
            }

            try {
                double amount = Double.valueOf(args[1]);

                prisonCell.setRentalPrice(amount);

                player.sendMessage(StringUtils.color("&7You have set the rental price to &c$" + amount + " &7for the Prison Cell \"&c" + prisonCell.getCellId() + "&7\"."));
            } catch (NumberFormatException ignored) {
                player.sendMessage(ChatColor.RED + "Please enter a valid double amount.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /prisoncell setrental (name) (amount) Note: arguments aren't required");
        }
    }
}
