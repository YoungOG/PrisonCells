package com.young.prisoncells.cells;

import com.google.common.collect.Sets;
import com.mongodb.client.MongoCollection;
import com.young.prisoncells.PrisonCells;
import com.young.prisoncells.cells.objects.PrisonDoor;
import com.young.prisoncells.cells.objects.PrisonRegion;
import com.young.prisoncells.utilities.DateUtil;
import com.young.prisoncells.utilities.JSONMessage;
import com.young.prisoncells.utilities.LocationSerialization;
import com.young.prisoncells.utilities.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PrisonCell {

    private final PrisonCells main = PrisonCells.getInstance();
    private final PrisonCellManager prisonCellManager = main.getPrisonCellManager();

    private final String cellId;
    private UUID tenant;
    private PrisonRegion prisonRegion;
    private double rentalPrice;
    private long rentalTime, maxRentalTime, defaultRentalTime;
    private HashSet<Location> prisonCellSignLocations = Sets.newHashSet();
    private HashSet<PrisonDoor> prisonDoors = Sets.newHashSet();
    private HashSet<UUID> prisonCellMembers = Sets.newHashSet();

    //TimeFrame
    //ExpirationDate
    //Permission to open door.
    //Door open time
    //Refund money from remaining time on unclaim
    //Regeneration

    public PrisonCell(String cellId) {
        this.cellId = cellId;
        this.rentalPrice = main.getConfig().getDouble("cells.default-rental-price", 1000.00);

        try {
            maxRentalTime = DateUtil.parseString(main.getConfig().getString("cells.max-rental-time", "28day"));
            defaultRentalTime = DateUtil.parseString(main.getConfig().getString("cells.default-rental-time", "1day"));
            rentalTime = DateUtil.parseDateDiff(main.getConfig().getString("cells.default-rental-time", "1day"), true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        startSignUpdateTask();
    }

    public PrisonCell(Document document) {
        try {
            maxRentalTime = DateUtil.parseString(main.getConfig().getString("cells.max-rental-time", "28day"));
            defaultRentalTime = DateUtil.parseString(main.getConfig().getString("cells.default-rental-time", "1day"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.cellId = document.getString("cellId");

        if (document.getString("tenant") != null)
            this.tenant = UUID.fromString(document.getString("tenant"));

        if (document.getString("prisonRegion") != null)
            this.prisonRegion = new PrisonRegion((Document) document.get("prisonRegion"));

        this.rentalPrice = document.getDouble("rentalPrice");
        this.rentalTime = document.getLong("rentalTime");

        if (document.containsKey("prisonCellSignLocations")) {
            List<String> locationString = (List<String>) document.get("prisonCellSignLocations");

            for (String line : locationString)
                prisonCellSignLocations.add(LocationSerialization.deserializeLocation(line));
        }

        if (document.containsKey("prisonCellDoors")) {
            List<Document> prisonCellDoorDocuments = (List<Document>) document.get("prisonCellDoors");

            for (Document prisonDoorDocument : prisonCellDoorDocuments)
                prisonDoors.add(new PrisonDoor(prisonDoorDocument));
        }

        if (document.containsKey("prisonCellMembers")) {
            List<String> prisonCellMemberUniqueIds = (List<String>) document.get("prisonCellMembers");

            for (String memberLine : prisonCellMemberUniqueIds)
                prisonCellMembers.add(UUID.fromString(memberLine));
        }

        startSignUpdateTask();
    }

    public Document toDocument() {
        Document document = new Document("cellId", cellId);

        if (tenant != null)
            document.append("tenant", tenant.toString());

        if (prisonRegion != null)
            document.append("prisonRegion", prisonRegion.toDocument());

        document.append("rentalPrice", rentalPrice);
        document.append("rentalTime", rentalTime);

        List<String> prisonCellSignLocationStrings = new ArrayList<>();
        for (Location signLocation : prisonCellSignLocations)
            if (signLocation != null)
                prisonCellSignLocationStrings.add(LocationSerialization.serializeLocation(signLocation));

        document.append("prisonCellSignLocations", prisonCellSignLocationStrings);

        List<Document> prisonDoorDocuments = new ArrayList<>();
        for (PrisonDoor prisonDoor : prisonDoors)
            if (prisonDoor.toDocument() != null)
                prisonDoorDocuments.add(prisonDoor.toDocument());

        document.append("prisonCellDoors", prisonDoorDocuments);

        List<String> prisonCellMembertrings = new ArrayList<>();
        for (UUID uniqueId : prisonCellMembers)
            prisonCellMembertrings.add(uniqueId.toString());

        document.append("prisonCellMembers", prisonCellMembertrings);

        return document;
    }

    public void setTenant(UUID uniqueId) {
        this.tenant = uniqueId;

        try {
            rentalTime = DateUtil.parseDateDiff("1day", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getRemainingRentalTime() {
        return rentalTime - System.currentTimeMillis();
    }

    public void claimCell(UUID uniqueId) {
        if (uniqueId == null)
            return;

        if (main.getEcononmy().isEnabled()) {
            if (main.getEcononmy().getBalance(Bukkit.getOfflinePlayer(uniqueId)) >= rentalPrice) {
                main.getEcononmy().withdrawPlayer(Bukkit.getOfflinePlayer(uniqueId), rentalPrice);

                setTenant(uniqueId);
                Bukkit.getPlayer(uniqueId).sendMessage(StringUtils.color("&7You have purchased this cell for &c$" + rentalPrice + "\n&7Remaining rental period: &c" + DateUtil.readableTime(getRemainingRentalTime(), false)));
            } else if (Bukkit.getPlayer(uniqueId) != null)
                Bukkit.getPlayer(uniqueId).sendMessage(ChatColor.RED + "You need at least $" + StringUtils.formatDouble(rentalPrice) + " to claim this Prison Cell for a rental period of " + DateUtil.readableTime(getDefaultRentalTime(), false) + ".");
        } else
            setTenant(uniqueId);
    }

    public void unclaimCell() {
        this.rentalPrice = main.getConfig().getDouble("cells.default-rental-price", 1000.00);

        for (UUID uniqueId : prisonCellMembers)
            if (Bukkit.getPlayer(uniqueId) != null)
                Bukkit.getPlayer(uniqueId).sendMessage(StringUtils.color("&7The Prison Cell you were apart of has been unclaimed by the owner."));

        prisonCellMembers.clear();

        if (tenant != null && main.getConfig().getBoolean("cells.unclaim.refund")) {
            if (main.getEcononmy().isEnabled())
                main.getEcononmy().depositPlayer(Bukkit.getOfflinePlayer(tenant), getUnclaimRefund());

            if (Bukkit.getPlayer(tenant) != null)
                Bukkit.getPlayer(tenant).sendMessage(StringUtils.color("&7You have been refunded &c$" + StringUtils.formatDouble(getUnclaimRefund()) + "."));
        }

        setTenant(null);

        clearSigns();

        for (PrisonDoor prisonDoor : prisonDoors)
            if (prisonDoor.isOpen())
                prisonDoor.setOpen(false);
    }

    public void remove() {
        MongoCollection<Document> prisonCellCollection = main.getMongoDatabase().getCollection("prison-cells");
        Document document = prisonCellCollection.find(new Document("cellId", cellId)).first();

        if (document == null || document.isEmpty())
            return;

        prisonCellCollection.deleteOne(toDocument());

        clearSigns();
    }

    public double getUnclaimRefund() {
        if (main.getConfig().getBoolean("cells.unclaim.refund"))
            return rentalPrice * getRemainingRentalTime() / defaultRentalTime;
        return 0.0;
    }

    public int getRemainingDays() {
        return (int) (getRemainingRentalTime() / (1000 * 60 * 60 * 24));
    }

    public double getPreciseRemainingDays() {
        return (double) (getRemainingRentalTime() / (1000 * 60 * 60 * 24));
    }

    public void clearSigns() {
        for (Location location : prisonCellSignLocations) {
            Block block = location.getBlock();

            if (block == null)
                return;

            if (!(block.getState() instanceof Sign))
                return;

            Sign sign = (Sign) block.getState();

            if (sign == null)
                return;

            for (int i = 0; i < 4; i++)
                sign.setLine(i, "");

            Bukkit.getScheduler().runTask(main, sign::update);
        }
    }

    public JSONMessage getPrisonCellInformation() {
        JSONMessage message = JSONMessage.create();
        for (String line : getPrisonCellInformationLines())
            message.then(StringUtils.color(line) + "\n");
        return message;
    }

    public List<String> getPrisonCellInformationLines() {
        List<String> lines = new ArrayList<>();
        lines.add("&7&m==========&r&7[ &c&l" + cellId + " &7]&m==========");
        lines.add("&7Tenant&c: " + (tenant != null ? Bukkit.getOfflinePlayer(tenant).getName() : "Not being rented"));
        lines.add("&7Rental Price&c: $" + StringUtils.formatDouble(rentalPrice));
        lines.add("&7Remaining Rental Time&c: " + (getRemainingRentalTime() > 0 ? DateUtil.readableTime(getRemainingRentalTime(), false) : "Not being rented"));
        lines.add("&7Prison Region&c: " + (prisonRegion == null ? "Not set" : ""));

        if (prisonRegion != null && (prisonRegion.getCorner1() != null && prisonRegion.getCorner2() != null)) {
            lines.add("&r &r &7Corner 1&c: " + prisonRegion.getCorner1().getWorld().getName() + " &7- (X&c: " + prisonRegion.getCorner1().getBlockX() + "&7, Y&c: " + prisonRegion.getCorner1().getBlockY() + "&7, Y&c: " + prisonRegion.getCorner1().getBlockZ() + "&7)");
            lines.add("&r &r &7Corner 2&c: " + prisonRegion.getCorner2().getWorld().getName() + " &7- (X&c: " + prisonRegion.getCorner2().getBlockX() + "&7, Y&c: " + prisonRegion.getCorner2().getBlockY() + "&7, Y&c: " + prisonRegion.getCorner2().getBlockZ() + "&7)");
        }

        lines.add("&7Prison Members(&c" + prisonCellMembers.size() + "&7):");
        if (prisonCellMembers.size() > 0)
            for (int i = 0; i < prisonCellMembers.size(); i++) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(new ArrayList<>(prisonCellMembers).get(i));
                lines.add("&r &r &7" + (1 + 1) + ". " + (offlinePlayer.isOnline() ? "&a" : "&c") + offlinePlayer.getName());
            }

        lines.add("&7Prison Signs(&c" + prisonCellSignLocations.size() + "&7):");

        if (prisonCellSignLocations.size() > 0)
            for (int i = 0; i < prisonCellSignLocations.size(); i++) {
                Location signLocation = (Location) prisonCellSignLocations.toArray()[i];

                if (signLocation != null)
                    lines.add("&r &r &7" + (i + 1) + ". &c" + signLocation.getWorld().getName() + " &7- (X&c: " + signLocation.getBlockX() + "&7, Y&c: " + signLocation.getBlockY() + "&7, Y&c: " + signLocation.getBlockZ() + "&7)");
            }

        lines.add("&7Prison Doors(&c" + prisonDoors.size() + "&7):");

        if (prisonDoors.size() > 0)
            for (int i = 0; i < prisonDoors.size(); i++) {
                PrisonDoor prisonDoor = (PrisonDoor) prisonDoors.toArray()[i];

                if (prisonDoor != null && (prisonDoor.getTopLocation() != null && prisonDoor.getBottomLocation() != null)) {
                    lines.add("&r &r &7" + (i + 1) + ":");
                    lines.add("&r &r &r &r &7Top Location&c: " + prisonDoor.getTopLocation().getWorld().getName() + " &7- (X&c: " + prisonDoor.getTopLocation().getBlockX() + "&7, Y&c: " + prisonDoor.getTopLocation().getBlockY() + "&7, Y&c: " + prisonDoor.getTopLocation().getBlockZ() + "&7)");
                    lines.add("&r &r &r &r &7Bottom Location&c: " + prisonDoor.getBottomLocation().getWorld().getName() + " &7- (X&c: " + prisonDoor.getBottomLocation().getBlockX() + "&7, Y&c: " + prisonDoor.getBottomLocation().getBlockY() + "&7, Y&c: " + prisonDoor.getBottomLocation().getBlockZ() + "&7)");
                }

            }

        return lines;
    }

    public PrisonDoor getPrisonDoorFromLocation(Location location) {
        for (PrisonDoor prisonDoor : prisonDoors)
            if (prisonDoor.getTopLocation() != null && prisonDoor.getBottomLocation() != null)
                if (prisonDoor.getTopLocation().equals(location) || prisonDoor.getBottomLocation().equals(location))
                    return prisonDoor;

        return null;
    }

    public void startSignUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (prisonCellSignLocations.size() > 0)
                    for (Location location : prisonCellSignLocations) {
                        Block block = location.getBlock();

                        if (block == null)
                            return;

                        if (!(block.getState() instanceof Sign))
                            return;

                        Sign sign = (Sign) block.getState();

                        if (sign == null)
                            return;

                        int index = 0;

                        if (isVacant())
                            for (String signLine : prisonCellManager.getVacantSignLines()) {
                                String signLineFormatted = StringUtils.color(signLine
                                        .replace("{CELL_ID}", cellId)
                                        .replace("{CELL_RENT}", "" + StringUtils.formatDouble(rentalPrice)));
                                sign.setLine(index, signLineFormatted.length() > 16 ? signLineFormatted.substring(16) : signLineFormatted);
                                index++;
                            }
                        else
                            for (String signLine : prisonCellManager.getClaimedSignLines()) {
                                String signLineFormatted = StringUtils.color(signLine
                                        .replace("{CELL_ID}", cellId)
                                        .replace("{CELL_RENT}", "" + StringUtils.formatDouble(rentalPrice))
                                        .replace("{CELL_TENANT}", Bukkit.getOfflinePlayer(tenant).getName())
                                        .replace("{CELL_REMAINING_TIME}", DateUtil.readableTime((getRemainingRentalTime() > 0 ? getRemainingRentalTime() : getDefaultRentalTime()), true)));
                                sign.setLine(index, signLineFormatted.length() > 16 ? signLineFormatted.substring(16) : signLineFormatted);
                                index++;
                            }

                        Bukkit.getScheduler().runTask(main, sign::update);
                    }
            }
        }.runTaskTimerAsynchronously(main, 30L, 20L);
    }

    public boolean isVacant() {
        return tenant == null;
    }

    public boolean hasLeaseExpired() {
        return System.currentTimeMillis() >= getRemainingRentalTime();
    }
}
