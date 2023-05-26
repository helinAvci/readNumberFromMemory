import java.io.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class _150120049_150120075_150120030_150120037 {
    public static ArrayList<String> decimalNumbs = new ArrayList<>();

    public static int[] binaryArr;
    public static double Mantissa;
    public static String floatType;
    public static int E;

    public static void main(String[] args) throws IOException {

        ArrayList<String> numbers = new ArrayList<>();

        Scanner input = new Scanner (System.in);
        System.out.print("inputFileName: ");
        String fileName = input.nextLine();
        System.out.print("byteOrdering: ");
        String order = input.nextLine();  //l or b
        System.out.print("dataType: "); // float, int, unsigned
        String type = input.nextLine();
        System.out.print("size: ");
        int size = input.nextInt();

        if(!fileName.contains(".txt"))
            fileName = fileName +".txt";

        File file = new File(fileName);
        Scanner input1 = new Scanner(file);


        while(input1.hasNext()) {
            String line = input1.nextLine();
            line = line.replaceAll("\\s", "");

            for(int i = 0;i < line.length();) {
                numbers.add(line.substring(i, i + 2*size));
                i += 2*size;
            }
        }

        if(order.equals("l")) {
            for(int i = 0;i <numbers.size();i++) {
                String str = "";
                for(int j = numbers.get(i).length();j >0; j -= 2) {
                    str += numbers.get(i).substring(j-2, j);
                }
                numbers.set(i, str);
            }
        }

        for(int i = 0;i< numbers.size();i++) {
            String binary = hexToBinary(numbers.get(i));
            numbers.set(i, binary);
        }

        String result;

        switch (type){
            case "unsigned":
                for(int i = 0;i < numbers.size();i++) {
                    result = binaryToUnsingnedInteger(numbers.get(i));
                    decimalNumbs.add(result);
                }
                break;
            case "int":
                for(int i = 0;i < numbers.size();i++) {
                    binaryArr = new int[size * 8];
                    takeArray(numbers.get(i));
                    result = signedConverter(binaryArr);
                    decimalNumbs.add(result);
                }
                break;
            case "float":
                for(int i = 0;i < numbers.size();i++) {
                    binaryArr = new int[size * 8];
                    takeArray(numbers.get(i));
                    result = floatConverter(size);
                    decimalNumbs.add(result);
                }
                break;
            default:
                System.out.println("Not acceptable data type. Please write only unsigned, int or float.");
        }

        printResult(size);
    }

    public static String hexToBinary(String hex ) {
        String binary ="";
        for(int i = 0;i < hex.length();i++) {
            switch(hex.charAt(i)){
                case '0' : binary += "0000";break;
                case '1' :binary += "0001";break;
                case '2' :binary += "0010";break;
                case '3' :binary += "0011";break;
                case '4' :binary += "0100";break;
                case '5' :binary += "0101";break;
                case '6' :binary += "0110";break;
                case '7' :binary += "0111";break;
                case '8' :binary += "1000";break;
                case '9' :binary += "1001";break;
                case 'a' :binary += "1010";break;
                case 'b' :binary += "1011";break;
                case 'c' :binary += "1100";break;
                case 'd' :binary += "1101";break;
                case 'e' :binary += "1110";break;
                case 'f' :binary += "1111";break;
            }

        }
        return binary;
    }

    public static String binaryToUnsingnedInteger(String binary) {
        long UnsignedInteger = 0;
        for(int i = 0;i < binary.length();i++) {
            if(binary.charAt(binary.length() - 1 - i) == '0'){
                break;
            } else {
                UnsignedInteger += Math.pow(2, i);
            }
        }
        return String.valueOf(UnsignedInteger);
    }

    public static String signedConverter(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1) {
                if (i == 0 && arr[0] == 1) {
                    sum-= Math.pow(2, (arr.length-i-1));
                }
                else {
                    sum += Math.pow(2, (arr.length-i-1));
                }
            }
        }
        return String.valueOf(sum);
    }


    public static void takeArray(String binary){
        for(int i = 0; i < binary.length(); i++){
            binaryArr[i] = Character.getNumericValue(binary.charAt(i));
        }
    }

    public static String floatConverter( int byteNumb){
        E = 0;
        int bias;
        Mantissa = 0;
        String sign = "";
        if(binaryArr[0] == 1)
            sign = "-";

        switch (byteNumb){
            case(1):
                bias = (int)(Math.pow(2,3) - 1);
                E = evaluateE(bias, 1,4);
                if(floatType.equals("Normalized") || floatType.equals("Denormalized")){
                    Mantissa = fraction( 5,7) ;
                }
                break;

            case(2):
                bias = (int)(Math.pow(2,5) - 1);
                E = evaluateE( bias, 1,6);
                if(floatType.equals("Normalized") || floatType.equals("Denormalized")){
                    Mantissa = fraction( 7,15) ;
                }
                break;
            case(3):
                bias = (int)(Math.pow(2,7) - 1);
                E =  evaluateE( bias, 1,8);
                roundtoEven( 9, 23);
                if(floatType.equals("Normalized") || floatType.equals("Denormalized")){
                    Mantissa = fraction( 9,23) ;
                }
                break;
            case(4):
                bias = (int)(Math.pow(2,9) - 1);
                E =  evaluateE(bias, 1,10);
                roundtoEven( 11, 31);
                if(floatType.equals("Normalized") || floatType.equals("Denormalized")){
                    Mantissa = fraction( 11,31) ;
                }
                break;
        }
        if(floatType.equals("NaN"))
            return "NaN";
        else if(floatType.equals("Infinity"))
            return sign + "inf";
        else if(Mantissa == 0){
            return sign + "0";
        }
        else{
            double decimalValue =   Math.pow(2,E) * Mantissa;
            return sign + precision(decimalValue);
        }
    }
    public static String precision(double value){
        String numb = String.valueOf(value);
        if(numb.contains("E"))
            numb = String.format("%.5e",value);
        else
            numb= String.format("%.5f",value);
        return numb;
    }

    public static void printResult(int size) throws IOException {
        FileWriter file = new FileWriter("output.txt");
        int numbInLine = 12/size;

        for(int i = 0;i < decimalNumbs.size();i++) {
            if(i % numbInLine == numbInLine -1)
                file.write(decimalNumbs.get(i) + "\n");
            else
                file.write(decimalNumbs.get(i) + " ");

        }
        file.close();
    }

    public static boolean isSpecial( int startIndex, int endIndex) {
        for(int i= startIndex ; i <= endIndex; i++){
            if(!(binaryArr[i] == 1))
                return false;
        }
        return true;
    }

    public static boolean isInfinity( int endIndex) {
        for(int i= endIndex +1 ; i< endIndex+13; i++){
            if(!(binaryArr[i] == 0))
                return false;
        }
        return true;
    }
    public static Boolean isDenormalized( int startIndex, int endIndex){
        for(int i= startIndex; i<= endIndex; i++){
            if(binaryArr[i] == 1)
                return false;
        }
        return true;
    }
    public static String isType( int startIndex, int endIndex){
        if(isSpecial( startIndex, endIndex)){
            if(isInfinity( endIndex))
                return "Infinity";
            else
                return "NaN";
        }
        else if(isDenormalized( startIndex, endIndex))
            return "Denormalized";
        else
            return "Normalized";

    }
    public static int exponent(int start, int end){
        int e = 0;
        int exp = end-start;
        for(int i= start; i<= end; i++){
            e += binaryArr[i] * Math.pow(2,exp);
            exp--;
        }
        return e;
    }
    public static int evaluateE(int bias, int startIndex, int endIndex){
        int E = 0;
        floatType = isType(startIndex, endIndex);
        switch (floatType){
            case "Denormalized":
                E = 1 -bias;
                Mantissa = 0;
                break;
            case "Normalized":
                E = exponent ( startIndex, endIndex) - bias;
                Mantissa = 1;
                break;
        }
        return E;
    }
    public static void add1(int index){
        int carry = 1;
        for(int i= index; i > index-13 && carry != 0; i--){
            carry = (binaryArr[i] + 1) / 2;
            binaryArr[i] = (binaryArr[i] + 1) % 2;
        }
        if(carry == 1)
            E++;
    }
    public static void assignToMinus1( int index){
        for(int i= index; i< binaryArr.length; i++){
            binaryArr[i] = -1;
        }
    }
    public static void roundtoEven( int start, int end){
        boolean roundUp = false;
        int index = start + 13;
        if(binaryArr[index] == 1){ //half or up
            for(int i= index + 1; i<= end; i++){
                if(binaryArr[i] == 1){ //up
                    add1(index - 1);
                    assignToMinus1( index);
                    roundUp = true;
                    break;
                }
            }
            if(!roundUp && binaryArr[start + 12 ] == 1){ //half
                add1( index-1);
                assignToMinus1(index);
            }
        }
        else //down
            assignToMinus1( index);
    }
    public static double fraction(int startIndex, int endIndex){
        int j = 1;
        for(int i= startIndex; i <= endIndex && binaryArr[i] != -1; i++, j++){
            Mantissa = Mantissa + binaryArr[i]* Math.pow(2,-1*(j));
        }
        return Mantissa;
    }
}