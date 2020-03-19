module teaGearPlanner {
    exports main;
    exports classes;

    opens classes to com.google.gson;
    opens main to com.google.gson;

    requires javafx.graphics;
    requires javafx.controls;
    requires com.google.gson;
    requires net.harawata.appdirs;
}