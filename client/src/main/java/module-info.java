module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;
    requires java.net.http;
    requires jjwt;
    requires static lombok;
    requires spring.web;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires spring.core;
    requires com.fasterxml.jackson.datatype.jsr310;

    exports com.example.client.dto to com.fasterxml.jackson.databind;
    opens com.example.client to javafx.fxml;
    exports com.example.client;
    exports com.example.client.controller;
    opens com.example.client.controller to javafx.fxml;
}