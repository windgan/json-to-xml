package org.aigu;

import javax.json.*;
import java.io.StringReader;

public class Main {

    public static void main(String[] args) throws Exception {
        String inputJsonString =
                "{\"menu\": {\n" +
                        "  \"id\": null,\n" +
                        "  \"value\": \"File\",\n" +
                        "  \"popup\": {\n" +
                        "    \"menuitem\": [\n" +
                        "      {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},\n" +
                        "      {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},\n" +
                        "      {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}\n" +
                        "    ]\n" +
                        "  }\n" +
                        "}}";


        JsonReader jsonReader = Json.createReader(new StringReader(inputJsonString));
        JsonStructure jsonStructure = jsonReader.read();
        jsonReader.close();

        JsonToXml parser = new JsonToXml();
        parser.setInput(jsonStructure);
        parser.run();
        parser.printOutput(parser.getOutput());
    }
}
