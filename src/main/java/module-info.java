module cz.cvut.fel.pjv.talacjos.jump_up_pjv {
    requires javafx.controls;
    requires javafx.fxml;


    opens cz.cvut.fel.pjv.talacjos.jump_up_pjv to javafx.fxml;
    exports cz.cvut.fel.pjv.talacjos.jump_up_pjv;
}