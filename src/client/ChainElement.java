package client;

import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class ChainElement {
    protected int dataType;
    protected ChainElement next;


    public byte[] handleRequestSerializable(int type, Object message) {
        byte[] bytes = setDataType(type);
        if(!checkDataType(type)) {
            return next.handleRequestSerializable(type, message);
        } else {
            byte[] messages = handleConcreteSerializable(message);
            byte[] allByte = new byte[bytes.length + messages.length];
            ByteBuffer byteBuffer = ByteBuffer.wrap(allByte);
            byteBuffer.put(bytes);
            byteBuffer.put(messages);
            return byteBuffer.array();
        }
    }

    public Object handleRequestDeserializable(byte[] bytes) {
        int type = getDataType(bytes);
        if(!checkDataType(type)) {
            return next.handleRequestDeserializable(bytes);
        } else {
            byte[] message = Arrays.copyOfRange(bytes, 4, bytes.length);
            return handleConcreteDeserializable(message);
        }
    }

    private int getDataType(byte[] bytes) {
        byte[] typ = Arrays.copyOfRange(bytes, 0, 4);
        ByteBuffer byteBuffer = ByteBuffer.wrap(typ);
        return byteBuffer.getInt();
    }

    private byte[] setDataType(int type) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(type);
        return byteBuffer.array();
    }

    private boolean checkDataType(int type) {
        if(this.dataType==type) {
            return true;
        }
        return false;
    }

    protected abstract byte[] handleConcreteSerializable(Object element);

    protected abstract Object handleConcreteDeserializable(byte[] bytes);
}

