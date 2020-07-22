package com.young.prisoncells.cells;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.UpdateOptions;
import com.young.prisoncells.PrisonCells;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Getter
public class PrisonCellManager {

    private PrisonCells main = PrisonCells.getInstance();
    private MongoCollection<Document> prisonCellCollection = main.getMongoDatabase().getCollection("prison-cells");
    private HashSet<PrisonCell> prisonCells = Sets.newHashSet();
    private HashMap<UUID, PrisonCell> currentlyEditingPrisonCells = Maps.newHashMap();
    private List<String> claimedSignLines, vacantSignLines;
    @Setter
    private int doorOpenTime = 2;

    public PrisonCellManager() {
        load();

        new BukkitRunnable() {
            @Override
            public void run() {
                save();
            }
        }.runTaskTimerAsynchronously(main, 0L, 300*20L);
    }

    private void load() {
        doorOpenTime = main.getConfig().getInt("cells.door-open-time", doorOpenTime);
        claimedSignLines = main.getConfig().getStringList("cells.signs.claimed-lines");
        vacantSignLines = main.getConfig().getStringList("cells.signs.vacant-lines");

        if (prisonCellCollection.countDocuments() > 0)
            for (Document document : prisonCellCollection.find())
                prisonCells.add(new PrisonCell(document));
    }

    public void save() {
        if (prisonCells.size() > 0)
            for (PrisonCell prisonCell : prisonCells)
                prisonCellCollection.replaceOne(Filters.eq("cellId", prisonCell.getCellId()), prisonCell.toDocument(), new UpdateOptions().upsert(true));
    }

    public PrisonCell getPrisonCell(String cellId) {
        for (PrisonCell prisonCell : prisonCells)
            if (prisonCell.getCellId().equalsIgnoreCase(cellId))
                return prisonCell;
        return null;
    }

    public PrisonCell getPrisonCell(Location location) {
        for (PrisonCell prisonCell : prisonCells)
            if (prisonCell.getPrisonRegion() != null && prisonCell.getPrisonRegion().isInside(location))
                return prisonCell;
        return null;
    }

    public PrisonCell getPrisonCell(UUID uniqueId) {
        for (PrisonCell prisonCell : prisonCells)
            if ((prisonCell.getTenant() != null && prisonCell.getTenant().equals(uniqueId)) || prisonCell.getPrisonCellMembers().contains(uniqueId))
                return prisonCell;
        return null;
    }

    public PrisonCell getPrisonCell(Player player) {
        return getPrisonCell(player.getUniqueId());
    }

    public PrisonCell getPrisonCellFromSignLocation(Location location) {
        for (PrisonCell prisonCell : prisonCells)
            if (prisonCell.getPrisonCellSignLocations().contains(location))
                return prisonCell;
        return null;
    }

    public PrisonCell getPrisonCellFromDoorLocation(Location location) {
        for (PrisonCell prisonCell : prisonCells)
            if (prisonCell.getPrisonDoorFromLocation(location) != null)
                return prisonCell;

        return null;
    }

    public HashSet<PrisonCell> getPrisonCells(UUID uniqueId) {
        HashSet<PrisonCell> prisonCellHashSet = Sets.newHashSet();
        for (PrisonCell prisonCell : prisonCells)
            if (prisonCell.getTenant().equals(uniqueId))
                prisonCellHashSet.add(prisonCell);
        return prisonCellHashSet;
    }
}
