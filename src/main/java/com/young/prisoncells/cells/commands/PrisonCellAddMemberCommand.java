package com.young.prisoncells.cells.commands;

import com.young.prisoncells.cells.PrisonCell;
import com.young.prisoncells.cells.objects.PrisonDoor;
import com.young.prisoncells.utilities.StringUtils;
import com.young.prisoncells.utilities.command.BaseCommand;
import com.young.prisoncells.utilities.command.Command;
import com.young.prisoncells.utilities.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class PrisonCellAddMemberCommand extends BaseCommand {

    @Command(name = "prisoncell.addmember",
            aliases = {"prisoncells.addmember", "pc.addmember", "cell.addmember", "cells.addmember"},
            permission = "prisoncell.command.administrator",
            inGameOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        String[] args = commandArgs.getArgs();
        Player player = commandArgs.getPlayer();

        if (getPrisonCellManager().getPrisonCells().size() <= 0) {
            player.sendMessage(ChatColor.RED + "You are not a tenant of a Prison Cell.");
            return;
        }

        PrisonCell prisonCell = new ArrayList<>(getPrisonCellManager().getPrisonCells(player.getUniqueId())).get(0);

        if (prisonCell == null) {
            player.sendMessage(ChatColor.RED + "You are not a tenant of a Prison Cell.");
            return;
        }

        if (args[0].equalsIgnoreCase(player.getName())) {
            player.sendMessage(ChatColor.RED + "You cannot add yourself as a member. Nice try.");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player could not be found.");
            return;
        }

        if (prisonCell.getPrisonCellMembers().contains(target.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "That player is already a a member of your cell.");
            return;
        }

        prisonCell.getPrisonCellMembers().add(target.getUniqueId());
        player.sendMessage(StringUtils.color("&7You have added &c" + target.getName() + " &7to your Prison Cell."));
        target.sendMessage(StringUtils.color("&7You have been added to &c" + player.getName() + "&7's Prison Cell."));
    }
}
