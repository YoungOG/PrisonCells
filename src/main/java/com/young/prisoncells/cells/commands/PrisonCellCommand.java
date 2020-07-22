package com.young.prisoncells.cells.commands;

import com.young.prisoncells.utilities.StringUtils;
import com.young.prisoncells.utilities.command.BaseCommand;
import com.young.prisoncells.utilities.command.Command;
import com.young.prisoncells.utilities.command.CommandArgs;

public class PrisonCellCommand extends BaseCommand {

    private final String[] HELP_LINES = {
            "&7&m================&r&7[ &c&lPrison Cells &7]&m================",
            "&c/cell &7- Displays this help page.",
            "&c/cell information &7- Shows information about your cell.",
            "&c/cell addMember [name] &7- Adds a member to your cell",
            "&c/cell removeMember [name] &7- Removes a member to your cell",
            "&c/cell leave [cell] &7- Vacate the selected cell.",
            "&c/cell list &7- Lists all cells.",
            "&c/cell create [name] &7- Creates and selects a new cell.",
            "&c/cell remove &7- Removes the currently selected cell.",
            "&c/cell select [cell/clear] &7- Selects a cell to edit.",
            "&c/cell setRegion &7- Sets the region for the selected cell.",
            "&c/cell setRentPrice &7- Sets the rent price for the selected cell.",
            "&c/cell addSign &7- Adds a sign to the selected cell.",
            "&c/cell removeSign &7- Removes a sign from the selected cell.",
            "&c/cell addDoor &7- Adds a door to the selected cell.",
            "&c/cell removeDoor &7- Removes a door from the selected cell.",
            "&c/cell evict [player] [cell/all] &7- Evicts the user from their cell.",
            "&c/cell setOwner [cell] [player] &7- Sets a new owner for the cell.",
            "&c/cell reload &7- Reloads the entire plugin."
    };

    @Command(name = "prisoncell", aliases = {"prisoncells", "pc", "cells", "cell"})
    public void onCommand(CommandArgs commandArgs) {
        int max = (commandArgs.getSender().hasPermission("prisoncells.command.administrator") ? (HELP_LINES.length - 1) : 7);

        for (int i = 0; i < max; i++)
            commandArgs.getSender().sendMessage(StringUtils.color(HELP_LINES[i]));
    }
}
