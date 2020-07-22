package com.young.prisoncells.cells.objects;

import com.young.prisoncells.utilities.LocationSerialization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

@Getter
@Setter
@AllArgsConstructor
public class PrisonDoor {

    private final Location topLocation, bottomLocation;

    public PrisonDoor(Document document) {
        this.topLocation = LocationSerialization.deserializeLocation(document.getString("topLocation"));
        this.bottomLocation = LocationSerialization.deserializeLocation(document.getString("bottomLocation"));
    }

    public Document toDocument() {
        Document document = new Document();

        if (topLocation != null && bottomLocation != null)
            document
                    .append("topLocation", LocationSerialization.serializeLocation(topLocation))
                    .append("bottomLocation", LocationSerialization.serializeLocation(bottomLocation));
        else
            return null;

        return document;
    }

    public boolean isOpen() {
        final Block block = bottomLocation.getBlock();
        BlockState blockState = block.getState();

        if (blockState.getData() instanceof Door && ((Door) blockState.getData()).isTopHalf())
            blockState = block.getRelative(BlockFace.DOWN).getState();

        return blockState.getData() instanceof Openable && ((Openable) blockState.getData()).isOpen();
    }

    public void setOpen(final boolean open) {
        final Block block = bottomLocation.getBlock();
        BlockState blockState = block.getState();

        if (blockState.getData() instanceof Door && ((Door) blockState.getData()).isTopHalf())
            blockState = block.getRelative(BlockFace.DOWN).getState();

        if (blockState.getData() instanceof Openable) {
            final Openable openable = (Openable) blockState.getData();
            openable.setOpen(open);
            blockState.setData((MaterialData) openable);
            blockState.update();
        }
    }
}
