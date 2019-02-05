package ru.hse.spb.gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiUtils {

    public static final void saveChartAsImage(String imageName, LineChart chart) {
        WritableImage snapShot = chart.snapshot(null, null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapShot, null), "png", new File(imageName + ".png"));
        } catch (IOException ignored) {
        }


    }

    public static final void saveSceneAsImage(String imageName, Scene scene) {
        WritableImage snapShot = scene.snapshot(null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapShot, null), "png", new File(imageName + ".png"));
        } catch (IOException ignored) {
        }


    }

}
