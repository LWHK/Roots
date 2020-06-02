package epicsquid.roots.container.slots;

import epicsquid.roots.spell.info.StaffSpellInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SlotSpellInfo extends Slot {
  private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
  private final ISlotProvider info;
  private int slot;

  public SlotSpellInfo(ISlotProvider info, int slot, int xPosition, int yPosition) {
    super(emptyInventory, 0, xPosition, yPosition);
    this.info = info;
    this.slot = slot;
  }

  @Override
  public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
    return ItemStack.EMPTY;
  }

  public int getSlot() {
    return slot;
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return false;
  }

  public static ItemStack stack = new ItemStack(Items.FLINT);

  // TODO
  @Override
  public ItemStack getStack() {
    StaffSpellInfo info = this.info.apply(this.slot);
    if (info == null) {
      return ItemStack.EMPTY;
    } else {
      return info.asStack();
    }
  }

  @Override
  public boolean getHasStack() {
    return info.apply(this.slot) != null;
  }

  @Override
  public void putStack(ItemStack stack) {
  }

  @Override
  public int getSlotStackLimit() {
    return 1;
  }

  @Override
  public int getItemStackLimit(ItemStack stack) {
    return 1;
  }

  // TODO?
  @Nullable
  @Override
  public String getSlotTexture() {
    return super.getSlotTexture();
  }

  @Override
  public ItemStack decrStackSize(int amount) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canTakeStack(EntityPlayer playerIn) {
    return false;
  }

  @Override
  public boolean isSameInventory(Slot other) {
    if (other instanceof SlotSpellInfo) {
      return ((SlotSpellInfo) other).info.apply(slot).equals(info.apply(slot));
    }
    return false;
  }

  public StaffSpellInfo getInfo () {
    return info.apply(slot);
  }

  @FunctionalInterface
  public interface ISlotProvider {
    StaffSpellInfo apply(int slot);
  }
}
