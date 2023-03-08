package fruit

import fruit.colormap.FruitColorMapService
import fruit.records.FruitOrderXMLParser
import fruit.records.FruitRecord
import fruit.records.FruitRecordService
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.xml.XmlParser

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

static void main(String[] args) {
    FruitRecordService frService = new FruitRecordService(
            new JsonSlurper(),
            new FruitOrderXMLParser(new XmlParser())
    )
    List<FruitRecord> fruitRecords = frService.getFruitRecords(new File("../../resources/data/FruitExport_20220601.json"))

    FruitColorMapService fcmService = new FruitColorMapService(
            new File("../../resources/data/FruitColorMap.json"),
            ["Banaan", "Appel", "Aardbei"],
            new JsonSlurper()
    )
    Map<String, Map<String, Integer>> result = Computations.getProductStateCountsFromFruitRecords(fruitRecords, fcmService)

    def bananaResult = Map.of("fruit",
            Map.of("Banaan", result.get("Banaan"))
    )
    def resultJson = JsonOutput.toJson(bananaResult)

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd")
    LocalDateTime  now = LocalDateTime.now()
    String date = dtf.format(now)

    File outputFile = new File(String.format("../../../output/FruitOutput_%s_10010.json", date))
    try (PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {
        out.write(resultJson)
    } catch (Exception e) {
        e.printStackTrace()
    }

}


