package poa.poask.effects.packets;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.VariableString;
import ch.njol.util.Kleenean;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poa.poask.util.reflection.EntityMetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EffRawMetadata extends Effect {

    static {
        Skript.registerEffect(EffRawMetadata.class, "add [data] [from|of] %string% to [packet] %object%");
    }

    private Expression<String> input;
    private Expression<Object> packet;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        input = (Expression<String>) exprs[0];
        packet = (Expression<Object>) exprs[1];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        String input = this.input.getSingle(event);

        if (this.input instanceof VariableString vs)
            input = vs.toUnformattedString(event);

        Object packet = this.packet.getSingle(event);

        if (input == null || packet == null) return;

        List<String> list = new ArrayList<>(List.of(input.split(" ")));

        if (list.get(0).equalsIgnoreCase("on"))
            list.remove(0);
        if (list.get(0).equalsIgnoreCase("armor"))
            list.remove(0);


        String[] args = list.toArray(new String[0]);

        if (packet instanceof EntityMetadata metadata) {
            switch (args[0].toLowerCase()) {
                case "fire" -> metadata.setOnFire(getBoolean(args));
                case "invisible" -> metadata.setInvisible(getBoolean(args));
                case "glowing" -> metadata.setGlow(getBoolean(args));
                case "silent" -> metadata.setSilent(getBoolean(args));
                case "gravity" -> metadata.setGravity(getBoolean(args));

                case "name" -> {
                    if (args.length == 1)
                        return;
                    if (args[1].equalsIgnoreCase("visible"))
                        metadata.setNameVisible(getBoolean(Arrays.copyOfRange(args, 1, 2)));
                    else {
                        String name = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                        metadata.setName(MiniMessage.miniMessage().deserialize(name));
                    }
                }

                case "pose" -> {
                    if (args.length == 1)
                        return;
                    metadata.setPose(args[1].toUpperCase());
                }

                case "stand" -> {
                    if (args.length < 2)
                        return;

                    switch (args[1].toLowerCase()) {
                        case "small" -> metadata.setIsSmall(Boolean.parseBoolean(args[2]));
                        case "arms" -> metadata.setHasArms(Boolean.parseBoolean(args[2]));
                        case "nobaseplate", "nobase", "noplate" -> metadata.setNoBase(Boolean.parseBoolean(args[2]));
                        case "marker" -> metadata.setIsMarker(Boolean.parseBoolean(args[2]));

                        case "left" -> {
                            switch (args[2].toLowerCase()) {
                                case "arm" ->
                                        metadata.setLeftArmRotation(Float.parseFloat(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
                                case "leg" ->
                                        metadata.setLeftLegRotation(Float.parseFloat(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
                            }
                        }
                        case "right" -> {
                            switch (args[2].toLowerCase()) {
                                case "arm" ->
                                        metadata.setRightArmRotation(Float.parseFloat(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
                                case "leg" ->
                                        metadata.setRightLegRotation(Float.parseFloat(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
                            }
                        }

                        case "head" ->
                                metadata.setHeadRotation(Float.parseFloat(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4]));
                        case "body" ->
                                metadata.setBodyRotation(Float.parseFloat(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4]));

                    }
                }
            }
        }
    }

    private static boolean getBoolean(String[] args) {
        if (args.length == 1)
            return true;
        return Boolean.parseBoolean(args[1]);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        String input = this.input.toString(event, debug);
        String packet = this.packet.toString(event, debug);
        return "add data from " + input + " to packet " + packet;
    }

}
