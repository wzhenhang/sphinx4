/*
 * Copyright 1999-2002 Carnegie Mellon University.  
 * Portions Copyright 2002 Sun Microsystems, Inc.  
 * Portions Copyright 2002 Mitsubishi Electronic Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 * 
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL 
 * WARRANTIES.
 *
 */

package edu.cmu.sphinx.knowledge.language.large;

import java.nio.ByteBuffer;

/**
 * Implements a buffer for trigrams read from disk.
 */
public class TrigramBuffer extends NGramBuffer {
    
    /**
     * Constructs a TrigramBuffer object with the given ByteBuffer.
     *
     * @param trigramsOnDisk the ByteBuffer with trigrams
     * @param numberNGrams the number of trigram follows in the ByteBuffer
     */
    public TrigramBuffer(ByteBuffer trigramsOnDisk,
			 int numberNGrams) {
        super(trigramsOnDisk, numberNGrams);
    }


    /**
     * Finds the trigram probabilities for the given third word in a trigram.
     *
     * @param thirdWordID the ID of the third word
     *
     * @return the TrigramProbability of the given third word
     */
    public TrigramProbability findTrigram(int thirdWordID) {

        // System.out.println("Looking for: " + thirdWordID);

        int mid, start = 0, end = getNumberNGrams();
        TrigramProbability trigram = null;

        while ((end - start) > 0) {
            mid = (start + end)/2;
            int midWordID = getWordID(mid);
	    if (midWordID < thirdWordID) {
                start = mid + 1;
            } else if (midWordID > thirdWordID) {
                end = mid;
            } else {
		trigram = getTrigramProbability(mid);
                break;
	    }
        }
        return trigram;
    }


    /**
     * Returns the TrigramProbability of the nth follower.
     *
     * @param nthFollower which follower
     *
     * @return the TrigramProbability of the nth follower
     */
    public final TrigramProbability getTrigramProbability(int nthFollower) {
        int nthPosition = nthFollower * LargeTrigramModel.BYTES_PER_TRIGRAM;
        getBuffer().position(nthPosition);

        int wordID = readTwoBytesAsInt();
        int probID = readTwoBytesAsInt();

        return (new TrigramProbability(wordID, probID));
    }
}
