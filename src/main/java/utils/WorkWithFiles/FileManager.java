package utils.WorkWithFiles;

import com.google.common.base.Preconditions;
/*import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;*/
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import utils.AppUtil;
import utils.SysUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.testng.Assert.fail;

public abstract class FileManager {

    protected static Logger LOG = Logger.getLogger(FileManager.class);

    public static void addLineToFile(String filePath, String value) {
        ConcurrentMap<String, Object> map = new ConcurrentHashMap<>();
        File file = new File(filePath);
        String name = file.getName();
        Object lock = map.get(name);
        if (lock == null) {
            map.putIfAbsent(name, new Object());
            lock = map.get(name);
        }
        synchronized (lock) {
            try {
                FileReader file_read = new FileReader(filePath);
                BufferedReader br = new BufferedReader(file_read);
                String line;
                line = br.readLine();
                while (line != null) {
                    value += "\r\n" + line;
                    line = br.readLine();
                }
                FileWriter file_write;
                file_write = new FileWriter(filePath);
                file_write.write(value);
                file_write.close();
                file_read.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
                fail(String.valueOf(e));
            }
        }
    }

    public static void writeDataToFile(String filePath, String url) {
        try {
            FileReader file_read = new FileReader(filePath);
            BufferedReader br = new BufferedReader(file_read);
            String line = br.readLine();
            while (line != null) {
                url += "\r\n" + line;
                line = br.readLine();
            }
            FileWriter file_write1 = new FileWriter(filePath);
            file_write1.write(url);
            file_write1.close();
            file_read.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createEmptyFile(String path) {
        try {
            FileWriter file_write = new FileWriter(path);
            file_write.write("");
            file_write.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileNameFromFolderByPartOfName(String folderLocation, String partOfFileName) {
        String fName = null;
        File folder = new File(folderLocation);
        File[] files = folder.listFiles();
        for (File file : files) {
            String fN = file.getName();
            if (fN.contains(partOfFileName)) {
                fName = fN;
                break;
            }
        }
        return fName;
    }

    public static void cleanFolderByLocation(String location) {
        File folder = new File(location);
        if (folder.exists()) {
            for (File fileInFolder : folder.listFiles()) {
                fileInFolder.delete();
            }
        } else {
            LOG.info("Such Folder does not exist: " + location);
        }
    }

    public static void cleanFolderWithSubFoldersByLocation(String location) {
        File folder = new File(location);
        if (folder.exists()) {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    removeFolder(file);
                } else {
                    file.delete();
                }
            }
        } else {
            LOG.info("Such Folder does not exist: " + location);
        }
    }

    private static void removeFolder(File folder) {
        try {
            FileUtils.deleteDirectory(folder);
        } catch (IOException e) {
            LOG.info("Can't remove folder: '" + folder + "', due to error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getFileContent(String fullFilePath, String... encoding) {
        String content = null;
        File file = new File(fullFilePath);
        try {
            if (encoding.length > 0) {
                content = FileUtils.readFileToString(file, encoding[0]);
            } else {
                content = FileUtils.readFileToString(file, StandardCharsets.UTF_8.name());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /*public static List<String> parseLocalFileToList(String fileName, Integer... waitForFileInSec) {
        List<String> result = new ArrayList<>();
        URL url = FileManager.class.getClassLoader().getResource(fileName);
        String filePath = AppUtil.getRelativePathToImageWithClass(url);
        if (waitForFileInSec.length != 0) {
            waitForDownloadFileByLocation(filePath, waitForFileInSec[0]);
        }
        try {
            result = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOG.info("Can't read file by path '" + filePath + "', due to error: " + e.getMessage());
        }
        return result;
    }

    public static void copyInputStreamToFile(InputStream inputStream, File file) {
        Preconditions.checkNotNull(inputStream);
        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*public static List<String> getExcelData(String filePath) {
        File f = new File(filePath);
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(f);
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(0);
        ArrayList<ArrayList<String>> dataInExcelFile = new ArrayList<ArrayList<String>>();
        for (int j = 0; j < sheet.getRows(); j++) {
            ArrayList<String> singleList = new ArrayList<String>();
            for (int i = 0; i < sheet.getColumns(); i++) {
                singleList.add(sheet.getCell(i, j).getContents());
            }
            dataInExcelFile.add(singleList);
        }
        List<String> result = new ArrayList<>();
        for (int i = 0; i < dataInExcelFile.size(); i++) {
            result.add(dataInExcelFile.get(i).toString());
        }
        return result;
    }*/

    public static List<String> getLinesFromFileByPattern( String filePath, String pattern ) {
        List<String> lines = new ArrayList<>();
        File file = new File( filePath );
        Scanner s = null;
        try {
            s = new Scanner( file );
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        }
        int count = 0;

        assert s != null;
        while ( s.hasNextLine() ) {
            String line = s.nextLine();

            if ( s.nextLine().contains( pattern ) ) {
                count++;
                System.out.println( line );
                lines.add( line );
            }

        }
        System.out.println( "Number of lines containing a pattern: " + count );

        s.close();
        return lines;
    }

    public static List<String> parseRemoteFileToList(URL fileLocation) {
        List<String> arrayList = new ArrayList<String>();
        try {
            if (fileLocation != null) {
                LOG.info("Parsing file '" + fileLocation.getHost() + fileLocation.getPath() + "' with retrieved data...");
                URLConnection urlConnection = fileLocation.openConnection();
                urlConnection.setReadTimeout(5000);
                urlConnection.setConnectTimeout(5000);
                InputStream stream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String str;
                while ((str = reader.readLine()) != null) {
                    arrayList.add(str);
                    LOG.info(str);
                }
                reader.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static List parseFileToList(File fileLocation) {
        List<String> arrayList = new ArrayList<String>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileLocation));
            String str;
            LOG.info("Parsing file '" + fileLocation.getName() + "' with retrieved data...");
            if (in == null) {
                fail("There is no data in file!!!");
            }
            while ((str = in.readLine()) != null) {
                arrayList.add(str);
            }
            in.close();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
        return arrayList;
    }

    public static File waitForDownloadFileByLocation(String path, int TIME) {
        File file = new File(path);
        int d = 0;
        while (!file.exists()) {
            d++;
            SysUtils.sleep(1000);
            System.out.println("Wait for file " + d + "...");
            if (d == TIME) {
                fail("Can't find a file by path: '" + path + "'");
            }
        }
        return file;
    }

    public static void overWriteFileUseBufferedWriter(String path, String string) {
        //File file = new File(path);
        try {
            //FileWriter fileWriter = new FileWriter(path);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));

            bufferedWriter.write(string);
            bufferedWriter.newLine();

            bufferedWriter.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void overWriteFileUseFileOutputStream(String fullFilePath, String content, String fileEncoding) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fullFilePath);
            OutputStream outputStream = new BufferedOutputStream(fileOutputStream);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, fileEncoding);
            outputStreamWriter.write(content);
            outputStreamWriter.close();
        } catch (IOException e) {
            LOG.error("Can not overwrite file " + fullFilePath + " fue to error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void writeDataToFile(String[] dataCharacters, String pathToFile) {
        try {
            File file = new File(pathToFile);
            boolean folderExist = file.getParentFile().exists();
            if (!folderExist) {
                folderExist = file.getParentFile().mkdirs();
            }
            boolean fileExist = file.exists();
            if (!fileExist) {
                fileExist = file.createNewFile();
            }
            if (folderExist && fileExist) {
                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                for (String dataCharacter : dataCharacters) {
                    bufferedWriter.write(dataCharacter);
                    bufferedWriter.newLine();
                }
                bufferedWriter.close();
                LOG.info("Done");
            } else {
                fail("Can't write data into file: " + pathToFile);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }

    public static void waitForFileInSecByFile(File file, int seconds) {
        if (!file.exists()) {
            int i = 0;
            do {
                SysUtils.sleep(1000);
                if (i++ > seconds) try {
                    throw new FileNotFoundException();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    break;
                }
            }
            while (!file.exists());
        }
        SysUtils.sleep(2000);
    }

    public static File waitForFileDownload(String folderLocation, String fileName, int attempts) {
        int counter = attempts;
        File folder = new File(folderLocation);
        File[] files = folder.listFiles();
        File result = null;
        boolean found = false;
        while (!found && counter > 0) {
            assert files != null;
            for (File file : files) {
                String name = file.getName();
                if (name.equals(fileName) && !name.endsWith(".crdownload")) {
                    LOG.info("$$$ Filename is: " + name);
                    found = true;
                    result = file;
                    break;
                }
            }
            counter--;
            SysUtils.sleep(1000);
            files = folder.listFiles();
            LOG.info("$$$ Waiting for file downloading.." + counter);
        }
        return result;
    }

    public static String waitForFileDownloadingByPartOfTheName(String folderLocation, String fileName, int numberOfAttempts) {
        int counter = numberOfAttempts;
        File folder = new File(folderLocation);
        File[] files = folder.listFiles();
        while (counter > 0) {
            assert files != null;
            for (File file : files) {
                String name = file.getName();
                if (name.contains(fileName) && !name.endsWith(".crdownload")) {
                    LOG.info("$$$ Filename is: " + name);
                    if (file.length() > 0) {
                        return name;
                    }
                }
            }
            counter--;
            SysUtils.sleep(1000);
            files = folder.listFiles();
            LOG.info("$$$ Waiting for file downloading.." + counter);
        }
        LOG.info("$$$ File with '" + fileName + "' in name doesn't exists");
        return null;
    }

    public static String getFileNameByFullPath(String fullPath) {
        return fullPath.substring(fullPath.lastIndexOf(SysUtils.getFileSeparator()) + 1);
    }

    /**
     * Excel file parser.
     */

    /*public static String[][] getExcelDataAndDeleteFile(String filePath) {
        File file = new File(filePath);
        waitForFileInSecByFile(file, 30);
        try {
            assert file.length() > 0 : "File '" + filePath + "' is empty.";

            Workbook workbook = Workbook.getWorkbook(file);
            assert workbook.getSheets().length != 0 : "Workbook does not contain any sheets.";

            Sheet sheet = workbook.getSheet(0);
            String[][] data = new String[sheet.getRows()][sheet.getColumns()];
            for (int j = 0; j < sheet.getRows(); j++) {
                for (int i = 0; i < sheet.getColumns(); i++) {
                    data[j][i] = sheet.getCell(i, j).getContents();
                }
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            boolean isFileDelete = file.delete();
            if (!isFileDelete) {
                LOG.error("Can not delete file : " + filePath);
            }
        }
        return null;
    }*/

    /**
     * Csv file parser.
     */

  /*  public static List<List<String>> getCsvFileData(String filePath, Integer... waitForFileInSec) {
        List<List<String>> fileData = new ArrayList<>();
        List<String> fileLines = parseLocalFileToList(filePath, waitForFileInSec);
        String csvRegex = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
        for (String fileLine : fileLines) {
            String[] lineValues = fileLine.split(csvRegex);
            for (int i = 0; i < lineValues.length; i++) {
                String lineRegex = "\".*,.*\"";
                Pattern pattern = Pattern.compile(lineRegex);
                Matcher matcher = pattern.matcher(lineValues[i]);
                if (matcher.find()) {
                    lineValues[i] = matcher.group(0).replace("\"", "");
                }
            }
            fileData.add(Arrays.asList(lineValues));
        }
        return fileData;
    }*/

    /**
     * XML file parser.
     */

    public static HashMap<String, File> unpackZipArchive(String filePath, String outputFolder) {
        HashMap<String, File> fileMap = new HashMap<>();
        try {
            FileInputStream zipFileInputStream = new FileInputStream(filePath);
            ZipInputStream zipInputStream = new ZipInputStream(zipFileInputStream);

            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String newFileName = zipEntry.getName();
                File newFile = new File(outputFolder, File.separator + newFileName);
                fileMap.put(newFileName, newFile);

                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                int length;
                byte[] buffer = new byte[1024];
                while ((length = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
                fileOutputStream.close();

                zipEntry = zipInputStream.getNextEntry();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileMap;
    }

    public static Document parseXMLFile(File file) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(file);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return document;
    }

}
