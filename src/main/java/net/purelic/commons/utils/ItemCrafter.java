package net.purelic.commons.utils;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.NBTTagByte;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ItemCrafter {

    private ItemStack item;

    public ItemCrafter(Material material) {
        this.item = new ItemStack(material);
    }

    public ItemCrafter(ItemStack item) {
        this.item = item;
    }

    public ItemCrafter amount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemCrafter name(String name) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + name);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCrafter lore() {
        return this.lore("");
    }

    public ItemCrafter lore(String lore) {
        ItemMeta meta = this.item.getItemMeta();
        List<String> itemLore = meta.getLore();
        if (itemLore == null) itemLore = new ArrayList<>();
        itemLore.add(ChatColor.RESET + lore);
        meta.setLore(itemLore);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCrafter durability(int durability) {
        this.item.setDurability((short) durability);
        return this;
    }

    public ItemCrafter setUnbreakable() {
        ItemMeta meta = this.item.getItemMeta();
        meta.spigot().setUnbreakable(true);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCrafter addTag(String tag) {
        return setTag(tag, "true");
    }

    public ItemCrafter setTag(String tag, int b) {
        net.minecraft.server.v1_8_R3.ItemStack itemCopy = this.getNMSItem();
        NBTTagCompound nbt = itemCopy.hasTag() ? itemCopy.getTag() : new NBTTagCompound();
        nbt.set(tag, new NBTTagByte((byte) b));
        itemCopy.setTag(nbt);
        this.item = CraftItemStack.asBukkitCopy(itemCopy);
        return this;
    }

    public ItemCrafter setTag(String tag, String value) {
        net.minecraft.server.v1_8_R3.ItemStack itemCopy = this.getNMSItem();
        NBTTagCompound nbt = itemCopy.hasTag() ? itemCopy.getTag() : new NBTTagCompound();
        nbt.set(tag, new NBTTagString(value));
        itemCopy.setTag(nbt);
        this.item = CraftItemStack.asBukkitCopy(itemCopy);
        return this;
    }

    public String getTag(String tag) {
        net.minecraft.server.v1_8_R3.ItemStack itemCopy = this.getNMSItem();
        if (itemCopy == null) return "";
        NBTTagCompound nbt = itemCopy.hasTag() ? itemCopy.getTag() : new NBTTagCompound();
        return nbt.getString(tag);
    }

    public ItemCrafter removeTag(String tag) {
        net.minecraft.server.v1_8_R3.ItemStack itemCopy = this.getNMSItem();
        NBTTagCompound nbt = itemCopy.hasTag() ? itemCopy.getTag() : new NBTTagCompound();
        nbt.remove(tag);
        itemCopy.setTag(nbt);
        this.item = CraftItemStack.asBukkitCopy(itemCopy);
        return this;
    }

    public ItemCrafter clearTags() {
        net.minecraft.server.v1_8_R3.ItemStack itemCopy = this.getNMSItem();
        itemCopy.setTag(null);
        this.item = CraftItemStack.asBukkitCopy(itemCopy);
        return this;
    }

    private net.minecraft.server.v1_8_R3.ItemStack getNMSItem() {
        try {
            // use reflection to get the NMS item (to access NBT tags)
            // this is surprisingly more efficient than cloning the CraftItemStack
            CraftItemStack craftItemStack;

            if (this.item instanceof CraftItemStack) {
                craftItemStack = (CraftItemStack) this.item;
            } else {
                craftItemStack = CraftItemStack.asCraftCopy(this.item);
            }

            Field handle = craftItemStack.getClass().getDeclaredField("handle");
            handle.setAccessible(true);
            return (net.minecraft.server.v1_8_R3.ItemStack) handle.get(craftItemStack);
        } catch (Exception e) {
            // if there happen to be any issues with using reflection we can always fallback to the old method
            return CraftItemStack.asNMSCopy(this.item);
        }
    }

    public boolean hasTag(String tag) {
        return !this.getTag(tag).isEmpty();
    }

    public ItemCrafter data(int data) {
        this.item.setData(new MaterialData(this.item.getType(), (byte) data));
        return this;
    }

    public ItemCrafter enchant(Enchantment enchantment, int level) {
        this.item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemCrafter enchant(Enchantment enchantment) {
        this.item.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemCrafter type(Material material) {
        this.item.setType(material);
        return this;
    }

    public ItemCrafter flag(ItemFlag flag) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(flag);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCrafter clearLore() {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(new ArrayList<>());
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCrafter clearEnchantments() {
        for (Enchantment enchantment : this.item.getEnchantments().keySet()) {
            this.item.removeEnchantment(enchantment);
        }

        return this;
    }

    public ItemCrafter color(Color color) {
        Material type = this.item.getType();
        if (type == Material.LEATHER_BOOTS || type == Material.LEATHER_CHESTPLATE || type == Material.LEATHER_HELMET || type == Material.LEATHER_LEGGINGS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) this.item.getItemMeta();
            meta.setColor(color);
            this.item.setItemMeta(meta);
            return this;
        }
        throw new IllegalArgumentException("#color is only applicable for leather armor!");
    }

    public ItemCrafter color(int rgb) {
        Material type = this.item.getType();
        if (type == Material.LEATHER_BOOTS || type == Material.LEATHER_CHESTPLATE || type == Material.LEATHER_HELMET || type == Material.LEATHER_LEGGINGS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) this.item.getItemMeta();
            meta.setColor(Color.fromRGB(rgb));
            this.item.setItemMeta(meta);
            return this;
        }
        throw new IllegalArgumentException("#color is only applicable for leather armor!");
    }

    public ItemCrafter command(String command, boolean opOnly) {
        this.setTag("command", command);
        this.setTag("op_only", "" + opOnly);
        return this;
    }

    // sends a plugin message to spring
    public ItemCrafter spring(String channel) {
        this.setTag("spring", channel);
        return this;
    }

    public ItemStack craft() {
        return this.item;
    }

//    private net.minecraft.server.v1_8_R3.ItemStack getCraftItemStack(ItemStack item) {
//        try {
//            Field nmsHandle = CraftItemStack.class.getDeclaredField("handle");
//            nmsHandle.setAccessible(true);
//            return (net.minecraft.server.v1_8_R3.ItemStack) nmsHandle.get(item);
//        } catch (IllegalAccessException | NoSuchFieldException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

}
