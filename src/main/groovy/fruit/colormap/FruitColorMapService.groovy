package fruit.colormap
import groovy.json.JsonSlurper
import java.util.stream.Collectors

class FruitColorMapService {
    private File file
    private Map<String, Map<String, String>> fruitColorMap
    private List<String> supportedFruits
    private JsonSlurper jsonSlurper

    FruitColorMapService(File file, List<String> supportedFruits, JsonSlurper jsonSlurper) {
        this.file = file
        this.supportedFruits = supportedFruits
        this.jsonSlurper = jsonSlurper
    }

    FruitColorMapService(Map<String, Map<String, String>> fruitColorMap) {
        this.fruitColorMap = fruitColorMap
    }

    public String getProductStateFromColorAndFruit(String fruit, String color) {
        Map<String, Map<String, String>> fcMap = getFruitColorMap()
        if (!fcMap.containsKey(fruit)) {
            throw new IllegalStateException(String.format("Fruit %s is not supported in FruitColorMap from file %s", fruit, file.name))
        }

        if (!fcMap.get(fruit).containsKey(color)) {
            throw new IllegalStateException(String.format("Color %s is not supported for fruit %s in FruitColorMap from file %s", color, fruit, file.name))
        }

        return fcMap.get(fruit).get(color)
    }

    public Map<String, Map<String, String>> getFruitColorMap() {
        if (fruitColorMap != null) {
            return fruitColorMap
        }

        Object parsedFruitColorMap = jsonSlurper.parse(file)

        if (parsedFruitColorMap == null) {
            throw new IllegalStateException(String.format("Unable to parse json file %s", file.name))
        }

        if (parsedFruitColorMap.fruit == null) {
            throw new IllegalStateException(String.format("Unable to retrieve fruit property in file %s", file.name))
        }

        List<Object> fruitList = parsedFruitColorMap.fruit as List<Object>
        fruitColorMap = fruitList
                .stream()
                .map(this::getFruitMap)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))

        return fruitColorMap
    }

    private Optional<Map.Entry<String, Map<String, String>>> getFruitMap(Object o) {
        Optional<String> matchingFruitType = getMatchingFruitType(o)
        if (matchingFruitType.isEmpty()) {
            return Optional.empty()
        }

        List<FruitColorAndProductStatePair> fruitColorAndProductStatePairs = o[matchingFruitType.get()] as List<FruitColorAndProductStatePair>
        if (fruitColorAndProductStatePairs == null) {
            return Optional.empty()
        }

        return Optional.of(Map.entry(matchingFruitType.get(),
                convertFruitColorAndProductStatePairsIntoMap(fruitColorAndProductStatePairs)))
    }

    private Optional<String> getMatchingFruitType(Object o) {
        for (String supportedFruit : supportedFruits) {
            if (o[supportedFruit] != null) {
                return Optional.of(supportedFruit)
            }
        }
        return Optional.empty()
    }

    private Map<String, String> convertFruitColorAndProductStatePairsIntoMap(List<FruitColorAndProductStatePair> fruitColorAndProductStatePairs) {
        Map<String, String> result = new HashMap<>()
        fruitColorAndProductStatePairs
                .stream()
                .forEach(pair -> result.put(pair.color, pair.productState))
        return result
    }

}
