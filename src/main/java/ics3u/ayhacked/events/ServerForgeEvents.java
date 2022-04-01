package ics3u.ayhacked.events;

import ics3u.ayhacked.AYHackED;
import ics3u.ayhacked.capabilities.Thirst;
import ics3u.ayhacked.capabilities.base.CapabilityProvider;
import ics3u.ayhacked.capabilities.WaterPollution;
import ics3u.ayhacked.registration.ModStructures;
import ics3u.ayhacked.registration.ModCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.HashMap;
import java.util.Map;

public class ServerForgeEvents {
    public static void worldCapAttachEvent(AttachCapabilitiesEvent<Chunk> event) {
        Chunk chunk = event.getObject();
        if (!chunk.getWorld().isRemote()) {
            if (!chunk.getCapability(ModCapabilities.WATER_POLLUTION_CAPABILITY).isPresent()) {
                CapabilityProvider<WaterPollution> provider = new CapabilityProvider<>(new WaterPollution(), ModCapabilities.WATER_POLLUTION_CAPABILITY);
                event.addCapability(new ResourceLocation(AYHackED.MODID, "water_pollution"), provider);
                event.addListener(provider::invalidate);
            }
        }
    }

    public static void playerAttachEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            if (!entity.getCapability(ModCapabilities.THIRST_CAPABILITY).isPresent()) {
                CapabilityProvider<Thirst> provider = new CapabilityProvider<>(new Thirst(), ModCapabilities.THIRST_CAPABILITY);
                event.addCapability(new ResourceLocation(AYHackED.MODID, "thirst"), provider);
                event.addListener(provider::invalidate);
            }
        }
    }

    public static void itemDespawnEvent(ItemExpireEvent event) {
        ItemEntity item = event.getEntityItem();
        World world = item.getEntityWorld();

        if (!world.isRemote()) {
            Biome biome = world.getBiome(item.getPosition());
            if (biome.getCategory() == Biome.Category.OCEAN || biome.getCategory() == Biome.Category.BEACH) {
                Chunk chunk = world.getChunkAt(item.getPosition());
                chunk.getCapability(ModCapabilities.WATER_POLLUTION_CAPABILITY).ifPresent(cap -> {
                    cap.addPollution(100L * item.getItem().getCount());
                });
            }
        }
    }

    public static void damageFish(LivingEvent.LivingUpdateEvent event) {
        World world = event.getEntityLiving().getEntityWorld();
        if (!world.isRemote()) {
            LivingEntity livingEntity = event.getEntityLiving();
            if (!livingEntity.isInWater()) return;
            if (!(livingEntity instanceof WaterMobEntity)) return;
            ((Chunk) livingEntity.getEntityWorld().getChunk(livingEntity.getPosition())).getCapability(ModCapabilities.WATER_POLLUTION_CAPABILITY).ifPresent(cap -> {
                if (cap.getPollutionAmount() > 1000) {
                    livingEntity.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100));
                }
                if (cap.getPollutionAmount() > 5000) {
                    livingEntity.addPotionEffect(new EffectInstance(Effects.POISON, 100));
                }
                if (cap.getPollutionAmount() > 8000) {
                    livingEntity.addPotionEffect(new EffectInstance(Effects.WITHER, 100));
                }
            });
        }
    }

    public static void biomeModification(final BiomeLoadingEvent event) {
        if (event.getCategory() != Biome.Category.OCEAN) return;
        event.getGeneration().getStructures().add(() -> ModStructures.CONFIGURED_BOAT);
    }

    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

            if (serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator && serverWorld.getDimensionKey().equals(World.OVERWORLD))
                return;

            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
            tempMap.put(ModStructures.BOAT, DimensionStructuresSettings.field_236191_b_.get(ModStructures.BOAT));
            serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
        }
    }

    public static void debug(ItemTossEvent event) {
        event.getEntityItem().lifespan = 100;
    }
}
