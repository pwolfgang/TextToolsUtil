module edu.temple.cla.papolicy.wolfgang.TextToolsUtil {
    requires edu.temple.cla.papolicy.wolfgang.StemmerFactory;

    requires transitive java.logging;
    requires transitive java.sql;

    exports edu.temple.cla.papolicy.wolfgang.texttools.util;
    exports picocli;

}
