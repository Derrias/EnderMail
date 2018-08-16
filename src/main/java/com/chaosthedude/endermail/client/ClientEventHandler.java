package com.chaosthedude.endermail.client;

import com.chaosthedude.endermail.config.ConfigHandler;
import com.chaosthedude.endermail.items.ItemPackageController;
import com.chaosthedude.endermail.registry.EnderMailBlocks;
import com.chaosthedude.endermail.registry.EnderMailItems;
import com.chaosthedude.endermail.util.EnumControllerState;
import com.chaosthedude.endermail.util.ItemUtils;
import com.chaosthedude.endermail.util.RenderUtils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class ClientEventHandler {
	
	private static final Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		if (event.phase == Phase.END && mc.player != null && !mc.gameSettings.hideGUI && !mc.gameSettings.showDebugInfo && (mc.currentScreen == null || (ConfigHandler.displayWithChatOpen && mc.currentScreen instanceof GuiChat))) {
			final EntityPlayer player = mc.player;
			final ItemStack stack = ItemUtils.getHeldItem(player, EnderMailItems.packageController);
			if (stack != null && stack.getItem() instanceof ItemPackageController) {
				final ItemPackageController packageController = (ItemPackageController) stack.getItem();
				if (packageController.getState(stack) == EnumControllerState.DELIVERING) {
					RenderUtils.drawLineOffsetStringOnHUD(I18n.format("string.endermail.status"), 5, 0, 0xFFFFFF, 0);
					RenderUtils.drawLineOffsetStringOnHUD(I18n.format("string.endermail.delivering"), 5, 0, 0xAAAAAA, 1);
				} else if (packageController.getState(stack) == EnumControllerState.SUCCESS) {
					RenderUtils.drawLineOffsetStringOnHUD(I18n.format("string.endermail.status"), 5, 0, 0xFFFFFF, 0);
					RenderUtils.drawLineOffsetStringOnHUD(I18n.format("string.endermail.success"), 5, 0, 0xAAAAAA, 1);

					RenderUtils.drawLineOffsetStringOnHUD(I18n.format("string.endermail.coordinates"), 5, 0, 0xFFFFFF, 3);
					RenderUtils.drawLineOffsetStringOnHUD(packageController.getDeliveryPos(stack).getX() + " " + packageController.getDeliveryPos(stack).getY() + " " + packageController.getDeliveryPos(stack).getZ(), 5, 0, 0xAAAAAA, 4);
				} else if (packageController.getState(stack) == EnumControllerState.FAILURE) {
					RenderUtils.drawLineOffsetStringOnHUD(I18n.format("string.endermail.status"), 5, 0, 0xFFFFFF, 0);
					RenderUtils.drawLineOffsetStringOnHUD(I18n.format("string.endermail.fail"), 5, 0, 0xAAAAAA, 1);
				}
			}
		}
	}

	@SubscribeEvent
	public void onRegisterModels(ModelRegistryEvent event) {
		for (Item item : EnderMailItems.REGISTRY) {
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
		
		for (Block block : EnderMailBlocks.REGISTRY) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
		}
	}

}
