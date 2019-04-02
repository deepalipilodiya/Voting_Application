import org.apache.log4j.Logger;
import utils.UtilFunc;
import java.util.*;

import static utils.UtilFunc.*;

public class VoteCount {

    //final static Logger logger = Logger.getLogger(VoteCount.class);




    public static void main(String[] args) {


        String fileName = args[0];
        Integer maxPref = Integer.valueOf(args[1]);
        Integer noOfVoters = Integer.valueOf(args[2]);


        ArrayList<String> lines = readFile(fileName);
        if(lines.size() > 0 && maxPref <= lines.size()){
        HashMap<Integer,String> fmap = new HashMap<>();
        fmap = fileToMap(lines,fmap);
        System.out.println(fmap.keySet());
        System.out.println(fmap.values());
        ArrayList<Queue> noOfBallots= new ArrayList<>();

        noOfBallots = readBallots(noOfVoters,lines,maxPref,noOfBallots);
        intialBallots(noOfBallots);
        processBallots(noOfBallots,fmap);
        }
        else{
            logger.info("Dont Use Empty File or number of Maximum prefrence should be less than " +
                    "number of destination Options in File : Please try again changing these");
        }






    }
}
