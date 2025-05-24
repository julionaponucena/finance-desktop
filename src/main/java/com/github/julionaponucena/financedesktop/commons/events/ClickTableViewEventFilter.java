package com.github.julionaponucena.financedesktop.commons.events;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClickTableViewEventFilter implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        Node node =(Node) event.getTarget();

        if(this.containsButton(node)) {
            event.consume();
        }
    }

    private boolean containsButton(Node node) {
        while (node != null && !(node instanceof MFXButton)) {
            node = node.getParent();
        }

        return node != null;
    }
}
