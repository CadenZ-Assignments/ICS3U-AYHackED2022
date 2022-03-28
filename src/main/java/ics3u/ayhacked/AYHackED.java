package ics3u.ayhacked;

import ics3u.ayhacked.events.ServerForgeEvents;
import ics3u.ayhacked.registration.ModItems;
import ics3u.ayhacked.registration.ModStructures;
import ics3u.ayhacked.water_pollution.WaterPollutionCapability;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
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
        modEventBus.addListener(AYHackED::onRegisterStructure);
        ModItems.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void commonSetup(FMLCommonSetupEvent event) {
        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        WaterPollutionCapability.register();

        forgeEventBus.addGenericListener(Chunk.class, ServerForgeEvents::worldCapAttachEvent);
        forgeEventBus.addListener(ServerForgeEvents::itemDespawnEvent);
        forgeEventBus.addListener(ServerForgeEvents::damageFish);
        forgeEventBus.addListener(ServerForgeEvents::biomeModification);
        forgeEventBus.addListener(ServerForgeEvents::debug);
    }

    public static void onRegisterStructure(final RegistryEvent.Register<Structure<?>> event) {
        ModStructures.registerStructures(event);
    }
}
