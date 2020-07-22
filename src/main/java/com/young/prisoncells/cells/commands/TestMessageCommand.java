package com.young.prisoncells.cells.commands;

import com.young.prisoncells.utilities.StringUtils;
import com.young.prisoncells.utilities.command.BaseCommand;
import com.young.prisoncells.utilities.command.Command;
import com.young.prisoncells.utilities.command.CommandArgs;
import org.bukkit.Bukkit;

public class TestMessageCommand extends BaseCommand {

    @Command(name = "testmessage", aliases = {"tm"})
    public void onCommand(CommandArgs commandArgs) {
        String[] args = commandArgs.getArgs();
        Bukkit.broadcastMessage(StringUtils.color(org.apache.commons.lang3.StringUtils.join(args, " ", 0, args.length)));
    }
}
