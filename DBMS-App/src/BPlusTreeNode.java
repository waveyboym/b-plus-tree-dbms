import java.util.Arrays;

public class BPlusTreeNode {
    public final int referenceArraySize = 9;
    public boolean leafNode = false;
    public int keyTally = 0;
    public int keys[] = new int[referenceArraySize - 1];
    BPlusTreeNode nextLeaf = null;
    BPlusTreeNode prevLeaf = null;
    public BPlusTreeNode references[] = new BPlusTreeNode[referenceArraySize];
    public student Students[] = new student[referenceArraySize - 1];

    public BPlusTreeNode(){
        Arrays.fill(this.keys, 0);
        Arrays.fill(this.references, null);
        Arrays.fill(this.Students, null);
    }

    public BPlusTreeNode(int key){
        this.keys[0] = key;
        ++this.keyTally;
        for(int i = 1; i < this.keys.length; ++i)this.keys[i] = 0;
        Arrays.fill(this.references, null);
        Arrays.fill(this.Students, null);
    }

    public BPlusTreeNode(BPlusTreeNode node, int startIndex, int stopIndex){
        Arrays.fill(this.keys, 0);
        int arrayiter = 0;

        for(int i = startIndex; i < stopIndex; ++i){
            this.keys[arrayiter] = node.keys[i];
            if(node.leafNode)this.Students[arrayiter] = node.Students[i];
            ++this.keyTally;
            ++arrayiter;
        }

        this.leafNode = node.leafNode;
        arrayiter = 0;
        if(startIndex != 0)++startIndex;

        for(int i = startIndex; i <= stopIndex; ++i){
            this.references[arrayiter] = node.references[i];
            ++arrayiter;
        }
    }

    public int getLastIndexKey(){
        int val = this.keys[this.keyTally - 1];
        this.keys[this.keyTally - 1] = 0;
        return val;
    }

    public student getLastIndexStudent(){
        student StudentNode = this.Students[this.keyTally - 1];
        this.Students[this.keyTally - 1] = null;
        --this.keyTally;
        return StudentNode;
    }

    public int getFirstIndexKey(){return this.keys[0];}

    public student getFirstIndexStudent(){return this.Students[0];}

}