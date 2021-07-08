package net.purelic.commons.api.player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.purelic.api.profile.Protocol;
import net.purelic.api.utils.component.PrefixComponent;
import net.purelic.commons.api.player.Protocold;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.sound.Sound.sound;
import static net.kyori.adventure.text.Component.text;

public interface PurelicPaperPlayerAudience extends Audience {

    /**
     * Classes implementing this that represents a group of players has to override this manually :(
     * @param message
     * @param includeLegacy
     */
    default void sendActionBar(final @NotNull Component message, final boolean includeLegacy){
        if(includeLegacy && this instanceof Purelative && ((Purelative)this).getProtocol().isLegacy()) this.sendMessage(message);
        else this.sendActionBar(message);
    }

    default void clearActionBar(){
        this.sendActionBar(text("")); //TODO:??
    }

    default void sendAlertMessage(final @NotNull Component message){
        this.sendMessage(text().append(PrefixComponent.COMMAND_ALERT_PREFIX, message.colorIfAbsent(NamedTextColor.RED)));
        this.playSound(sound(key("random.click"), Sound.Source.MASTER, 1, 1));
    }

    default void sendErrorMessage(final @NotNull Component message){
        this.sendMessage(text().append(PrefixComponent.COMMAND_ERROR_PREFIX, message));
        this.playSound(sound(key("??? break")/*TODO*/, Sound.Source.MASTER, 1, 1));
    }

    default void sendSuccessMessage(final @NotNull Component message){
        this.sendMessage(text().append(PrefixComponent.COMMAND_SUCCESS_PREFIX, message.colorIfAbsent(NamedTextColor.GREEN)));
        this.playSound(sound(key("random.orb"), Sound.Source.MASTER, 1, 1));
    }
    //TODO: fix new title default times
    //TODO: fancy chat message
    //TODO: Naughty messages
}
