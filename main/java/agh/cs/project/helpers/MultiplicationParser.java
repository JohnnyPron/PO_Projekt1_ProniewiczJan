package agh.cs.project.helpers;

public class MultiplicationParser {
    public static int parseResultOddOrEven(int reference, float multiplier){
        float product = reference * multiplier;
        int productRoundFloor = (int) product;
        int finalProduct;
        if(reference % 2 == 1){
            if(productRoundFloor % 2 == 1){
                finalProduct = productRoundFloor;
            }
            else{
                finalProduct = Math.round(product / 2) * 2 + 1;
            }
        }
        else{
            finalProduct = Math.round(product / 2) * 2;
            if(finalProduct == 0) { finalProduct = 2; }
        }
        return finalProduct;
    }
}
