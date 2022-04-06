package ics3u.ayhacked.events;

import ics3u.ayhacked.AYHackED;
import ics3u.ayhacked.capabilities.Thirst;
import ics3u.ayhacked.capabilities.WaterPollution;
import ics3u.ayhacked.capabilities.base.CapabilityProvider;
import ics3u.ayhacked.network.Networking;
import ics3u.ayhacked.network.SyncThirstPacket;
import ics3u.ayhacked.registration.ModCapabilities;
import ics3u.ayhacked.registration.ModStructures;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;

import javax.annotation.Nonnull;
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

    private static final int maxCooldown = 4000;
    private static int cooldown = maxCooldown;

    public static void removeThirstOvertime(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;

        if (!player.getEntityWorld().isRemote() && !player.isCreative()) {
            cooldown--;
            if (cooldown <= 0) {
                player.getCapability(ModCapabilities.THIRST_CAPABILITY).ifPresent(cap -> {
                    cap.removeThirst(1);
                    Networking.sendToClient(new SyncThirstPacket(cap.getThirst()), (ServerPlayerEntity) player);
                    cooldown = maxCooldown;
                });
            }
        }
    }

    public static void drinkEvent(LivingEntityUseItemEvent.Finish event) {
        ItemStack item = event.getItem();
        World world = event.getEntityLiving().getEntityWorld();
        LivingEntity player = event.getEntityLiving();

        if (!(player instanceof PlayerEntity)) return;
        if (world.isRemote()) return;
        if (!(item.getItem() instanceof PotionItem)) return;
        // is not water
        if (!PotionUtils.getEffectsFromStack(item).isEmpty()) return;

        drink((PlayerEntity) player);
    }

    public static void playerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.getEntityWorld().isRemote()) return;
        player.getCapability(ModCapabilities.THIRST_CAPABILITY).ifPresent(cap -> Networking.sendToClient(new SyncThirstPacket(cap.getThirst()), (ServerPlayerEntity) event.getPlayer()));
    }

    private static void drink(@Nonnull PlayerEntity player) {
        player.getCapability(ModCapabilities.THIRST_CAPABILITY).ifPresent(cap -> {
            if (!(cap.getThirst() < 10)) return;
            cap.addThirst(1);
            Networking.sendToClient(new SyncThirstPacket(cap.getThirst()), (ServerPlayerEntity) player);
            player.swingArm(Hand.MAIN_HAND);

            ((Chunk) player.getEntityWorld().getChunk(player.getPosition())).getCapability(ModCapabilities.WATER_POLLUTION_CAPABILITY).ifPresent(chunkCap -> {
                if (chunkCap.getPollutionAmount() > 1000) {
                    player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100));
                }
                if (chunkCap.getPollutionAmount() > 5000) {
                    player.addPotionEffect(new EffectInstance(Effects.POISON, 100));
                }
                if (chunkCap.getPollutionAmount() > 8000) {
                    player.addPotionEffect(new EffectInstance(Effects.WITHER, 100));
                }
            });
        });
    }
}
