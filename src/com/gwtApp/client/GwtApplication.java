package com.gwtApp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gwtApp.util.Constants.*;

public class GwtApplication extends VerticalPanel {

    private Label lblServerReply = new Label();
    private TextBox userInput = new TextBox();
    private Button sendButton = new Button(ENTER);
    private Button sortButton = new Button(SORT);
    private Button resetButton = new Button(RESET);
    private Label invalidInputDataMassage = new Label();
    private VerticalPanel numbersPanel = new VerticalPanel();
    private Grid grid = new Grid(LIMIT, 1);
    private List<Integer> values;
    private boolean isSorted;
    private int count;


    public GwtApplication() {
        displayIntroScreen();

        sortButton.addClickHandler(event -> sortGrid());

        sendButton.addClickHandler(event -> {
            invalidInputDataMassage.setText(userInput
                    .getValue()
                    .matches(INPUT_REGEX) ? BLANK : INVALID_DATA_MASSAGE);
            if (BLANK.equals(invalidInputDataMassage.getText())) {
                count = Integer.parseInt(userInput.getText());
                displaySortScreen();
            }
        });

        resetButton.addClickHandler(event -> {
            RootPanel.get(HEADER).remove(0);
            userInput.setText(BLANK);
            grid.clear();
            displayIntroScreen();
        });

        final AsyncCallback<String> callback = new AsyncCallback<String>() {
            public void onSuccess(String result) {
                lblServerReply.setText(result);
            }

            public void onFailure(Throwable caught) {
                lblServerReply.setText(SERVER_ERROR_MSG);
            }
        };

        sendButton.addClickHandler(event -> getService().getString(userInput.getText(), callback));
    }

    private void displayIntroScreen() {
        RootPanel.get(HEADER).add(new Label(INTRO_SCREEN));
        Label question = new Label(USER_QUESTION);
        numbersPanel.clear();
        numbersPanel.add(question);
        numbersPanel.add(userInput);
        numbersPanel.add(sendButton);
        numbersPanel.add(invalidInputDataMassage);
        add(numbersPanel);
        setComponentsLocation(question);
    }

    private void displaySortScreen() {
        RootPanel.get(HEADER).remove(0);
        RootPanel.get(HEADER).add(new Label(SORT_SCREEN));

        numbersPanel.clear();
        setupGrid(count);

        HorizontalPanel gridPanel = new HorizontalPanel();
        gridPanel.add(grid);
        VerticalPanel buttonsPanel = new VerticalPanel();
        buttonsPanel.add(sortButton);
        buttonsPanel.add(resetButton);
        gridPanel.add(buttonsPanel);
        numbersPanel.add(gridPanel);
        setComponentsLocation(buttonsPanel);
    }

    private void sortGrid() {
        Integer[] array = new Integer[values.size()];
        fillArray(array);
        quickSort(array, 0, array.length - 1);
        if (!isSorted) {
            reverse(array);
        }
        values = Arrays.asList(array);
        isSorted = !isSorted;
        fillGrid();
    }

    private void fillArray(Integer[] array) {
        for (int i = 0; i < values.size(); i++) {
            array[i] = values.get(i);
        }
    }

    private void reverse(Integer[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            int temp = array[array.length - i - 1];
            array[array.length - i - 1] = array[i];
            array[i] = temp;
            if (!isSorted){
                values = Arrays.asList(array);
                fillGrid();
            }
        }
    }

    private void quickSort(Integer[] array, int low, int high) {
        if (array.length == 0 || low >= high)
            return;

        int middle = low + (high - low) / 2;
        int barrier = array[middle];

        int i = low, j = high;
        while (i <= j) {
            while (array[i] < barrier) {
                i++;
            }
            while (array[j] > barrier) {
                j--;
            }
            if (i <= j) {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
                if (isSorted){
                    values = Arrays.asList(array);
                    fillGrid();
                }
            }
        }
        if (low < j)
            quickSort(array, low, j);

        if (high > i)
            quickSort(array, i, high);
    }

    private void setupGrid(int rowsCount) {
        isSorted = false;
        values = Stream.concat(Stream.generate(() -> (int) (Math.random() * EXPECTED_NUM))
                .limit(1), Stream.generate(() -> (int) (Math.random() * MAX))
                .limit(rowsCount - 1))
                .collect(Collectors.toList());
        fillGrid();
    }

    private void fillGrid() {
        int columnsCount = values.size() / LIMIT;
        final int[] columnIndex = {0};
        while (columnsCount-- >= 0) {
            AtomicInteger rowIndex = new AtomicInteger();
            fillColumn(rowIndex, columnIndex[0]++);
        }
    }

    private void fillColumn(AtomicInteger rowIndex, int columnIndex) {
        grid.resizeColumns(columnIndex + 1);
        values.stream()
                .skip(columnIndex * LIMIT)
                .limit(LIMIT)
                .forEach(num -> {
                    Timer timer = new Timer() {
                        @Override
                        public void run() {
                            grid.setWidget(rowIndex.getAndIncrement(),
                                    columnIndex, getWidget(String.valueOf(num)));
                        }
                    };
                    timer.schedule(DELAY);
                });
    }

    private Button getWidget(String value) {
        Button button = new Button(value);
        button.addClickHandler(event -> {
            if (Integer.parseInt(button.getText()) <= 30) {
                grid.clear();
                setupGrid(Integer.parseInt(button.getText()));
            } else {
                PopupPanel popup = new PopupPanel(true, true);
                popup.setPopupPosition(500, 10);
                popup.add(new HTML(POPUP_MASSAGE));
                popup.show();
            }
        });
        return button;
    }

    private void setComponentsLocation(Label question) {
        numbersPanel.setCellHorizontalAlignment(question, ALIGN_CENTER);
        numbersPanel.setCellHorizontalAlignment(userInput, ALIGN_CENTER);
        numbersPanel.setCellHorizontalAlignment(sendButton, ALIGN_CENTER);
        numbersPanel.setCellHorizontalAlignment(invalidInputDataMassage, ALIGN_CENTER);
        numbersPanel.setSpacing(PADDING);
        numbersPanel.setWidth(NUMBERS_PANEL_WIDTH);
    }

    private void setComponentsLocation(VerticalPanel buttonsPanel) {
        buttonsPanel.setSpacing(PADDING);
        buttonsPanel.setWidth(BUTTONS_PANEL_WIDTH);
        buttonsPanel.setCellHorizontalAlignment(sortButton, ALIGN_CENTER);
        buttonsPanel.setCellHorizontalAlignment(resetButton, ALIGN_CENTER);
        grid.setCellPadding(PADDING);
        sortButton.setWidth(BUTTONS_WIDTH);
        resetButton.setWidth(BUTTONS_WIDTH);
    }

    public static GwtAppServiceAsync getService() {
        return GWT.create(GwtAppService.class);
    }
}
