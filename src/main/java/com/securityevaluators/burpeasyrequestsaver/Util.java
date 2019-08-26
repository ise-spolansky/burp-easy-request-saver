package com.securityevaluators.burpeasyrequestsaver;

import java.nio.file.Path;

public abstract class Util {
    public static String getBaseName(Path path) {
        String name = path.getFileName().toString();
        int index = name.lastIndexOf('.');
        if (index < 0) return name;
        return name.substring(0, index);
    }

    public static String getExtension(Path path) {
        String name = path.getFileName().toString();
        int index = name.lastIndexOf('.');
        if (index < 0) return "";
        return name.substring(index);
    }
}
