package cache.store;

import cache.core.DiskElementInfo;
import cache.strategy.ExpirationStrategy;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nastya
 */
public class DiskStore<K, V> {

    private final ExpirationStrategy expirationStrategy;
    private final long maxBytesLocalDisk;
    private final String pathToLocalDisk;
    private static final String FILE_NAME = "cache-storage.data";

    private RandomAccessFile randomAccessFile;
    private ConcurrentHashMap<K, DiskElementInfo> elementsMap = new ConcurrentHashMap();

    public DiskStore(ExpirationStrategy expirationStrategy, long maxBytesLocalDisk, String pathToLocalDisk) {
        this.expirationStrategy = expirationStrategy;
        this.maxBytesLocalDisk = maxBytesLocalDisk;
        this.pathToLocalDisk = pathToLocalDisk;

        try {
            initialiseFile();
        } catch (Exception ex) {
            Logger.getLogger(DiskStore.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initialiseFile() throws Exception {

        File dir = new File(pathToLocalDisk);

        if (!dir.exists()) {
            dir.mkdirs();
        } else if (!dir.isDirectory()) {
            throw new Exception("The path:" + pathToLocalDisk + " is not a directory.");
        }

        File file = new File(pathToLocalDisk, FILE_NAME);
        randomAccessFile = new RandomAccessFile(file, "rw");
    }

    public V get(K key) {
        V diskElement = null;
        if (elementsMap.containsKey(key)) {
            DiskElementInfo diskElementInfo = elementsMap.get(key);

            try {
                randomAccessFile.seek(diskElementInfo.pointer);
                byte[] buffer = new byte[diskElementInfo.payloadSize];
                randomAccessFile.readFully(buffer);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                diskElement = (V) objectInputStream.readObject();
            } catch (Exception ex) {
                Logger.getLogger(DiskStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return diskElement;
    }

    public ExpirationStrategy getExpirationStrategy() {
        return expirationStrategy;
    }

    public long getMaxBytesLocalDisk() {
        return maxBytesLocalDisk;
    }

    public String getPathToLocalDisk() {
        return pathToLocalDisk;
    }

}
