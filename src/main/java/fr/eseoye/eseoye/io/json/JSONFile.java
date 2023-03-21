package fr.eseoye.eseoye.io.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class JSONFile {

    private static final ObjectMapper FILES_MAPPER = new ObjectMapper().enable(DeserializationFeature.USE_LONG_FOR_INTS);

    protected Path path;

    private volatile Map<String, Object> dataMap;

    /**
     * Create a new JSON file instance
     *
     * @param path     - the path of the file
     * @param fileName - the file name with the extension
     */
    public JSONFile(String path, String fileName) {
        this.path = Path.of(path, fileName);

        try {
            readFile();

            reviewFormat();

        } catch (IOException e) {
            System.out.println("Unexpected error while loading file " + this.path);
        }
    }

    /**
     * Using this function to control if the value contained in the file are those expected
     */
    public abstract void reviewFormat();

    /**
     * Using this function to apply change to the content right before saving the file
     */
    public abstract void preSave();

    /**
     * Save the file and write the content
     *
     * @throws IOException
     */
    public void saveFile() throws IOException {
        preSave();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    FileWriter fw = new FileWriter(path.toString());

                    fw.write(FILES_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(dataMap));

                    fw.flush();
                    fw.close();

                    readFile();

                } catch (IOException e) {
                    System.out.println("Unexpected error while saving file " + path);
                }

            }
        }, "File-Save-Thread").start();
    }

    /**
     * Reload the file instance running in the program
     * Any change made to the original file that are not saved will be overwritten
     *
     * @throws IOException
     */
    public void reloadFile() throws IOException {
        this.dataMap.clear();

        readFile();
    }

    /**
     * Get the file path (absolute path + file name)
     *
     * @return a path object
     */
    public Path getFilePath() {
        return this.path;
    }

    /**
     * Cast the raw file object to a HashMap
     *
     * @return a HashMap representing the file
     */
    protected HashMap<String, Object> getData() {
        return (HashMap<String, Object>) this.dataMap;
    }

    /**
     * Cast key representing an HashMap to access data
     *
     * @return a HashMap representing a JSON Object
     */
    @SuppressWarnings("unchecked")
    protected HashMap<String, Object> getData(String src) {
        Object obj = getData().get(src);
        if (obj instanceof HashMap) return (HashMap<String, Object>) obj;
        else throw new ClassCastException("The key " + src + " didn't contained a HashMap ");
    }

    /**
     * Read the file's content
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    protected void readFile() throws IOException {
        try {
            this.dataMap = FILES_MAPPER.readValue(getClass().getClassLoader().getResourceAsStream(this.path.toString()), HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
            this.dataMap = new HashMap<>();
        }
    }
}