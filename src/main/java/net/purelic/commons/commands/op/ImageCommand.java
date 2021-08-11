package net.purelic.commons.commands.op;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;

public class ImageCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("image")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .argument(StringArgument.of("image url"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                String url = c.get("image url");

                MapView view = Bukkit.getMap(player.getItemInHand().getDurability());

                for (MapRenderer mapRenderer : view.getRenderers()) {
                    view.removeRenderer(mapRenderer);
                }

                try {
                    ImageRenderer renderer = new ImageRenderer(url);
                    view.addRenderer(renderer);
                    CommandUtils.sendAlertMessage(player, "Rending map image...");
                } catch (IOException e) {
                    CommandUtils.sendErrorMessage(player, "An error occurred! Is the image URL correct?");
                }
            });
    }

    private static class ImageRenderer extends MapRenderer {

        private final SoftReference<BufferedImage> cacheImage;
        private boolean hasRendered = false;

        public ImageRenderer(String url) throws IOException {
            this.cacheImage = new SoftReference<>(this.getImage(url));
        }

        @Override
        public void render(MapView view, MapCanvas canvas, Player player) {
            if (this.hasRendered) {
                return;
            }

            if (this.cacheImage.get() != null) {
                canvas.drawImage(0, 0, this.cacheImage.get());
                this.hasRendered = true;
            } else {
                CommandUtils.sendErrorMessage(player, "Attempted to render the image, but the cached image was missing!");
                this.hasRendered = true;
            }
        }

        public BufferedImage getImage(String url) throws IOException {
            boolean useCache = ImageIO.getUseCache();

            // Temporarily disable cache, if it isn't already,
            // so we can get the latest image.
            ImageIO.setUseCache(false);

            // Resize the image
            BufferedImage image = resize(new URL(url), new Dimension(128, 128));

            // Re-enable it with the old value.
            ImageIO.setUseCache(useCache);

            return image;
        }

        public BufferedImage resize(final URL url, final Dimension size) throws IOException {
            final BufferedImage image = ImageIO.read(url);
            final BufferedImage resized = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g = resized.createGraphics();
            g.drawImage(image, 0, 0, size.width, size.height, null);
            g.dispose();
            return resized;
        }

    }

}
