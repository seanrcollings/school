package com.example;
// QuadraticProbing Hash table class
//
// CONSTRUCTION: an approximate initial size or default of 101
//
// ******************PUBLIC OPERATIONS*********************
// bool insert( x )       --> Insert x
// bool remove( x )       --> Remove x
// bool contains( x )     --> Return true if x is present
// void makeEmpty( )      --> Remove all items


import java.util.Arrays;

/**
 * Probing table implementation of hash tables.
 * Note that all "matching" is based on the equals method.
 * @author Mark Allen Weiss
 */
public class HashTable<E>
{
    /**
     * Construct the hash table.
     */
    public HashTable( )
    {
        this(DEFAULT_TABLE_SIZE_1, DEFAULT_TABLE_SIZE_2);
    }ne

    /**
     * Construct the hash table.
     * @param size1 the approximate initial size of the first array
     * @param size2 the appriximate intiail size of the second array
     */
    public HashTable( int size1, int size2 )
    {
        allocateArray( size1, size2 );
        doClear( );
    }

    /**
     * Insert into the hash table. If the item is
     * already present, do nothing.
     * Implementation issue: This routine doesn't allow you to use a lazily deleted location.  Do you see why?
     * @param x the item to insert.
     */
    public boolean insert( E x ){
        if (contains(x)) { return true; };

        HashEntry<E> [] currentArray = array;
        int MAX_INSERTS = 10;
        int currentInserts = 0;
        E item = x;

        while (currentInserts < MAX_INSERTS) {
            int wantsToBeAtPos = hash(item, currentArray);

            if (currentArray[wantsToBeAtPos] == null) {
                occupiedCt++;
                currentArray[wantsToBeAtPos] = new HashEntry<E>(item);
                return true;
            } else {
                HashEntry<E> temp = currentArray[wantsToBeAtPos];
                currentArray[wantsToBeAtPos] = new HashEntry<E>(item);
                item = temp.element;
                currentInserts++;
                currentArray = Arrays.equals(currentArray, array) ? array2 : array;
            }
        }
        rehash();
        return insert(x);
    }

    public String toString (int limit){
        StringBuilder sb = new StringBuilder();
        int ct=0;
        sb.append("FIRST ARRAY\n");
        for (int i=0; i < array.length && ct < limit; i++){
            if (array[i]!=null && array[i].isActive) {
                sb.append(i).append(": ").append(array[i].element).append("\n");
                ct++;
            }
        }
        sb.append("SECOND ARRAY\n");
        for (int i=0; i < array2.length && ct < limit; i++){
            if (array2[i]!=null && array2[i].isActive) {
                sb.append(i).append(": ").append(array2[i].element).append("\n");
                ct++;
            }
        }
        return sb.toString();
    }

    /**
     * Expand the hash table.
     */
    private void rehash( )
    {
        HashEntry<E> [ ] oldArray1 = array;
        HashEntry<E> [ ] oldArray2 = array2;

        // Create a new double-sized, empty table
        allocateArray( 2 * oldArray1.length, 2 * oldArray2.length );
        occupiedCt = 0;
        currentActiveEntries = 0;

        // Copy table over
        for( HashEntry<E> entry : oldArray1 )
            if( entry != null && entry.isActive )
                insert( entry.element );

        for( HashEntry<E> entry : oldArray2 )
            if( entry != null && entry.isActive )
                insert( entry.element );
    }

    /**
     * Method that performs quadratic probing resolution.
     * @param x the item to search for.
     * @return the position where the search terminates.
     * Never returns an inactive location.
     */
    private int findPos( E x )
    {
        int offset = 1;
        int currentPos = hash( x, array );

        while( array[ currentPos ] != null &&
                !array[ currentPos ].element.equals( x ) )
        {
            currentPos += offset;  // Compute ith probe
            offset += 2;
            if( currentPos >= array.length )
                currentPos -= array.length;
        }

        return currentPos;
    }

    private int isAtPos(E x) {
        int pos = hash(x, array);
        if (array[pos] == null) return -1;
        if (array[pos].element.equals(x)) return pos;

        pos = hash(x, array2);
        if (array2[pos] == null) return -1;
        if (array2[pos].element.equals(x))  return pos;

        return -1;
    }


    /**
     * Remove from the hash table.
     * @param x the item to remove.
     * @return true if item removed
     */
    public boolean remove( E x ) {
        int currentPos = findPos( x );
        if( isActive( currentPos ) ) {
            array[ currentPos ].isActive = false;
            currentActiveEntries--;
            return true;
        }
        return false;
    }

    /**
     * Get current size.
     * @return the size.
     */
    public int size( )
    {
        return currentActiveEntries;
    }

    /**
     * Get length of internal table.
     * @return the size.
     */
    public int capacity( )
    {
        return array.length + array2.length;
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return true if item is found
     */
    public boolean contains( E x )
    {
        int currentPos = isAtPos( x );
        return currentPos != -1;
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return the matching item.
     */
    public E find( E x ) {
        int currentPos = isAtPos( x );

        if (currentPos == -1)
            return null;

        return array[currentPos] != null ?
                array[currentPos].element :
                array2[currentPos].element;
    }

    /**
     * Return true if currentPos exists and is active.
     * @param currentPos the result of a call to findPos.
     * @return true if currentPos is active.
     */
    private boolean isActive( int currentPos )
    {
        return array[ currentPos ] != null && array[ currentPos ].isActive;
    }

    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty( )
    {
        doClear( );
    }

    private void doClear( )
    {
        occupiedCt = 0;
        Arrays.fill(array, null);
        Arrays.fill(array2, null);
    }

    private int hash(E x, HashEntry<E> [] array)
    {
        int hashVal = x.hashCode( );

        hashVal %= array.length;
        if( hashVal < 0 )
            hashVal += array.length;

        return hashVal;
    }

    private static class HashEntry<E>
    {
        public E  element;   // the element
        public boolean isActive;  // false if marked deleted

        public HashEntry( E e )
        {
            this( e, true );
        }

        public HashEntry( E e, boolean i )
        {
            element  = e;
            isActive = i;
        }
    }

    private static final int DEFAULT_TABLE_SIZE_1 = 101;
    private static final int DEFAULT_TABLE_SIZE_2 = 107;

    private HashEntry<E> [ ] array; // The array of elements
    private HashEntry<E> [] array2;
    private int occupiedCt;         // The number of occupied cells: active or deleted
    private int currentActiveEntries;                  // Current size

    /**
     * Internal method to allocate array.
     * @param arraySize2 the size of the array.
     */
    private void allocateArray( int arraySize1, int arraySize2 )
    {
        array  = new HashEntry[ nextPrime(arraySize1) ];
        array2 = new HashEntry[ nextPrime(arraySize2) ];
    }

    /**
     * Internal method to find a prime number at least as large as n.
     * @param n the starting number (must be positive).
     * @return a prime number larger than or equal to n.
     *
     */
    private static int nextPrime( int n )
    {
        if( n % 2 == 0 )
            n++;

        for( ; !isPrime( n ); n += 2 )
            ;

        return n;
    }

    /**
     * Internal method to test if a number is prime.
     * Not an efficient algorithm.
     * @param n the number to test.
     * @return the result of the test.
     */
    private static boolean isPrime( int n )
    {
        if( n == 2 || n == 3 )
            return true;

        if( n == 1 || n % 2 == 0 )
            return false;

        for( int i = 3; i * i <= n; i += 2 )
            if( n % i == 0 )
                return false;

        return true;
    }


    public static void main( String [] args ) {

    }

}

