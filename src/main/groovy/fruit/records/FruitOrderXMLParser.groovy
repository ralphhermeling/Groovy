package fruit.records


import groovy.xml.XmlParser

import java.util.stream.Collectors

class FruitOrderXMLParser {
    private XmlParser xmlParser

    FruitOrderXMLParser(XmlParser xmlParser){
        this.xmlParser = xmlParser
    }

    public List<FruitRecord> getFruitRecords(String fruitOrderXML) {

        def fruitData = new XmlParser().parseText(fruitOrderXML)
        if (fruitData == null) {
            throw new IllegalStateException("Unable to parse xml from fruitOrder string")
        }

        Optional<Node> fruitColorsNode = getChildNodeByName("FruitColors", fruitData.children())
        if(fruitColorsNode.isEmpty()){
            throw new IllegalStateException("Unable to find FruitColors in parsed XML")
        }

        Optional<Node> fruitTypesNode = getChildNodeByName("FruitTypes", fruitData.children())
        if(fruitTypesNode.isEmpty()){
            throw new IllegalStateException("Unable to find FruitTypes in parsed XML")
        }

        Optional<Node> fruitRecordsNode = getChildNodeByName("FruitRecords", fruitData.children())
        if(fruitRecordsNode.isEmpty()){
            throw new IllegalStateException("Unable to find FruitRecords in parsed XML")
        }

        Map<String, String> fruitColors = getMapWithAttributes(fruitColorsNode.get().children() as List<Node>, "id", "desc")
        Map<String, String> fruitTypes = getMapWithAttributes(fruitTypesNode.get().children() as List<Node>, "id", "desc")

        List<Node> fruitRecordNodes = fruitRecordsNode.get().children() as List<Node>
        List<FruitRecord> fruitRecords = fruitRecordNodes
                .stream()
                .map(node -> enrichFruitRecord(node, fruitTypes, fruitColors))
                .collect(Collectors.toList())
        return fruitRecords

    }

    private Optional<Node> getChildNodeByName(String name, List<Node> children){
        return children
                .stream()
                .filter((node) -> node.name() == name)
                .findFirst()
    }

    private Map<String, String> getMapWithAttributes(List<Node> children, String attributeAsKey, String attributeAsValue) {

        return children
                .stream()
                .filter((node) -> {
                    if (node.attribute(attributeAsKey) != null && node.attribute(attributeAsKey) instanceof String && node.attribute(attributeAsValue) != null && node.attribute(attributeAsValue) instanceof String) {
                        return true
                    }
                    return false
                })
                .collect(Collectors.toMap(node -> node.attribute(attributeAsKey),
                        node -> node.attribute(attributeAsValue)))
    }

    private FruitRecord enrichFruitRecord(Node fruitRecordNode, Map<String, String> fruitTypeMap, Map<String, String> fruitColorMap) {
        List<Node> parameters = fruitRecordNode.value() as List<Node>

        Map<String, String> paramsMap = parameters.stream().collect(
                Collectors.toMap(
                        node -> node.name(),
                        node -> node.value()[0]
                )
        )

        String mappedFruitColor = fruitColorMap.get(paramsMap.get("FruitColor"))
        String mappedFruitType = fruitTypeMap.get(paramsMap.get("FruitType"))

        return new FruitRecord()
                .setColor(mappedFruitColor)
                .setType(mappedFruitType)
                .setExpiryDate(paramsMap.get("FruitExpiryDate"))
                .build()

    }

}
