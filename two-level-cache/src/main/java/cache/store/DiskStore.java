package cache.store;

import cache.core.DiskElementInfo;
import cache.strategy.ExpirationStrategy;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import org.apache.log4j.Logger;

public class DiskStore<K, V> {

    private static final Logger logger = Logger.getLogger(DiskStore.class);
    private final String fileName = UUID.randomUUID() + "_cache-storage.data";
    private final ExpirationStrategy expirationStrategy;
    private final long maxBytesLocalDisk;
    private final String pathToLocalDisk;
    private RandomAccessFile randomAccessFile;
    private final LinkedHashMap<K, DiskElementInfo> elementsMap;
    private List<DiskElementInfo> freeSpace = new ArrayList<>();
    private long storageSize;

    public DiskStore(ExpirationStrategy expirationStrategy, long maxBytesLocalDisk, String pathToLocalDisk) {
        this.expirationStrategy = expirationStrategy;
        this.maxBytesLocalDisk = maxBytesLocalDisk;
        this.pathToLocalDisk = pathToLocalDisk;
        elementsMap = expirationStrategy.createDiskLevelCache();
        try {
            initialiseFile();
        } catch (Exception ex) {
            logger.error("Cannot create disk storage file: ", ex);
        }
    }

    private void initialiseFile() throws Exception {

        File dir = new File(pathToLocalDisk);

        if (!dir.exists()) {
            dir.mkdirs();
        } else if (!dir.isDirectory()) {
            throw new Exception("The path:" + pathToLocalDisk + " is not a directory.");
        }

        File file = new File(pathToLocalDisk, fileName);
        randomAccessFile = new RandomAccessFile(file, "rw");
    }

    public boolean isOverflow() {
        return storageSize >= maxBytesLocalDisk;
    }

    public V get(K key) {
        V diskElement = null;
        if (elementsMap.containsKey(key)) {
            DiskElementInfo diskElementInfo = elementsMap.get(key);
            byte[] buffer = new byte[diskElementInfo.getPayloadSize()];
            try {
                randomAccessFile.seek(diskElementInfo.getPointer());
                randomAccessFile.readFully(buffer);
                diskElement = readObjectFromFile(buffer);
            } catch (IOException ex) {
                logger.error("Exception while reading disk storage: ", ex);
            }
        }
        return diskElement;
    }

    private V readObjectFromFile(byte[] buffer) {
        V diskElement = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            diskElement = (V) objectInputStream.readObject();
        } catch (Exception ex) {
            logger.error("Exception while reading disk storage: ", ex);
        }
        return diskElement;
    }

    public void put(K key, V value) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(bos)) {

            objectOutputStream.writeObject(value);
            int payloadSize = bos.toByteArray().length;
            DiskElementInfo diskElementInfo = findfreeBlock(payloadSize);
            if (diskElementInfo == null) {
                diskElementInfo = new DiskElementInfo();
                diskElementInfo.setBlockSize(payloadSize);
                diskElementInfo.setPayloadSize(payloadSize);
                diskElementInfo.setPointer(randomAccessFile.getFilePointer());
            }

            randomAccessFile.write(bos.toByteArray());
            storageSize += payloadSize;
            elementsMap.put(key, diskElementInfo);
        } catch (IOException ex) {
            logger.error("Exception while writing disk storage: ", ex);
        }
    }

    public void remove(K key) {
        DiskElementInfo diskElementInfo = elementsMap.get(key);
        if (diskElementInfo != null) {
            storageSize -= diskElementInfo.getBlockSize();
            diskElementInfo.setPayloadSize(0);
            freeSpace.add(diskElementInfo);
            elementsMap.remove(key);
        }
    }

    private DiskElementInfo findfreeBlock(int bufferLength) {
        for (int i = 0; i < freeSpace.size(); i++) {
            DiskElementInfo diskElementInfo = freeSpace.get(i);
            if (diskElementInfo.getBlockSize() > bufferLength) {
                freeSpace.remove(i);
                diskElementInfo.setPayloadSize(bufferLength);
                return diskElementInfo;
            }
        }
        return null;
    }

    public LinkedHashMap<K, DiskElementInfo> getElementsMap() {
        return elementsMap;
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
