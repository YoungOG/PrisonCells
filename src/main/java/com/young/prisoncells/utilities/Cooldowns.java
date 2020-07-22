package com.young.prisoncells.utilities;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.UUID;

public class Cooldowns {

	public static Table<UUID, String, Long> cooldowns = HashBasedTable.create();

	public static long getCooldown(UUID id, String key) {
		return calculateRemainder(cooldowns.get(id, key));
	}

	public static long setCooldown(UUID id, String key, long delay) {
		return calculateRemainder(cooldowns.put(id, key, System.currentTimeMillis() + delay));
	}

	public static boolean tryCooldown(UUID id, String key, long delay) {
		if (getCooldown(id, key) <= 0) {
			setCooldown(id, key, delay);
			return true;
		}

		return false;
	}

	public static void removeCooldowns(UUID id) {
		cooldowns.row(id).clear();
	}

	private static long calculateRemainder(Long expireTime) {
		return expireTime != null ? expireTime - System.currentTimeMillis() : Long.MIN_VALUE;
	}
}