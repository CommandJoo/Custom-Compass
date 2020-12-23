package de.jo.customcompass;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{

	public HashMap<Player, Entity> locations = new HashMap<>();
	
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("Custom Compass aktiviert!");
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> Bukkit.getOnlinePlayers().forEach(player -> {
			try {
				player.setCompassTarget(locations.get(player).getLocation());
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}), 1, 10);
	}

	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent event) {
		final Player player = event.getPlayer();
		final Entity entity = event.getRightClicked();

		ItemStack is = new ItemStack(Material.COMPASS);
		ItemMeta meta = is.getItemMeta();

		if (meta != null) {
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.addEnchant(Enchantment.DURABILITY, 1, true);
		}
		is.setItemMeta(meta);

		if(player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {
			player.getInventory().setItemInMainHand(is);
			try {
				locations.remove(player);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			locations.put(player, entity);
		}
 	}
}
