package cache.core;

/**
 *
 * @author Nastya
 */
public class DiskElementInfo  {
 
    public final long pointer;
    public final int payloadSize;
    DiskElementInfo(long pointer, int payloadSize) {
        this.pointer = pointer;
        this.payloadSize = payloadSize;
    }
}
