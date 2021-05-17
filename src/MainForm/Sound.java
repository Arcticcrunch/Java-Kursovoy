package MainForm;

import javax.sound.sampled.*;
import java.io.File;

public class Sound
{
    private String musicFile = System.getProperty("user.dir") + "/";
    private File soundFile;
    private AudioInputStream stream;
    private AudioFormat format;
    private DataLine.Info info;
    private Clip clip;


    public Sound(String baseDirectoryFileName)
    {
        try
        {
            musicFile = musicFile + baseDirectoryFileName;
            soundFile = new File(musicFile);
            stream = AudioSystem.getAudioInputStream(soundFile);
            format  = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.loop(0);
            clip.open(stream);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void Play()
    {
        if (clip != null)
        {
            clip.setMicrosecondPosition(0);
            clip.start();
        }
    }
    public void ContinuePlay()
    {
        if (clip != null)
        {
            clip.start();
        }
    }

    public void Stop()
    {
        if (clip != null)
        {
            clip.stop();
        }
    }

    public void SetVolume(float volume)
    {
        if (clip != null)
        {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }

    public Clip GetClip()
    {
        return this.clip;
    }

    public boolean IsPlaying()
    {
        if (clip != null)
            return clip.isRunning();
        else return false;
    }

}
