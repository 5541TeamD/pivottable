package ca.concordia.pivottable.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.net.URI;
import java.util.Map;
import java.util.HashMap;

public class FileUtils {

    /**
     * Quick way to read a UTF-8 file into a string
     * @param filePath the path of the file (it can be a file on disk or in resources)
     * @return The contents of the file
     * @throws IOException in case of error reading the file
     */
    public static String readFileContents(String filePath) throws IOException {
        byte[] encoded;
        try {
            encoded = Files.readAllBytes(Paths.get(filePath));
        } catch (NoSuchFileException|FileSystemNotFoundException nsfe) {
            URL url = FileUtils.class.getResource(filePath);
            FileSystem fs = null;
            try {
                URI fileURI = url.toURI();
                fs = initFileSystem(fileURI);
                encoded = Files.readAllBytes(Paths.get(fileURI));
            } catch (URISyntaxException uriSE) {
                throw new IOException(uriSE.getMessage());
            } catch (NullPointerException npe) {
                throw new IOException(npe.getMessage());
	    } finally {
                if (fs != null) {
                    fs.close();
                }
            }
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }

    private static FileSystem initFileSystem(URI uri) throws IOException
    {
        try
        {
            return FileSystems.getFileSystem(uri);
        }
        catch( FileSystemNotFoundException e )
        {
            Map<String, String> env = new HashMap<>();
            env.put("create", "true");
            return FileSystems.newFileSystem(uri, env);
        }
    }

}
