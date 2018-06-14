import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

public class FaceDetector {

	private Image image;
	private MatOfRect faces;

	public FaceDetector(Image image) {
		this.image = image;
	}

	public Image detectFaces() {
		Mat frame = prepareImage();

		Rect[] facesArray = locateFaces(frame).toArray();

		for (int i = 0; i < facesArray.length; i++) {
			Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
		}

		MatOfByte byteMat = new MatOfByte();
		Imgcodecs.imencode(".jpg", frame, byteMat);
		return new Image(new ByteArrayInputStream(byteMat.toArray()));
	}

	private MatOfRect locateFaces(Mat frame) {
		int boxSize = 0;

		int height = frame.rows();
		if (Math.round(height * 0.2f) > 0) {
			boxSize = Math.round(height);
		}

		CascadeClassifier faceCascade = new CascadeClassifier();

		faceCascade.detectMultiScale(frame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(boxSize, boxSize), new Size());

		return faces;
	}

	private Mat prepareImage() {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();

		// Byte array of pixels: (times 4 for R, G, B, and A)
		byte[] buffer = new byte[width * height * 4];

		PixelReader reader = image.getPixelReader();

		// Establishes Pixel Format: (RGBA)
		WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();

		// Fills buffer with pixel data from image
		reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);

		// Creates OpenCV Matrix
		Mat frame = new Mat(height, width, CvType.CV_8UC4);

		// Loads matrix with pixel data
		frame.put(0, 0, buffer);

		// Matrix to load grayscale image
		Mat grayFrame = new Mat();

		// Convert Image Frame to Grayscale
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

		// Equalize the Histogram to optimize performance
		Imgproc.equalizeHist(grayFrame, grayFrame);

		return grayFrame;
	}

}

/*
 * Algorithm:
 * 
 * Prepare image by converting it to grayscale and equalizing it
 * 
 * Equalizing: Whites get whiter, Blacks get blacker, features become
 * distinctive
 * 
 * First, Analyze for Haar-Like Feature
 * 
 * Haar-Like Features: Uses certain patterns (edges, lines, and "checkers")
 * 
 * Use Classification for Faces to detect possible faces
 * 
 * 
 * 
 * 
 */
