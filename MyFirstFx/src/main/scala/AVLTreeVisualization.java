import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class AVLTreeVisualization extends Application {

    // Define the dimensions of the application window
    private static final int WIDTH = 800;
    private static final int HEIGHT = 650;

    private AVLTreePane avlTreePane; // The pane for drawing the AVL tree

    @Override
    public void start(Stage primaryStage) {
        avlTreePane = new AVLTreePane(); // Initialize the AVL tree pane

        // Create input elements for inserting, deleting, and finding values in the AVL tree
        TextField valueField = new TextField();
        Button insertButton = new Button("Insert");
        insertButton.setOnAction(e -> insertValue(valueField.getText()));

        TextField deleteField = new TextField();
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteValue(deleteField.getText()));

        TextField findField = new TextField();
        Button findButton = new Button("Find");
        findButton.setOnAction(e -> findValue(findField.getText()));

        // Arrange the input elements horizontally
        HBox inputBox = new HBox(valueField, insertButton, deleteField, deleteButton, findField, findButton);
        inputBox.setSpacing(10);
        inputBox.setPadding(new Insets(10));

        // Create a vertical layout to hold the input elements and the AVL tree pane
        VBox root = new VBox(inputBox, avlTreePane);
        root.setPadding(new Insets(10));

        // Create the main scene with the root layout and set it to the primary stage
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setTitle("AVL Tree Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to handle the insertion of a value into the AVL tree
    private void insertValue(String value) {
        try {
            int val = Integer.parseInt(value); // Convert the input value to an integer
            avlTreePane.insertValue(val); // Insert the value into the AVL tree pane
        } catch (NumberFormatException e) {
            // Handle invalid input (non-numeric input)
        }
    }

    // Method to handle the deletion of a value from the AVL tree
    private void deleteValue(String value) {
        try {
            int val = Integer.parseInt(value); // Convert the input value to an integer
            avlTreePane.deleteValue(val); // Delete the value from the AVL tree pane
        } catch (NumberFormatException e) {
            // Handle invalid input (non-numeric input)
        }
    }

    // Method to handle finding a value in the AVL tree
    private void findValue(String value) {
        try {
            int val = Integer.parseInt(value); // Convert the input value to an integer
            boolean found = avlTreePane.findValue(val); // Check if the value exists in the AVL tree
            if (found) {
                System.out.println("Value found: " + val);
            } else {
                System.out.println("Value not found: " + val);
            }
        } catch (NumberFormatException e) {
            // Handle invalid input (non-numeric input)
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Inner class representing the pane to draw the AVL tree
    private class AVLTreePane extends Pane {
        private AVLTree<Integer> tree; // The AVL tree data structure

        public AVLTreePane() {
            // The constructor is empty, as the tree will be initialized when values are inserted.
        }

        // Method to insert a value into the AVL tree
        public void insertValue(Integer value) {
            if (tree == null) {
                tree = new AVLTree<>(); // Create a new AVL tree if it does not exist
            }
            tree.insert(value); // Insert the value into the AVL tree
            drawTree(); // Redraw the AVL tree after the insertion
        }

        // Method to delete a value from the AVL tree
        public void deleteValue(Integer value) {
            if (tree != null) {
                tree.delete(value); // Delete the value from the AVL tree
                drawTree(); // Redraw the AVL tree after the deletion
            }
        }

        // Method to find a value in the AVL tree
        public boolean findValue(Integer value) {
            if (tree != null) {
                return tree.find(value); // Check if the value exists in the AVL tree
            }
            return false;
        }

        // Method to draw the AVL tree on the pane
        private void drawTree() {
            this.getChildren().clear(); // Clear the pane before redrawing the tree
            if (tree != null) {
                // Start drawing the AVL tree from the root at the center of the pane
                drawSubtree(tree.getRoot(), WIDTH / 2, 30, WIDTH / 4);
            }
        }

        // Method to draw a subtree rooted at the specified node
        private void drawSubtree(AVLTree.AVLTreeNode<Integer> node, double x, double y, double hGap) {
            if (node.left != null) {
                // Draw the left subtree recursively and connect it to the current node
                drawSubtree(node.left, x - hGap, y + 50, hGap / 2);
                connectNodes(x, y, x - hGap, y + 50);
            }

            if (node.right != null) {
                // Draw the right subtree recursively and connect it to the current node
                drawSubtree(node.right, x + hGap, y + 50, hGap / 2);
                connectNodes(x, y, x + hGap, y + 50);
            }

            drawNode(node, x, y); // Draw the current node
        }

        // Method to draw a node with its value as a circle and label
        private void drawNode(AVLTree.AVLTreeNode<Integer> node, double x, double y) {
            Circle circle = new Circle(x, y, 15); // Create a circle to represent the node
            circle.setFill(Color.WHITE);
            circle.setStroke(Color.BLACK);

            Label label = new Label(node.element.toString()); // Create a label to display the value
            label.setLayoutX(x - 5);
            label.setLayoutY(y - 5);

            this.getChildren().addAll(circle, label); // Add the circle and label to the pane
        }

        // Method to draw a line connecting two nodes
        private void connectNodes(double x1, double y1, double x2, double y2) {
            Line line = new Line(x1, y1 + 15, x2, y2 - 15); // Create a line connecting the nodes
            this.getChildren().add(line); // Add the line to the pane
        }

        // Method to get the root of the AVL tree
        public AVLTree.AVLTreeNode<Integer> getRoot() {
            return tree.getRoot();
        }
    }

    // Static inner class representing the AVL tree data structure
    private static class AVLTree<E extends Comparable<E>> {
        private AVLTreeNode<E> root;

        public AVLTree() {
        }

        // Method to insert a new element into the AVL tree
        public void insert(E element) {
            if (root == null) {
                root = new AVLTreeNode<>(element);
            } else {
                root = insert(root, element);
            }
        }

        // Recursive method to insert a new element into the AVL tree
        private AVLTreeNode<E> insert(AVLTreeNode<E> node, E element) {
            if (node == null) {
                return new AVLTreeNode<>(element);
            }

            int compareResult = element.compareTo(node.element);

            if (compareResult < 0) {
                node.left = insert(node.left, element);
            } else if (compareResult > 0) {
                node.right = insert(node.right, element);
            }

            return balance(node); // Balance the tree after insertion
        }

        // Method to delete an element from the AVL tree
        public void delete(E element) {
            root = delete(root, element);
        }

        // Recursive method to delete an element from the AVL tree
        private AVLTreeNode<E> delete(AVLTreeNode<E> node, E element) {
            if (node == null) {
                return null;
            }

            int compareResult = element.compareTo(node.element);

            if (compareResult < 0) {
                node.left = delete(node.left, element);
            } else if (compareResult > 0) {
                node.right = delete(node.right, element);
            } else if (node.left != null && node.right != null) {
                AVLTreeNode<E> minNode = findMin(node.right);
                node.element = minNode.element;
                node.right = delete(node.right, minNode.element);
            } else {
                node = (node.left != null) ? node.left : node.right;
            }

            return balance(node); // Balance the tree after deletion
        }

        // Method to find an element in the AVL tree
        public boolean find(E element) {
            return find(root, element);
        }

        // Recursive method to find an element in the AVL tree
        private boolean find(AVLTreeNode<E> node, E element) {
            if (node == null) {
                return false;
            }

            int compareResult = element.compareTo(node.element);

            if (compareResult < 0) {
                return find(node.left, element);
            } else if (compareResult > 0) {
                return find(node.right, element);
            } else {
                return true; // Element found in the tree
            }
        }

        // Method to balance a node in the AVL tree
        private AVLTreeNode<E> balance(AVLTreeNode<E> node) {
            if (node == null) {
                return null;
            }

            // Check and perform rotations if necessary to balance the tree
            if (height(node.left) - height(node.right) > 1) {
                if (height(node.left.left) >= height(node.left.right)) {
                    node = rotateWithLeftChild(node);
                } else {
                    node = doubleWithLeftChild(node);
                }
            } else if (height(node.right) - height(node.left) > 1) {
                if (height(node.right.right) >= height(node.right.left)) {
                    node = rotateWithRightChild(node);
                } else {
                    node = doubleWithRightChild(node);
                }
            }

            node.height = Math.max(height(node.left), height(node.right)) + 1;
            return node;
        }

        // Method to get the height of a node
        private int height(AVLTreeNode<E> node) {
            return node == null ? -1 : node.height;
        }

        // Methods to perform various rotations for balancing the tree
        private AVLTreeNode<E> rotateWithLeftChild(AVLTreeNode<E> k2) {
            AVLTreeNode<E> k1 = k2.left;
            k2.left = k1.right;
            k1.right = k2;
            k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
            k1.height = Math.max(height(k1.left), k2.height) + 1;
            return k1;
        }

        private AVLTreeNode<E> rotateWithRightChild(AVLTreeNode<E> k1) {
            AVLTreeNode<E> k2 = k1.right;
            k1.right = k2.left;
            k2.left = k1;
            k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
            k2.height = Math.max(height(k2.right), k1.height) + 1;
            return k2;
        }

        private AVLTreeNode<E> doubleWithLeftChild(AVLTreeNode<E> k3) {
            k3.left = rotateWithRightChild(k3.left);
            return rotateWithLeftChild(k3);
        }

        private AVLTreeNode<E> doubleWithRightChild(AVLTreeNode<E> k1) {
            k1.right = rotateWithLeftChild(k1.right);
            return rotateWithRightChild(k1);
        }

        // Method to find the minimum value in a subtree rooted at the given node
        private AVLTreeNode<E> findMin(AVLTreeNode<E> node) {
            if (node == null) {
                return null;
            }
            while (node.left != null) {
                node = node.left;
            }
            return node;
        }

        // Static inner class representing a node in the AVL tree
        private static class AVLTreeNode<E> {
            E element;
            AVLTreeNode<E> left;
            AVLTreeNode<E> right;
            int height;

            public AVLTreeNode(E element) {
                this(element, null, null);
            }

            public AVLTreeNode(E element, AVLTreeNode<E> left, AVLTreeNode<E> right) {
                this.element = element;
                this.left = left;
                this.right = right;
                this.height = 0;
            }
        }

        // Method to get the root of the AVL tree
        public AVLTreeNode<E> getRoot() {
            return root;
        }
    }
}