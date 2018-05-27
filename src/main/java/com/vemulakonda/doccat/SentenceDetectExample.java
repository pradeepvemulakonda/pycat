package com.vemulakonda.doccat;

import com.google.common.io.Resources;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;

public class SentenceDetectExample {

    public static void main(String[] args) {
        try {
            new SentenceDetectExample().sentenceDetect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to detect sentences in a paragraph/string
     * @throws InvalidFormatException
     * @throws IOException
     */
    public void sentenceDetect() throws InvalidFormatException,    IOException {
        String paragraph = "I woould like to get a loan. I want to find the status";

        // refer to model file "en-sent,bin", available at link http://opennlp.sourceforge.net/models-1.5
        InputStream is = Resources.getResource("models/en-sent.bin").openStream();
        SentenceModel model = new SentenceModel(is);

        // feed the model to SentenceDetectorME class
        SentenceDetectorME sdetector = new SentenceDetectorME(model);

        // detect sentences in the paragraph
        String sentences[] = sdetector.sentDetect(paragraph);

        // print the sentences detected, to console
        for(int i=0;i<sentences.length;i++){
            System.out.println(sentences[i]);
        }
        is.close();
    }
}