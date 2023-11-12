package dev.prognitio.injuriesretained;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class RuneOfHealth extends Item {

    static String uuid = "899d0fd1-77fe-430c-a347-9001d068db19";
    static String id = InjuriesRetained.MODID + ":addhealth";


    public RuneOfHealth(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        AttributeInstance instance = user.getAttribute(Attributes.MAX_HEALTH);
        double amount = 2;
        if (instance.getModifier(UUID.fromString(uuid)) != null) {
            amount += instance.getModifier(UUID.fromString(uuid)).getAmount();
        }
        instance.removeModifier(UUID.fromString(uuid));
        AttributeModifier addedHealth = new AttributeModifier(UUID.fromString(uuid), id, amount, AttributeModifier.Operation.ADDITION);
        instance.addPermanentModifier(addedHealth);
        stack.shrink(1);

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack item, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add(Component.literal("This rune is imbued with the power of vitality.").withStyle(ChatFormatting.DARK_RED));

        if (Screen.hasShiftDown()) {
            components.add(Component.literal("Right click to gain an extra heart.").withStyle(ChatFormatting.RED));
        }

        super.appendHoverText(item, level, components, tooltipFlag);
    }
}
