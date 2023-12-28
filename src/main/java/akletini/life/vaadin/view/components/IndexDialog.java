package akletini.life.vaadin.view.components;

import akletini.life.core.index.Indexes;
import akletini.life.core.index.service.IndexService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.io.IOException;

public class IndexDialog extends VerticalLayout {

    Grid<Indexes> grid;
    Dialog dialog;

    public IndexDialog(IndexService indexService) {
        dialog = new Dialog();
        grid = new Grid<>();
        Button startIndexButton = new Button("Start index");
        startIndexButton.addClickListener(event -> {
            try {
                if (grid.getSelectedItems().size() == Indexes.values().length) {
                    indexService.reIndexAllIndexes();
                } else {
                    for (Indexes index : grid.getSelectedItems()) {
                        indexService.reIndexSingleIndex(index.getIndexName());
                    }
                }
            } catch (IOException e) {
                add(new ErrorModal(e.getMessage()));
            }
        });

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(Indexes::name)
                .setHeader("Index")
                .setSortable(true);
        grid.setItems(Indexes.values());
        grid.setMinWidth("50em");
        dialog.add(grid);
        dialog.add(startIndexButton);
        add(dialog);
        dialog.open();
    }
}
