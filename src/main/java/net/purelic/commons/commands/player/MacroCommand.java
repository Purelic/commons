package net.purelic.commons.commands.player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.commands.parsers.PlayerArgument;
import net.purelic.commons.profile.Rank;
import net.purelic.commons.utils.ChatUtils;
import net.purelic.commons.utils.Fetcher;
import net.purelic.commons.utils.text.ClickAction;
import net.purelic.commons.utils.text.ListBuilder;
import net.purelic.commons.utils.text.TextBuilder;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MacroCommand {

    private static final double COOLDOWN = 5D; // 5 seconds
    private static final Map<UUID, Macro> LAST_MACRO = new HashMap<>();
    private static final Map<UUID, Long> LAST_SENT = new HashMap<>();

    private enum Macro {

        BF("Big fists", false, true),
        BG("Ballin' game", false, true),
        BM("Bangin' map", false, true),
        EZ("You're the best", false, true),
        GF("Good fight", false, false),
        GG("Good game", false, false),
        GGG("GG game", false, true),
        GJ("Good job", true, false),
        GL("Good luck", true, false),
        GM("morning", "Good morning", true, false),
        GN("Good night", true, false),
        HF("Have fun", true, false),
        ILY("I <3 you", true, false),
        JK("Just kidding", true, false),
        LG("Let's go", true, false),
        NJ("Nice job", true, false),
        NP("No problem", true, false),
        NS("Nice shot", true, false),
        RIP("Rest in peace", true, false),
        SUP("What's sup", false, true),
        TY("Thank you", true, false),
        WP("Well played", true, false),
        YW("You're welcome", true, false),
        ;

        private final String command;
        private final String message;
        private final boolean premium;
        private final boolean hidden;

        Macro(String message, boolean premium, boolean hidden) {
            this.command = "/" + this.name().toLowerCase();
            this.message = message;
            this.premium = premium;
            this.hidden = hidden;
        }

        Macro(String command, String message, boolean premium, boolean hidden) {
            this.command = "/" + command;
            this.message = message;
            this.premium = premium;
            this.hidden = hidden;
        }

        public String getCommand() {
            return this.command;
        }

        public String getMessage() {
            return this.message;
        }

        public boolean isPremium() {
            return this.premium;
        }

        public boolean isHidden() {
            return this.hidden;
        }

        public void registerCommand() {
            CustomCommand cmd = mgr ->
                mgr.commandBuilder(this.command.substring(1))
                    .senderType(Player.class)
                    .argument(PlayerArgument.optional("player"))
                    .handler(c -> {
                        if (this.isPremium()
                            && Permission.notPremium(c, "Only Premium players can use this macro command!")) {
                            return;
                        }

                        sayMacro((Player) c.getSender(), c.getOptional("player"));
                    });

            Commons.registerCommand(cmd);
        }

        private void sayMacro(Player sender, Optional<Player> target) {
            String message = this.message;

            if (target.isPresent()) {
                Player player = target.get();
                message += ", " + (player.getName().equalsIgnoreCase("Without_Regret") ? "Reggie" : player.getName());
            }

            message += "!";

            BaseComponent[] fancyMessage = new ComponentBuilder(": ")
                .append(message).italic(true)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.command + " Macro").create()))
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, this.command))
                .create();

            UUID uuid = sender.getUniqueId();
            boolean block = false;

            if (LAST_MACRO.containsKey(uuid)) {
                boolean sameMacro = LAST_MACRO.get(uuid) == this;
                double timeLeft = (LAST_SENT.get(uuid) + COOLDOWN * 1000L) - System.currentTimeMillis();
                if (sameMacro && timeLeft > 0) block = true;
            }

            if (block) {
                BaseComponent[] fancyName = new BaseComponent[] { Fetcher.getFancyName(sender, sender) };
                sender.sendMessage((BaseComponent[]) ArrayUtils.addAll(fancyName, fancyMessage));
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    BaseComponent[] fancyName = new BaseComponent[] { Fetcher.getFancyName(sender, player) };
                    ChatUtils.sendMessage(player, (BaseComponent[]) ArrayUtils.addAll(fancyName, fancyMessage));
                }
            }

            LAST_MACRO.put(uuid, this);
            LAST_SENT.put(uuid, System.currentTimeMillis());
        }

    }

    public static void registerMacroCommands() {
        ListBuilder list = new ListBuilder("Macro Commands", "No macros to display!");

        for (Macro macro : Macro.values()) {
            macro.registerCommand();

            if (macro.isHidden()) continue;

            list.add(TextBuilder
                .bullet(ChatColor.AQUA + macro.getCommand() + ChatColor.WHITE + " - " + macro.getMessage() + "!" +
                    (macro.isPremium() ? " " + Rank.PREMIUM.getColor() + Rank.PREMIUM.getFlair() : ""))
                .onHover(macro.getCommand())
                .onClick(ClickAction.runCommand(macro.getCommand()))
            );
        }

        CustomCommand cmd = mgr ->
            mgr.commandBuilder("macros")
                .senderType(Player.class)
                .handler(c -> {
                    Player player = (Player) c.getSender();
                    list.send(player);
                });

        Commons.registerCommand(cmd);
    }

}
