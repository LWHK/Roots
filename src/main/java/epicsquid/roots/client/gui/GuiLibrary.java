/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.client.gui;

import epicsquid.mysticallib.client.gui.InvisibleButton;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.container.ContainerLibrary;
import epicsquid.roots.container.slots.SlotImposerModifierInfo;
import epicsquid.roots.container.slots.SlotImposerSpellInfo;
import epicsquid.roots.container.slots.SlotLibraryInfo;
import epicsquid.roots.container.slots.SlotSpellInfo;
import epicsquid.roots.network.MessageResetLibraryScreen;
import epicsquid.roots.network.MessageSetImposerSlot;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiLibrary extends GuiContainer {

  private ContainerLibrary container;
  private InvisibleButton backButton;

  public GuiLibrary(@Nonnull ContainerLibrary container) {
    super(container);
    this.container = container;
    xSize = 256;
    ySize = 152;
  }

  private boolean isSelectSpell () {
    return container.isSelectSpell();
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    if (!isSelectSpell()) {
      FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
      StaffSpellStorage storage = container.getSpellStorage();
      if (storage != null) {
        StaffSpellInfo info = storage.getSpellInSlot(container.getSpellSlot());
        if (info != null) {
          String name = info.getSpell().getTextColor() + "" + TextFormatting.BOLD + I18n.format("roots.spell." + info.getSpell().getName() + ".name");
          RenderHelper.enableGUIStandardItemLighting();
          this.drawCenteredString(renderer, name, this.width / 2, guiTop, 0xFFFFFFFF);
        }
      }
    }
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  public void initGui() {
    super.initGui();

    this.buttonList.clear();
    this.backButton = new InvisibleButton(0, guiLeft + 183, guiTop + 136, 32, 22, I18n.format("roots.imposer.back"));
    this.buttonList.add(this.backButton);
  }

  private static ResourceLocation SPELL_SELECT = new ResourceLocation(Roots.MODID, "textures/gui/staff_gui.png");
  private static ResourceLocation SPELL_MODIFY = new ResourceLocation(Roots.MODID, "textures/gui/staff_gui_modifiers.png");

  protected ResourceLocation getTexture() {
    return container.isSelectSpell() ? SPELL_SELECT : SPELL_MODIFY;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    ResourceLocation tex = getTexture();
    backButton.visible = tex != SPELL_SELECT;
    this.mc.getTextureManager().bindTexture(tex);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(i, j, 0, 0, 256, 152);
  }

  @Override
  public void drawSlot(Slot slot) {
    int i2 = slot.xPos - 2;
    int j2 = slot.yPos - 2;
    if (slot instanceof SlotLibraryInfo) {
      SlotLibraryInfo info = (SlotLibraryInfo) slot;
    } else if (slot instanceof SlotSpellInfo) {
      SlotSpellInfo info = (SlotSpellInfo) slot;
    }
    super.drawSlot(slot);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == backButton.id) {
      MessageResetLibraryScreen packet = new MessageResetLibraryScreen();
      PacketHandler.INSTANCE.sendToServer(packet);
      container.setSelectSpell();
    }

    super.actionPerformed(button);
  }
}
