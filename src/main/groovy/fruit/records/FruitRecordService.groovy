package fruit.records

import groovy.json.JsonSlurper

class FruitRecordService {

    private JsonSlurper jsonSlurper
    private FruitOrderXMLParser fruitOrderXMLParser
    FruitRecordService(JsonSlurper jsonSlurper, FruitOrderXMLParser fruitOrderXMLParser){
        this.jsonSlurper = jsonSlurper
        this.fruitOrderXMLParser = fruitOrderXMLParser
    }

    public List<FruitRecord> getFruitRecords(File file) {
        def fruitExport = jsonSlurper.parse(file)

//  Rudimentary input validation, I would prefer to use an package which decodes JSON to Groovy/Java objects e.g. Jackson
        if (fruitExport == null || fruitExport.fruitOrder == null || fruitExport.fruitOrder.orderBase64 == null) {
            throw new IllegalStateException(String.format("We can't parse file %s because it does not satisfy FruitExport schema", file.name))
        }

        byte[] decodedBase64FruitOrder = fruitExport.fruitOrder.orderBase64.decodeBase64()
        String fruitOrder = new String(decodedBase64FruitOrder)

        return fruitOrderXMLParser.getFruitRecords(fruitOrder)
    }


}
