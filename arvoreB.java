class BTree {
    private Node root;
    private int degree;

    BTree(int degree) {
        this.root = null;
        this.degree = degree;
    }

    // Estrutura do nó
    private static class Node {
        int[] keys;
        Node[] children;
        int n; // Número de chaves no nó
        boolean leaf;

        Node(int degree, boolean leaf) {
            this.keys = new int[2 * degree - 1];
            this.children = new Node[2 * degree];
            this.n = 0;
            this.leaf = leaf;
        }
    }

    // Método para inserir uma chave na árvore
    public void insert(int key) {
        if (root == null) {
            root = new Node(degree, true);
            root.keys[0] = key;
            root.n = 1;
        } else {
            if (root.n == 2 * degree - 1) {
                Node newRoot = new Node(degree, false);
                newRoot.children[0] = root;
                splitChild(newRoot, 0, root);
                int i = 0;
                if (newRoot.keys[0] < key) {
                    i++;
                }
                insertNonFull(newRoot.children[i], key);
                root = newRoot;
            } else {
                insertNonFull(root, key);
            }
        }
    }

    // Método para inserir em um nó não cheio
    private void insertNonFull(Node node, int key) {
        int i = node.n - 1;
        if (node.leaf) {
            while (i >= 0 && key < node.keys[i]) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }
            node.keys[i + 1] = key;
            node.n++;
        } else {
            while (i >= 0 && key < node.keys[i]) {
                i--;
            }
            i++;
            if (node.children[i].n == 2 * degree - 1) {
                splitChild(node, i, node.children[i]);
                if (key > node.keys[i]) {
                    i++;
                }
            }
            insertNonFull(node.children[i], key);
        }
    }

    // Método para dividir um nó
    private void splitChild(Node parentNode, int i, Node nodeToSplit) {
        Node newNode = new Node(degree, nodeToSplit.leaf);
        newNode.n = degree - 1;

        for (int j = 0; j < degree - 1; j++) {
            newNode.keys[j] = nodeToSplit.keys[j + degree];
        }

        if (!nodeToSplit.leaf) {
            for (int j = 0; j < degree; j++) {
                newNode.children[j] = nodeToSplit.children[j + degree];
            }
        }

        nodeToSplit.n = degree - 1;

        for (int j = parentNode.n; j >= i + 1; j--) {
            parentNode.children[j + 1] = parentNode.children[j];
        }
        parentNode.children[i + 1] = newNode;

        for (int j = parentNode.n - 1; j >= i; j--) {
            parentNode.keys[j + 1] = parentNode.keys[j];
        }
        parentNode.keys[i] = nodeToSplit.keys[degree - 1];

        parentNode.n++;
    }

    // Método para buscar uma chave na árvore
    public boolean search(int key) {
        return searchKey(root, key);
    }

    private boolean searchKey(Node node, int key) {
        int i = 0;
        while (i < node.n && key > node.keys[i]) {
            i++;
        }
        if (i < node.n && key == node.keys[i]) {
            return true;
        }
        if (node.leaf) {
            return false;
        }
        return searchKey(node.children[i], key);
    }
}
class Main {
    public static void main(String[] args) {
        // Criar uma árvore B com grau 3 (um exemplo, pode ser outro valor)
        BTree bTree = new BTree(3);

        // Inserir chaves na árvore
        bTree.insert(10);
        bTree.insert(20);
        bTree.insert(5);
        bTree.insert(6);
        bTree.insert(12);
        bTree.insert(30);

        // Verificar se uma chave existe na árvore
        int keyToSearch = 31;
        boolean keyFound = bTree.search(keyToSearch);

        if (keyFound) {
            System.out.println("A chave " + keyToSearch + " está na árvore.");
        } else {
            System.out.println("A chave " + keyToSearch + " não está na árvore.");
        }
    }
}
