module criptex{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.desktop;


    exports criptex;
    opens criptex to javafx.fxml;

    exports shared.remote;
    opens shared.remote to javafx.fxml;
    exports shared.status;
    opens shared.status to javafx.fxml;
    exports shared.dto;
    opens shared.dto to javafx.fxml;
    exports util;
    opens util to javafx.fxml;
    exports controller;
    opens controller to javafx.fxml;
    exports service;
    opens service to javafx.fxml;
    exports client;
    opens client to javafx.fxml;
}