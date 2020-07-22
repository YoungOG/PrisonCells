package com.young.prisoncells.cells.commands;

import com.young.prisoncells.cells.PrisonCell;
import com.young.prisoncells.utilities.JSONMessage;
import com.young.prisoncells.utilities.StringUtils;
import com.young.prisoncells.utilities.command.BaseCommand;
import com.young.prisoncells.utilities.command.Command;
import com.young.prisoncells.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PrisonCellListCommand extends BaseCommand {

    @Command(name = "prisoncell.list",
            aliases = {"prisoncells.list", "pc.list", "cell.list", "cells.list", "prisoncell.list", "prisoncells.list", "pc.list", "cell.list", "cells.list"},
            permission = "prisoncell.command.administrator")
    public void onCommand(CommandArgs commandArgs) {
        CommandSender sender = commandArgs.getSender();
        Player player = commandArgs.getPlayer();

        if (getPrisonCellManager().getPrisonCells().size() <= 0) {
            sender.sendMessage(ChatColor.RED + "There currently are no prison cells setup.");
            return;
        }

        ArrayList<PrisonCell> prisonCells = new ArrayList<>(getPrisonCellManager().getPrisonCells());

        if (sender instanceof Player) {
            player.sendMessage(StringUtils.color("&7Available Prison Cells(&c" + getPrisonCellManager().getPrisonCells().size() + "&7):"));

            JSONMessage message = JSONMessage.create();
            for (int i = 0; i < prisonCells.size(); i++) {
                PrisonCell prisonCell = prisonCells.get(i);

                if (i < prisonCells.size() - 1)
                    message.then(StringUtils.color("&b" + prisonCell.getCellId()))
                            .tooltip(prisonCell.getPrisonCellInformation())
                            .then(StringUtils.color("&7, "));
                else
                    message.then(StringUtils.color("&b" + prisonCell.getCellId()))
                            .tooltip(prisonCell.getPrisonCellInformation());
            }

            message.send(player);
        } else {
            sender.sendMessage(StringUtils.color("&7Available Prison Cells(&c" + getPrisonCellManager().getPrisonCells().size() + "&7):"));

            List<String> cellIds = new ArrayList<>();
            for (PrisonCell prisonCell : prisonCells)
                cellIds.add(StringUtils.color("&b" + prisonCell.getCellId()));

            sender.sendMessage(StringUtils.color(cellIds.toString().replace("[", "").replace("]", "").replace(",", "&7,")));
        }
    }
}
