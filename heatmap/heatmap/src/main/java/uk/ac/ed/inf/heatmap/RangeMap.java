package uk.ac.ed.inf.heatmap;

/* An class that maps from a range to some data of type T
*   Makes use of a binary search tree, but using ranges instead of single
*   values.
*   
* The intention of this class is to make specifying custom rgb mappings
*   easy for the fictional researchers and phd students that this work
*   is to be passed on to.   
*
*   WARNING: Very much a problem specific helper class in its current form
*/
public class RangeMap<T> {
    
    /* Root node for the tree */
    RangeNode<T> root;

    /* Constructor for the class */
    public RangeMap(){
        this.root = null;
    }

    /** Node class for the binary search tree*/
    private static class RangeNode<T> implements Comparable<RangeNode<T>>{
        /** Start and end are the beginning and end of the range. Start is inclusive, end is not */
        int start;
        int end;
        T data;
        /** Child nodes */
        RangeNode<T> left;
        RangeNode<T> right;
    
        /**
         * Constructor
         * @param start the start of the range (inclusive)
         * @param end the end of the range (not inclusive)
         * @param data the value mapped to by any int in the range
         */
        RangeNode(int start, int end, T data){
            if(start >= end){
                throw new IllegalArgumentException("Start must be less than end");
            }
            this.start = start;
            this.end = end;
            this.data = data;
            left = null;
            right = null;
        }

        /* We use two compareTwo methods, one for inserting nodes into the tree, the other for searching 
        *   for ints within it
        */
        @Override
        public int compareTo(RangeNode<T> node){
            if(this.end <= node.start){
                return -1;
            } else if(this.start >= node.end){
                return 1;
            } else{ 
                return 0;
            }
        }

        public int compareTo(int getVal){
            if(getVal < start){
                return 1;
            } else if(end <= getVal){
                return -1;
            } else{
                return 0;
            }
        }
    }

    /**
     * Helper function for add
     * @param pointer the node below which we wish to insert toAdd
     * @param toAdd the node we wish to insert
     * @return the node that the add method will place at pointer's position
     */
    private RangeNode<T> addRecursion(RangeNode<T> pointer, RangeNode<T> toAdd){
        //If pointer is null, simply replace it with toAdd
        if(pointer == null){
            return toAdd;
        //If pointer is not null, see if we can recurse down to the left or right child
        } else if(toAdd.compareTo(pointer) == -1){
            pointer.left = addRecursion(pointer.left, toAdd);
        } else if(toAdd.compareTo(pointer) == 1){
            pointer.right = addRecursion(pointer.right, toAdd);
        //This code occurs if you entered a range overlapping another range in the tree
        } else{
            throw new IllegalArgumentException("Nodes must not have overlapping ranges");
        }
        //If we aren't replacing with toAdd, we just return it
        return pointer;
    }

    /**
     * Given the attributes for a RangeNode, creates one and inserts it into the tree
     * @param start The start of the range (inclusive)
     * @param end   The end of the range (not inclusive)
     * @param data  The data we wish to store at the node
     */
    public void add(int start, int end, T data){
        var toAdd = new RangeNode<T>(start, end, data);
        this.root = addRecursion(this.root, toAdd);
    }
    
    /**
     * Helper function for get, recursively searches the the tree consisting of pointer and its
     *  children
     * @param pointer   The node we start searching from
     * @param getVal    The value for which we want a matching range
     * @return  Either null, or the data corresponding to the range that getVal fits into
     */
    private T getRecursion(RangeNode<T> pointer, int getVal){
        if(pointer == null){
            return null;
        } else if(pointer.compareTo(getVal) == 1){
            return getRecursion(pointer.left, getVal);
        } else if(pointer.compareTo(getVal) == -1){
            return getRecursion(pointer.right, getVal);
        } else{
            return pointer.data;
        }
    }

    /**
     * Returns data of type T if getVal is within a range assigned a data value by the tree
     *  or null otherwise
     * @param getVal The value for which we want a matching range
     * @return  Data of type T, or null
     */
    public T get(int getVal){
        return getRecursion(root, getVal);
    }
}
