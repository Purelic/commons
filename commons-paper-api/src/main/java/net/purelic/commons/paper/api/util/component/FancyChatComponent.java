package net.purelic.commons.paper.api.util.component;

public class FancyChatComponent { //TODO: move into implementation of Purelative#sendFancyMessage

    /*private static final UrlValidator VALIDATOR = new UrlValidator();

    public static Component fancy(Component prefix, String message){
        TextComponent.Builder fancyMessage = text().append(text(": "));
        String[] split = message.split(" ");

        for (String word : split) {
            boolean mentioned = false;

            for (Player online : Bukkit.getOnlinePlayers()) {
                // Search for mentions in message
                if (word.equalsIgnoreCase(online.getName())) {
                    word = online.getName();

                    fancyMessage
                            .append(text(word + " ", NamedTextColor.GREEN))
                            .hoverEvent(showText(text("Click to PM")))
                            .clickEvent(ClickEvent.suggestCommand("/msg " + word + " "));

                    mentioned = true;

                    // Play sound if mentioned player is in the message audience
                    if (audience.contains(online)) {
                        online.playSound(online.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                    }

                    break;
                }
            }

            if (!mentioned) {
                // Check for valid links
                String link = (word.startsWith("http://") || word.startsWith("https://")) ? word : ("https://" + word);

                if (VALIDATOR.isValid(link)) {
                    word = word.replace("http://", "").replace("https://", "").replace("www.", "");

                    String truncSmall = ((word.length() >= 20) ? (word.substring(0, 20) + "...") : word);
                    String truncMedium = ((word.length() >= 30) ? (word.substring(0, 30) + "...") : word);

                    fancyMessage
                            .append(text(truncSmall + " ", NamedTextColor.AQUA))
                            .hoverEvent(showText(
                                    text("Click to Open ")
                                            .append(text("(At Own Risk)\n", NamedTextColor.GRAY))
                                            .append(text(truncMedium, NamedTextColor.AQUA, TextDecoration.ITALIC))))
                            .clickEvent(openUrl(link));
                } else {
                    fancyMessage.append(text(word + " "));
                }
            }
        }
    }*/
}

