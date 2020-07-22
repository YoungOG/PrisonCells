package com.young.prisoncells.cells.commands;

import com.young.prisoncells.cells.PrisonCell;
import com.young.prisoncells.utilities.StringUtils;
import com.young.prisoncells.utilities.command.BaseCommand;
import com.young.prisoncells.utilities.command.Command;
import com.young.prisoncells.utilities.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PrisonCellSetTenantCommand extends BaseCommand {

    @Command(name = "prisoncell.settenant",
            aliases = {"prisoncells.settenant", "pc.settenant", "cell.settenant", "cells.settenant", "prisoncell.settenant", "prisoncells.settenant", "pc.settenant", "cell.settenant", "cells.settenant"},
            permission = "prisoncell.command.administrator",
            inGameOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        String[] args = commandArgs.getArgs();
        Player player = commandArgs.getPlayer();

        if (args.length == 1) {
            String name = args[0];

            if (getPrisonCellManager().getCurrentlyEditingPrisonCells().containsKey(player.getUniqueId())) {
                if (getPrisonCellManager().getCurrentlyEditingPrisonCells().get(player.getUniqueId()) != null) {
                    PrisonCell prisonCell = getPrisonCellManager().getCurrentlyEditingPrisonCells().get(player.getUniqueId());

                    if (name.equalsIgnoreCase("none")) {
                        prisonCell.setTenant(null);
                        player.sendMessage(StringUtils.color("&7You have removed the tenant from the \"&c" + prisonCell.getCellId() + "&7\" Prison Cell."));
                        return;
                    }

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);

                    if (offlinePlayer == null || offlinePlayer.getUniqueId() == null) {
                        player.sendMessage(ChatColor.RED + "That player could not be found.");
                        return;
                    }

                    prisonCell.claimCell(offlinePlayer.getUniqueId());
                    player.sendMessage(StringUtils.color("&7You have set \"&c" + offlinePlayer.getName() + "&7\" as the tenant of the \"&c" + prisonCell.getCellId() + "&7\" Prison Cell ."));
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have a Prison Cell currently selected.\nPlease either select a cell or specify the cell ID.");
                }
            } else
                player.sendMessage(ChatColor.RED + "You do not have a Prison Cell currently selected.\nPlease either select a cell or specify the cell ID.");
        } else if (args.length == 2) {
            String cellId = args[0];
            String name = args[1];

            if (getPrisonCellManager().getPrisonCell(cellId) == null) {
                player.sendMessage(ChatColor.RED + "There is not a Prison Cell with that cell ID.");
                return;
            }

            PrisonCell prisonCell = getPrisonCellManager().getPrisonCell(cellId);

            if (name.equalsIgnoreCase("none")) {
                prisonCell.setTenant(null);
                player.sendMessage(StringUtils.color("&7You have removed the tenant from the \"&c" + prisonCell.getCellId() + "&7\" Prison Cell."));
                return;
            }

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);

            if (offlinePlayer == null || offlinePlayer.getUniqueId() == null) {
                player.sendMessage(ChatColor.RED + "That player could not be found.");
                return;
            }

            prisonCell.claimCell(offlinePlayer.getUniqueId());
            player.sendMessage(StringUtils.color("&7You have set \"&c" + offlinePlayer.getName() + "&7\" as the tenant of the \"&c" + prisonCell.getCellId() + "&7\" Prison Cell ."));
        } else
            player.sendMessage(ChatColor.RED + "Usage: /prisoncell settenant (cellId) [name/none]");
    }
}
