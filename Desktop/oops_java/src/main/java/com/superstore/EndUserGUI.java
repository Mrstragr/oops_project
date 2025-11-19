package com.superstore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * EndUserGUI for end user operations.
 * Supports browsing categories/subcategories/items in hierarchical windows,
 * search with partial matching, and alphabetical sorting.
 */
public class EndUserGUI extends JFrame {
    private Server server;
    private EndUser user;

    public EndUserGUI(Server server, EndUser user) {
        this.server = server;
        this.user = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("End User Dashboard - " + user.getName());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Menu Bar
        setJMenuBar(createMenuBar());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel Blue
        JLabel titleLabel = new JLabel("End User Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel with Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Browse Tab
        JPanel browsePanel = new JPanel(new GridLayout(3, 1, 10, 10));
        browsePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        browsePanel.setBackground(new Color(245, 245, 245));

        JButton selectStoreBtn = createStyledButton("Select Store to Browse", "Choose a store to start browsing categories");
        selectStoreBtn.addActionListener(e -> showStoreSelection());
        browsePanel.add(selectStoreBtn);

        JButton searchBtn = createStyledButton("Search Items", "Search for items across all stores with partial matching");
        searchBtn.addActionListener(e -> showSearchDialog());
        browsePanel.add(searchBtn);

        JButton viewAllBtn = createStyledButton("View All Available Items", "Browse all items in all stores");
        viewAllBtn.addActionListener(e -> showAllItems());
        browsePanel.add(viewAllBtn);

        tabbedPane.addTab("Browse & Search", browsePanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Status Bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        add(statusPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            dispose();
            new LoginGUI(server);
        });
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);

        // View Menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem refreshItem = new JMenuItem("Refresh");
        refreshItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Refreshed"));
        viewMenu.add(refreshItem);
        menuBar.add(viewMenu);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "End User Dashboard\nBrowse stores, search items, and view availability."));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JButton createStyledButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setToolTipText(tooltip);
        return button;
    }

    // Show store selection dialog
    private void showStoreSelection() {
        String[] storeIds = server.getStores().keySet().toArray(new String[0]);
        String selectedStoreId = (String) JOptionPane.showInputDialog(
            this,
            "Select a Store:",
            "Choose Store",
            JOptionPane.QUESTION_MESSAGE,
            null,
            storeIds,
            storeIds[0]
        );

        if (selectedStoreId != null) {
            Store store = server.getStore(selectedStoreId);
            if (store != null) {
                showCategorySelection(store);
            }
        }
    }

    // Show category selection dialog for a store
    private void showCategorySelection(Store store) {
        String[] categoryNames = store.getCategories().stream()
            .map(Category::getName)
            .toArray(String[]::new);

        String selectedCategoryName = (String) JOptionPane.showInputDialog(
            this,
            "Select a Category:",
            "Choose Category",
            JOptionPane.QUESTION_MESSAGE,
            null,
            categoryNames,
            categoryNames[0]
        );

        if (selectedCategoryName != null) {
            Category selectedCategory = store.getCategories().stream()
                .filter(cat -> cat.getName().equals(selectedCategoryName))
                .findFirst()
                .orElse(null);

            if (selectedCategory != null) {
                showSubcategorySelection(store, selectedCategory);
            }
        }
    }

    // Show subcategory selection dialog
    private void showSubcategorySelection(Store store, Category category) {
        String[] subcategoryNames = category.getSubcategories().stream()
            .map(Subcategory::getName)
            .toArray(String[]::new);

        String selectedSubcategoryName = (String) JOptionPane.showInputDialog(
            this,
            "Select a Subcategory:",
            "Choose Subcategory",
            JOptionPane.QUESTION_MESSAGE,
            null,
            subcategoryNames,
            subcategoryNames[0]
        );

        if (selectedSubcategoryName != null) {
            Subcategory selectedSubcategory = category.getSubcategories().stream()
                .filter(sub -> sub.getName().equals(selectedSubcategoryName))
                .findFirst()
                .orElse(null);

            if (selectedSubcategory != null) {
                showItemsDialog(store, category, selectedSubcategory);
            }
        }
    }

    // Show items in a new window with sorting option
    private void showItemsDialog(Store store, Category category, Subcategory subcategory) {
        JDialog itemsDialog = new JDialog(this, "Items in " + subcategory.getName(), true);
        itemsDialog.setSize(500, 400);
        itemsDialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Items list
        List<Item> items = new ArrayList<>(subcategory.getItems());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        updateItemList(listModel, items, false); // Initially unsorted

        JList<String> itemList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(itemList);
        dialogPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton sortBtn = new JButton("Sort Alphabetically");
        sortBtn.addActionListener(e -> {
            updateItemList(listModel, items, true);
        });
        buttonPanel.add(sortBtn);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> itemsDialog.dispose());
        buttonPanel.add(closeBtn);

        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

        itemsDialog.add(dialogPanel);
        itemsDialog.setVisible(true);
    }

    private void updateItemList(DefaultListModel<String> model, List<Item> items, boolean sort) {
        model.clear();
        if (sort) {
            items.sort(Comparator.comparing(Item::getName));
        }
        for (Item item : items) {
            model.addElement(item.toString() + " (Qty: " + item.getQuantity() + ")");
        }
    }

    // Show search dialog with partial matching
    private void showSearchDialog() {
        JDialog searchDialog = new JDialog(this, "Search Items", true);
        searchDialog.setSize(500, 300);
        searchDialog.setLocationRelativeTo(this);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Search for item (partial name):"));
        inputPanel.add(searchField);
        inputPanel.add(searchBtn);
        searchPanel.add(inputPanel, BorderLayout.NORTH);

        DefaultListModel<String> resultsModel = new DefaultListModel<>();
        JList<String> resultsList = new JList<>(resultsModel);
        JScrollPane resultsScroll = new JScrollPane(resultsList);
        searchPanel.add(resultsScroll, BorderLayout.CENTER);

        searchBtn.addActionListener(e -> {
            String query = searchField.getText().toLowerCase().trim();
            resultsModel.clear();

            List<String> results = new ArrayList<>();
            for (Store store : server.getStores().values()) {
                for (Category cat : store.getCategories()) {
                    for (Subcategory sub : cat.getSubcategories()) {
                        for (Item item : sub.getItems()) {
                            if (item.getName().toLowerCase().contains(query) && item.getQuantity() > 0) {
                                results.add(store.getName() + " > " + cat.getName() + " > " + sub.getName() + " > " + item.toString() + " (Qty: " + item.getQuantity() + ")");
                            }
                        }
                    }
                }
            }

            // Sort results alphabetically
            results.sort(String::compareToIgnoreCase);
            for (String result : results) {
                resultsModel.addElement(result);
            }

            if (results.isEmpty()) {
                resultsModel.addElement("No items found matching '" + query + "'");
            }
        });

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> searchDialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(closeBtn);
        searchPanel.add(buttonPanel, BorderLayout.SOUTH);

        searchDialog.add(searchPanel);
        searchDialog.setVisible(true);
    }

    // Show all available items across stores
    private void showAllItems() {
        JDialog allItemsDialog = new JDialog(this, "All Available Items", true);
        allItemsDialog.setSize(600, 500);
        allItemsDialog.setLocationRelativeTo(this);

        JTextArea allItemsText = new JTextArea();
        allItemsText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(allItemsText);

        StringBuilder allItems = new StringBuilder();
        for (Store store : server.getStores().values()) {
            allItems.append("=== ").append(store.getName()).append(" ===\n");
            for (Category cat : store.getCategories()) {
                allItems.append("Category: ").append(cat.getName()).append("\n");
                for (Subcategory sub : cat.getSubcategories()) {
                    allItems.append("  Subcategory: ").append(sub.getName()).append("\n");
                    for (Item item : sub.getItems()) {
                        if (item.getQuantity() > 0) {
                            allItems.append("    ").append(item.toString()).append(" (Qty: ").append(item.getQuantity()).append(")\n");
                        }
                    }
                }
            }
            allItems.append("\n");
        }

        allItemsText.setText(allItems.toString());
        allItemsDialog.add(scrollPane);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> allItemsDialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(closeBtn);
        allItemsDialog.add(buttonPanel, BorderLayout.SOUTH);

        allItemsDialog.setVisible(true);
    }
}
