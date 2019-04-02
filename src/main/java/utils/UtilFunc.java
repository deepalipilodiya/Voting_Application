package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.apache.log4j.Logger;


public  class UtilFunc {


    public final static Logger logger = Logger.getLogger(UtilFunc.class);

    ///////Read Venue List From File///////////////////
    public static ArrayList<String> readFile(String fileName){


        ArrayList<String> listOfLines = new ArrayList<>();
        try {
            BufferedReader bufReader = new BufferedReader(new FileReader(fileName));

            String line = bufReader.readLine();
            while (line != null) {
                listOfLines.add(line);
                line = bufReader.readLine();
            }
            bufReader.close();
        }

        catch (FileNotFoundException ex){

            logger.error(
                    "Unable to open file '" +
                            fileName + "'");
            System.exit(1);

        }
        catch (IOException ex){
            logger.error(
                    "Error reading file '"
                            + fileName + "'");
            System.exit(1);

        }
        return listOfLines;
    }


    ///////Utility Function to CHeck to Check If String is Numeric
    public static boolean isNumeric(String strNum) {

        return strNum.matches("-?\\d+(\\.\\d+)?");
    }

    ///////Convert or Type Cast String to Int
    public static int convertToInt(String val){
        int retVal=0;
        if(isNumeric(val)){
            retVal = Integer.parseInt(val);
        }
        return retVal;
    }


    ///////Read Input from User and Add to Ballots
    public static ArrayList<Queue> readBallots(int noOfVoters,ArrayList<String> lines ,int maxPref,ArrayList<Queue> noOfBallots){

        for (int i = 0; i < noOfVoters; i++) {

            logger.info("Voter " + (i + 1) + " : Please Enter the Preference in the Order of your Favourites : ");
            int j = 1;
            for (String s : lines) {
                int flag = 0;
                System.out.println(s + " : " + j);
                j++;
            }

            Scanner sc = new Scanner(System.in);
            sc.useDelimiter("\\n");
            int k = 0;

            String pref;
            int pref_num;
            HashSet<Integer> noDupSet = new HashSet<>();
            Queue<Integer> q = new LinkedList<>();
            for (int input = 0; input < maxPref; input++) {
                logger.info("Please Enter Your Preference number  " + (input + 1) + " -> ");

                pref = sc.nextLine();
                while (!isNumeric(pref) || !(convertToInt(pref) >= 0 && convertToInt(pref) <= lines.size())) {

                    logger.info("Not Valid - Enter Again " + pref);
                    pref = sc.nextLine();
                }
                pref_num = convertToInt(pref);

                if (pref_num >= 1 && pref_num <= lines.size() && !noDupSet.contains(pref_num)) {
                    q.add(pref_num);
                    k = 1;
                    noDupSet.add(pref_num);
                }


                if (pref_num == 0) {
                    break;
                }


            }

            if (k == 1) {
                noOfBallots.add(q);

            }


        }

        return noOfBallots;
    }


    /////////Display All The Initial Ballots

    public static void intialBallots(ArrayList<Queue> noOfBallots){
        for (int size1 = 0; size1 < noOfBallots.size(); size1++) {

            Queue<Integer> queueData1 = noOfBallots.get(size1);
            for (Integer queueElemet : queueData1) {
                System.out.println("final print " + queueElemet);
            }
            System.out.println("----------------------------------------" );
        }
    }

    ////////Process All the Ballots from each Voters

    public static void processBallots(ArrayList<Queue> noOfBallots,HashMap<Integer,String> fmap){
        boolean inComplete=true;
        int quota=0;
        int loopCnt=0;
        HashMap<Integer,Integer> destCnt = new HashMap<Integer,Integer>();
        HashSet<Integer> rejectDest = new HashSet <>();

        while(inComplete) {
            loopCnt++;
            quota = (noOfBallots.size() / 2) + 1;
            logger.info("Quota is " + quota);
            destCnt = updateBallots(noOfBallots,rejectDest,destCnt,loopCnt);
            System.out.println("dest " + destCnt);

            int maxVal = Collections.max(destCnt.values());

            if(maxVal >= quota){
                inComplete=processMaxBallot(destCnt,inComplete,maxVal,fmap);
                if(!inComplete)
                    break;
            }
            else{
                destCnt=ambigousPref(destCnt,noOfBallots,rejectDest);

                maxVal = Collections.max(destCnt.values());
                if(maxVal >= quota){
                inComplete=processMaxBallot(destCnt,inComplete,maxVal,fmap);
                if(!inComplete)
                    break;
                }

                if(inComplete){
                    rejectDest=processMinBallot(destCnt,rejectDest,fmap);
                    System.out.println("reject " + rejectDest);
                }
            }
        }
    }


    ////////Utility Function to Process Ballots if greater than Quota
    public static boolean processMaxBallot(HashMap<Integer,Integer> destCnt,boolean inComplete,int maxVal,
                                            HashMap<Integer,String> fmap){

            ArrayList<Integer> finalDest = new ArrayList<>();
            for (Integer mkey : destCnt.keySet()) {
                if (destCnt.get(mkey).equals(maxVal)) {
                    finalDest.add(mkey);
                }
            }

            if (finalDest.size() > 1){
                int rnd = new Random().nextInt(finalDest.size());
                logger.info("Final Destination is : " + finalDest.get(rnd) + " : " + fmap.get(finalDest.get(rnd)));
                inComplete=false;
            }
            else {
                logger.info("Final Destination is : " + finalDest.get(0) + " : " + fmap.get(finalDest.get(0)));
                inComplete=false;
            }
        return inComplete;
    }

    ////////Utility Function to Process Ballots if less than Quota
    public static HashSet<Integer> processMinBallot(HashMap<Integer,Integer> destCnt,HashSet<Integer> rejectDest,
                                                     HashMap<Integer,String> fmap){

        int minVal = Collections.min(destCnt.values());
        if(destCnt.size()>1) {

            ArrayList<Integer> rejDest = new ArrayList<>();
            for (Integer mkey : destCnt.keySet()) {
                if (destCnt.get(mkey).equals(minVal)) {
                    rejDest.add(mkey);
                }
            }

            for (Integer mkey : rejDest) {
                if (destCnt.get(mkey).equals(minVal)) {
                    destCnt.remove(mkey);
                    rejectDest.add(mkey);
                    logger.info("Option Elimitated : " + fmap.get(mkey));
                }
            }
        }
        return rejectDest;
    }


    ////////Update Ballot as per the Rules Defined in the Problem(Remove eliminated Venues from Ballots)
    public static HashMap<Integer,Integer> updateBallots(ArrayList<Queue> noOfBallots,HashSet<Integer> rejectDest,HashMap<Integer,Integer> destCnt,int loopCnt){

        for (int size = 0; size < noOfBallots.size(); size++) {
            int currentElem=0;
            Queue<Integer> queueData = noOfBallots.get(size);
            if(queueData.size() > 0){
                currentElem = queueData.peek();
            }

            if (rejectDest.contains(currentElem)) {
                queueData.poll();
                if(queueData.size() > 0) {
                    currentElem = queueData.peek();
                }

                while (rejectDest.contains(currentElem) && queueData.size() > 0){
                    queueData.poll();
                   if(queueData.size() > 0)
                    currentElem =queueData.peek();
                }

                if(queueData.size() == 0){
                    noOfBallots.remove(size);
                }
                else{
                    if(currentElem !=0) {
                        destCnt=updateValidDest(currentElem,destCnt);
                    }
                }
            } else {
                if(currentElem !=0) {
                    if (destCnt.containsKey(currentElem) ) {
                        if(loopCnt==1){
                            destCnt.put(currentElem, (destCnt.get(currentElem) + 1));}
                    } else {
                        destCnt.put(currentElem, 1);
                    }
                }
            }
        }
        return destCnt;
    }

    ////////Remove ambiguity if all the venues have same preferences
    public static HashMap<Integer,Integer> ambigousPref(HashMap<Integer,Integer> destCnt,
                                                  ArrayList<Queue> noOfBallots,
                                                    HashSet<Integer> rejectDest){

        Set<Integer> values = new HashSet<Integer>(destCnt.values());
        while(values.size() ==1 && destCnt.size() > 1) {

            int ballotSize = noOfBallots.size();
            for (int size = 0; size < noOfBallots.size(); size++) {
                int currentElem = 0;
                Queue<Integer> queueData = noOfBallots.get(size);
                queueData.poll();

                if(queueData.size() > 0) {
                    currentElem = queueData.peek();
                }

                while (rejectDest.contains(currentElem) && queueData.size() > 0){
                    queueData.poll();
                    if(queueData.size() > 0)
                    currentElem =queueData.peek();
                }

                if(queueData.size() == 0){
                    noOfBallots.remove(size);
                    if(noOfBallots.size()==0){
                        logger.info("No Unique preference was Identified. Perform Re-Poll");
                        System.exit(1);
                    }
                }
                else{
                    if(currentElem !=0) {
                        destCnt=updateValidDest(currentElem,destCnt);
                    }
                }
            }
            values = new HashSet<Integer>(destCnt.values());
        }
        return destCnt;
    }

    ////////Update validated Venues
    public static HashMap<Integer,Integer> updateValidDest(int currentElem,HashMap<Integer,Integer> destCnt){
        if (destCnt.containsKey(currentElem)) {
            destCnt.put(currentElem, (destCnt.get(currentElem) + 1));
        } else {
            destCnt.put(currentElem, 1);
        }

        return destCnt;

    }

    public static HashMap<Integer,String> fileToMap(ArrayList<String> lines,HashMap<Integer,String> fmap){

        for(int i=0;i<lines.size();i++){
            fmap.put((i+1),lines.get(i));
        }
        return fmap;
    };

}
