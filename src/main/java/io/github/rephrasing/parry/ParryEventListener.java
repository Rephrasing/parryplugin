package io.github.rephrasing.parry;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ParryEventListener implements Listener {

    private final List<UUID> playerParryCooldowns = new ArrayList<>();

    @EventHandler
    private void onPlayerDamage(EntityDamageByEntityEvent event) {
        Entity damagerEntity = event.getDamager();
        Entity damagedEntity = event.getEntity();
        if (!(damagedEntity instanceof Player)) return;
        Player player = (Player) damagedEntity;

        if (!player.isBlocking()) return;
        if (isOnParryCooldown(player)) return;
        Parry.getInstance().getLogger().info("Player " + player.getName() + " is out of cooldown");
        event.setDamage(0);
        if (damagerEntity instanceof LivingEntity) {
            PotionEffect debuff = new PotionEffect(PotionEffectType.SLOW, Parry.getParryEffectsDuranceTicks(), 2);
            debuff.apply((LivingEntity) damagerEntity);
        }

        PotionEffect buff = new PotionEffect(PotionEffectType.SPEED, Parry.getParryEffectsDuranceTicks(), 2);
        buff.apply(player);

        player.getWorld().playEffect(player.getLocation(), Effect.CLOUD,5, 5);
        this.applyParryCooldown(player);
    }

    private void applyParryCooldown(Player player) {
        playerParryCooldowns.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskLaterAsynchronously(Parry.getInstance(), ()->{
            playerParryCooldowns.remove(player.getUniqueId());
        }, Parry.getParryCooldownTicks());
    }

    public boolean isOnParryCooldown(Player player) {
        return this.playerParryCooldowns.contains(player.getUniqueId());
    }
}
