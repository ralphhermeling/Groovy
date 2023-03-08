package fruit
class FruitTypeAndProductStatePair {
    private String fruitType
    private String productState

    FruitTypeAndProductStatePair(String fruitType, String productState){
        this.fruitType = fruitType
        this.productState = productState
    }

    public String getFruitType(){
        return fruitType
    }

    public String getProductState(){
        return productState
    }
}
