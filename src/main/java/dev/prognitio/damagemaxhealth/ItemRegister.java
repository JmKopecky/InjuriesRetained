package dev.prognitio.damagemaxhealth;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegister {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DamageMaxHealth.MODID);

    public static final RegistryObject<Item> RUNE_OF_HEALTH = ITEMS.register(
            "rune_of_health", () -> new RuneOfHealth(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

    public static final RegistryObject<Item> GREATER_RUNE_OF_HEALTH = ITEMS.register(
            "greater_rune_of_health", () -> new GreaterRuneOfHealth(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
}
