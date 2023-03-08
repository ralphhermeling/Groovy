import fruit.records.FruitOrderXMLParser
import fruit.records.FruitRecord
import fruit.records.FruitRecordService
import groovy.json.JsonSlurper
import groovy.test.GroovyTestCase
import groovy.xml.XmlParser

class FruitRecordServiceTest extends GroovyTestCase {
    void testGetFruitRecords() {
        def fruitRecordService = new FruitRecordService(new JsonSlurper(), new FruitOrderXMLParser(new XmlParser()))
        def fruitRecordsActual = fruitRecordService.getFruitRecords(new File("./src/test/fixtures/FruitExport_20230308.json"))
        def fruitRecordsExpected = List.of(
                new FruitRecord().setType("Banaan").setColor("Geel").setExpiryDate("2023-06-01T09:44:22").build(),
                new FruitRecord().setType("Banaan").setColor("Bruin").setExpiryDate("2023-06-01T09:44:22").build(),
                new FruitRecord().setType("Aardbei").setColor("Rood").setExpiryDate("2023-06-01T09:44:22").build(),
                new FruitRecord().setType("Aardbei").setColor("Rood").setExpiryDate("2023-06-01T09:44:22").build(),
                new FruitRecord().setType("Banaan").setColor("Bruin").setExpiryDate("2023-06-01T09:44:22").build(),
                new FruitRecord().setType("Aardbei").setColor("Rood").setExpiryDate("2023-06-01T09:44:22").build(),
                new FruitRecord().setType("Aardbei").setColor("Rood").setExpiryDate("2023-06-01T09:44:22").build(),
                new FruitRecord().setType("Appel").setColor("Geel").setExpiryDate("2022-05-01T09:44:22").build()
        )
        assertTrue(areFruitRecordsEqual(fruitRecordsExpected, fruitRecordsActual))
    }

    private areFruitRecordsEqual(List<FruitRecord> one, List<FruitRecord> two){
        if(one == two){
            return true
        }

        else if(one.size() != two.size()){
            return false
        }

        int i = 0
        int j = 0

        while(i<one.size()){
            if(!isFruitRecordEqual(one.get(i),two.get(j))){
                return false
            }
            i+=1
            j+=1
        }
        return true

    }

    private isFruitRecordEqual(FruitRecord o, FruitRecord t){
        return o.getType().equals(t.getType()) && o.getColor().equals(t.getColor()) && o.getExpiryDate().equals(t.getExpiryDate())
    }
}
