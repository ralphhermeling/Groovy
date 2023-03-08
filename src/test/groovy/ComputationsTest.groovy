import fruit.Computations
import fruit.colormap.FruitColorMapService
import fruit.records.FruitRecord
import groovy.test.GroovyTestCase

class ComputationsTest extends GroovyTestCase {
    void testProductStateCounterComputation() {

        List<FruitRecord> fruitRecords = List.of(
                new FruitRecord().setType("Aardbei").setColor("Bruin").build(),
                new FruitRecord().setType("Aardbei").setColor("Bruin").build(),
                new FruitRecord().setType("Aardbei").setColor("Rood").build(),
                new FruitRecord().setType("Aardbei").setColor("Rood").build(),
                new FruitRecord().setType("Banaan").setColor("Groen").build(),
                new FruitRecord().setType("Banaan").setColor("Geel").build()
        )


        def fruitColorMapServiceMock = new FruitColorMapService(Map.of(
                "Aardbei", Map.of("Bruin", "rot", "Rood", "rijp") as Map<String, String>,
                "Banaan", Map.of("Groen", "groen", "Geel", "rijp") as Map<String, String>
        ))


        def result = Computations.getProductStateCountsFromFruitRecords(fruitRecords, fruitColorMapServiceMock)

        assertEquals(2, result.get("Aardbei").get("rijp"))
        assertEquals(2, result.get("Aardbei").get("rot"))
        assertEquals(1, result.get("Banaan").get("groen"))
        assertEquals(1, result.get("Banaan").get("rijp"))

    }
}
