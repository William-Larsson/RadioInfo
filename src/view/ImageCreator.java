package view;
import model.XMLParser;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A class for creating Images that will be shown in the GUI.
 */
public class ImageCreator {

    /**
     * Convert an object to an image icon with a size of the
     * given dimensions. If size is larger or smaller that
     * the image size than the image will scale.
     * @param obj = a String to an image resource.
     * @return = image icon.
     */
    public ImageIcon objectToImageIconOfSize(Object obj, int width, int height){
        ImageIcon img = null;
        try {
            if (obj instanceof URL){
                img = new ImageIcon(ImageIO.read((URL)obj));
            } else {
                img = getReplacementImageIcon((String)obj);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (img != null) {
            Image image = img.getImage();
            Image newImg = image.getScaledInstance(width, height,  Image.SCALE_SMOOTH);
            return new ImageIcon(newImg);
        } else return img;
    }


    /**
     * Get an image from the /resources file and convert to
     * a ImageIcon data type.
     * @param file = the file name (including a "/" at the start)
     * @return = the image
     * @throws IOException = in case the image doesn't exists.
     */
    private ImageIcon getReplacementImageIcon(String file) throws IOException{
        InputStream is = XMLParser.class.getResourceAsStream(file);
        BufferedImage image = ImageIO.read(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        baos.flush();
        byte[] imgContent = baos.toByteArray();
        baos.close();
        return new ImageIcon(imgContent);
    }
}
