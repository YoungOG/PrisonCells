package com.young.prisoncells.utilities;

import org.bukkit.ChatColor;

import java.text.DecimalFormat;

public class StringUtils {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String formatDouble(double amount) {
        return decimalFormat.format(amount);
    }
}
