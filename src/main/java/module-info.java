module criptex{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.desktop;
    requires java.sql;


    exports criptex;
    opens criptex to javafx.fxml;

    exports shared.remote;
    opens shared.remote to javafx.fxml;
    exports shared.dto;
    opens shared.dto to javafx.fxml;
    exports util;
    opens util to javafx.fxml;
    exports controller;
    opens controller to javafx.fxml;
    exports ui.component;
    opens ui.component to javafx.fxml;
    exports model;
    opens model to javafx.fxml;
    exports view.manager;
    opens view.manager to javafx.fxml;
    exports rmi.network;
    opens rmi.network to javafx.fxml;
}