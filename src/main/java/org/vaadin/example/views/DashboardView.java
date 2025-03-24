package org.vaadin.example.views;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import org.vaadin.example.backend.entity.Book;
import org.vaadin.example.backend.service.BookService;

import java.util.function.Consumer;
import java.util.stream.Stream;

@Route("dashboard")
public class DashboardView extends VerticalLayout {
    private final BookService bookService;
    private final Grid<Book> grid = new Grid<>(Book.class, false);

    public DashboardView(BookService bookService) {
        this.bookService = bookService;
        configureGrid();
        add(
                new H1("Book Store Dashboard"),
                createAddButton(),
                grid
        );
        updateGrid();
    }

    private void configureGrid() {
        grid.addColumn(Book::getTitle).setHeader("Title");
        grid.addColumn(Book::getAuthor).setHeader("Author");
        grid.addColumn(Book::getPublicationYear).setHeader("Year");

        grid.addComponentColumn(book -> {
            HorizontalLayout buttons = new HorizontalLayout();

            // Edit Button
            Button editBtn = new Button(VaadinIcon.EDIT.create());
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editBtn.addClickListener(e -> showEditDialog(book));

            // Delete Button
            Button deleteBtn = new Button(VaadinIcon.TRASH.create());
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteBtn.addClickListener(e -> {
                showConfirmDeleteDialog(this::deleteBookRecord, book);

            });

            buttons.add(editBtn, deleteBtn);
            return buttons;
        }).setHeader("Actions");


        // Make rows clickable
        grid.addItemClickListener(e -> showDetailDialog(e.getItem()));
        grid.setWidthFull();
    }

    private void deleteBookRecord(Book book){
        bookService.delete(book);
        updateGrid();
    }

    private void showDetailDialog(Book book) {
        Dialog dialog = new Dialog();
        VerticalLayout mainLayout = new VerticalLayout();

        // Title with bold and large font using Heading component
        H2 title = new H2(book.getTitle());
        title.getStyle().set("margin", "0");
        HorizontalLayout titleLayout = new HorizontalLayout(title);
        titleLayout.setWidthFull();
        titleLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        // Key-Value pairs using HorizontalLayout
        HorizontalLayout authorRow = new HorizontalLayout(
//                getBoldText("Author: "),

        );


        var span1 = new Span("Author: ");
//        span1.addClassName("title_span");
        // Apply inline CSS styles
        span1.getStyle()
                .set("font-weight", "bold")    // Make text bold
                .set("font-size", "18px");
        var span2 = new Span(book.getAuthor());
        authorRow.add(span1, span2);


        HorizontalLayout yearRow = new HorizontalLayout(
                new Span("Publication Year:"),
                new Span(book.getPublicationYear().toString())
        );

        HorizontalLayout descRow = new HorizontalLayout(
                new Span("Description:"),
                new Span(book.getDescription())
        );

        // Configure rows
        Stream.of(authorRow, yearRow, descRow).forEach(row -> {
            row.setSpacing(true);
            row.setAlignItems(Alignment.BASELINE);
        });

        // Add all components to layout
        mainLayout.add(
                title,
                new VerticalLayout(authorRow, yearRow, descRow)
        );

        mainLayout.setSpacing(false);
        mainLayout.setPadding(false);
        dialog.add(mainLayout);
        dialog.open();
    }

    private void showConfirmDeleteDialog(Consumer<Book> consumer, Book book) {
        Dialog dialog = new Dialog();
        VerticalLayout mainLayout = new VerticalLayout();

        // Title with bold and large font using Heading component
        H2 title = new H2("Are you sure you want to delete this book?");
        title.getStyle().set("margin", "0");
        mainLayout.add(title);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        horizontalLayout.setSpacing(true);

        Button yesBtn = new Button("Yes", VaadinIcon.FILE_REMOVE.create(), buttonClickEvent -> {
            //call callback function to remove record from db
            consumer.accept(book);
            dialog.close();
        });


        Button noBtn = new Button("No", VaadinIcon.REPLY.create(), buttonClickEvent -> {
            //call callback function to remove record from db
            dialog.close();
        });
        horizontalLayout.add(yesBtn);
        horizontalLayout.add(noBtn);
        mainLayout.add(horizontalLayout);
        dialog.add(mainLayout);
        dialog.open();

    }

    private Button createAddButton() {
        Button button = new Button("Add Book", VaadinIcon.PLUS.create());
        button.addClickListener(e -> showAddDialog());
        return button;
    }

    private void showAddDialog() {
        Dialog dialog = new Dialog();
        TextField title = new TextField("Title");
        TextField author = new TextField("Author");
        TextField year = new TextField("Publication Year");
        TextArea description = new TextArea("Description");

        year.setPattern("\\d*");
        year.setMaxLength(4);

        Binder<Book> binder = new Binder<>(Book.class);

        configureBinder(binder, title,author, year, description);

        Button save = new Button("Save", e -> {
            if (binder.validate().isOk()) {
                Book book = new Book();
                try {
                    binder.writeBean(book);
                    bookService.save(book);
                    updateGrid();
                    dialog.close();
                } catch (ValidationException ex) {
                    Notification.show("Validation error: " + ex.getMessage());
                }
            }
        });

        FormLayout form = new FormLayout();
        form.add(title, author, year, description);
        dialog.add(form, save);
        dialog.open();
    }

    private void showEditDialog(Book book) {
        Dialog dialog = new Dialog();
        VerticalLayout layout = new VerticalLayout();

        // Form fields
        TextField title = new TextField("Title");
        TextField author = new TextField("Author");
        TextField year = new TextField("Publication Year");
        TextArea description = new TextArea("Description");

        // Initialize fields with existing values
        title.setValue(book.getTitle());
        author.setValue(book.getAuthor());
        year.setValue(book.getPublicationYear().toString());
        description.setValue(book.getDescription());

        // Validation binder
        Binder<Book> binder = new Binder<>(Book.class);
        configureBinder(binder, title, author, year, description);

        Button save = new Button("Save", e -> {
            if (binder.validate().isOk()) {
                try {
                    binder.writeBean(book);
                    bookService.save(book);
                    updateGrid();
                    dialog.close();
                    Notification.show("Book updated successfully");
                } catch (ValidationException ex) {
                    Notification.show("Validation error: " + ex.getMessage());
                }
            }
        });

        layout.add(
                new H3("Edit Book"),
                createFormLayout(title, author, year, description),
                save
        );

        dialog.add(layout);
        dialog.open();
    }

    private FormLayout createFormLayout(TextField title,
                                        TextField author,
                                        TextField year,
                                        TextArea description) {
        FormLayout form = new FormLayout();
        form.add(
                title,
                author,
                year,
                description
        );
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );
        return form;
    }

    private void configureBinder(Binder<Book> binder,
                                 TextField title,
                                 TextField author,
                                 TextField year,
                                 TextArea description) {

        binder.forField(title)
                .asRequired("Title is required")
                .bind(Book::getTitle, Book::setTitle);

        binder.forField(author)
                .asRequired("Author is required")
                .bind(Book::getAuthor, Book::setAuthor);

        binder.forField(year)
                .asRequired("Year is required")
                .withValidator(v -> !v.isBlank(), "Year cannot be empty")
                .withValidator(v -> v.matches("\\d+"), "Must be a valid year")
                .bind(
                        book -> book.getPublicationYear() != null ? book.getPublicationYear().toString() : "",
                        (book, v) -> book.setPublicationYear(Integer.parseInt(v))
                );

        binder.forField(description)
                .asRequired("Description is required")
                .bind(Book::getDescription, Book::setDescription);
    }

    private void updateGrid() {
        grid.setItems(bookService.findAll());
    }
}