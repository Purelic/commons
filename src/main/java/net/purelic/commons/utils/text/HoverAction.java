package net.purelic.commons.utils.text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * A class representing the actions a client can do when a component is hovered
 */
public interface HoverAction {

    /**
     * Get the Chat-Component action
     *
     * @return the Chat-Component action
     */
    HoverEvent.Action action();

    /**
     * The value paired to the action
     *
     * @return the value paired tot the action
     */
    BaseComponent[] value();

    /**
     * Creates a show_text action: when the component is hovered the text used as parameter will be displayed
     *
     * @param text the text to display
     * @return a new HoverAction instance
     */
    static HoverAction showText(BaseComponent... text) {
        return new SimpleHoverAction(HoverEvent.Action.SHOW_TEXT, text);
    }

    /**
     * Creates a show_text action: when the component is hovered the text used as parameter will be displayed
     *
     * @param text the text to display
     * @return a new HoverAction instance
     */
    static HoverAction showText(String text) {
        return new SimpleHoverAction(HoverEvent.Action.SHOW_TEXT, new TextComponent(text));
    }

    class SimpleHoverAction implements HoverAction {

        private final HoverEvent.Action action;
        private final BaseComponent[] value;

        public SimpleHoverAction(HoverEvent.Action action, BaseComponent... value) {
            this.action = action;
            this.value = value;
        }

        @Override
        public HoverEvent.Action action() {
            return this.action;
        }

        @Override
        public BaseComponent[] value() {
            return this.value;
        }

    }

}
