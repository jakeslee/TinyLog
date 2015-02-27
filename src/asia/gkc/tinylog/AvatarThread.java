package asia.gkc.tinylog;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jakes on 14-12-7.
 */
public class AvatarThread implements Runnable {
    String email;


    public AvatarThread(String email) {
        this.email = email;
    }

    @Override

    public void run() {
        try {
            String emailMd5 = Util.md5Hex(email);
            File file = new File(Util.getDeployPath() + "/img/avatar/email/" + emailMd5);
            URL url = new URL("http://cn.gravatar.com/avatar/" + emailMd5 + "?s=40&r=x");
            BufferedImage image = ImageIO.read(url);
            ImageIO.write(image, "jpg", file);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
