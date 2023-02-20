package com.azyf.finalyearproject;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class javaFXTest extends Application {
    private static final int CANVAS_WIDTH = 600;
    private static final int CANVAS_HEIGHT = 400;
    private Canvas canvas;


    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 500;



    public static void main (String[] args)  {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println(Core.VERSION);

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane root = buildGUI();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();

    }
    public BorderPane buildGUI() throws IOException {
        BorderPane root = new BorderPane();
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        root.setCenter(canvas);
        Image sprite = new Image("Assets\\Images\\Sprites\\default-sprite.png");
/*
        Mat src = Imgcodecs.imread(sprite.getUrl());
        Mat newSprite = new Mat();
        Core.flip(src, newSprite, 1);
        Imgcodecs.imwrite(sprite.getUrl(), newSprite);
        sprite = new Image(sprite.getUrl());


 */
        // Mat src = Imgcodecs.imread(sprite.getUrl());
        //Mat newSprite = new Mat();
        //Imgcodecs.imwrite(sprite.getUrl(), newSprite);
        // sprite = new Image(sprite.getUrl());
/*
        Mat src = Imgcodecs.imread(sprite.getUrl());
        Mat dst = new Mat(src.rows(), src.cols(), src.type());
        Point point = new Point(300, 200);
        Mat rotationMatrix = Imgproc.getRotationMatrix2D(point, 30, 1);
        Size size = new Size(src.cols(), src.cols());
        Imgproc.warpAffine(src, dst, rotationMatrix, size);
        Imgcodecs.imwrite(sprite.getUrl(), dst);
        sprite = new Image(sprite.getUrl());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(sprite, 50,50);
        
 */
        /*

         */


        /*
        String file = "Assets\\Images\\Sprites\\default.png";
        Mat src = Imgcodecs.imread(file);
        Mat dest = new Mat(src.rows() + 100 , src.cols() +100, src.type());
        Point point = new Point(src.cols() / 2.0, src.rows() / 2.0);
        Mat rotationMatrix = Imgproc.getRotationMatrix2D(point, 98, 0.50);
        Size size = new Size(src.cols() + 100 , src.cols() + 100);
        Imgproc.warpAffine(src, dest, rotationMatrix, size);

        BufferedImage img = (BufferedImage) HighGui.toBufferedImage(dest);
        File saveImage = new File("Cache\\before.png");
        ImageIO.write(img, "png", saveImage);

        Mat dst = new Mat(dest.rows(), dest.cols(), dest.type());

        Point p1 = new Point( 0,0 );
        Point p2 = new Point( dst.cols() - 1, 0 );
        Point p3 = new Point( 0, dst.rows() - 1 );
        Point p4 = new Point( dest.cols()*0.0, dest.rows()*0.33 );
        Point p5 = new Point( dest.cols()*0.85, dest.rows()*0.25 );
        Point p6 = new Point( dest.cols()*0.15, dest.rows()*0.7 );
        MatOfPoint2f ma1 = new MatOfPoint2f(p1,p2,p3);
        MatOfPoint2f ma2 = new MatOfPoint2f(p4,p5,p6);
        Mat tranformMatrix = Imgproc.getAffineTransform(ma1,ma2);
        size = new Size(dst.cols(), dst.cols());
        Imgproc.warpAffine(dest, dst, tranformMatrix, size);
        img = (BufferedImage) HighGui.toBufferedImage(dst);
        saveImage = new File("Cache\\test.png");
        ImageIO.write(img, "png", saveImage);
/*
        src =  Imgcodecs.imread(saveImage.getPath());
        dst = new Mat();
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY);
        Imgcodecs.imwrite("Cache\\gray.png", dst);
        src =  Imgcodecs.imread("Cache\\gray.png");
        dst = new Mat();
        Imgproc.adaptiveThreshold(src, dst, 120, Imgproc.ADAPTIVE_THRESH_MEAN_C,  Imgproc.THRESH_BINARY, 11, 12);
        Imgcodecs.imwrite("Cache\\threshold.png", dst);
        return root;

 */
        /*
        String file = "Assets\\Images\\Sprites\\default.png";
        Mat src = Imgcodecs.imread(file);
        Mat dst = new Mat(src.rows(), src.cols(), src.type());
        Core.rotate(src,  dst,Core.ROTATE_90_CLOCKWISE);
        Mat dest = new Mat(src.rows(), src.cols(), src.type());
        Core.flip(dst, dest, 1);
        Imgcodecs.imwrite("Cache\\3.png", dest);
         */
return root;
    }
}
