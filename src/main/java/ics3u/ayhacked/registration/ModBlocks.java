package ics3u.ayhacked.registration;

import ics3u.ayhacked.AYHackED;
import ics3u.ayhacked.blocks.ChunkModifierBlock;
import ics3u.ayhacked.te.CleanerTileEntity;
import ics3u.ayhacked.te.PolluterTileEntity;
import ics3u.ayhacked.utils.MiscHelper;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    private static final DeferredRegister<Block> BLOCK_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, AYHackED.MODID);
    private static final DeferredRegister<Item> ITEM_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, AYHackED.MODID);
    private static final DeferredRegister<TileEntityType<?>> TE_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, AYHackED.MODID);

    public static RegistryObject<ChunkModifierBlock> CLEANER_BLOCK = BLOCK_DEFERRED_REGISTER.register("cleaner", () -> new ChunkModifierBlock(true));
    public static RegistryObject<Item> CLEANER_BLOCK_ITEM = ITEM_DEFERRED_REGISTER.register("cleaner", () -> new BlockItem(CLEANER_BLOCK.get(), MiscHelper.DEFAULT_PROPERTIES));
    public static RegistryObject<TileEntityType<CleanerTileEntity>> CLEANER_TE = TE_DEFERRED_REGISTER.register("cleaner", () -> TileEntityType.Builder.create(CleanerTileEntity::new, CLEANER_BLOCK.get()).build(null));

    public static RegistryObject<ChunkModifierBlock> POLLUTER_BLOCK = BLOCK_DEFERRED_REGISTER.register("polluter", () -> new ChunkModifierBlock(false));
    public static RegistryObject<BlockItem> POLLUTER_BLOCK_ITEM = ITEM_DEFERRED_REGISTER.register("polluter", () -> new BlockItem(POLLUTER_BLOCK.get(), MiscHelper.DEFAULT_PROPERTIES));
    public static RegistryObject<TileEntityType<PolluterTileEntity>> POLLUTER_TE = TE_DEFERRED_REGISTER.register("polluter", () -> TileEntityType.Builder.create(PolluterTileEntity::new, POLLUTER_BLOCK.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_DEFERRED_REGISTER.register(bus);
        ITEM_DEFERRED_REGISTER.register(bus);
        TE_DEFERRED_REGISTER.register(bus);
    }
}
