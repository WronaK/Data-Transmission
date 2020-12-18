package client;

import java.nio.charset.StandardCharsets;

public class TextElement extends ChainElement {

    public TextElement() {
        this.dataType = 0;
        this.next = new ImageElement();
    }

    @Override
    protected byte[] handleConcreteSerializable(Object element) {
        byte[] bytes = null;
        bytes = ((String) element).getBytes(StandardCharsets.UTF_8);
        return bytes;
    }

    @Override
    protected Object handleConcreteDeserializable(byte[] bytes) {
        String text = new String(bytes, StandardCharsets.UTF_8);
        return text;
    }
}
