package com.antarip.biggershulkerboxes.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.IntStream;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin {
    @Shadow
    private NonNullList<ItemStack> itemStacks;

    @Unique
    private static final int[] SLOTS_54 = IntStream.range(0, 54).toArray();

    @Inject(method = "<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At("TAIL"))
    private void biggershulkerboxes$initPosState(BlockPos pos, BlockState state, CallbackInfo ci) {
        biggershulkerboxes$ensureSize();
    }

    @Inject(method = "<init>(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At("TAIL"))
    private void biggershulkerboxes$initColorPosState(DyeColor color, BlockPos pos, BlockState state, CallbackInfo ci) {
        biggershulkerboxes$ensureSize();
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void biggershulkerboxes$loadAdditional(ValueInput input, CallbackInfo ci) {
        biggershulkerboxes$ensureSize();
    }

    @Inject(method = "getContainerSize", at = @At("HEAD"), cancellable = true)
    private void biggershulkerboxes$getContainerSize(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(54);
    }

    @Inject(method = "createMenu", at = @At("HEAD"), cancellable = true)
    private void biggershulkerboxes$createMenu(int syncId, Inventory playerInventory, CallbackInfoReturnable<AbstractContainerMenu> cir) {
        biggershulkerboxes$ensureSize();
        cir.setReturnValue(ChestMenu.sixRows(syncId, playerInventory, (Container) (Object) this));
    }

    @Inject(method = "getSlotsForFace", at = @At("HEAD"), cancellable = true)
    private void biggershulkerboxes$allSlots(Direction direction, CallbackInfoReturnable<int[]> cir) {
        cir.setReturnValue(SLOTS_54);
    }

    @Unique
    private void biggershulkerboxes$ensureSize() {
        if (this.itemStacks != null && this.itemStacks.size() >= 54) {
            return;
        }
        NonNullList<ItemStack> expanded = NonNullList.withSize(54, ItemStack.EMPTY);
        if (this.itemStacks != null) {
            for (int i = 0; i < this.itemStacks.size(); i++) {
                expanded.set(i, this.itemStacks.get(i));
            }
        }
        this.itemStacks = expanded;
    }
}
