package dev.prognitio.injuriesretained;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = InjuriesRetained.MODID)
public class Events {

    static String damageAttrUUID = "1e7cf150-2463-461f-b02a-7c72936c98bc";
    static String damageAttributeID = InjuriesRetained.MODID + ":damagedecrease";

    static String respawnAttrUUID = "853d2844-9717-45ef-8e76-7f242690da9a";
    static String respawnAttributeID = InjuriesRetained.MODID + ":respawnbuff";

    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) {

            double healthDecreaseValue = -1 * event.getAmount();

            AttributeModifier healthDecrease = new AttributeModifier(UUID.fromString(damageAttrUUID), damageAttributeID,
                    healthDecreaseValue, AttributeModifier.Operation.ADDITION);
            AttributeInstance instance = entity.getAttribute(Attributes.MAX_HEALTH);

            if (instance.getModifier(UUID.fromString(damageAttrUUID)) != null) {
                //preexisting value for the modifier

                double previousAmount = instance.getModifier(UUID.fromString(damageAttrUUID)).getAmount();

                healthDecrease = new AttributeModifier(UUID.fromString(damageAttrUUID), damageAttributeID,
                        previousAmount + healthDecreaseValue, AttributeModifier.Operation.ADDITION);

                instance.removeModifier(UUID.fromString(damageAttrUUID));
            }

            //add the modifier if it won't kill the player, else kill the player
            if (entity.getMaxHealth() > -1 * healthDecrease.getAmount()) {
                instance.addPermanentModifier(healthDecrease);
                event.setCanceled(false);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(GreaterHealthRuneProvider.GHRBonus).ifPresent(original -> event.getEntity().getCapability(GreaterHealthRuneProvider.GHRBonus).ifPresent(cloned -> {
                cloned.copyFrom(original);
            }));
        }

        if (event.isWasDeath()) {
            Player player = event.getEntity();
            final double respawnBonus = 40.0;
            if (player.getCapability(GreaterHealthRuneProvider.GHRBonus).isPresent()) {
                player.getCapability(GreaterHealthRuneProvider.GHRBonus).ifPresent(cap -> {
                    AttributeModifier respawnIncrease = new AttributeModifier(UUID.fromString(respawnAttrUUID), respawnAttributeID,
                            respawnBonus + cap.getHealthBonus(), AttributeModifier.Operation.ADDITION);

                    player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(respawnIncrease);
                    player.setHealth(player.getMaxHealth());
                });
            }

        } else {
            //Player returned from end dimension, make sure health is correct
            event.getEntity().setHealth(event.getEntity().getMaxHealth());
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        final double respawnBonus = 40.0;
        if (player.getCapability(GreaterHealthRuneProvider.GHRBonus).isPresent()) {
            player.getCapability(GreaterHealthRuneProvider.GHRBonus).ifPresent(cap -> {
                AttributeModifier respawnIncrease = new AttributeModifier(UUID.fromString(respawnAttrUUID), respawnAttributeID,
                        respawnBonus + cap.getHealthBonus(), AttributeModifier.Operation.ADDITION);

                if (!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(respawnIncrease)) {
                    player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(respawnIncrease);
                    player.setHealth(player.getMaxHealth());
                }
            });
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (event.getObject() != null) {
                if (!(event.getObject()).getCapability(GreaterHealthRuneProvider.GHRBonus).isPresent()) {
                    event.addCapability(new ResourceLocation(InjuriesRetained.MODID, "greater_health_rune_bonus"), new GreaterHealthRuneProvider());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(GreaterHealthRuneCap.class);
    }
}
