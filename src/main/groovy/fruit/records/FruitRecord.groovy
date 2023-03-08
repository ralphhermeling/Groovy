package fruit.records
public class FruitRecord {
    private String type
    private String color

    private String expiryDate
    public FruitRecord setType(String type) {
        this.type = type
        return this
    }

    public String getColor() {
        return color
    }

    public FruitRecord setColor(String color) {
        this.color = color
        return this
    }

    public String getType() {
        return type
    }

    public String getExpiryDate() {
        return expiryDate
    }

    public FruitRecord setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate
        return this
    }

    public FruitRecord build(){
        return this
    }

    @Override
    public String toString(){
        return String.format("Type: %s \n Color: %s \n ExpiryDate: %s", this.type, this.color, this.expiryDate)
    }

}
