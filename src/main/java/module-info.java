module com.github.julionaponucena.financedesktop {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;
    requires MaterialFX;
    requires VirtualizedFX;
    requires mfx.resources;
    requires mfx.core;
    requires flyway.core;

    exports com.github.julionaponucena.financedesktop;

    opens com.github.julionaponucena.financedesktop to
            javafx.graphics,
            javafx.fxml,
            MaterialFX,
            VirtualizedFX,
            mfx.resources,
            mfx.core;

    opens com.github.julionaponucena.financedesktop.modules.main to javafx.fxml;
    opens com.github.julionaponucena.financedesktop.modules.index to javafx.fxml;
    opens com.github.julionaponucena.financedesktop.modules.blockregister.controllers to javafx.fxml;
    opens com.github.julionaponucena.financedesktop.modules.blockregister.controllers.factories to javafx.fxml;
    opens com.github.julionaponucena.financedesktop.modules.blockregister.services to javafx.fxml;
    opens com.github.julionaponucena.financedesktop.modules.blockregister.viewmodels to javafx.fxml;
    opens com.github.julionaponucena.financedesktop.modules.registers.controllers to javafx.fxml;
    opens com.github.julionaponucena.financedesktop.modules.category.controllers to javafx.fxml;
    opens fxmls to javafx.fxml;
    opens css to javafx.fxml;
    opens migrations;
    opens com.github.julionaponucena.financedesktop.modules.blockregister.controllers.observers to javafx.fxml;
    opens com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels to javafx.fxml;
    opens com.github.julionaponucena.financedesktop.modules.registers.services to javafx.fxml;
    opens com.github.julionaponucena.financedesktop.modules.main.controllers.factories to javafx.fxml;
    opens com.github.julionaponucena.financedesktop.modules.main.controllers to javafx.fxml;
    opens com.github.julionaponucena.financedesktop.modules.registers.services.converters to javafx.fxml;
    opens com.github.julionaponucena.financedesktop.commons to javafx.fxml;
    opens com.github.julionaponucena.financedesktop.commons.listmanager to javafx.fxml;
}