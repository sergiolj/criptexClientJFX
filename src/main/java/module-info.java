module br.edu.ucsal.sergiolj.criptexclientjfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens br.edu.ucsal.sergiolj.criptexclientjfx to javafx.fxml;
    exports br.edu.ucsal.sergiolj.criptexclientjfx;
}