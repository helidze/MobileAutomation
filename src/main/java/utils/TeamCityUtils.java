package utils;

import com.google.common.base.Preconditions;
import org.apache.ws.commons.util.Base64;
import org.testng.Assert;
import org.apache.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class TeamCityUtils {

    public void downloadApplication(String urlForDownloading, String appLocation) {
        String enc_string = "admin:123456";
        String encoding = Base64.encode(enc_string.getBytes());

        try {
            Preconditions.checkNotNull(urlForDownloading);
            Preconditions.checkNotNull(appLocation);

            URL url = new URL(urlForDownloading);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + encoding);

            ReadableByteChannel rbc = Channels.newChannel(urlConnection.getInputStream());
            FileOutputStream fos = new FileOutputStream(appLocation);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            File file = new File(appLocation);
            Assert.assertTrue(file.exists());
        } catch (Exception e) {
            Assert.fail("File doesn't exists. Check url from TeamCity:\n" + e.getMessage());
        }
    }

}
