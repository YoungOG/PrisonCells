package com.young.prisoncells.cells.gui;

import com.young.prisoncells.PrisonCells;
import com.young.prisoncells.cells.PrisonCell;
import com.young.prisoncells.cells.PrisonCellManager;
import com.young.prisoncells.utilities.DateUtil;
import com.young.prisoncells.utilities.ItemBuilder;
import com.young.prisoncells.utilities.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class GuiManager {

    private PrisonCells main = PrisonCells.getInstance();
    private PrisonCellManager prisonCellManager = PrisonCells.getInstance().getPrisonCellManager();

    private static final ItemStack FILLER_GLASS_PANE = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name("&r").build();

    public GuiManager() {

    }

    public void showCellPurchaseMenu(Player player, PrisonCell prisonCell) {
        Inventory inventory = Bukkit.createInventory(player, InventoryType.HOPPER, ChatColor.translateAlternateColorCodes('&', "&1&lPurchase Cell"));

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, FILLER_GLASS_PANE);

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    inventory.setItem(2,
                            new ItemBuilder(Material.EMERALD)
                                    .name("&a&lPurchase Cell: &e" + prisonCell.getCellId())
                                    .addLore("")
                                    .addLore("&7Purchase this cell for &a" + DateUtil.readableTime(prisonCell.getDefaultRentalTime(), false))
                                    .addLore("&7Cost: &a$" + StringUtils.formatDouble(prisonCell.getRentalPrice()) + "&7.").build());
                } catch (Exception ignored) {
                }
            }
        }.runTaskTimerAsynchronously(main, 0L, 10L);

        player.openInventory(inventory);
    }

    public void showCellMenu(Player player, PrisonCell prisonCell) {
        Inventory inventory = Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', "&a&lCell Menu&e&l: " + prisonCell.getCellId()));

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, FILLER_GLASS_PANE);

        new BukkitRunnable() {
            @Override
            public void run() {
                inventory.setItem(1,
                        new ItemBuilder(Material.BOOK)
                                .name("&a&lCell Information")
                                .addLore("")
                                .addLore("&7Time Left: &e" + DateUtil.readableTime(prisonCell.getRemainingRentalTime(), false))
                                .addLore("&7Daily Cost: &e$" + StringUtils.formatDouble(prisonCell.getRentalPrice()))
                        .addLore((prisonCell.getRemainingDays() > 1 ? "&7Total Cost: &e$" + StringUtils.formatDouble(prisonCell.getPreciseRemainingDays() * prisonCell.getRentalPrice()) : "")).build());

                inventory.setItem(3,
                        new ItemBuilder(Material.WATCH)
                                .name("&6&lExtend Rental")
                                .addLore("")
                                .addLore("&7Extend your ownership of this cell.").build());

                inventory.setItem(5,
                        new ItemBuilder(Material.SKULL_ITEM).durability(3).amount(prisonCell.getPrisonCellMembers().size() > 0 ? prisonCell.getPrisonCellMembers().size() : 1)
                                .name("&a&lMember Management")
                                .addLore("")
                                .addLore("&7Manage the members of this cell.").build());

                inventory.setItem(7,
                        new ItemBuilder(Material.BARRIER)
                                .name("&c&lUnclaim Cell")
                                .addLore("")
                                .addLore("&7Unclaim and refund the remaining time?")
                                .addLore("&7Amount Refunded: &c$" + StringUtils.formatDouble(prisonCell.getUnclaimRefund())).build());
            }
        }.runTaskTimerAsynchronously(main, 0L, 10L);

        player.openInventory(inventory);
    }

    public void showRentalExtentionMenu(Player player, PrisonCell prisonCell) {
        Inventory inventory = Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', "&6&lExtend Rental&e&l: " + prisonCell.getCellId()));

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, FILLER_GLASS_PANE);

        for (int i = 1; i < 8; i++)
            inventory.setItem(i,
                    new ItemBuilder(Material.PAPER)
                            .name("&a&lExtend By &e&l" + i + " &a&lDay" + (prisonCell.isVacant() ? "s" : ""))
                            .addLore("")
                            .addLore("&7Total Cost: &e$" + StringUtils.formatDouble(prisonCell.getRentalPrice() * i))
                            .addLore("&7Daily Cost: &e$" + StringUtils.formatDouble(prisonCell.getRentalPrice())).build());

        player.openInventory(inventory);
    }

    public void showMemberManagementMenu(Player player, PrisonCell prisonCell) {
        Inventory inventory = Bukkit.createInventory(player, 54, ChatColor.translateAlternateColorCodes('&', "&a&lMember Management&7&l(&e&l" + prisonCell.getPrisonCellMembers().size() + "&7&l): &e&l" + prisonCell.getCellId()));

        for (UUID memberUniqueId : prisonCell.getPrisonCellMembers())
            inventory.addItem(new ItemBuilder(Material.SKULL_ITEM)
                    .durability(3)
                    .owner(Bukkit.getOfflinePlayer(memberUniqueId))
                    .name("&5" + Bukkit.getOfflinePlayer(memberUniqueId).getName())
                    .addLore("")
                    .addLore("&7&nClick&r &7to remove this member.").build());

        player.openInventory(inventory);
    }

    public void showMemberRemoveConfirmationMenu(Player player, String target) {
        Inventory inventory = Bukkit.createInventory(player, InventoryType.HOPPER, ChatColor.translateAlternateColorCodes('&', "&c&lKick " + target + "?"));

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, FILLER_GLASS_PANE);

        inventory.setItem(1, new ItemBuilder(Material.EMERALD_BLOCK)
                .name("&a&lYes")
                .addLore("")
                .addLore("&7Remove this member.").build());

        inventory.setItem(3, new ItemBuilder(Material.BARRIER)
                .name("&c&lNo")
                .addLore("")
                .addLore("&7Do not remove this member.").build());

        player.openInventory(inventory);
    }

    public void showCellUnclaimMenu(Player player, PrisonCell prisonCell) {
        Inventory inventory = Bukkit.createInventory(player, InventoryType.HOPPER, ChatColor.translateAlternateColorCodes('&', "&c&lUnclaim: " + prisonCell.getCellId() + "?"));

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, FILLER_GLASS_PANE);

        new BukkitRunnable() {
            @Override
            public void run() {
                inventory.setItem(1, new ItemBuilder(Material.EMERALD_BLOCK)
                        .name("&a&lYes")
                        .addLore("")
                        .addLore("&7Unclaim this Cell.")
                        .addLore("&7You will only receive: &e$" + StringUtils.formatDouble(prisonCell.getUnclaimRefund())).build());

                inventory.setItem(3, new ItemBuilder(Material.BARRIER)
                        .name("&c&lNo")
                        .addLore("")
                        .addLore("&7Do not unclaim this Cell.").build());
            }
        }.runTaskTimerAsynchronously(main, 0L, 10L);

        player.openInventory(inventory);
    }
}
