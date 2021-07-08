package net.purelic.commons.paper.utils.book;

import net.md_5.bungee.api.chat.BaseComponent;
import net.purelic.commons.paper.utils.CommandUtils;
import net.purelic.commons.paper.utils.VersionUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

/**
 * Helps the user to create a book
 */
public class BookBuilder {

    private final BookMeta meta;
    private final ItemStack book;

    public BookBuilder() {
        this(new ItemStack(Material.WRITTEN_BOOK));
    }

    /**
     * Creates a new instance of the BookBuilder from an ItemStack representing the book item
     *
     * @param book the book's ItemStack
     */
    public BookBuilder(ItemStack book) {
        this.book = book;
        this.meta = (BookMeta) book.getItemMeta();
    }

    /**
     * Sets the title of the book
     *
     * @param title the title of the book
     * @return the BookBuilder's calling instance
     */
    public BookBuilder title(String title) {
        meta.setTitle(title);
        return this;
    }

    /**
     * Sets the author of the book
     *
     * @param author the author of the book
     * @return the BookBuilder's calling instance
     */
    public BookBuilder author(String author) {
        meta.setAuthor(author);
        return this;
    }

    /**
     * Sets the pages of the book without worrying about json or interactivity
     *
     * @param pages text-based pages
     * @return the BookBuilder's calling instance
     */
    public BookBuilder pagesRaw(String... pages) {
        meta.setPages(pages);
        return this;
    }

    /**
     * Sets the pages of the book without worrying about json or interactivity
     *
     * @param pages text-based pages
     * @return the BookBuilder's calling instance
     */
    public BookBuilder pagesRaw(List<String> pages) {
        meta.setPages(pages);
        return this;
    }

    /**
     * Sets the pages of the book
     *
     * @param pages the pages of the book
     * @return the BookBuilder's calling instance
     */
    public BookBuilder pages(BaseComponent[]... pages) {
        NmsBookHelper.setPages(meta, pages);
        return this;
    }

    public BookBuilder pages(PageBuilder pageBuilder) {
        return this.pages(pageBuilder.build());
    }

    /**
     * Sets the pages of the book
     *
     * @param pages the pages of the book
     * @return the BookBuilder's calling instance
     */
    public BookBuilder pages(List<BaseComponent[]> pages) {
        NmsBookHelper.setPages(meta, pages.toArray(new BaseComponent[0][]));
        return this;
    }

    /**
     * Creates the book
     *
     * @return the built book
     */
    public ItemStack build() {
        // Prevent common causes of "Invalid Book Tag"
        if (!meta.hasAuthor()) {
            meta.setAuthor("");
        }
        if (!meta.hasTitle()) {
            meta.setTitle("");
        }
        if (!meta.hasPages()) {
            this.pages(new BaseComponent[]{});
        }
        book.setItemMeta(meta);
        return book;
    }

    /**
     * Opens a book GUI to the player
     *
     * @param player the player
     */
    @SuppressWarnings("deprecation")
    public void open(Player player) {
        if (VersionUtils.isLegacy(player)) {
            CommandUtils.sendErrorMessage(player, "Book GUIs aren't support in 1.7!");
            return;
        }

        ItemStack book = this.build();

        player.closeInventory();
        // Store the previous item
        ItemStack hand = player.getItemInHand();

        player.setItemInHand(book);
        player.updateInventory();

        // Opening the GUI
        NmsBookHelper.openBook(player, book);

        // Returning whatever was on hand.
        player.setItemInHand(hand);
        player.updateInventory();

        // Play sound effect
        player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
    }

}
