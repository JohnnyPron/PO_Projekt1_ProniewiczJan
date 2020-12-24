package agh.cs.project.world_sim;
import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonParser {
    private int width;
    private int height;
    private int jungleRatioReciprocal;
    private int animalNumber;
    private int grassNumber;
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;

    public static JsonParser readJsonFile() throws FileNotFoundException {
        Gson gson = new Gson();
        return gson.fromJson(new FileReader("src\\main\\java\\agh\\cs\\project\\world_sim\\parameters.json"), JsonParser.class);
    }

    public Integer[] parseToArray(){
        Integer[] paramArray = {width, height, jungleRatioReciprocal, animalNumber, grassNumber, startEnergy, moveEnergy, plantEnergy};
        return paramArray;
    }
}
