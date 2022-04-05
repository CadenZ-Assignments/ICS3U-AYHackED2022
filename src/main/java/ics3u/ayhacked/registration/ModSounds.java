package ics3u.ayhacked.registration;

import ics3u.ayhacked.AYHackED;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
    private static final DeferredRegister<SoundEvent> SOUND_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AYHackED.MODID);

    public static final RegistryObject<SoundEvent> BoatDiscSound = SOUND_DEFERRED_REGISTER.register("boat_music", () -> new SoundEvent(new ResourceLocation(AYHackED.MODID, "boat_music")));

    public static void register(IEventBus modEventBus) {
        SOUND_DEFERRED_REGISTER.register(modEventBus);
    }
}
