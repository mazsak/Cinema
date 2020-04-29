package web_servies;

import javax.imageio.ImageIO;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.soap.SOAPBinding;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@MTOM
@WebService(serviceName = "imageservice")
@BindingType(value = SOAPBinding.SOAP11HTTP_MTOM_BINDING)
public class ImageService {

    public Image getImage(String path) {
        try {
            File image = new File("C:\\Users\\adam\\Desktop\\lotr_memes\\" + path);
            return ImageIO.read(image);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}