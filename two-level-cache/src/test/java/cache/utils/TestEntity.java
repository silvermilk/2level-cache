package cache.utils;

import java.io.Serializable;

public class TestEntity implements Serializable {

    private final String field;
    
    public TestEntity(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
