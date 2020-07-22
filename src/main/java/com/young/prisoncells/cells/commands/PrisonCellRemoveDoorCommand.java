package com.young.prisoncells.cells.commands;

import com.young.prisoncells.cells.PrisonCell;
import com.young.prisoncells.cells.objects.PrisonDoor;
import com.young.prisoncells.utilities.StringUtils;
import com.young.prisoncells.utilities.command.BaseCommand;
import com.young.prisoncells.utilities.command.Command;
import com.young.prisoncells.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;

import java.util.Set;

public class PrisonCellRemoveDoorCommand extends BaseCommand {

    @Command(name = "prisoncell.removedoor",
            aliases = {"prisoncells.removedoor", "pc.removedoor", "cell.removedoor", "cells.removedoor", "prisoncell.deldoor", "prisoncells.deldoor", "pc.deldoor", "cell.deldoor", "cells.deldoor"},
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

                    if (targetBlock.getType().toString().contains("DOOR")) {
                        player.sendMessage(ChatColor.RED + "You must be targeting a door.");
                        return;
                    }

                    Door door = (Door) targetBlock.getState().getData();

                    if (door == null) {
                        player.sendMessage(ChatColor.RED + "You must be targeting a door.");
                        return;
                    }

                    if (!door.isTopHalf()) {
                        player.sendMessage(ChatColor.RED + "Please target the top half of the Prison Door.");
                        return;
                    }

                    PrisonDoor prisonDoor = new PrisonDoor(targetBlock.getLocation(), targetBlock.getRelative(BlockFace.DOWN).getLocation());

                    if (!prisonCell.getPrisonDoors().contains(prisonDoor)) {
                        player.sendMessage(ChatColor.RED + "This Prison Door location is not apart of this cell.");
                        return;
                    }

                    prisonCell.getPrisonDoors().remove(prisonDoor);

                    player.sendMessage(StringUtils.color("&7Successfully removed that Prison Door from the Prison Cell \"&c" + prisonCell.getCellId() + "&7\"."));
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

        if (targetBlock.getType().toString().contains("DOOR")) {
            player.sendMessage(ChatColor.RED + "You must be targeting a door.");
            return;
        }

        Door door = (Door) targetBlock.getState().getData();

        if (door == null) {
            player.sendMessage(ChatColor.RED + "You must be targeting a door.");
            return;
        }

        if (!door.isTopHalf()) {
            player.sendMessage(ChatColor.RED + "Please target the top half of the Prison Door.");
            return;
        }

        PrisonDoor prisonDoor = new PrisonDoor(targetBlock.getLocation(), targetBlock.getRelative(BlockFace.DOWN).getLocation());

        if (!prisonCell.getPrisonDoors().contains(prisonDoor)) {
            player.sendMessage(ChatColor.RED + "This Prison Door location is not apart of this cell.");
            return;
        }

        prisonCell.getPrisonDoors().remove(prisonDoor);

        player.sendMessage(StringUtils.color("&7Successfully removed that Prison Door from the Prison Cell \"&c" + prisonCell.getCellId() + "&7\"."));
    }
}
