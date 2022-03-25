package ics3u.ayhacked.events;

import ics3u.ayhacked.AYHackED;
import ics3u.ayhacked.water_pollution.WaterPollutionCapProv;
import ics3u.ayhacked.water_pollution.WaterPollutionCapability;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ServerForgeEvents {
    public static void worldCapAttachEvent(AttachCapabilitiesEvent<Chunk> event) {
        Chunk chunk = event.getObject();
        if (!chunk.getWorld().isRemote()) {
            if (!chunk.getCapability(WaterPollutionCapability.WATER_POLLUTION_CAPABILITY).isPresent()) {
                WaterPollutionCapProv provider = new WaterPollutionCapProv();
                event.addCapability(new ResourceLocation(AYHackED.MODID, "water_pollution"), provider);
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
                chunk.getCapability(WaterPollutionCapability.WATER_POLLUTION_CAPABILITY).ifPresent(cap -> {
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
            ((Chunk) livingEntity.getEntityWorld().getChunk(livingEntity.getPosition())).getCapability(WaterPollutionCapability.WATER_POLLUTION_CAPABILITY).ifPresent(cap -> {
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

    public static void debug(ItemTossEvent event) {
        event.getEntityItem().lifespan = 100;
    }
}
