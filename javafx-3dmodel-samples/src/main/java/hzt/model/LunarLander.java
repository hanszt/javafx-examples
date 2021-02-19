package hzt.model;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.scene.Node;

import java.net.URL;

public class LunarLander extends Model3D {

    public LunarLander() {
//        loadModel(getClass().getResource("lunarSample/lunarlandernofoil_carbajal.3ds"));
    }

    private void loadModel(URL url) {
        TdsModelImporter importer = new TdsModelImporter();
        importer.read(url);

        for (Node node : importer.getImport()) {
            getChildren().add(node);
        }
    }

}
