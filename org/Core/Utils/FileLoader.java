package org.Core.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


public class FileLoader {
    Map<String, String> Item = new HashMap<>(10);
    public List<MapClass> Objects = new ArrayList<>();
    Gson gson = new Gson();
    JsonObject jsonObject;
    MapClass Map;
    JsonArray js;

    public void TSXLoader(File file) {
        try {

            DocumentBuilderFactory fcty = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = fcty.newDocumentBuilder();
            Document docs = builder.parse(file);

            docs.getDocumentElement().normalize();
            NodeList nodeList = docs.getElementsByTagName("tileset");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NamedNodeMap attr = node.getAttributes();
                for (int j = 0; j < attr.getLength(); j++) {
                    String name = attr.item(j).getNodeName();
                    String value = attr.item(j).getNodeValue();
                    Item.put(name, value);
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public String GetTSXElement(String Obj) {
        return Item.get(Obj);
    }

    public void FileReader(File file) {
        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseReader(new FileReader(file));
            JsonElement Layout = jsonElement.getAsJsonObject();
             Map = gson.fromJson(Layout.toString(), MapClass.class);

            
            Objects.add(Map);
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public List<MapClass> getObjects() {
        return Objects;
    }
    public MapClass getMap() {
        return Map;
    }
}

