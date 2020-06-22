package com.leanix.rssfeed.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestLoadPropertyFile {

    Properties prop;


    public Properties getProperty(String path) throws IOException {

        prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);

        prop.load(inputStream);

        return prop;
    }

}
