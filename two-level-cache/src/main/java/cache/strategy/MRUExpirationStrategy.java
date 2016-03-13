/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache.strategy;

import cache.store.DiskStore;
import cache.store.MemoryStore;
import java.util.LinkedHashMap;

/**
 *
 * @author Nastya
 */
public class MRUExpirationStrategy implements ExpirationStrategy {

    @Override
    public LinkedHashMap createMemoryLevelCache(int maxEntries) {
        //"Not supported yet."
        return null;
    }

    @Override
    public LinkedHashMap createDiskLevelCache(DiskStore diskLevel) {
        //"Not supported yet."
        return null;
    }

}
