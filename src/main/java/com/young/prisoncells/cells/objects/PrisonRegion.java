package com.young.prisoncells.cells.objects;

import com.google.common.collect.Sets;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.young.prisoncells.utilities.LocationSerialization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PrisonRegion {

    public static HashMap<UUID, PrisonRegion> regionSelectionUsers = new HashMap<>();
    private Location corner1, corner2;
    private HashSet<ProtectedRegion> protectedRegions = Sets.newHashSet();

    public PrisonRegion(Document document) {
        this.corner1 = LocationSerialization.deserializeLocation(document.getString("corner1"));
        this.corner2 = LocationSerialization.deserializeLocation(document.getString("corner2"));
    }

    public Document toDocument() {
        return new Document("corner1", LocationSerialization.serializeLocation(corner1))
                .append("corner2", LocationSerialization.serializeLocation(corner2));
    }

    public boolean isInside(Location location) {
        if (corner1 != null && corner2 != null) {
            int minX = min(corner1.getBlockX(), corner2.getBlockX()),
                    minY = min(corner1.getBlockY(), corner2.getBlockY()),
                    minZ = min(corner1.getBlockZ(), corner2.getBlockZ()),
                    maxX = max(corner1.getBlockX(), corner2.getBlockX()),
                    maxY = max(corner1.getBlockY(), corner2.getBlockY()),
                    maxZ = max(corner1.getBlockZ(), corner2.getBlockZ());

            return (minX <= location.getBlockX() && location.getBlockX() <= maxX && minY <= location.getBlockY() && location.getBlockY() <= maxY && minZ <= location.getBlockZ() && location.getBlockZ() <= maxZ);
        }

        return false;
    }

    public int min(final int a, final int b) {
        if (a < b)
            return a;
        return b;
    }

    public int max(final int a, final int b) {
        if (a > b)
            return a;
        return b;
    }

    public static PrisonRegion getPrisonRegionFromUser(final Player player) {
        return getPrisonRegionFromUser(player.getUniqueId());
    }

    public static PrisonRegion getPrisonRegionFromUser(final UUID uniqueId) {
        if (regionSelectionUsers.containsKey(uniqueId))
            return regionSelectionUsers.get(uniqueId);
        else {
            PrisonRegion region = new PrisonRegion();
            regionSelectionUsers.put(uniqueId, region);

            return region;
        }
    }

//    public Set<ProtectedRegion> getProtectedRegions(Location location) {
//        RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld()));
//
////get the regions at a given location
//        Collection<ProtectedRegion> collection = rm.getRegions().values();
//
//        for ( ProtectedRegion region : collection) {
//
//        }
//
//        return;
//    }
}