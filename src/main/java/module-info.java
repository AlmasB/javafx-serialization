/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
module com.almasb.javafx.serialization {
    requires kotlin.stdlib;
    requires javafx.controls;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.module.kotlin;

    exports com.almasb.javafx.serialization;
}