package classes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Resourced Icons
 *
 * @author Tealeaf
 * @version 2.0.0
 * @since 2.0.0
 */
public enum Icon {
    ADD("iconAdd.png"), DELETE("iconDelete.png"), WARNING("iconWarning.png"), ERROR("iconError.png"), COPY("iconCopy.png");

    private final Image image;

    Icon(String name) {
        image = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(name)));
    }

    /**
     * Converts the image into an image view
     *
     * @param size Square-size of the image view
     * @return Image View with the image and given {@code size}
     * @since 2.0.0
     */
    public ImageView getImageView(int size) {
        ImageView r = new ImageView(image);
        r.setFitHeight(size);
        r.setFitWidth(size);
        return r;
    }
}
