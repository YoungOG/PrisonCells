package com.young.prisoncells.cells.commands;

import com.young.prisoncells.cells.PrisonCell;
import com.young.prisoncells.utilities.StringUtils;
import com.young.prisoncells.utilities.command.BaseCommand;
import com.young.prisoncells.utilities.command.Command;
import com.young.prisoncells.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PrisonCellLeaveCommand extends BaseCommand {

    @Command(name = "prisoncell.leave",
            aliases = {"prisoncells.leave", "pc.leave", "cell.leave", "cells.leave", "prisoncell.leave", "prisoncells.leave", "pc.leave", "cell.leave", "cells.leave"})
    public void onCommand(CommandArgs commandArgs) {
        String[] args = commandArgs.getArgs();
        Player player = commandArgs.getPlayer();

        if (args.length == 0) {
            PrisonCell prisonCell = getPrisonCellManager().getPrisonCell(player);

            if (prisonCell == null) {
                player.sendMessage(ChatColor.RED + "You are not apart of a Prison Cell.");
                return;
            }

            if (!prisonCell.getTenant().equals(player.getUniqueId())) {
                prisonCell.getPrisonCellMembers().remove(player.getUniqueId());

                player.sendMessage(StringUtils.color("&7You have left the \"&c" + prisonCell.getCellId() + "&7\" Prison Cell."));
            } else {
                prisonCell.unclaimCell();
                player.sendMessage(StringUtils.color("&7You have unclaimed this prison cell."));
            }
        }
    }
}
