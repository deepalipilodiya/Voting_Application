import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.*;
import static utils.UtilFunc.*;

public class OracleChallengeTest {
    public static HashMap<Integer,String> fmap = new HashMap<Integer,String>();
    public static ArrayList<String> file = new ArrayList<>();


    @BeforeClass
    public static void setUpBeforeClass(){
        fmap.put(1,"Winery Tour");
        fmap.put(2,"Ten Pin Bowling");
        fmap.put(3,"Movie Night");
        fmap.put(4,"Museum Visit");
        fmap.put(5,"Bowling");

        Collections.addAll(file,"Winery Tour", "Ten Pin Bowling", "Movie Night", "Museum Visit", "Bowling");
    }



    @Test
    public void reafFileTest(){
        assertEquals(file,readFile("src/test/resources/DestList.txt"));
    }

    @Test
    public void isNumericTest(){
        assertEquals(true,isNumeric("2"));
        assertEquals(false,isNumeric("a"));
    }


    @Test
    public void convertToIntTest(){
        assertEquals(2,convertToInt("2"));
        assertEquals(0,convertToInt("a"));
    }


    @Test
    public void processMaxBallotTest(){

        HashMap<Integer,Integer> destCnt = new HashMap<>();
        destCnt.put(1,2);
        destCnt.put(2,2);
        assertEquals(false,processMaxBallot(destCnt,true,2,fmap));

    }


    @Test
    public void processMinBallotTest(){

        HashMap<Integer,Integer> destCnt = new HashMap<>();
        HashSet<Integer> hashSet = new HashSet<>();
        HashSet<Integer> hashSet1 = new HashSet<>();
        hashSet.add(3);
        destCnt.put(1,2);
        destCnt.put(2,2);
        destCnt.put(3,1);
        assertEquals(hashSet,processMinBallot(destCnt,hashSet1,fmap));

    }


    @Test
    public void updateBallotTest(){


        ArrayList<Queue> ballots = new ArrayList<>();
        Queue<Integer> q1 = new LinkedList<>() ;
        Queue<Integer> q2 = new LinkedList<>() ;
        q1.add(1);
        q1.add(2);
        q2.add(2);
        q2.add(2);
        ballots.add(q1);
        ballots.add(q2);


        HashMap<Integer,Integer> destCnt = new HashMap<>();
        HashMap<Integer,Integer> destCnt1 = new HashMap<>();
        destCnt1.put(1,1);
        destCnt1.put(2,1);
        HashSet<Integer> hashSet = new HashSet<>();

        assertEquals(destCnt1,updateBallots(ballots,hashSet,destCnt,1));

    }


    @Test
    public void ambigousPrefTest(){


        ArrayList<Queue> ballots = new ArrayList<>();
        Queue<Integer> q1 = new LinkedList<>() ;
        Queue<Integer> q2 = new LinkedList<>() ;
        Queue<Integer> q3 = new LinkedList<>() ;
        q1.add(1);
        q1.add(2);
        q2.add(3);
        q2.add(4);
        q3.add(1);
        q3.add(5);
        ballots.add(q1);
        ballots.add(q2);
        ballots.add(q3);


        HashMap<Integer,Integer> destCnt = new HashMap<>();
        destCnt.put(1,1);
        destCnt.put(2,1);
        HashMap<Integer,Integer> destCnt1 = new HashMap<>();
        destCnt1.put(1,1);
        destCnt1.put(2,2);
        destCnt1.put(4,1);
        destCnt1.put(5,1);
        HashSet<Integer> hashSet = new HashSet<>();

        assertEquals(destCnt1,ambigousPref(destCnt,ballots,hashSet));

    }

    @Test
    public void updateValidDestTest(){

        HashMap<Integer,Integer> destCnt = new HashMap<>();
        destCnt.put(1,1);
        HashMap<Integer,Integer> destCnt1 = new HashMap<>();
        destCnt1.put(1,2);
        HashMap<Integer,Integer> destCnt2 = new HashMap<>();
        destCnt2.put(1,2);
        destCnt2.put(3,1);


        assertEquals(destCnt1,updateValidDest(1,destCnt));
        assertEquals(destCnt2,updateValidDest(3,destCnt));

    }


    @Test
    public void fileToMapTest(){
        HashMap<Integer,String> fmap1 = new HashMap<Integer,String>();
        assertEquals(fmap,fileToMap(file,fmap1));

    }





}
