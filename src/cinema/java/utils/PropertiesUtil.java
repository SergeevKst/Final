package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class PropertiesUtil {

    public static final Properties PROPERTIES = new Properties();

   static  {
        loadProperties();
    }
    public static String get (String key){

       return PROPERTIES.getProperty(key);
    }

    private static void loadProperties(){

      try (var inputStream = Files.newInputStream(Path.of("resources","application.properties"))){
          PROPERTIES.load(inputStream);
      } catch (IOException e) {
          throw new RuntimeException(e);
      }

    }

    private PropertiesUtil() {
    }
}
