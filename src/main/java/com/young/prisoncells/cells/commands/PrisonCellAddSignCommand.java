package com.young.prisoncells.cells.commands;

import com.google.common.collect.Sets;
import com.young.prisoncells.cells.PrisonCell;
import com.young.prisoncells.utilities.StringUtils;
import com.young.prisoncells.utilities.command.BaseCommand;
import com.young.prisoncells.utilities.command.Command;
import com.young.prisoncells.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.HashSet;
import java.util.Set;

public class PrisonCellAddSignCommand extends BaseCommand {

    @Command(name = "prisoncell.addsign",
            aliases = {"prisoncells.addsign", "pc.addsign", "cell.addsign", "cells.addsign"},
            permission = "prisoncell.command.administrator",
            inGameOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        String[] args = commandArgs.getArgs();
        Player player = commandArgs.getPlayer();

        if (args.length == 0) {
            if (getPrisonCellManager().getCurrentlyEditingPrisonCells().containsKey(player.getUniqueId())) {
                if (getPrisonCellManager().getCurrentlyEditingPrisonCells().get(player.getUniqueId()) != null) {
                    PrisonCell prisonCell = getPrisonCellManager().getCurrentlyEditingPrisonCells().get(player.getUniqueId());
                    Block targetBlock = player.getTargetBlock((Set<Material>) null, 100);

                    if (targetBlock.getType() != Material.WALL_SIGN && targetBlock.getType() != Material.SIGN_POST && targetBlock.getType() != Material.SIGN) {
                        player.sendMessage(ChatColor.RED + "You must be targeting a sign or wall post.");
                        return;
                    }

                    if (prisonCell.getPrisonCellSignLocations().contains(targetBlock.getLocation())) {
                        player.sendMessage(ChatColor.RED + "This sign or wall post location is already apart of this cell.");
                        return;
                    }

                    prisonCell.getPrisonCellSignLocations().add(targetBlock.getLocation());

                    player.sendMessage(StringUtils.color("&7Successfully added that sign to the Prison Cell \"&c" + prisonCell.getCellId() + "&7\"."));
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
        Block targetBlock = player.getTargetBlock((Set<Material>) null, 100);

        if (targetBlock.getType() != Material.WALL_SIGN && targetBlock.getType() != Material.SIGN_POST && targetBlock.getType() != Material.SIGN) {
            player.sendMessage(ChatColor.RED + "You must be targeting a sign or wall post.");
            return;
        }

        if (prisonCell.getPrisonCellSignLocations().contains(targetBlock.getLocation())) {
            player.sendMessage(ChatColor.RED + "This sign or wall post location is already apart of this cell.");
            return;
        }

        prisonCell.getPrisonCellSignLocations().add(targetBlock.getLocation());

        player.sendMessage(StringUtils.color("&7Successfully added that sign to the Prison Cell \"&c" + prisonCell.getCellId() + "&7\"."));
    }
}
