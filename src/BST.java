public class BST {

    private static class Node {
        int key;
        Node left;
        Node right;

        Node(int key) {
            this.key = key;
        }
    }

    private Node root;

    public void insert(int key) {
        Node newNode = new Node(key);
        if (root == null) {
            root = newNode;
            return;
        }
        Node cur = root;
        while (true) {
            if (key == cur.key) {
                return;
            }
            if (key < cur.key) {
                if (cur.left == null) {
                    cur.left = newNode;
                    return;
                }
                cur = cur.left;
            } else {
                if (cur.right == null) {
                    cur.right = newNode;
                    return;
                }
                cur = cur.right;
            }
        }
    }
    public boolean search(int key) {
        Node cur = root;
        while (cur != null) {
            if (key == cur.key) {
                return true;
            }
            if (key < cur.key) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return false;
    }

    public void delete(int key) {
        Node parent = null;
        Node cur = root;
        while (cur != null && cur.key != key) {
            parent = cur;
            if (key < cur.key) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        if (cur == null) {
            return;
        }

        if (cur.left != null && cur.right != null) {
            Node smallestParent = cur;
            Node smallest = cur.right;
            while (smallest.left != null) {
                smallestParent = smallest;
                smallest = smallest.left;
            }
            cur.key = smallest.key;
            parent = smallestParent;
            cur = smallest;
        }
        Node child;
        if (cur.left != null) {
            child = cur.left;
        } else {
            child = cur.right;
        }

        if (parent == null) {
            root = child;
        } else if (parent.left == cur) {
            parent.left = child;
        } else {
            parent.right = child;
        }
    }

    public int height() {
        if (root == null) {
            return 0;
        }
        java.util.ArrayDeque<Node> queue = new java.util.ArrayDeque<>();
        queue.add(root);
        int h = 0;
        while (!queue.isEmpty()) {
            int count = queue.size();
            for (int i = 0; i < count; i++) {
                Node n = queue.poll();
                if (n.left != null) queue.add(n.left);
                if (n.right != null) queue.add(n.right);
            }
            h++;
        }
        return h;
    }
}