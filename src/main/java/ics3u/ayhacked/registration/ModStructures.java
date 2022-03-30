package ics3u.ayhacked.registration;

import com.google.common.collect.ImmutableMap;
import ics3u.ayhacked.AYHackED;
import ics3u.ayhacked.structures.BoatPiece;
import ics3u.ayhacked.structures.BoatStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.event.RegistryEvent;

public class ModStructures {
    public static Structure<NoFeatureConfig> BOAT = new BoatStructure(NoFeatureConfig.field_236558_a_);
    public static IStructurePieceType BOAT_PIECE_TYPE = BoatPiece.Piece::new;
    public static StructureFeature<?, ?> CONFIGURED_BOAT = BOAT.withConfiguration(NoFeatureConfig.field_236559_b_);

    public static void registerStructures(RegistryEvent.Register<Structure<?>> event) {
        AYHackED.LOGGER.info("Registering boat structure");

        BOAT.setRegistryName(new ResourceLocation(AYHackED.MODID, "boat"));
        event.getRegistry().register(BOAT);
        registerStructure(BOAT, new StructureSeparationSettings(16, 8, 384881743));
        Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(AYHackED.MODID, "boat_piece"), BOAT_PIECE_TYPE);


        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(AYHackED.MODID, "configured_boat"), CONFIGURED_BOAT);
    }

    private static <F extends Structure<?>> void registerStructure(F structure, StructureSeparationSettings structureSeparationSettings)
    {
        Structure.NAME_STRUCTURE_BIMAP.put(structure.getRegistryName().toString(), structure);

        DimensionStructuresSettings.field_236191_b_ =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.field_236191_b_)
                        .put(structure, structureSeparationSettings)
                        .build();
    }
}
