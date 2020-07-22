package com.young.prisoncells.cells.gui;

import com.young.prisoncells.PrisonCells;
import com.young.prisoncells.cells.PrisonCell;
import com.young.prisoncells.cells.PrisonCellManager;
import com.young.prisoncells.utilities.DateUtil;
import com.young.prisoncells.utilities.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class GuiListeners implements Listener {

    private PrisonCells main = PrisonCells.getInstance();
    private PrisonCellManager prisonCellManager = main.getPrisonCellManager();
    private GuiManager guiManager = main.getGuiManager();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String titleName = ChatColor.stripColor(event.getInventory().getTitle());
        ItemStack itemStack = event.getCurrentItem();

        if (itemStack == null)
            return;

        if (titleName.contains("Purchase Cell")) {
            event.setCancelled(true);

            if (itemStack.getType() == Material.EMERALD) {
                PrisonCell prisonCell = prisonCellManager.getPrisonCell(ChatColor.stripColor(itemStack.getItemMeta().getDisplayName().replace(" ", "")).split(":")[1]);

                if (prisonCell == null) {
                    player.closeInventory();
                    return;
                }

                prisonCell.claimCell(player.getUniqueId());
                player.closeInventory();
            }
        }

        if (titleName.contains("Cell Menu")) {
            event.setCancelled(true);

            PrisonCell prisonCell = prisonCellManager.getPrisonCell(titleName.replace(" ", "").split(":")[1]);

            if (prisonCell == null) {
                player.closeInventory();
                return;
            }

            if (itemStack.getType() == Material.WATCH) {
                player.closeInventory();
                guiManager.showRentalExtentionMenu(player, prisonCell);
            }

            if (itemStack.getType() == Material.SKULL_ITEM) {
                player.closeInventory();
                guiManager.showMemberManagementMenu(player, prisonCell);
            }

            if (itemStack.getType() == Material.BARRIER) {
                player.closeInventory();
                guiManager.showCellUnclaimMenu(player, prisonCell);
            }
        }

        if (titleName.contains("Extend Rental")) {
            event.setCancelled(true);

            PrisonCell prisonCell = prisonCellManager.getPrisonCell(titleName.replace(" ", "").split(":")[1]);

            if (prisonCell == null) {
                player.closeInventory();
                return;
            }

            if (itemStack.getType() == Material.PAPER) {
                int slot = event.getSlot();

                if (slot >= 0 && slot <= 7) {
                    player.closeInventory();

                    if ((prisonCell.getRentalTime() + DateUtil.parseString(slot + "day")) > (prisonCell.getMaxRentalTime() + System.currentTimeMillis())) {
                        player.sendMessage(StringUtils.color("&cYou cannot extend the rental period past " + DateUtil.readableTime(prisonCell.getMaxRentalTime(), false)));
                        player.closeInventory();
                        return;
                    }

                    if (main.getEcononmy().isEnabled()) {
                        if (main.getEcononmy().getBalance(Bukkit.getOfflinePlayer(player.getUniqueId())) >= prisonCell.getRentalPrice() * slot) {
                            main.getEcononmy().withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), prisonCell.getRentalPrice() * slot);
                            prisonCell.setRentalTime(prisonCell.getRentalTime() + DateUtil.parseString(slot + "day"));
                            player.closeInventory();
                            player.sendMessage(StringUtils.color("&aYou have extended the rental period by &e" + slot + " &aday" + (slot > 1 ? "s" : "") + ". Remaining rental period: &e" + DateUtil.readableTime(prisonCell.getRemainingRentalTime(), false)));
                        } else {
                            player.closeInventory();
                            player.sendMessage(StringUtils.color("&cYou need at least " + prisonCell.getRentalPrice() * slot + " to extend the rental period by " + slot + " day" + (slot > 1 ? "s" : "") + "."));
                        }
                    } else {
                        player.closeInventory();
                        player.sendMessage(StringUtils.color("&cThe economy is not currently enabled."));
                    }
                }
            }
        }

        if (titleName.contains("Member Management")){
            event.setCancelled(true);

            PrisonCell prisonCell = prisonCellManager.getPrisonCell(titleName.replace(" ", "").split(":")[1]);

            if (prisonCell == null) {
                player.closeInventory();
                return;
            }

            if (itemStack.getType() == Material.SKULL_ITEM) {
                SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

                player.closeInventory();
                guiManager.showMemberRemoveConfirmationMenu(player, skullMeta.getOwner());
            }
        }

        if (titleName.contains("Kick")) {
            event.setCancelled(true);

            String name = titleName.replace("?", "").split(" ")[1];
            OfflinePlayer target = Bukkit.getOfflinePlayer(name);

            if (target == null) {
                player.closeInventory();
                player.sendMessage(ChatColor.RED + "That player could not be found.");
                return;
            }

            PrisonCell prisonCell = prisonCellManager.getPrisonCell(player);

            if (prisonCell == null) {
                player.closeInventory();
                return;
            }

            if (itemStack.getType() == Material.BARRIER) {
                player.closeInventory();
                guiManager.showMemberManagementMenu(player, prisonCell);
                return;
            }

            if (itemStack.getType() == Material.EMERALD_BLOCK) {
                prisonCell.getPrisonCellMembers().remove(target.getUniqueId());

                player.sendMessage(StringUtils.color("&aSuccessfully removed " + target.getName() + " as a member of your Prison Cell."));

                if (target.isOnline())
                    ((Player) target).sendMessage(StringUtils.color("&cYou are no longer a member of " + player.getName() + "'s Prison Cell."));

                player.closeInventory();
            }
        }

        if (titleName.contains("Unclaim")) {
            event.setCancelled(true);

            String name = titleName.replace("?", "").split(" ")[1];

            PrisonCell prisonCell = prisonCellManager.getPrisonCell(name);

            if (prisonCell == null) {
                player.closeInventory();
                return;
            }

            if (itemStack.getType() == Material.BARRIER) {
                player.closeInventory();
                guiManager.showCellMenu(player, prisonCell);
                return;
            }

            if (itemStack.getType() == Material.EMERALD_BLOCK) {
                player.closeInventory();

                prisonCell.unclaimCell();
            }
        }
    }
}
