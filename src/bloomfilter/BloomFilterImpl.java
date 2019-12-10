
package bloomfilter;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Pushpa Patil
 */
public class BloomFilterImpl extends AbstractSet<Object> {

	byte[] set;
	int k;

	int setSize;
	int currentSetSize;

	MessageDigest m;

	/**
	 * @param size
	 *            the size(m) of the byte array backing the BloomSet
	 * @param keySize
	 *            the number of bits(k) sit for each object in the set.
	 */
	public BloomFilterImpl(int size, int keySize) {
		setSize = size;
		set = new byte[setSize];
		k = keySize;
		currentSetSize = 0;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("MD5 Hash not found");
		}
	}

	private int getHash(int i) {
		m.reset();
		byte[] bytes = ByteBuffer.allocate(4).putInt(i).array();
		m.update(bytes, 0, bytes.length);
		return Math.abs(new BigInteger(1, m.digest()).intValue()) % (set.length - 1);
	}

	@Override
	public boolean add(Object obj) {
		int[] toSet = getSetArray(obj);

		for (int x : toSet)
			set[x] = 1;

		currentSetSize++;
		return true;
	}

	@Override
	public boolean contains(Object obj) {
		int[] toSet = getSetArray(obj);

		for (int x : toSet)
			if (set[x] != 1)
				return false;

		return true;
	}

	private int[] getSetArray(Object obj) {
		int[] toSet = new int[k];

		toSet[0] = getHash(obj.hashCode());

		for (int i = 1; i < k; i++)
			toSet[i] = (getHash(toSet[i - 1]));

		return toSet;
	}

	@Override
	public int size() {
		return currentSetSize;
	}

	@Override
	public boolean isEmpty() {
		return currentSetSize < 1;
	}

	
	@Override
	public Object[] toArray() {
		return null;
	}

	
	@Override
	public Object[] toArray(Object[] a) {
		return null;
	}

	
	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection c) {
		Iterator i = c.iterator();

		while (i.hasNext())
			if (!contains(i.next()))
				return false;

		return true;
	}

	@Override
	public boolean addAll(Collection c) {
		Iterator i = c.iterator();

		while (i.hasNext())
			if (!add(i.next()))
				return false;
		return true;
	}

	
	@Override
	public boolean retainAll(Collection c) {
		return false;
	}

	
	@Override
	public boolean removeAll(Collection c) {
		return false;
	}

	@Override
	public void clear() {
		currentSetSize = 0;
		set = new byte[setSize];
	}

	@Override
	public Iterator<Object> iterator() {
		return null;
	}
}