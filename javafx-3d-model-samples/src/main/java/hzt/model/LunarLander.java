package hzt.model;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;

import java.net.URL;

public class LunarLander extends Model3D {

    public LunarLander() {
        loadModel(getClass().getResource("lunarSample/lunarlandernofoil_carbajal.3ds"));
    }

    private void loadModel(URL url) {
        var importer = new TdsModelImporter();
        importer.read(url);
        super.getChildren().addAll(importer.getImport());
    }

}
