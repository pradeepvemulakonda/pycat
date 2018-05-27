package com.vemulakonda.doccat;

import com.google.common.io.Resources;
import opennlp.tools.doccat.*;
import opennlp.tools.ml.AbstractTrainer;
import opennlp.tools.ml.naivebayes.NaiveBayesTrainer;
import opennlp.tools.util.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClassifierTest {

    public static void main(String[] args) throws IOException {
        ObjectStream lineStream = null;
        ObjectStream sampleStream = null;

        try {
            InputStreamFactory dataIn = new MarkableFileInputStreamFactory(new File(Resources.getResource("train/en-movie-category.train").getFile()));
            lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
            sampleStream = new DocumentSampleStream(lineStream);
            TrainingParameters params = new TrainingParameters();
            params.put(TrainingParameters.ITERATIONS_PARAM, 10 + "");
            params.put(TrainingParameters.CUTOFF_PARAM, 0 + "");
            params.put(AbstractTrainer.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);

            FeatureGenerator[] featureGenerators = {new NGramFeatureGenerator(1, 1),
                    new NGramFeatureGenerator(2, 5)};
            DoccatFactory factory = new DoccatFactory(featureGenerators);

            DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, factory);
            // save the model to local
            BufferedOutputStream modelOut = new BufferedOutputStream(new FileOutputStream("model" + File.separator + "en-movie-classifier-maxent.bin"));
            model.serialize(modelOut);
            System.out.println("\nTrained Model is saved locally at : " + "model" + File.separator + "en-movie-classifier-maxent.bin");

            // test the model file by subjecting it to prediction
            DocumentCategorizer documentCategorizer = new DocumentCategorizerME(model);
            String[] docWords = "Afterwards Stuart and Charlie notice Kate in the photos Stuart took at Leopolds ball and realise that her destiny must be to go back and be with Leopold That night while Kate is accepting her promotion at a company banquet he and Charlie race to meet her and show her the pictures Kate initially rejects their overtures and goes on to give her acceptance speech but it is there that she sees Stuarts picture and realises that she truly wants to be with Leopold".replaceAll("[^A-Za-z]", " ").split(" ");
            double[] probabailities = documentCategorizer.categorize(docWords);

            // print the probabilities of the categories
            System.out.println("\n---------------------------------\nCategory : Probability\n---------------------------------");
            for (int i = 0; i < documentCategorizer.getNumberOfCategories(); i++) {
                System.out.println(documentCategorizer.getCategory(i) + " : " + probabailities[i]);
            }
            System.out.println("---------------------------------");

            System.out.println("\n" + documentCategorizer.getBestCategory(probabailities) + " : is the predicted category for the given sentence.");
        } finally {
            if(lineStream != null) {
                lineStream.close();
            }

            if(sampleStream != null) {
                sampleStream.close();
            }
        }
    }
}
