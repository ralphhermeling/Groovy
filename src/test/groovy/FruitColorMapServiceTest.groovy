import fruit.colormap.FruitColorMapService
import groovy.json.JsonSlurper
import groovy.test.GroovyTestCase

class FruitColorMapServiceTest extends GroovyTestCase {

    void testGetProductStateFromColorAndFruit() {
        def fruitColorMapService = new FruitColorMapService(Map.of("Aardbei", Map.of("Bruin", "rot", "Rood", "rijp") as Map<String, String>,
                "Banaan", Map.of("Groen", "groen", "Geel", "rijp") as Map<String, String>))

        assertEquals("rot", fruitColorMapService.getProductStateFromColorAndFruit("Aardbei", "Bruin"))
        assertEquals("rijp", fruitColorMapService.getProductStateFromColorAndFruit("Aardbei", "Rood"))
        assertEquals("groen", fruitColorMapService.getProductStateFromColorAndFruit("Banaan", "Groen"))
        assertEquals("rijp", fruitColorMapService.getProductStateFromColorAndFruit("Banaan", "Geel"))

    }

    void testGetFruitColorMap() {
        def fruitColorMapService = new FruitColorMapService(new File("./src/test/fixtures/FruitColorMap.json"), ["Banaan", "Appel"], new JsonSlurper())

        def fruitColorMapActual = fruitColorMapService.getFruitColorMap()
        def fruitColorMapExpected = Map.of("Banaan", Map.of("Bruin", "overrijp", "Geel", "rijp") as Map<String, String>,
                "Appel", Map.of("Bruin", "rot", "Geel", "rijp") as Map<String, String>)

        assertTrue(areEqualNestedMap(fruitColorMapActual, fruitColorMapExpected))

    }

    private boolean areEqualNestedMap(Map<String, Map<String, String>> first, Map<String, Map<String, String>> second) {
        if (first.size() != second.size()) {
            return false;
        }

        return first.entrySet().stream()
                .allMatch(e -> areEqual(e.getValue(), second.get(e.getKey())));
    }

    private boolean areEqual(Map<String, String> first, Map<String, String> second) {
        if (first.size() != second.size()) {
            return false;
        }

        return first.entrySet().stream()
                .allMatch(e -> e.getValue().equals(second.get(e.getKey())));
    }
}
