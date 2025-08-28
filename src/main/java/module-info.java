module com.madmax {

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.madmax to javafx.fxml, com.google.gson;
    opens com.madmax.Controllers to javafx.fxml;
    opens com.madmax.Models to javafx.fxml, com.google.gson;
    opens com.madmax.Simulation to com.google.gson;

    exports com.madmax;
    exports com.madmax.Models;

}
