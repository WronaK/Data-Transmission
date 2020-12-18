package client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageElement extends ChainElement {

    public ImageElement() {
        this.dataType = 1;
        this.next = new SoundElement();
    }

    @Override
    protected byte[] handleConcreteSerializable(Object element) {
        byte[] bytes = null;
        try {
            BufferedImage bufferedImage = ImageIO.read((File) element);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "bmp", bos);
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    protected Object handleConcreteDeserializable(byte[] bytes) {
        ByteArrayInputStream img = new ByteArrayInputStream(bytes);
        try {
            BufferedImage image = ImageIO.read(img);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
