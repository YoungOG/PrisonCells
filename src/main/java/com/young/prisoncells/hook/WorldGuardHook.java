package com.young.prisoncells.hook;

import com.young.prisoncells.PrisonCells;

public class WorldGuardHook {

    private PrisonCells main = PrisonCells.getInstance();

//    public void createRegion(PrisonCell prisonCell) {
//        if (prisonCell.getPrisonRegion() != null) {
//            ProtectedCuboidRegion defaultRegion = getDefaultRegion(prisonCell);
//            prisonCell.getPrisonRegion().getProtectedRegions().add(defaultRegion);
//            defaultRegion.setDirty(true);
//        } else {
//            regionManager.removeRegion(protectedRegion.getId());
//            createRegion(prisonCell);
//        }
//    }
//
//    private static ProtectedCuboidRegion getDefaultRegion(final PrisonCell prisonCell) {
//        Location location1 = null;
//        Location location2 = null;
//        if (prisonCell.getRegion() instanceof CuboidRegion) {
//            final CuboidRegion cuboidRegion = (CuboidRegion)prisonCell.getRegion();
//            location1 = cuboidRegion.getLocation1();
//            location2 = cuboidRegion.getLocation2();
//        }
//        final ProtectedCuboidRegion protectedCuboidRegion = new ProtectedCuboidRegion(getRegionName(prisonCell), new BlockVector(location1.getX(), location1.getY(), location1.getZ()), new BlockVector(location2.getX(), location2.getY(), location2.getZ()));
//        protectedCuboidRegion.setPriority(50000);
//        StateFlag[] allowed;
//        for (int length = (allowed = WorldGuardHook.ALLOWED).length, i = 0; i < length; ++i) {
//            protectedCuboidRegion.setFlag((Flag)allowed[i], (Object)StateFlag.State.ALLOW);
//        }
//        StateFlag[] denied;
//        for (int length2 = (denied = WorldGuardHook.DENIED).length, j = 0; j < length2; ++j) {
//            protectedCuboidRegion.setFlag((Flag)denied[j], (Object)StateFlag.State.DENY);
//        }
//        final DefaultDomain defaultDomain = new DefaultDomain();
//        if (prisonCell.hasOwner()) {
//            defaultDomain.addPlayer(prisonCell.getOwner().getUniqueId());
//            final Iterator<CellUser> iterator = prisonCell.getMemberManager().getMembers().iterator();
//            while (iterator.hasNext()) {
//                defaultDomain.addPlayer(iterator.next().getUniqueId());
//            }
//        }
//        protectedCuboidRegion.setOwners(defaultDomain);
//        protectedCuboidRegion.setMembers(defaultDomain);
//        return protectedCuboidRegion;
//    }
//
//    public static void onClaimed(final PrisonCell prisonCell) {
//        final RegionManager regionManager = getWorldGuard().getRegionManager(prisonCell.getWorld());
//        final String regionName = getRegionName(prisonCell);
//        if (regionManager.hasRegion(regionName)) {
//            final ProtectedRegion region = regionManager.getRegion(regionName);
//            final DefaultDomain defaultDomain = new DefaultDomain();
//            if (prisonCell.hasOwner()) {
//                defaultDomain.addPlayer(prisonCell.getOwner().getUniqueId());
//                final Iterator<CellUser> iterator = prisonCell.getMemberManager().getMembers().iterator();
//                while (iterator.hasNext()) {
//                    defaultDomain.addPlayer(iterator.next().getUniqueId());
//                }
//            }
//            region.setOwners(defaultDomain);
//            region.setMembers(defaultDomain);
//            region.setDirty(true);
//            try {
//                regionManager.saveChanges();
//            }
//            catch (StorageException ex) {
//                ex.printStackTrace();
//            }
//        }
//        else {
//            createRegion(prisonCell);
//        }
//    }
//
//    public static void onUnclaimed(final PrisonCell prisonCell) {
//        final RegionManager regionManager = getWorldGuard().getRegionManager(prisonCell.getWorld());
//        final String regionName = getRegionName(prisonCell);
//        if (regionManager.hasRegion(regionName)) {
//            final ProtectedRegion region = regionManager.getRegion(regionName);
//            final DefaultDomain defaultDomain = new DefaultDomain();
//            region.setOwners(defaultDomain);
//            region.setMembers(defaultDomain);
//            region.setDirty(true);
//            try {
//                regionManager.saveChanges();
//            }
//            catch (StorageException ex) {
//                ex.printStackTrace();
//            }
//        }
//        else {
//            createRegion(prisonCell);
//        }
//    }
//
//    public static void removeRegion(final PrisonCell prisonCell) {
//        final RegionManager regionManager = getWorldGuard().getRegionManager(prisonCell.getWorld());
//        final String regionName = getRegionName(prisonCell);
//        if (regionManager.hasRegion(regionName)) {
//            regionManager.removeRegion(regionName);
//            try {
//                regionManager.saveChanges();
//            }
//            catch (StorageException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    public static void addMember(final PrisonCell prisonCell, final CellUser cellUser) {
//        final RegionManager regionManager = getWorldGuard().getRegionManager(prisonCell.getWorld());
//        final String regionName = getRegionName(prisonCell);
//        if (regionManager.hasRegion(regionName)) {
//            final ProtectedRegion region = regionManager.getRegion(regionName);
//            region.setDirty(true);
//            region.getOwners().addPlayer(cellUser.getUniqueId());
//            region.getMembers().addPlayer(cellUser.getUniqueId());
//            try {
//                regionManager.saveChanges();
//            }
//            catch (StorageException ex) {
//                ex.printStackTrace();
//            }
//        }
//        else {
//            createRegion(prisonCell);
//        }
//    }
//
//    public static void removeMember(final PrisonCell prisonCell, final CellUser cellUser) {
//        final RegionManager regionManager = getWorldGuard().getRegionManager(prisonCell.getWorld());
//        final String regionName = getRegionName(prisonCell);
//        if (regionManager.hasRegion(regionName)) {
//            final ProtectedRegion region = regionManager.getRegion(regionName);
//            region.setDirty(true);
//            region.getOwners().removePlayer(cellUser.getUniqueId());
//            region.getMembers().removePlayer(cellUser.getUniqueId());
//            try {
//                regionManager.saveChanges();
//            }
//            catch (StorageException ex) {
//                ex.printStackTrace();
//            }
//        }
//        else {
//            createRegion(prisonCell);
//        }
//    }
}
