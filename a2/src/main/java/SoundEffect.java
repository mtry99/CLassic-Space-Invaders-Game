import javafx.scene.media.AudioClip;

public class SoundEffect {
    private AudioClip soundEffect;

    public SoundEffect(String URL){
        soundEffect = new AudioClip(getClass().getResource(URL).toExternalForm());
    }

    public void playSound(){
        soundEffect.play();
    }
}
