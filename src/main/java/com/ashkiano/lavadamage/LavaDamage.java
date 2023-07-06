package com.ashkiano.lavadamage;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

//TODO přidat na git
//TODO přidat configurovatelnost
//TODO udělat něco jako zčervenání obrazovky
//TODO přidat výpis do chatu a nebo do actionbaru že je horko a jsi blízko lávy
//TODO udelat aby ten efekt negovala permise
//TODO udelat aby ten efekt negovalo netherite brnko
//TODO udelat aby ten efekt negoval nějaký enchant
public class LavaDamage extends JavaPlugin {
    private int CHECK_RADIUS;
    private static final int DAMAGE_AMOUNT = 2;

    @Override
    public void onEnable() {
        // Ensure config.yml is loaded or created
        this.saveDefaultConfig();

        // Load CHECK_RADIUS from config.yml
        CHECK_RADIUS = this.getConfig().getInt("CHECK_RADIUS", 5);

        // Initialize Metrics for plugin analytics
        Metrics metrics = new Metrics(this, 18976);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    // Check if player is in survival or adventure mode
                    if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                        if (isNearLava(player)) {
                            player.damage(DAMAGE_AMOUNT);
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0L, 20L);  // Run every second (20 ticks = 1 second)
    }

    private boolean isNearLava(Player player) {
        for (int x = -CHECK_RADIUS; x <= CHECK_RADIUS; x++) {
            for (int y = -CHECK_RADIUS; y <= CHECK_RADIUS; y++) {
                for (int z = -CHECK_RADIUS; z <= CHECK_RADIUS; z++) {
                    if (player.getLocation().add(x, y, z).getBlock().getType() == Material.LAVA) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}