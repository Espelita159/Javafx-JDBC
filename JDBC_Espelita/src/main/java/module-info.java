module com.example.jdbc_espelita {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.jdbc_espelita to javafx.fxml;
    exports com.example.jdbc_espelita;
}