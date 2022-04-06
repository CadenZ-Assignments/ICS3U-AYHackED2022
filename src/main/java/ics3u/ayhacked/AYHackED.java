package ics3u.ayhacked;

import ics3u.ayhacked.events.ServerForgeEvents;
import ics3u.ayhacked.network.Networking;
import ics3u.ayhacked.registration.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(AYHackED.MODID)
public class AYHackED {
    public static final String MODID = "ayhacked";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final ItemGroup GROUP = new ItemGroup("ayhacked") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.WaterPollutionGauge.get());
        }
    };

    public AYHackED() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(AYHackED::commonSetup);
        modEventBus.addGenericListener(Structure.class, AYHackED::onRegisterStructure);
        ModItems.register(modEventBus);
        ModSounds.register(modEventBus);
        ModBlocks.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void commonSetup(FMLCommonSetupEvent event) {
        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        ModCapabilities.register();
        Networking.registerMessages();

        forgeEventBus.addGenericListener(Chunk.class, ServerForgeEvents::worldCapAttachEvent);
        forgeEventBus.addGenericListener(Entity.class, ServerForgeEvents::playerAttachEvent);
        forgeEventBus.addListener(ServerForgeEvents::itemDespawnEvent);
        forgeEventBus.addListener(ServerForgeEvents::damageFish);
        forgeEventBus.addListener(EventPriority.HIGH, ServerForgeEvents::biomeModification);
        forgeEventBus.addListener(EventPriority.NORMAL, ServerForgeEvents::addDimensionalSpacing);
        forgeEventBus.addListener(ServerForgeEvents::removeThirstOvertime);
        forgeEventBus.addListener(ServerForgeEvents::rightClick);
    }

    public static void onRegisterStructure(final RegistryEvent.Register<Structure<?>> event) {
        ModStructures.registerStructures(event);
    }
}
