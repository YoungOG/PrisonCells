package com.young.prisoncells.utilities.command;

import com.young.prisoncells.PrisonCells;
import com.young.prisoncells.cells.PrisonCellManager;
import lombok.Getter;

@Getter
public class BaseCommand {

    private final PrisonCells main = PrisonCells.getInstance();
    private final PrisonCellManager prisonCellManager = main.getPrisonCellManager();

    public BaseCommand() {
        main.getCommandFramework().registerCommands(this);
    }
}
