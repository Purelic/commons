package net.purelic.commons.api;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.purelic.api.utils.component.PrefixComponent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.sound.Sound.sound;
import static net.kyori.adventure.text.Component.text;

public interface PurelicPaperPlayerAudience extends Audience, Protocold {

    default void sendActionBar(final @NotNull Component message, final boolean includeLegacy){
        if(includeLegacy && this.getProtocol().value() >= 5) this.sendMessage(message);
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
