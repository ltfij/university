package com.hjwylde.uni.comp422.project01.question02_2;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.partition;
import static java.util.Arrays.asList;

import com.hjwylde.uni.comp422.project01.common.math.Feature;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

/**
 * TODO: Documentation.
 *
 * @author Henry J. Wylde
 */
public final class Main {

    private static final String FORMAT = "png";

    private static final String ROOT_PATH = "/project01/question02_2/";
    private static final String TRAINING_FACES_PATH = ROOT_PATH + "train/face/";
    private static final String TRAINING_NON_FACES_PATH = ROOT_PATH + "train/non-face/";
    private static final String TESTING_FACES_PATH = ROOT_PATH + "test/face/";
    private static final String TESTING_NON_FACES_PATH = ROOT_PATH + "test/non-face/";

    private static final String OUT_PATH = "out/project01/question02_2/";
    private static final String TRAINING_DATA_SET_PATH = OUT_PATH + "training.csv";
    private static final String TESTING_DATA_SET_PATH = OUT_PATH + "testing.csv";

    private static final Map<String, BufferedImage> TRAINING_FACES_IMAGES = new HashMap<>();
    private static final Map<String, BufferedImage> TRAINING_NON_FACES_IMAGES = new HashMap<>();
    private static final Map<String, BufferedImage> TESTING_FACES_IMAGES = new HashMap<>();
    private static final Map<String, BufferedImage> TESTING_NON_FACES_IMAGES = new HashMap<>();

    static {
        checkState(ImageIO.getImageReadersBySuffix(FORMAT).hasNext(), "format not supported '{}'",
                FORMAT);

        CodeSource source = Main.class.getProtectionDomain().getCodeSource();
        try {
            URL url = source.getLocation();

            if (url.getFile().endsWith(".jar")) {
                ZipInputStream zin = new ZipInputStream(url.openStream());

                for (ZipEntry entry; (entry = zin.getNextEntry()) != null; ) {
                    if (entry.getName().startsWith(TRAINING_FACES_PATH)) {
                        BufferedImage image = checkNotNull(ImageIO.read(zin),
                                "image cannot be " + "null");
                        TRAINING_FACES_IMAGES.put(entry.getName(), image);
                    } else if (entry.getName().startsWith(TRAINING_NON_FACES_PATH)) {
                        BufferedImage image = checkNotNull(ImageIO.read(zin),
                                "image cannot be " + "null");
                        TRAINING_NON_FACES_IMAGES.put(entry.getName(), image);
                    } else if (entry.getName().startsWith(TESTING_FACES_PATH)) {
                        BufferedImage image = checkNotNull(ImageIO.read(zin),
                                "image cannot be null");
                        TESTING_FACES_IMAGES.put(entry.getName(), image);
                    } else if (entry.getName().startsWith(TESTING_NON_FACES_PATH)) {
                        BufferedImage image = checkNotNull(ImageIO.read(zin),
                                "image cannot be null");
                        TESTING_NON_FACES_IMAGES.put(entry.getName(), image);
                    }
                }
            } else {
                Files.walkFileTree(Paths.get(url.getFile() + "/../../resources/main", TRAINING_FACES_PATH),
                        new SimpleFileVisitor<Path>() {
                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                                    throws IOException {
                                try (InputStream in = Files.newInputStream(file)) {
                                    BufferedImage image = checkNotNull(ImageIO.read(in),
                                            "image cannot be " + "null");
                                    TRAINING_FACES_IMAGES.put(file.getFileName().toString(), image);
                                }

                                return FileVisitResult.CONTINUE;
                            }
                        });
                Files.walkFileTree(Paths.get(url.getFile() + "/../../resources/main", TRAINING_NON_FACES_PATH),
                        new SimpleFileVisitor<Path>() {
                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                                    throws IOException {
                                try (InputStream in = Files.newInputStream(file)) {
                                    BufferedImage image = checkNotNull(ImageIO.read(in),
                                            "image cannot be " + "null");
                                    TRAINING_NON_FACES_IMAGES.put(file.getFileName().toString(),
                                            image);
                                }

                                return FileVisitResult.CONTINUE;
                            }
                        });
                Files.walkFileTree(Paths.get(url.getFile() + "/../../resources/main", TESTING_FACES_PATH),
                        new SimpleFileVisitor<Path>() {
                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                                    throws IOException {
                                try (InputStream in = Files.newInputStream(file)) {
                                    BufferedImage image = checkNotNull(ImageIO.read(in),
                                            "image cannot be " + "null");
                                    TESTING_FACES_IMAGES.put(file.getFileName().toString(), image);
                                }

                                return FileVisitResult.CONTINUE;
                            }
                        });
                Files.walkFileTree(Paths.get(url.getFile() + "/../../resources/main", TESTING_NON_FACES_PATH),
                        new SimpleFileVisitor<Path>() {
                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                                    throws IOException {
                                try (InputStream in = Files.newInputStream(file)) {
                                    BufferedImage image = checkNotNull(ImageIO.read(in),
                                            "image cannot be " + "null");
                                    TESTING_NON_FACES_IMAGES.put(file.getFileName().toString(),
                                            image);
                                }

                                return FileVisitResult.CONTINUE;
                            }
                        });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This class cannot be instantiated.
     */
    private Main() {}

    public static void main(String[] args) throws IOException {
        List<Datum> trainingData = getTrainingData();
        List<Datum> testingData = getTestingData();

        writeTrainingData(trainingData);
        writeTestingData(testingData);

        Iterable<List<Datum>> trainingDataFaces = partition(filter(trainingData,
                new Predicate<Datum>() {
                    @Override
                    public boolean apply(Datum input) {
                        return input.getClazz();
                    }
                }), 500);
        Iterable<List<Datum>> trainingDataNonFaces = partition(filter(trainingData,
                new Predicate<Datum>() {
                    @Override
                    public boolean apply(Datum input) {
                        return !input.getClazz();
                    }
                }), 500);
        Iterable<List<Datum>> testingDataFaces = partition(filter(testingData,
                new Predicate<Datum>() {
                    @Override
                    public boolean apply(Datum input) {
                        return input.getClazz();
                    }
                }), 500);
        Iterable<List<Datum>> testingDataNonFaces = partition(filter(testingData,
                new Predicate<Datum>() {
                    @Override
                    public boolean apply(Datum input) {
                        return !input.getClazz();
                    }
                }), 500);

        Classifier.Builder builder = Classifier.builder();
        builder.train(trainingDataFaces.iterator().next());
        builder.train(trainingDataNonFaces.iterator().next());

        Classifier classifier = builder.build();

        Iterable<Datum> testData = concat(testingDataFaces.iterator().next(), testingDataNonFaces.iterator().next());

        for (double d = 0; d <= 1; d += 0.05) {
            classifier.setEvidence(d);

            classify(classifier, testData);
        }
    }

    private static void classify(Classifier classifier, Iterable<Datum> data) {
        Map<Datum, Boolean> results = new HashMap<>();
        for (Datum datum : data) {
            results.put(datum, classifier.classify(datum));
        }

        int tp = 0;
        int tn = 0;
        int fp = 0;
        int fn = 0;

        for (Map.Entry<Datum, Boolean> entry : results.entrySet()) {
            boolean expected = entry.getKey().getClazz();
            boolean received = entry.getValue();

            if (expected) {
                if (received) {
                    tp++;
                } else {
                    fn++;
                }
            } else {
                if (received) {
                    fp++;
                } else {
                    tn++;
                }
            }
        }

        System.out.println( ((double) tp) / (tp + fn));
        System.out.println( ((double) fp) / (fp + tn));
    }

    private static ImmutableList<Datum> getTestingData() {
        ImmutableList.Builder<Datum> builder = ImmutableList.builder();

        for (Map.Entry<String, BufferedImage> entry : TESTING_FACES_IMAGES.entrySet()) {
            String name = entry.getKey();
            BufferedImage image = entry.getValue();

            Datum datum = new Datum(name, true, Feature.extract(image, Feature.FEATURES));

            builder.add(datum);
        }

        for (Map.Entry<String, BufferedImage> entry : TESTING_NON_FACES_IMAGES.entrySet()) {
            String name = entry.getKey();
            BufferedImage image = entry.getValue();

            Datum datum = new Datum(name, false, Feature.extract(image, Feature.FEATURES));

            builder.add(datum);
        }

        return builder.build();
    }

    private static ImmutableList<Datum> getTrainingData() {
        ImmutableList.Builder<Datum> builder = ImmutableList.builder();

        for (Map.Entry<String, BufferedImage> entry : TRAINING_FACES_IMAGES.entrySet()) {
            String name = entry.getKey();
            BufferedImage image = entry.getValue();

            Datum datum = new Datum(name, true, Feature.extract(image, Feature.FEATURES));

            builder.add(datum);
        }

        for (Map.Entry<String, BufferedImage> entry : TRAINING_NON_FACES_IMAGES.entrySet()) {
            String name = entry.getKey();
            BufferedImage image = entry.getValue();

            Datum datum = new Datum(name, false, Feature.extract(image, Feature.FEATURES));

            builder.add(datum);
        }

        return builder.build();
    }

    private static void writeTestingData(List<? extends Datum> data) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("name,");
        for (int i = 0; i < Feature.FEATURES.size(); i++) {
            sb.append("f").append(i).append(",");
        }

        sb.append("class\n");

        for (Datum datum : data) {
            sb.append(datum.getName()).append(",");
            for (int value : datum.getFeatures()) {
                sb.append(value).append(",");
            }
            sb.append(datum.getClazz()).append("\n");
        }

        Files.createDirectories(Paths.get(TESTING_DATA_SET_PATH).getParent());

        Files.write(Paths.get(TESTING_DATA_SET_PATH), asList(sb.toString().split("\n")),
                StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static void writeTrainingData(List<? extends Datum> data) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("name,");
        for (int i = 0; i < Feature.FEATURES.size(); i++) {
            sb.append("f").append(i).append(",");
        }

        sb.append("class\n");

        for (Datum datum : data) {
            sb.append(datum.getName()).append(",");
            for (int value : datum.getFeatures()) {
                sb.append(value).append(",");
            }
            sb.append(datum.getClazz()).append("\n");
        }

        Files.createDirectories(Paths.get(TRAINING_DATA_SET_PATH).getParent());

        Files.write(Paths.get(TRAINING_DATA_SET_PATH), asList(sb.toString().split("\n")),
                StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}
