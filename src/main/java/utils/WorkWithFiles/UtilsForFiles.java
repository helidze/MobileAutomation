package utils.WorkWithFiles;


import org.apache.log4j.Logger;

import org.testng.Assert;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UtilsForFiles {
    private static Logger LOG = Logger.getLogger(UtilsForFiles.class);

    public static Map downloadAndUnpackArchive(InputStream inputStream, File targetDir) throws IOException {
        LOG.info("Download location is: " + targetDir);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        File zip = null;  //"arc"+System.currentTimeMillis()
        try {
            if (inputStream.available() == 0) {
                throw new IOException("Download content is null");
            }
            InputStream in = new BufferedInputStream(inputStream);
            // make sure we get the actual file
            zip = File.createTempFile("arc" + System.currentTimeMillis(), ".zip", targetDir);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(zip));

            copyInputStream(in, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        return unpackArchive(zip, targetDir);


    }

    /**
     * Unpack a zip file
     *
     * @param theFile
     * @param targetDir
     * @return the file
     * @throws IOException
     */
    public static Map unpackArchive(File theFile, File targetDir) throws IOException {
        if (!theFile.exists()) {
            throw new IOException(theFile.getAbsolutePath() + " does not exist");
        }
        if (!buildDirectory(targetDir)) {
            throw new IOException("Could not create directory: " + targetDir);
        }
        ZipFile zipFile = new ZipFile(theFile);
        Map<String, File> map = new HashMap<String, File>();
        for (Enumeration entries = zipFile.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            File file = new File(targetDir, File.separator + entry.getName());
            map.put(entry.getName(), file);
            if (!buildDirectory(file.getParentFile())) {
                throw new IOException("Could not create directory: " + file.getParentFile());
            }
            if (!entry.isDirectory()) {
                copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(file)));
            } else {
                if (!buildDirectory(file)) {
                    throw new IOException("Could not create directory: " + file);
                }
            }
        }
        zipFile.close();
        return map;
    }

    public static void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len = in.read(buffer);
        while (len >= 0) {
            out.write(buffer, 0, len);
            len = in.read(buffer);
        }
        in.close();
        out.close();
    }

    public static boolean buildDirectory(File file) {
        return file.exists() || file.mkdirs();
    }


}