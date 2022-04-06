package ics3u.ayhacked.blocks;

import ics3u.ayhacked.registration.ModBlocks;
import ics3u.ayhacked.te.AbstractChunkModifierTE;
import ics3u.ayhacked.utils.MiscHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class ChunkModifierBlock extends DirectionalBlock {
    private final Supplier<AbstractChunkModifierTE> tileEntityFactory;

    public ChunkModifierBlock(boolean cleaner) {
        super(Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).hardnessAndResistance(4, 8).setRequiresTool());

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

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double)pos.getX() + MiscHelper.randomInRange(0.2D, 1.5D);
        double d1 = (double)pos.getY() + MiscHelper.randomInRange(0D, 1D);
        double d2 = (double)pos.getZ() + MiscHelper.randomInRange(0.2D, 1.5D);
        worldIn.addParticle(ParticleTypes.CLOUD, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }
}
