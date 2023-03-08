package fruit

import fruit.colormap.FruitColorMapService
import fruit.records.FruitRecord

import java.util.stream.Collectors

class Computations {
    static Map<String, Map<String, Integer>> getProductStateCountsFromFruitRecords(List<FruitRecord> fruitRecords, FruitColorMapService fcmService) {
        return fruitRecords
                .stream()
                .map((fruitRecord) -> new FruitTypeAndProductStatePair(
                        fruitRecord.getType(),
                        fcmService.getProductStateFromColorAndFruit(fruitRecord.getType(), fruitRecord.getColor())
                ))
                .collect(
                        Collectors.groupingBy(
                                FruitTypeAndProductStatePair::getFruitType,
                                Collectors.groupingBy(
                                        FruitTypeAndProductStatePair::getProductState, Collectors.counting()
                                )
                        )
                ) as Map<String, Map<String, Integer>>
    }
}
