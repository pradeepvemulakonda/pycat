package com.vemulakonda.doccat;

import com.google.common.io.Resources;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.IOException;
import java.io.InputStream;

public class TokenizerModelExample {

    public static void main(String[] args) {
        InputStream modelIn = null;

        try {
            modelIn =Resources.getResource("models/en-token.bin").openStream();
            TokenizerModel model = new TokenizerModel(modelIn);
            TokenizerME tokenizer = new TokenizerME(model);
            String tokens[] = tokenizer.tokenize("Westpac Banking Corporation is a very big bank");
            double tokenProbs[] = tokenizer.getTokenProbabilities();

            System.out.println("Token\t: Probability\n-------------------------------");
            for(int i=0;i<tokens.length;i++){
                System.out.println(tokens[i]+"\t: "+tokenProbs[i]);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                }
                catch (IOException e) {
                }
            }
        }
    }
}