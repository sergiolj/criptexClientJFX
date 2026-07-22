module br.edu.ucsal.sergiolj.criptexclientjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;


    opens br.edu.ucsal.sergiolj.criptexclientjfx to javafx.fxml;
    exports br.edu.ucsal.sergiolj.criptexclientjfx;
    exports shared;
    opens shared to javafx.fxml;
    exports shared.dto;
    opens shared.dto to javafx.fxml;
}