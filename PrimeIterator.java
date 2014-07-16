package edu.neumont.algorithms;

import java.util.Iterator;

public class PrimeIterator implements Iterator<Integer> {

	int nextNumber = 0;
	boolean[] isPrime;
	
	public PrimeIterator(int max) {
		// initially assume all integers are prime
		isPrime = new boolean[max + 1];

		for (int i = 2; i <= max; i++) {
			isPrime[i] = true;
		}

		// mark non-primes <= N using Sieve of Eratosthenes
		for (int i = 2; i * i <= max; i++) {
			// if i is prime, then mark multiples of i as nonprime
			// suffices to consider multiples i, i+1, ..., N/i
			if (isPrime[i]) {
				for (int j = i; i * j <= max; j++) {
					isPrime[i * j] = false;
				}
			}
		}
	}

	// Returns whether this iterator has any more prime numbers less than max
	@Override
	public boolean hasNext() {
		while (nextNumber < isPrime.length && !isPrime[nextNumber]) {
			nextNumber++;
		}

		if (nextNumber < isPrime.length) {
			return true;
		}

		return false;
	}

	@Override
	public Integer next() {

		while (nextNumber < isPrime.length && !isPrime[nextNumber]) {
			if (!isPrime[nextNumber]) {
				nextNumber++;
			}
		}

		return nextNumber++;

	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
}
