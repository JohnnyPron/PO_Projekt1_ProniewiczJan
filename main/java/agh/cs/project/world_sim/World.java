package agh.cs.project.world_sim;
import java.io.FileNotFoundException;

import static java.lang.System.out;

/**
 * @author Proniewicz Jan, 297989, Informatyka - Data Science
 *
 * Tworząc ten projekt, wzorowałem się na swoich rozwiązaniach zadań z poprzednich laboratorów oraz na kodzie
 * z przykładowego projektu (w kontekście json-a, gui i wizualizacji)
 */
public class World {
    public static void main(String[] args) {
        try {
            JsonParser parameters = JsonParser.readJsonFile();
            Integer[] defaultParams = parameters.parseToArray();
            SettingsMenu menu = new SettingsMenu(defaultParams);
        } catch (FileNotFoundException fex){
            out.println(fex.getMessage());
        }
    }
}
