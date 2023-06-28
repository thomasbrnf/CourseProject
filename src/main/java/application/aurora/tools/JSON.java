package application.aurora.tools;

import application.aurora.world.Main;
import application.aurora.micro_objects.AstronautIntern;
import application.aurora.micro_objects.tools.AstronautConfig;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static application.aurora.micro_objects.tools.AstronautConfig.fromAstronaut;
import static application.aurora.micro_objects.tools.AstronautTools.deleteAllAstronauts;

public class JSON {
    private static final Stage stage = new Stage();
    private static void saveData(List<AstronautIntern> arrayToJSON, String path) throws IOException {
        FileWriter file = new FileWriter(path, false);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(file);

        jsonGenerator.writeStartArray();
        arrayToJSON.forEach(object -> {
            try {
                jsonGenerator.writeObject(fromAstronaut(object));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        jsonGenerator.writeEndArray();
        jsonGenerator.close();
    }
    private static void parseData(File file) throws IOException {
        if (file == null) return;
        deleteAllAstronauts();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(file);
        for (JsonNode item : jsonNode) {
            AstronautConfig object = objectMapper.readValue(item.traverse(), AstronautConfig.class);
            object.toAstronaut();
        }
    }

    public static void saveDataToFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.setInitialFileName("data.json");
        FileChooser.ExtensionFilter textFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(textFilter);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) JSON.saveData(Main.astronauts, file.getPath());
    }
    public static void openDataFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\"));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JSON files(*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(stage);
        JSON.parseData(file);
    }
}
