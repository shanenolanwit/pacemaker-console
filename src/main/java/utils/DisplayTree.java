package utils;

import java.util.List;

public class DisplayTree {

    final String name;
    final List<DisplayTree> children;

    public DisplayTree(String name, List<DisplayTree> children) {
        this.name = name;
        this.children = children;
    }

    public void print() {
        print("", true);
    }
    
    

    public String getName() {
		return name;
	}

	public List<DisplayTree> getChildren() {
		return children;
	}

	//http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
	private void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + name);
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1)
                    .print(prefix + (isTail ?"    " : "│   "), true);
        }
    }
}