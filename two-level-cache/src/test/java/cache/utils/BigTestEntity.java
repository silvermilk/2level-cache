package cache.utils;

public class BigTestEntity extends TestEntity {

    private final String someLongField;
    private final String someVeryLongField;

    public BigTestEntity(String field, String someLongField, String someVeryLongField) {
        super(field);

        this.someLongField = someLongField;
        this.someVeryLongField = someVeryLongField;
    }

    public String getSomeLongField() {
        return someLongField;
    }

    public String getSomeVeryLongField() {
        return someVeryLongField;
    }

}
