module lab {
    requires transitive javafx.controls;
    requires javafx.media;
    requires javafx.fxml;
    requires javafx.base;
    requires org.apache.logging.log4j;
    requires static lombok;
    requires javafx.graphics;
    requires java.desktop;
    requires java.logging;
    requires java.net.http;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens lab to javafx.fxml, com.fasterxml.jackson.databind;
    exports lab;
}