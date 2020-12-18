package client;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SoundElement extends ChainElement {

    public SoundElement() {
        this.dataType = 2;
        this.next = null;
    }

    @Override
    protected byte[] handleConcreteSerializable(Object element) {
        byte[] bytes = null;

        try {
            AudioInputStream bufferedAudio = AudioSystem.getAudioInputStream((File) element);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            AudioSystem.write(bufferedAudio, AudioFileFormat.Type.WAVE, bos);
            bytes = bos.toByteArray();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    @Override
    protected Object handleConcreteDeserializable(byte[] bytes) {
        ByteArrayInputStream v = new ByteArrayInputStream(bytes);
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(v);
            return audio;
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
