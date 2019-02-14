module TextToolsUtil {
    requires StemmerFactory;
    requires info.picocli;
    requires log4j;

    requires transitive java.logging;
    requires transitive java.sql;

    exports edu.temple.cla.papolicy.wolfgang.texttools.util;

}
