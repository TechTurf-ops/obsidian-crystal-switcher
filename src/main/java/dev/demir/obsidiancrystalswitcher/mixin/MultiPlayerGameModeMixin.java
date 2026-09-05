package dev.demir.obsidiancrystalswitcher.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    @Unique
    private boolean obsidianCrystalSwitcher$wasObsidian;

    @Unique
    private int obsidianCrystalSwitcher$countBefore;

    @Inject(method = "useItemOn", at = @At("HEAD"))
    private void obsidianCrystalSwitcher$captureUse(
            LocalPlayer player,
            InteractionHand hand,
            BlockHitResult hitResult,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        ItemStack held = player.getItemInHand(hand);
        this.obsidianCrystalSwitcher$wasObsidian = held.is(Items.OBSIDIAN);
        this.obsidianCrystalSwitcher$countBefore = held.getCount();
    }

    @Inject(method = "useItemOn", at = @At("RETURN"))
    private void obsidianCrystalSwitcher$switchAfterPlacement(
            LocalPlayer player,
            InteractionHand hand,
            BlockHitResult hitResult,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (!this.obsidianCrystalSwitcher$wasObsidian) {
            return;
        }

        InteractionResult result = cir.getReturnValue();
        if (!(result instanceof InteractionResult.Success)) {
            return;
        }

        ItemStack after = player.getItemInHand(hand);
        boolean obsidianWasConsumed = after.getCount() < this.obsidianCrystalSwitcher$countBefore;

        // In creative mode the stack count does not decrease, but the successful
        // interaction still represents a locally accepted placement attempt.
        if (!obsidianWasConsumed && !player.hasInfiniteMaterials()) {
            return;
        }

        int crystalSlot = -1;
        for (int slot = 0; slot < 9; slot++) {
            if (player.getInventory().getItem(slot).is(Items.END_CRYSTAL)) {
                crystalSlot = slot;
                break;
            }
        }

        if (crystalSlot < 0 || player.getInventory().getSelectedSlot() == crystalSlot) {
            return;
        }

        player.getInventory().setSelectedSlot(crystalSlot);
        player.connection.send(new ServerboundSetCarriedItemPacket(crystalSlot));
    }
}
