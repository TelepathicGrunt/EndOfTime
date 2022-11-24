package me.isRadiant.endoftime.client.mixins;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.isRadiant.endoftime.core.interfaces.IPlayerBoolean;
import me.isRadiant.endoftime.core.registration.ComponentReg;
import me.isRadiant.endoftime.core.utils.ModConstants;
import me.isRadiant.endoftime.core.utils.ModHelper;
import me.isRadiant.endoftime.core.utils.WeightedSelection;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin
{
    @Inject(at = @At("TAIL"), method = "onPlayerConnect()V")
    public void onPlayerConnectInjection(ClientConnection connection, ServerPlayerEntity player, CallbackInfo cInfo)
    {
        World world = player.getWorld();
        IPlayerBoolean component = ComponentReg.PLAYER_BOOLEANS.get(player);

        //Create selection Groups
        WeightedSelection<ItemStack> swordSelection = new WeightedSelection<>();
        WeightedSelection<ItemStack> seedSelection = new WeightedSelection<>();
        WeightedSelection<ItemStack> saplingSelection = new WeightedSelection<>();
        Random seedRand = new Random();
        Random saplingRand = new Random();

        //Add sword selection
        swordSelection.addEntry(new ItemStack(Items.WOODEN_SWORD, 1), .5F);
        swordSelection.addEntry(new ItemStack(Items.STONE_SWORD, 1), .14F);
        swordSelection.addEntry(new ItemStack(Items.IRON_SWORD, 1), .2F);
        swordSelection.addEntry(new ItemStack(Items.GOLDEN_SWORD, 1), .1F);
        swordSelection.addEntry(new ItemStack(Items.DIAMOND_SWORD, 1), .5F);
        swordSelection.addEntry(new ItemStack(Items.NETHERITE_SWORD, 1), .01F);

        //Add seed selection
        seedSelection.addEntry(new ItemStack(Items.WHEAT_SEEDS, seedRand.nextInt(0, 4) + 1), .3F);
        seedSelection.addEntry(new ItemStack(Items.BEETROOT_SEEDS, seedRand.nextInt(0, 4) + 1), .2F);
        seedSelection.addEntry(new ItemStack(Items.CARROT, seedRand.nextInt(0, 4) + 1), .2F);
        seedSelection.addEntry(new ItemStack(Items.POTATO, seedRand.nextInt(0, 4) + 1), .2F);
        seedSelection.addEntry(new ItemStack(Items.MELON_SEEDS, seedRand.nextInt(0, 4) + 1), .1F);

        //Add sapling selection
        saplingSelection.addEntry(new ItemStack(Items.OAK_SAPLING, saplingRand.nextInt(0, 2) + 2), .16F);
        saplingSelection.addEntry(new ItemStack(Items.SPRUCE_SAPLING, saplingRand.nextInt(0, 2) + 2), .14F);
        saplingSelection.addEntry(new ItemStack(Items.BIRCH_SAPLING, saplingRand.nextInt(0, 2) + 2), .14F);
        saplingSelection.addEntry(new ItemStack(Items.JUNGLE_SAPLING, saplingRand.nextInt(0, 2) + 2), .14F);
        saplingSelection.addEntry(new ItemStack(Items.ACACIA_SAPLING, saplingRand.nextInt(0, 2) + 2), .14);
        saplingSelection.addEntry(new ItemStack(Items.DARK_OAK_SAPLING, saplingRand.nextInt(0, 2) + 2), .14);
        saplingSelection.addEntry(new ItemStack(Items.MANGROVE_PROPAGULE, saplingRand.nextInt(0, 2) + 2),   .14);

        //If the player has received spawn items, or the biome is not end of time, do nothing
        if(component.getHasReceivedSpawnItems() || !world.getBiome(player.getBlockPos()).matchesId(ModConstants.ENDOFTIME_BIOME_ID)) return;

        //If they have not, then set the value to true and spawn items.
        component.setHasReceivedSpawnItems(true);
        ModHelper.LOG.info("You have received your spawn items!");
        player.giveItemStack(swordSelection.getRandom());
        player.giveItemStack(new ItemStack(Items.DIRT, 6));
        player.giveItemStack(seedSelection.getRandom());
        player.giveItemStack(saplingSelection.getRandom());
        //TODO: Give player a lava boat
        //player.giveItemStack(new ItemStack(ItemReg.LAVA_BOAT, 1));
    }
}