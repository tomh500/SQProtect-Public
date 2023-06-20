package com.windx.protect;

import org.bukkit.Bukkit;

import org.bukkit.Server;

import org.bukkit.ChatColor;

import org.bukkit.GameMode;

import org.bukkit.World;

import org.bukkit.command.Command;

import org.bukkit.command.CommandSender;

import org.bukkit.entity.Entity;

import org.bukkit.entity.Player;

import org.bukkit.event.Listener;

import org.bukkit.plugin.PluginManager;

import org.bukkit.plugin.java.JavaPlugin;

public class KillProtect extends JavaPlugin implements Listener {

    @Override

    public void onEnable() {

        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(this, this);

        pluginManager.registerEvents(new NameProtect(), this);

        pluginManager.registerEvents(new VoidProtect(), this);

    }

    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("kill")) {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                if (args.length == 0) {

                    // 没有任何参数

                    player.setGameMode(GameMode.SURVIVAL); // 设置玩家为生存模式

                    player.setHealth(0.0d); // 使玩家死亡

                } else if (args[0].equalsIgnoreCase("@a")) {

                    // 杀死所有玩家

                    for (Entity entity : player.getWorld().getEntities()) {

                        if (isPlayer(entity)) {

                            ((Player) entity).setGameMode(GameMode.SURVIVAL); // 设置玩家为生存模式

                            ((Player) entity).damage(1000); // 使玩家死亡

                        }

                    }

                } else if (args[0].equalsIgnoreCase("@e")) {

                    // 杀死所有实体，附带特殊处理

                    killAllEntities(player.getWorld());

                } else if (args[0].equalsIgnoreCase("*")) {

                    // 杀死所有实体

                    for (Entity entity : player.getWorld().getEntities()) {

                        if (!isPlayer(entity)) {

                            entity.remove(); // 将非玩家实体删除，以原版方式击杀

                        }

                    }

                } else if (args[0].equalsIgnoreCase("***")) {

                    // 杀死所有实体和所有玩家

                    for (Entity entity : player.getWorld().getEntities()) {

                        if (isPlayer(entity)) {

                            ((Player) entity).setGameMode(GameMode.SURVIVAL); // 设置玩家为生存模式

                            ((Player) entity).damage(1000); // 使玩家死亡

                        } else {

                            entity.remove(); // 将非玩家实体删除，以原版方式击杀

                        }

                    }

                } else if (args.length == 1)  {

                    // 杀死指定玩家

                    Player target = Bukkit.getPlayer(args[0]);

                    if (target != null) {

                        target.setGameMode(GameMode.SURVIVAL);

                        target.setHealth(0.0d);

                    } else {

                        player.sendMessage(ChatColor.RED + "Could not find player " + args[0] + ".");

                    }

                }

                return true;

            } else {

                sender.sendMessage("This command can only be used by players!");

                return true;

            }

        } else if (command.getName().equalsIgnoreCase("suicide")) {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                player.setGameMode(GameMode.SURVIVAL); // 将玩家设置为生存模式

                player.damage(1000); // 执行自杀过程

                return true;

            } else {

                sender.sendMessage("This command can only be used by players!");

                return true;

            }

        }

        return false;

    }

    // 判断实体类型是否为玩家

    public boolean isPlayer(Entity entity) {

        return entity instanceof Player;

    }

    // 删除所有实体并使玩家死亡

public void killAllEntities(World world) {

    // 获取服务器实例

    Server server = Bukkit.getServer();

    // 遍历所有实体

    for (Entity entity : world.getEntities()) {

        if (!(entity instanceof Player)) {

            // 删除非玩家实体

            entity.remove();

        } else {

            // 将玩家设置为生存模式

            Player player = (Player) entity;

            player.setGameMode(GameMode.SURVIVAL);

            // 立即杀死玩家

            player.setHealth(0.0);

        }

    }

    // 通知玩家所有实体都已删除

    server.broadcastMessage("All entities have been removed, and players have been killed.");

}

}

