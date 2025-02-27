package com.gregtechceu.gtceu.api.machine.feature;

import com.gregtechceu.gtceu.api.gui.MachineUIFactory;
import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * @author KilaBash
 * @date 2023/2/17
 * @implNote A machine that has gui. can be opened via right click.
 */
public interface IUIMachine extends IUIHolder, IInteractedMachine, IMachineFeature {

    default boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return true;
    }

    @Override
    default InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (this.shouldOpenUI(player, hand, hit)  && player instanceof ServerPlayer serverPlayer) {
            MachineUIFactory.INSTANCE.openUI(self(), serverPlayer);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    default boolean isInvalid() {
        return self().isInValid();
    }

    @Override
    default boolean isRemote() {
        var level = self().getLevel();
        return level == null ? LDLib.isRemote() : level.isClientSide;
    }

    @Override
    default void markAsDirty() {
        self().markDirty();
    }
}
