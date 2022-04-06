package ics3u.ayhacked.blocks;

import ics3u.ayhacked.registration.ModBlocks;
import ics3u.ayhacked.te.AbstractChunkModifierTE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ChunkModifierBlock extends DirectionalBlock {
    private final Supplier<AbstractChunkModifierTE> tileEntityFactory;

    public ChunkModifierBlock(boolean cleaner) {
        super(Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).setRequiresTool());

        tileEntityFactory = cleaner ? () -> ModBlocks.CLEANER_TE.get().create() : () -> ModBlocks.POLLUTER_TE.get().create();

        BlockState state = this.getStateContainer().getBaseState();
        state.with(FACING, Direction.NORTH);

        this.setDefaultState(state);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateBuilder) {
        super.fillStateContainer(stateBuilder);
        stateBuilder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.with(FACING, rotation.rotate(blockState.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.with(FACING, mirror.mirror(blockState.get(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext) {

        BlockState state = super.getStateForPlacement(blockItemUseContext);

        if (state == null) {
            return null;
        }

        state.with(FACING, blockItemUseContext.getNearestLookingDirection());

        return this.getDefaultState().with(FACING, blockItemUseContext.getPlacementHorizontalFacing());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return tileEntityFactory.get();
    }
}
