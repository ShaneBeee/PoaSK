package poa.poask.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import io.papermc.paper.event.player.PlayerTrackEntityEvent;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import poa.poask.util.packetListener.ClientBlockChangeEvent;

public class Events extends SimpleEvent {

    static {
        // Client block change event
        Skript.registerEvent("Client Block Change", SimpleEvent.class, ClientBlockChangeEvent.class,
                "client block change");

        EventValues.registerEventValue(ClientBlockChangeEvent.class, Block.class, new Getter<>() {
            @Override
            public @Nullable Block get(ClientBlockChangeEvent event) {
                return event.getBlock();
            }
        }, EventValues.TIME_NOW);

        EventValues.registerEventValue(ClientBlockChangeEvent.class, BlockData.class, new Getter<>() {
            @Override
            public @Nullable BlockData get(ClientBlockChangeEvent event) {
                return event.getNewState();
            }
        }, EventValues.TIME_NOW);

        // Player load entity event
        Skript.registerEvent("Player Load Entity", SimpleEvent.class, PlayerTrackEntityEvent.class,
                "player load entity");

        EventValues.registerEventValue(PlayerTrackEntityEvent.class, Entity.class, new Getter<>() {
            @Override
            public Entity get(PlayerTrackEntityEvent event) {
                return event.getEntity();
            }
        }, EventValues.TIME_NOW);
    }

}
