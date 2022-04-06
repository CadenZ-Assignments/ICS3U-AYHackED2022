package ics3u.ayhacked.registration;

import ics3u.ayhacked.AYHackED;
import ics3u.ayhacked.items.WaterPollutionGauge;
import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    private static final DeferredRegister<Item> ITEM_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, AYHackED.MODID);

    public static final RegistryObject<Item> WaterPollutionGauge = ITEM_DEFERRED_REGISTER.register("water_pollution_gauge", WaterPollutionGauge::new);
    public static final RegistryObject<Item> MusicDisc = ITEM_DEFERRED_REGISTER.register("boat_disc", () -> new MusicDiscItem(20000, ModSounds.BoatDiscSound, new Item.Properties().maxStackSize(1).group(AYHackED.GROUP).rarity(Rarity.RARE)));

    public static void register(IEventBus modEventBus) {
        ITEM_DEFERRED_REGISTER.register(modEventBus);
    }
}
