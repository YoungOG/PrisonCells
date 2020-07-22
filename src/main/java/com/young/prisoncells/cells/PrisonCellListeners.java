package com.young.prisoncells.cells;

import com.young.prisoncells.PrisonCells;
import com.young.prisoncells.cells.gui.GuiManager;
import com.young.prisoncells.cells.objects.PrisonRegion;
import com.young.prisoncells.utilities.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PrisonCellListeners implements Listener {

    private PrisonCells main = PrisonCells.getInstance();
    private PrisonCellManager prisonCellManager = main.getPrisonCellManager();
    private GuiManager guiManager = main.getGuiManager();
    private Material wandMaterial;

    public PrisonCellListeners() {
        wandMaterial = Material.valueOf(main.getConfig().getString("regions.wand-material", Material.BONE.toString()));
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() != null && event.getItem().getType() == wandMaterial && event.getClickedBlock() != null && player.hasPermission("prisoncells.administrator") && player.getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(true);

            PrisonRegion region = PrisonRegion.getPrisonRegionFromUser(player);

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Location location = event.getClickedBlock().getLocation();
                region.setCorner1(location);
                player.sendMessage(StringUtils.color("&7Point 1: &c" + location.getWorld().getName() + " &7- (X&c: " + location.getBlockX() + "&7, Y&c: " + location.getBlockY() + "&7, Z&c: " + location.getBlockZ() + "&7) has been selected."));
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Location location = event.getClickedBlock().getLocation();
                region.setCorner2(location);
                player.sendMessage(StringUtils.color("&7Point 2: &c" + location.getWorld().getName() + " &7- (X&c: " + location.getBlockX() + "&7, Y&c: " + location.getBlockY() + "&7, Z&c: " + location.getBlockZ() + "&7) has been selected."));
            }
        }

        if (event.getClickedBlock() != null) {
            Block clickedBlock = event.getClickedBlock();

            if (clickedBlock.getType() == Material.SIGN_POST || clickedBlock.getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) clickedBlock.getState();
                PrisonCell prisonCell = prisonCellManager.getPrisonCellFromSignLocation(sign.getLocation());

                if (prisonCell == null)
                    return;

                if (prisonCell.isVacant())
                    guiManager.showCellPurchaseMenu(player, prisonCell);
                else if (prisonCell.getTenant().equals(player.getUniqueId()))
                    guiManager.showCellMenu(player, prisonCell);
            }

            if (clickedBlock.getType().toString().contains("DOOR") && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                PrisonCell prisonCell = prisonCellManager.getPrisonCellFromDoorLocation(clickedBlock.getLocation());

                if (prisonCell == null)
                    return;

                if (prisonCell.getTenant() == null)
                    return;

                if (!prisonCell.getTenant().equals(player.getUniqueId()) || !prisonCell.getPrisonCellMembers().contains(player.getUniqueId()))
                    return;

                prisonCell.getPrisonDoorFromLocation(clickedBlock.getLocation()).setOpen(prisonCell.getPrisonDoorFromLocation(clickedBlock.getLocation()).isOpen());
            }

            if (clickedBlock.getType().toString().contains("CHEST") && event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            }
        }
    }
}
