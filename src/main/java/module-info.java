module cz.cvut.fel.pjv.talacjos.jump_up {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;
    requires com.google.gson;


    opens cz.cvut.fel.pjv.talacjos.jump_up to javafx.fxml;
    exports cz.cvut.fel.pjv.talacjos.jump_up;
    exports  cz.cvut.fel.pjv.talacjos.jump_up.controller;
}