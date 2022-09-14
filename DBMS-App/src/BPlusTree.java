import java.util.Arrays;
import java.io.IOException;

class findNodeToInsertToResults{
    BPlusTreeNode childNode = null;
    BPlusTreeNode parentNode = null;
    BPlusTreeNode grandparentNode = null;
    int indexOfChildInParent = -1;
    int indexOfParentInGP = -1;
}

class splitNodes{
    BPlusTreeNode node1 = null;
    BPlusTreeNode node2 = null;
    boolean linknodes = true;
    int middlekey = 0;
}

class parentNodes{
    BPlusTreeNode parent;
    int indexOfChild;
}

class deleteNodes{
    BPlusTreeNode leafNodeptr = null;
    BPlusTreeNode directionalNode = null;
}

public class BPlusTree {
    private BPlusTreeNode rootNode;

    public BPlusTree(){
        this.rootNode = new BPlusTreeNode();
        this.rootNode.leafNode = true;
    }

    public void insert(int key, student StudentNode) throws IOException {
        //find a node to insert into
        findNodeToInsertToResults nodeData = this.findNodeToInsertAt(this.rootNode, this.rootNode, this.rootNode, key);
        if(nodeData == null || nodeData.childNode == null || nodeData.parentNode == null || nodeData.indexOfChildInParent == -1
            || nodeData.grandparentNode == null || nodeData.indexOfParentInGP == -1){
                throw new IOException("null pointer exception encountered: BPlusTree.java:43  key to insert: " + key);
        }
    
        if(nodeData.childNode.keyTally < nodeData.childNode.referenceArraySize - 1){
            nodeData.childNode = this.insertKey(nodeData.childNode, key, StudentNode);
            return;
        }
        else{
            //split nodes
            splitNodes nodes = this.split__NODE(nodeData.childNode, key, true, StudentNode);

            if(nodeData.childNode == this.rootNode){
                this.rootNode = new BPlusTreeNode(nodes.middlekey);
                this.rootNode.references[0] = nodes.node1;
                this.rootNode.references[1] = nodes.node2;
                if(nodes.linknodes){
                    this.rootNode.references[0].nextLeaf = this.rootNode.references[1];
                    this.rootNode.references[1].prevLeaf = this.rootNode.references[0];
                }
                return;
            }
            else{
                //create a new node and place into in position on parentnode after child node
                if(nodeData.parentNode.keyTally < nodeData.parentNode.referenceArraySize - 1){
                    if(nodes.linknodes){
                        if(nodeData.childNode.prevLeaf != null){
                            nodeData.childNode.prevLeaf.nextLeaf = nodes.node1;//link prev to node1
                            nodes.node1.prevLeaf = nodeData.childNode.prevLeaf;//link node1 prev to prev
                        }

                        nodes.node1.nextLeaf = nodes.node2;
                        nodes.node2.prevLeaf = nodes.node1;

                        if(nodeData.childNode.nextLeaf != null){
                            nodes.node2.nextLeaf = nodeData.childNode.nextLeaf;
                            nodeData.childNode.nextLeaf.prevLeaf = nodes.node2;
                        }
                    }
                    nodeData.parentNode.references[nodeData.indexOfChildInParent] = nodes.node1;

                    nodeData.parentNode.references[nodeData.indexOfChildInParent] = nodes.node1;
                    nodeData.parentNode = this.insertReferenceAt(nodeData.parentNode, nodeData.indexOfChildInParent + 1, nodes.node2);
                    nodeData.parentNode = this.insertKey(nodeData.parentNode, nodes.middlekey, null);
                    return;
                }
                else{
                    while(true){

                        if((nodeData.parentNode != this.rootNode)){
                            parentNodes parentobj = findParent(nodeData.parentNode, this.rootNode);
                            if(parentobj == null)throw new IOException("no parent found: BPlusTree.java:96  key to insert: " + key);
                            nodeData.grandparentNode = parentobj.parent;
                            nodeData.indexOfParentInGP = parentobj.indexOfChild;
                        }

                        if(nodes.linknodes){
                            if(nodeData.childNode.prevLeaf != null){
                                nodeData.childNode.prevLeaf.nextLeaf = nodes.node1;//link prev to node1
                                nodes.node1.prevLeaf = nodeData.childNode.prevLeaf;//link node1 prev to prev
                            }

                            nodes.node1.nextLeaf = nodes.node2;
                            nodes.node2.prevLeaf = nodes.node1;

                            if(nodeData.childNode.nextLeaf != null){
                                nodes.node2.nextLeaf = nodeData.childNode.nextLeaf;
                                nodeData.childNode.nextLeaf.prevLeaf = nodes.node2;
                            }
                        }
                        nodeData.parentNode.references[nodeData.indexOfChildInParent] = nodes.node1;

                        splitNodes parentnode = this.split__NODE(nodeData.parentNode, nodes.middlekey, false, null);
                        parentnode.node1.leafNode = false;
                        parentnode.node2.leafNode = false;
                        parentnode = this.insertReferenceAtCorrectNode(parentnode, nodes.node1, nodes.node2);

                        //reference grandparent to parent nodes
                        
                        if(nodeData.grandparentNode == nodeData.parentNode){
                            this.rootNode = null;
                            this.rootNode = new BPlusTreeNode(parentnode.middlekey);
                            this.rootNode.references[0] = parentnode.node1;
                            this.rootNode.references[1] = parentnode.node2;
                            return;
                        }
                        else if(nodeData.grandparentNode.keyTally < nodeData.grandparentNode.referenceArraySize - 1){
                            nodeData.grandparentNode.references[nodeData.indexOfParentInGP] = parentnode.node1;
                            nodeData.grandparentNode = this.insertReferenceAt(nodeData.grandparentNode, nodeData.indexOfParentInGP + 1, parentnode.node2);
                            nodeData.grandparentNode = this.insertKey(nodeData.grandparentNode, parentnode.middlekey, null);
                            return;
                        }
                        else{
                            nodeData.indexOfChildInParent = nodeData.indexOfParentInGP;
                            nodes.node1 = parentnode.node1;
                            nodes.node2 = parentnode.node2;
                            nodes.linknodes = false;
                            nodes.middlekey = parentnode.middlekey;
                            nodeData.parentNode = nodeData.grandparentNode;
                        }
                    }
                }
            }
        }
    }

    public void delete(int key) throws IOException{
        deleteNodes nodesToModify = new deleteNodes();
        nodesToModify.leafNodeptr = this.findKeyNodes(key, this.rootNode);
        
        if(nodesToModify.leafNodeptr == null)throw new IOException("null pointer exception encountered: BPlusTree.java:157  key to delete: " + key);
        
        //delete key
        if(nodesToModify.leafNodeptr.keyTally > (nodesToModify.leafNodeptr.keys.length / 2)){
            nodesToModify.directionalNode = this.findGuidingNode(key, this.rootNode);
            nodesToModify.leafNodeptr = DELETE__shiftKeysByOneFrom(key, nodesToModify.leafNodeptr);
            if(nodesToModify.directionalNode != null){
                nodesToModify.directionalNode = replaceKey(key, nodesToModify.leafNodeptr.getFirstIndexKey(), nodesToModify.directionalNode);
            }
            return;
        }

        BPlusTreeNode leftSiblingNode = nodesToModify.leafNodeptr.prevLeaf;
        BPlusTreeNode rightSiblingNode = nodesToModify.leafNodeptr.nextLeaf;

        if(leftSiblingNode != null && (leftSiblingNode.keyTally > (nodesToModify.leafNodeptr.keys.length / 2))){
            //find directional node of first key in leafnode
            int oldkey = nodesToModify.leafNodeptr.keys[0];
            nodesToModify.directionalNode = this.findGuidingNode(oldkey, this.rootNode);
            //take node in last position of sibling
            int keyToAdd = leftSiblingNode.getLastIndexKey();
            student StudentNode = leftSiblingNode.getLastIndexStudent();

            //place in direction node and in first index of leafnode
            if(nodesToModify.directionalNode != null)
                nodesToModify.directionalNode = replaceKey(oldkey, keyToAdd, nodesToModify.directionalNode);
            nodesToModify.leafNodeptr = DELETE__shiftKeysByOneFrom(key, nodesToModify.leafNodeptr);
            nodesToModify.leafNodeptr = AddInFirstPos(StudentNode, keyToAdd, nodesToModify.leafNodeptr);
            return;
        }
        else if(rightSiblingNode != null && (rightSiblingNode.keyTally > (nodesToModify.leafNodeptr.keys.length / 2))){
            nodesToModify.directionalNode = this.findGuidingNode(key, this.rootNode);
            nodesToModify.leafNodeptr = DELETE__shiftKeysByOneFrom(key, nodesToModify.leafNodeptr);
            //find directional node of first key in leafnode
            int oldkey = rightSiblingNode.keys[0];
            student StudentNode = rightSiblingNode.getFirstIndexStudent();
            nodesToModify.leafNodeptr = AddInLastPos(StudentNode, oldkey, nodesToModify.leafNodeptr);

            if(nodesToModify.directionalNode != null){
                nodesToModify.directionalNode = replaceKey(key, nodesToModify.leafNodeptr.keys[0], nodesToModify.directionalNode);
            }

            //find right sibling dir node and replace key in dir node
            nodesToModify.directionalNode = this.findGuidingNode(oldkey, this.rootNode);
            rightSiblingNode = DELETE__shiftKeysByOneFrom(oldkey, rightSiblingNode);
            //place in direction node
            if(nodesToModify.directionalNode != null)
                nodesToModify.directionalNode = replaceKey(oldkey, rightSiblingNode.getFirstIndexKey(), nodesToModify.directionalNode);

            return;
        }
        else if(leftSiblingNode != null && (leftSiblingNode.keyTally + nodesToModify.leafNodeptr.keyTally - 1 < nodesToModify.leafNodeptr.referenceArraySize - 1)){
            //merge sibling and deletenode and replace value and directional node
            int oldkey = nodesToModify.leafNodeptr.keys[0];
            parentNodes parentdata = this.findParent(nodesToModify.leafNodeptr, this.rootNode);
            nodesToModify.directionalNode = this.findGuidingNode(oldkey, this.rootNode);

            nodesToModify.leafNodeptr = DELETE__shiftKeysByOneFrom(key, nodesToModify.leafNodeptr);
            leftSiblingNode = appendNode(leftSiblingNode, nodesToModify.leafNodeptr);

            if(parentdata == null)throw new IOException("cannot find the parent: BPlusTree.java:214");

            //perform normal delete
            if(parentdata.indexOfChild - 1 >= 0 && parentdata.parent.references[parentdata.indexOfChild - 1] == leftSiblingNode){
                parentdata.parent.references[parentdata.indexOfChild] = leftSiblingNode;
                parentdata.parent = DELETE__shiftKeysByOneFrom(oldkey, parentdata.parent);
            }
            else if(parentdata.indexOfChild == 0){//perform special parent deletetion
                parentdata.parent = shiftArrObjectsDownByOne(parentdata.parent);

                if(parentdata.parent.keyTally > 0){
                    int keyToAdd = parentdata.parent.references[parentdata.indexOfChild + 1].keys[0];
                    if(nodesToModify.directionalNode != null)
                        nodesToModify.directionalNode = replaceKey(oldkey, keyToAdd, nodesToModify.directionalNode);
                }
            }

            if(parentdata.parent.keyTally == 0 && parentdata.parent == this.rootNode){
                parentdata.parent.references[0] = null;
                this.rootNode = leftSiblingNode;
            }
            else if(parentdata.parent != this.rootNode && parentdata.parent.keyTally < (parentdata.parent.keys.length / 2)){
                this.rebalanceParents(parentdata.parent);
            }
            return;
        }
        else if(rightSiblingNode != null && (rightSiblingNode.keyTally + nodesToModify.leafNodeptr.keyTally - 1 < nodesToModify.leafNodeptr.referenceArraySize - 1)){
            //merge sibling and deletenode and replace value and directional node
            int oldkey = rightSiblingNode.keys[0];
            parentNodes parentdata = this.findParent(nodesToModify.leafNodeptr, this.rootNode);

            nodesToModify.leafNodeptr = DELETE__shiftKeysByOneFrom(key, nodesToModify.leafNodeptr);
            nodesToModify.leafNodeptr = appendNode(nodesToModify.leafNodeptr, rightSiblingNode);
            
            if(parentdata == null)throw new IOException("null pointer exception encountered: BPlusTree.java:247  key to delete: " + key);

            //perform normal delete
            parentdata.parent = DELETE__shiftKeysByOneFrom(oldkey, parentdata.parent);

            if(parentdata.parent.keyTally == 0 && parentdata.parent == this.rootNode){
                parentdata.parent.references[0] = null;
                this.rootNode = nodesToModify.leafNodeptr;
            }
            else if(parentdata.parent != this.rootNode && parentdata.parent.keyTally < (parentdata.parent.keys.length / 2)){
                this.rebalanceParents(parentdata.parent);
            }
            return;
        }
        else throw new IOException("no left or right sibling found or both are full or cannot take on extra key tallys: BPlusTree.java:258  key to delete: " + key);
    }

    public student findThisKey(int key){
        BPlusTreeNode leafNodeptr = this.findKeyNodes(key, this.rootNode);
        int index = Arrays.binarySearch(leafNodeptr.keys, 0, leafNodeptr.keyTally, key);
        return leafNodeptr.Students[index];
    }
    public BPlusTreeNode get_ROOT(){return this.rootNode;}

    public void displayLinkedList(BPlusTreeNode node){
        if(node == null || !node.leafNode)return;
        System.out.print("address" + node);
        System.out.print("  || array values: ");
        for(int i = 0; i < node.keys.length; ++i)System.out.println(node.keys[i] + " ");
        System.out.print("key tally: " + node.keyTally + "  leaf node?: " + node.leafNode);
        if(node.nextLeaf != null){
            System.out.println(" || next-leaf: " + node.nextLeaf);
        }
        else{
            System.out.println(" || next-leaf: nullptr");
        }
        this.displayLinkedList(node.nextLeaf);
    }

    public void displayTree(BPlusTreeNode root){
        if(root != null){
            if(!root.leafNode && root.references[0] != null) this.displayTree(root.references[0]);

            else this.displayLinkedList(root);
        }
        else return;
    }

    public void displayEntireTree(BPlusTreeNode node){
        if(node != null){
            System.out.print("address: " +node);
            System.out.print(" || array values: ");
            for(int i = 0; i < node.keys.length; ++i){
                System.out.print(node.keys[i] + " ");
            }
            System.out.print("key tally: " + node.keyTally + " is leaf? " + node.leafNode);
            if(node.nextLeaf != null){
                System.out.print(" || next-leaf: " + node.nextLeaf);
                if(node.prevLeaf != null){
                    System.out.println(" || prev-leaf: " + node.prevLeaf);
                }
                else{
                    System.out.println(" || prev-leaf: nullptr");
                }
            }
            else{
                System.out.print(" || next-leaf: nullptr");
                if(node.prevLeaf != null){
                    System.out.println(" || prev-leaf: " + node.prevLeaf);
                }
                else{
                    System.out.println(" || prev-leaf: nullptr");
                }
            }
            
            /*for(int i = 0; i < node.referenceArraySize; ++i){
                if(node.references[i] != null){
                    System.out.println("Pointing to: " + node.references[i] + ", in index: " + i);
                }
                else{
                    System.out.println("Pointing to: null, in index: " + i);
                }
            }*/

            for(int i = 0; i < node.referenceArraySize; ++i){
                this.displayEntireTree(node.references[i]);
            }
        }
        else return;
    }
    
    public BPlusTreeNode getFirstLeaf(BPlusTreeNode root){
        if(root != null){
            if(!root.leafNode && root.references[0] != null){
                return this.getFirstLeaf(root.references[0]);
            }
            else{
                return root;
            }
        }
        else return null;
    }

    //helper functions

    private BPlusTreeNode insertKey(BPlusTreeNode child, int key, student StudentNode) throws IOException{
        if(child.keyTally == child.referenceArraySize - 1){
            StringBuilder output = new StringBuilder("[");
            for(int i = 0; i < child.keyTally; ++i){
                if(i < child.keyTally - 1){
                    output.append(child.keys[i]).append(",");
                }
                else output.append(child.keys[i]);
            }
            output.append("]");
            throw new IOException("overflow in array: BPlusTree.java:355  key to insert: " + key + " array: " + output);
        }
        child.keys[child.keyTally] = key;
        ++child.keyTally;
        Arrays.sort(child.keys, 0, child.keyTally);
        int index = Arrays.binarySearch(child.keys, 0, child.keyTally, key);
        
        if(StudentNode != null)child = this.addtudentAtThisIndex(child, index, StudentNode);

        return child;
    }
    
    private findNodeToInsertToResults createReturnObj(BPlusTreeNode child, BPlusTreeNode parent, BPlusTreeNode grandparent, int childindex, int parentindex){
        findNodeToInsertToResults dataobj = new findNodeToInsertToResults();
        dataobj.childNode = child;
        dataobj.parentNode = parent;
        dataobj.grandparentNode = grandparent;
        dataobj.indexOfChildInParent = childindex;
        dataobj.indexOfParentInGP = parentindex;
        return dataobj;
    }
    
    private findNodeToInsertToResults findNodeToInsertAt(BPlusTreeNode child, BPlusTreeNode parent, BPlusTreeNode grandparent, int key){    
        if(child != null){
            for(int i = 0; i < child.referenceArraySize; ++i){
                if(i == child.keyTally){
                    //go to last reference because value is bigger than
                    //largest value in node keys array
                    if(child.references[i] != null){
                        return this.findNodeToInsertAt(child.references[i], child, parent, key);
                    }
                    else if(child.leafNode){ 
                        int childindex = findIndexOfChild(child, parent);
                        int parentindex = findIndexOfChild(parent, grandparent);
                        return this.createReturnObj(child, parent, grandparent, childindex, parentindex);
                    }
                    else return null;
                }
                else{
                    if(key < child.keys[i]){
                        if(child.references[i] != null){
                            return this.findNodeToInsertAt(child.references[i], child, parent, key);
                        }
                        else if(child.leafNode){ 
                            int childindex = findIndexOfChild(child, parent);
                            int parentindex = findIndexOfChild(parent, grandparent);
                            return this.createReturnObj(child, parent, grandparent, childindex, parentindex);
                        }
                        else return null;
                    }
                }
            }
            return null;
        }
        else return null;
    }
    
    private int findIndexOfChild(BPlusTreeNode child, BPlusTreeNode parent){
        if(child == parent)return 0;
        for(int i = 0; i < parent.referenceArraySize; ++i){
            if(parent.references[i] == child)return i;
        }
        return -1;
    }
    
    private parentNodes findParent(BPlusTreeNode node, BPlusTreeNode parent){
        if(parent != null){
            for(int i = 0; i <= parent.keyTally; ++i){
                if(i == parent.keyTally){
                    if(parent.references[i] == node){
                        parentNodes obj = new parentNodes();
                        obj.indexOfChild = findIndexOfChild(node, parent);
                        obj.parent = parent;
                        return obj;
                    }
                    else return this.findParent(node, parent.references[i]);
                }
                else if(node.keys[0] < parent.keys[i]){
                    if(parent.references[i] == node){
                        parentNodes obj = new parentNodes();
                        obj.indexOfChild = findIndexOfChild(node, parent);
                        obj.parent = parent;
                        return obj;
                    }
                    else return this.findParent(node, parent.references[i]);
                }
                else if(node.keys[0] == parent.keys[i]){
                    if(parent.references[i + 1] == node){
                        parentNodes obj = new parentNodes();
                        obj.indexOfChild = findIndexOfChild(node, parent);
                        obj.parent = parent;
                        return obj;
                    }
                    else return this.findParent(node, parent.references[i + 1]);
                }
            }
            return null;
        }
        return null;
    }

    private splitNodes split__NODE(BPlusTreeNode node, int keytoinsert, boolean insertthiskey, student StudentNode) throws IOException{
        splitNodes nodes = new splitNodes();
        int addAmount = this.getAmountToAdd(node.keys, keytoinsert);

        int upperboundnum = node.keys[((node.keys.length) / 2) - addAmount];
        int lowerboundindex = ((node.keys.length) / 2) - 1 - addAmount;
        if(lowerboundindex < 0){
            StringBuilder output = new StringBuilder("[");
            for(int i = 0; i < node.keyTally; ++i){
                if(i < node.keyTally - 1){
                    output.append(node.keys[i]).append(",");
                }
                else output.append(node.keys[i]);
            }
            output.append("]");
            throw new IOException("cannot access lowebound index before split: BPlusTree.java:472  key to insert: " + keytoinsert + " array: " + output);
        }
        int lowerboundnum = node.keys[lowerboundindex];

        nodes.node1 = this.getSection(node, 0, ((node.keys.length) / 2) - addAmount);
        nodes.node1.leafNode = true;

        nodes.node2 = this.getSection(node, ((node.keys.length) / 2) - addAmount, node.keys.length);
        if(!insertthiskey)nodes.node2 = this.shiftKeysDownByOne(nodes.node2);
        nodes.node2.leafNode = true;

        if(keytoinsert < lowerboundnum){
            nodes.middlekey = nodes.node2.keys[0];
            nodes.node1 = this.insertKey(nodes.node1, keytoinsert, StudentNode);
        }
        else if(keytoinsert > upperboundnum){
            nodes.middlekey = upperboundnum;
            nodes.node2 = this.insertKey(nodes.node2, keytoinsert, StudentNode);
        }
        else if((keytoinsert > lowerboundnum && keytoinsert < upperboundnum) && (nodes.node2.keyTally == nodes.node1.keyTally) && addAmount != 1){
            nodes.middlekey = keytoinsert;
            nodes.node2 = this.insertKey(nodes.node2, keytoinsert, StudentNode);
        }
        else if((keytoinsert > lowerboundnum && keytoinsert < upperboundnum)  && (nodes.node2.keyTally != nodes.node1.keyTally) && addAmount == 1){
            nodes.middlekey = nodes.node2.keys[0];
            nodes.node1 = this.insertKey(nodes.node1, keytoinsert, StudentNode);
        }
        return nodes;
    }

    private int getAmountToAdd(int[] keys, int keyToInsert){
        int[] newkeys = new int[keys.length + 1];

        System.arraycopy(keys, 0, newkeys, 0, keys.length);
        newkeys[newkeys.length - 1] = keyToInsert;
        Arrays.sort(newkeys);
        int index = Arrays.binarySearch(newkeys, keyToInsert);

        if(index >= newkeys.length/2)return 0;
        else return 1;
    }
    
    private BPlusTreeNode getSection(BPlusTreeNode node, int startIndex, int endIndex){
        return new BPlusTreeNode(node, startIndex, endIndex);
    }
    
    private BPlusTreeNode insertReferenceAt(BPlusTreeNode node, int insertPos, BPlusTreeNode insertnode){
        for(int i = node.referenceArraySize - 1; i >= insertPos; --i){
            if(i - 1 >= 0) node.references[i] = node.references[i - 1];
            else break;
        }
        node.references[insertPos] = insertnode;
        return node;
    }

    private splitNodes insertReferenceAtCorrectNode(splitNodes parentnodes, BPlusTreeNode insertedNode, BPlusTreeNode nodeToInsert) throws IOException{
        if(nodeToInsert.keys[0] < parentnodes.middlekey){
            int indexOfChild = -1;

            for(int i = 0; i < parentnodes.node1.referenceArraySize; ++i){
                if(parentnodes.node1.references[i] == insertedNode){
                    indexOfChild = i;
                    break;
                }
            }

            if(indexOfChild == -1)throw new IOException("cannot find index of sibling node BPlusTree.java:537");

            parentnodes.node1 = this.insertReferenceAt(parentnodes.node1, indexOfChild + 1, nodeToInsert);
        }
        else {//if(nodeToInsert.keys[0] >= parentnodes.middlekey){
            int indexOfChild = -1;

            for(int i = 0; i < parentnodes.node2.referenceArraySize; ++i){
                if(parentnodes.node2.references[i] == insertedNode){
                    indexOfChild = i;
                    break;
                }
            }

            parentnodes.node2 = this.insertReferenceAt(parentnodes.node2, indexOfChild + 1, nodeToInsert);
        }

        return parentnodes;
    }

    private BPlusTreeNode findKeyNodes(int key, BPlusTreeNode node){
        if(node != null){
            for(int i = 0; i < node.referenceArraySize; ++i){
                if(i == node.keyTally){
                    //go to last reference because value is bigger than
                    //largest value in node keys array
                    if(node.references[i] != null)return this.findKeyNodes(key, node.references[i]);
                    else return node;
                }
                else{
                    if(key < node.keys[i]){
                        if(node.references[i] != null)return this.findKeyNodes(key, node.references[i]);
                        else return node;
                    }
                }
            }
            return null;
        }
        return null;
    }

    private BPlusTreeNode findGuidingNode(int key, BPlusTreeNode node){
        if(node != null){
            for(int i = 0; i < node.referenceArraySize; ++i){
                if(i == node.keyTally){
                    //go to last reference because value is bigger than
                    //largest value in node keys array
                    if(node.references[i] != null)return this.findGuidingNode(key, node.references[i]);
                    else return null;
                }
                else{
                    if(key < node.keys[i]){
                        if(node.references[i] != null)return this.findGuidingNode(key, node.references[i]);
                        else return null;
                    }
                    else if(key == node.keys[i] && !node.leafNode)return node;
                }
            }
            return null;
        }
        return null;
    }
    
    private void rebalanceParents(BPlusTreeNode parent) throws IOException{
        while(true){
            parentNodes grandparentdata = this.findParent(parent, this.rootNode);

            if(grandparentdata.indexOfChild == 0){
                BPlusTreeNode parentRightSibling = grandparentdata.parent.references[grandparentdata.indexOfChild + 1];
                if(parentRightSibling == null)throw new IOException("cannot get parents right sibling: BPlusTree.java:607");

                if(parentRightSibling.keyTally > (parentRightSibling.keys.length / 2)){
                    int newKeyToInsert = grandparentdata.parent.keys[0];//also in grandparentdata.parent.keys[0]
                    parent = this.insertKey(parent, newKeyToInsert, null);
                    parent.references[parent.keyTally] = parentRightSibling.references[0];
                    
                    grandparentdata.parent.keys[0] = parentRightSibling.keys[0];
                    parentRightSibling = shiftArrObjectsDownByOne(parentRightSibling);
                    return;
                }
                else{//merge both parents
                    if(grandparentdata.parent == this.rootNode && grandparentdata.parent.keyTally == 1){
                        //merge right parent and this parent and make it new root
                        parent = appendGuideNode(parent, parentRightSibling);
                        parent = this.insertKey(parent, grandparentdata.parent.keys[0], null);
                        this.rootNode = parent;
                        return;
                    }
                    else{
                        //merge right parent and this parent
                        parent = appendGuideNode(parent, parentRightSibling);
                        parent = this.insertKey(parent, grandparentdata.parent.keys[0], null);
                        grandparentdata.parent = shiftArrObjectsDownByOne(grandparentdata.parent);
                        grandparentdata.parent.references[grandparentdata.indexOfChild] = parent;

                        if(grandparentdata.parent == this.rootNode)return;
                        else if(grandparentdata.parent.keyTally > (grandparentdata.parent.keys.length / 2))return;
                        else parent = grandparentdata.parent;//invoke iteration here
                    }
                }
            }
            else{
                BPlusTreeNode parentLeftSibling = grandparentdata.parent.references[grandparentdata.indexOfChild - 1];
                if(parentLeftSibling == null)throw new IOException("cannot get parents left sibling: BPlusTree.java:605");

                if(parentLeftSibling.keyTally > (parentLeftSibling.keys.length / 2)){
                    //insert reference for this key and insert this key
                    int newKeyToInsert = parentLeftSibling.keys[parentLeftSibling.keyTally - 1];

                    //place reference in first position and move everything up by one
                    parent = addReferenceInFirstPos(parent, parentLeftSibling.references[parentLeftSibling.keyTally]);
                    parent = this.insertKey(parent, grandparentdata.parent.keys[grandparentdata.indexOfChild - 1], null);
                    parentLeftSibling = DELETE__shiftKeysByOneFrom(newKeyToInsert, parentLeftSibling);

                    grandparentdata.parent.keys[grandparentdata.indexOfChild - 1] = newKeyToInsert;
                    return;
                }
                else{//merge both parents
                    if(grandparentdata.parent == this.rootNode && grandparentdata.parent.keyTally == 1){
                        //merge right parent and this parent and make it new root
                        parentLeftSibling = appendGuideNode(parentLeftSibling, parent);
                        parentLeftSibling = this.insertKey(parentLeftSibling, grandparentdata.parent.keys[0], null);
                        this.rootNode = parentLeftSibling;
                        return;
                    }
                    else{
                        //merge left parent and this parent
                        int GP_key = grandparentdata.parent.keys[grandparentdata.indexOfChild - 1];
                        parentLeftSibling = appendGuideNode(parentLeftSibling, parent);
                        parentLeftSibling = this.insertKey(parentLeftSibling, GP_key, null);
                        grandparentdata.parent.references[grandparentdata.indexOfChild] = parentLeftSibling;
                        grandparentdata.parent = DELETE__shiftKeysByOneFrom(GP_key, grandparentdata.parent);

                        if(grandparentdata.parent == this.rootNode)return;
                        else if(grandparentdata.parent.keyTally > (grandparentdata.parent.keys.length / 2))return;
                        else parent = grandparentdata.parent;//invoke iteration here
                    }
                }
            }
        }
    }


    //re-orgranising functions for nodes
    private BPlusTreeNode addtudentAtThisIndex(BPlusTreeNode node, int index, student StudentNode){
        for(int i = node.keyTally - 1; i > index; --i){
            node.Students[i] = node.Students[i - 1];
        }
        node.Students[index] = StudentNode;
        return node;
    }

    private BPlusTreeNode shiftKeysDownByOne(BPlusTreeNode node){
        for(int i = 0; i < node.referenceArraySize - 1; ++i){
            if(i + 1 < node.referenceArraySize - 1){
                node.keys[i] = node.keys[i + 1];
            }
        }
        node.keys[node.referenceArraySize - 2] = 0;
        --node.keyTally;

        return node;
    }

    private BPlusTreeNode shiftArrObjectsDownByOne(BPlusTreeNode node){
        if(node.keyTally == 0)return node;

        for(int i = 0; i < node.keys.length - 1; ++i){
            if(i + 1 < node.keys.length - 1){
                node.Students[i] = node.Students[i + 1];
                node.keys[i] = node.keys[i + 1];
            }
            else if(i + 1 == node.keys.length - 1){
                node.Students[i] = null;
                node.keys[i] = 0;
            }
        }

        for(int i = 0; i < node.referenceArraySize; ++i){
            if(i + 1 < node.referenceArraySize - 1)node.references[i] = node.references[i + 1];
            else if(i + 1 == node.referenceArraySize - 1)node.references[i] = null;
        }

        --node.keyTally;

        return node;
    }

    private BPlusTreeNode DELETE__shiftKeysByOneFrom(int key, BPlusTreeNode node) throws IOException{
        if(node.keyTally == 0)throw new IOException("cannot delete key: "+ key +" as there are 0 keys in node.BPlusTree.java:733");
        int index = Arrays.binarySearch(node.keys, 0, node.keyTally, key);

        for(int i = index; i < node.keys.length - 1; ++i){
            if(i + 1 < node.keys.length - 1){
                node.Students[i] = node.Students[i + 1];
                node.keys[i] = node.keys[i + 1];
            }
            else if(i + 1 == node.keys.length - 1){
                node.Students[i] = null;
                node.keys[i] = 0;
            }
        }

        for(int i = index; i < node.referenceArraySize; ++i){
            if(i + 1 < node.referenceArraySize - 1)node.references[i] = node.references[i + 1];
            else if(i + 1 == node.referenceArraySize - 1)node.references[i] = null;
        }

        --node.keyTally;
        return node;
    }

    private BPlusTreeNode AddInFirstPos(student StudentNode, int key, BPlusTreeNode node){
        for(int i = node.keys.length - 1; i >= 0; --i){
            if(i - 1 >= 0){
                node.keys[i] = node.keys[i - 1];
                node.Students[i] = node.Students[i - 1];
            }
            else{ 
                node.keys[i] = key;
                node.Students[i] = StudentNode;
            }
        }
        ++node.keyTally;
        for(int i = node.referenceArraySize - 1; i >= 0; --i){
            if(i - 1 >= 0)node.references[i] = node.references[i - 1];
            else node.references[i] = null;
        }

        return node;
    }

    private BPlusTreeNode appendNode(BPlusTreeNode originalnode, BPlusTreeNode node){
        int arrayiter = originalnode.keyTally;

        for(int i = 0; i < node.keyTally; ++i){
            originalnode.keys[arrayiter] = node.keys[i];
            originalnode.Students[arrayiter] = node.Students[i];
            ++arrayiter;
        }

        originalnode.nextLeaf = node.nextLeaf;
        if(originalnode.nextLeaf != null){
            originalnode.nextLeaf.prevLeaf = originalnode;
        }

        originalnode.keyTally += node.keyTally;
        return originalnode;
    }

    private BPlusTreeNode replaceKey(int oldkey, int newkey, BPlusTreeNode node) throws IOException{
        if(node.keyTally == 0)throw new IOException("cannot replace old key: "+ oldkey + " with new key: " + newkey +" as there are 0 keys in node.BPlusTree.java:795");
        int index = Arrays.binarySearch(node.keys, 0, node.keyTally, oldkey);
        node.keys[index] = newkey;
        return node;
    }

    private BPlusTreeNode AddInLastPos(student StudentNode, int key, BPlusTreeNode node){
        node.keys[node.keyTally] = key;
        node.Students[node.keyTally] = StudentNode;
        ++node.keyTally;
        return node;
    }

    private BPlusTreeNode addReferenceInFirstPos(BPlusTreeNode originalnode, BPlusTreeNode node){
        ++originalnode.keyTally;
        for(int i = originalnode.referenceArraySize - 1; i >= 0; --i){
            if(i - 1 >= 0)originalnode.references[i] = originalnode.references[i - 1];
            else originalnode.references[i] = node;
        }

        for(int i = originalnode.keys.length - 1; i >= 0; --i){
            if(i - 1 >= 0){
                originalnode.keys[i] = originalnode.keys[i - 1];
                originalnode.Students[i] = originalnode.Students[i - 1];
            }
            else{ 
                originalnode.keys[i] = originalnode.references[1].keys[0];
            }
        }
        return originalnode;
    }

    private BPlusTreeNode appendGuideNode(BPlusTreeNode originalnode, BPlusTreeNode node){
        int arrayiter = originalnode.keyTally;

        for(int i = 0; i < node.keyTally; ++i){
            originalnode.keys[arrayiter] = node.keys[i];
            ++arrayiter;
        }
        arrayiter = originalnode.keyTally + 1;

        for(int i = 0; i <= node.keyTally; ++i){
            originalnode.references[arrayiter] = node.references[i];
            ++arrayiter;
        }

        originalnode.keyTally += node.keyTally;
        return originalnode;
    }
}
