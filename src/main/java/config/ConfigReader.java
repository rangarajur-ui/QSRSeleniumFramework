package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties prop;

    static{
        prop = new Properties();
        try(FileInputStream fis = new FileInputStream("config.properties")) {
            prop.load(fis);
        }catch(IOException e){
            throw new RuntimeException("config.properties not found at project root",e);
        }
    }

    public static String getProperty(String key){
        return prop.getProperty(key);
    }
}
